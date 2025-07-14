package es.udc.ws.app.model.partidoservice;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.Partido.SqlPartidoDao;
import es.udc.ws.app.model.Partido.SqlPartidoDaoFactory;
import es.udc.ws.app.model.compra.Sale;
import es.udc.ws.app.model.compra.SqlSaleDao;
import es.udc.ws.app.model.compra.SqlSaleDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;
import es.udc.ws.app.model.partidoservice.Exceptions.*;
import es.udc.ws.app.model.partidoservice.Exceptions.NoSuficientesEntradasException;
import es.udc.ws.app.model.partidoservice.Exceptions.PartidoEmpezadoException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class PartidoServiceImpl implements PartidoService{
    private final DataSource dataSource;
    private SqlPartidoDao partidoDao = null;
    private SqlSaleDao saleDao = null;

    public PartidoServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        partidoDao = SqlPartidoDaoFactory.getDao();
        //partidoDao= new Jdbc3CcSqlPartidoDao();
        saleDao = SqlSaleDaoFactory.getDao();
        //saleDao=new Jdbc3CcSqlSaleDao();
    }

    private void validatePartido(Partido partido) throws InputValidationException {

        PropertyValidator.validateMandatoryString("EquipoVisitante", partido.getEquipoVisitante());
        PropertyValidator.validateLong("EntradasVendidas", partido.getEntradasVendidas(), 0, partido.getEntradasDisponibles());
        PropertyValidator.validateLong("EntradasDisponibles", partido.getEntradasDisponibles(), 0,MAX_SEATS);
        PropertyValidator.validateDouble("PrecioEntrada", partido.getPrecioEntradas(), 0,MAX_PRICE);
        validateSum("SumaEntradas", partido.getEntradasVendidas(), partido.getEntradasDisponibles());
        validateNotPastDate("FechaHoraAlta",partido.getFechaHoraCelebracion());
    }
    private void validateSum(String propertyName, long value1, long value2) throws InputValidationException {
        long sum = value1 + value2;
        if (sum < 1) {
            throw new InputValidationException("numero de entradas erroneo");
        }
    }
    public static void validateNotPastDate(String propertyName, LocalDateTime date) throws InputValidationException {
        LocalDateTime currentDate = LocalDateTime.now();

        if (date.isBefore(currentDate)) {
            throw new InputValidationException(propertyName + " La fecha no debe ser anterior a la fecha actual.");
        }
    }

    @Override
    public Partido darAltaPartido(Partido partido) throws InputValidationException {
        validatePartido(partido);
        partido.setFechaHoraAlta(LocalDateTime.now());

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Partido createdPartido = partidoDao.crearPartido(connection, partido);

                /* Commit. */
                connection.commit();

                return createdPartido;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void validateemail(String email) throws InputValidationException {
        email = email.trim();
        if (email.length() < 1) {
            throw new InputValidationException("El email no debe ser nulo.");
        }
    }
    @Override
    public Sale comprarEntradas(String emailUsuario, Long partidoId, String numeroTarjetaBancaria,int numeroEntradas) throws InstanceNotFoundException, InputValidationException, NoSuficientesEntradasException, PartidoEmpezadoException {
        PropertyValidator.validateLong("EntradasDisponibles", numeroEntradas, 1,MAX_SEATS);
        PropertyValidator.validateCreditCard(numeroTarjetaBancaria);
        validateemail(emailUsuario);
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Partido partido = partidoDao.obtenerPartidoPorId(connection, partidoId);
                int entradasdisponibles=0;
                entradasdisponibles=partido.getEntradasDisponibles();
                if (partido.getFechaHoraCelebracion().isBefore(LocalDateTime.now()))
                    throw new PartidoEmpezadoException(partido.getPartidoId());
                if(numeroEntradas>partido.getEntradasDisponibles()- partido.getEntradasVendidas())
                    throw new NoSuficientesEntradasException(partido.getPartidoId());

                int nuevasvendidas=partido.getEntradasVendidas()+numeroEntradas;
                partido.setEntradasVendidas(nuevasvendidas);
                partidoDao.actualizarPartido(connection, partido);

                Sale sale = saleDao.crearCompra(connection, new Sale(emailUsuario, partidoId, numeroTarjetaBancaria,
                        numeroEntradas, LocalDateTime.now(),false,partido.getPrecioEntradas()));


                /* Commit. */
                connection.commit();

                return sale;

            } catch (InstanceNotFoundException |NoSuficientesEntradasException |PartidoEmpezadoException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Partido> buscarPartidosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws InputValidationException {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new InputValidationException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        try (Connection connection = dataSource.getConnection()) {
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                List<Partido> partidos = partidoDao.obtenerPartidosEntreFechas(connection, fechaInicio, fechaFin);

                connection.commit();

                return partidos;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Partido buscarPartidoPorId(Long partidoId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return partidoDao.obtenerPartidoPorId(connection, partidoId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Sale> obtenerComprasPorUsuario(String emailUsuario) throws InputValidationException {
        PropertyValidator.validateMandatoryString("emailUsuario", emailUsuario);
        try (Connection connection = dataSource.getConnection()) {
            return saleDao.obtenerComprasPorUsuario(connection, emailUsuario);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void marcarEntradasRecogidas(Long saleId, String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException, TarjetaBancariaNoCoincideException, EntradasYaRecogidasException {

        PropertyValidator.validateCreditCard(numeroTarjetaBancaria);

        try (Connection connection = dataSource.getConnection()) {
            // Primero, obtén el objeto Sale correspondiente a saleId
            Sale sale = saleDao.obtenerCompraPorId(connection, saleId);

            // Asegúrate de que la tarjeta bancaria proporcionada coincida con la almacenada en el objeto Sale
            if (!numeroTarjetaBancaria.equals(sale.getNumeroTarjetaBancaria())) {
                throw new TarjetaBancariaNoCoincideException(saleId);
            }

            // Marca las entradas como recogidas
            if(!sale.isEntradasRecogidas()) {
                sale.setEntradasRecogidas(true);
            }else {
                throw new EntradasYaRecogidasException(saleId);
            }
            // Luego, actualiza el objeto Sale en la base de datos
            saleDao.actualizarCompra(connection, sale);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }catch(InstanceNotFoundException|TarjetaBancariaNoCoincideException | EntradasYaRecogidasException e){
            throw e;
        }
    }
}



