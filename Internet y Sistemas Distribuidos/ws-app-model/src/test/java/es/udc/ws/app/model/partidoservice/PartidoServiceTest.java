package es.udc.ws.app.model.partidoservice;

import es.udc.ws.app.model.Partido.SqlPartidoDao;
import es.udc.ws.app.model.Partido.SqlPartidoDaoFactory;
import es.udc.ws.app.model.compra.Jdbc3CcSqlSaleDao;
import es.udc.ws.app.model.compra.Sale;
import es.udc.ws.app.model.compra.SqlSaleDao;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.compra.SqlSaleDaoFactory;
import es.udc.ws.app.model.partidoservice.Exceptions.*;
import es.udc.ws.app.model.partidoservice.Exceptions.NoSuficientesEntradasException;
import es.udc.ws.app.model.partidoservice.Exceptions.PartidoEmpezadoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PartidoServiceTest {
    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";

    private final String VALID_CREDIT_CARD_NUMBER2 = "1234567790123456";
    private final String USER_ID = "pepe@gmail.com";
    private final long NON_EXISTENT_PARTIDO_ID=9999999;

    private final long NON_EXISTENT_SALE_ID=9999999;

    private final String INVALID_CREDIT_CARD_NUMBER = "";
    private static PartidoService partidoService = null;

    private static SqlSaleDao saleDao = null;
    private static SqlPartidoDao partidoDao = null;
    @BeforeAll

    public static void init() {

        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        partidoService = PartidoServiceFactory.getService();
        //partidoService=new PartidoServiceImpl();
        saleDao = SqlSaleDaoFactory.getDao();
        //saleDao =new Jdbc3CcSqlSaleDao();
        partidoDao= SqlPartidoDaoFactory.getDao();
    }

    private void updatePartido(Partido partido) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                partidoDao.actualizarPartido(connection, partido);

                /* Commit. */
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void removePartido(Long partidoId){

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                partidoDao.eliminarPartido(connection, partidoId);

                /* Commit. */
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void removeSale(Long saleId){

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                saleDao.eliminarCompra(connection, saleId);

                /* Commit. */
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (InstanceNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public Sale findSale(Long saleId) throws InstanceNotFoundException {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        Sale sale;

        try (Connection connection = dataSource.getConnection()) {

            sale = saleDao.obtenerCompraPorId(connection, saleId);
            LocalDateTime now = LocalDateTime.now();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sale;
    }

    private Partido getValidPartido(String visitante,int year_time) {
        LocalDateTime time= LocalDate.of(year_time, Month.DECEMBER, 18).atStartOfDay();
        return new Partido(visitante, time, 33, 33, 0);
    }
    private Partido getValidPartido() {
        return getValidPartido("visitante1",2024);
    }
    @Test
    void createAndFind() throws InputValidationException, InstanceNotFoundException {
        Partido partido = getValidPartido();
        Partido addedPartido = null;

        try {

            // Create Partido
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);

            addedPartido = partidoService.darAltaPartido(partido);

            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            // Find Partido
            Partido foundPartido = partidoService.buscarPartidoPorId(addedPartido.getPartidoId());

            assertEquals(addedPartido, foundPartido);
            assertEquals(foundPartido.getEntradasDisponibles(),partido.getEntradasDisponibles());
            assertEquals(foundPartido.getEntradasVendidas(),partido.getEntradasVendidas());
            assertEquals(foundPartido.getEquipoVisitante(),partido.getEquipoVisitante());
            assertEquals(foundPartido.getPrecioEntradas(),partido.getPrecioEntradas());
            assertTrue((foundPartido.getFechaHoraAlta().compareTo(beforeCreationDate) >= 0)
                             && (foundPartido.getFechaHoraAlta().compareTo(afterCreationDate) <= 0));


        } finally {
            // Clear Database
            if (addedPartido!=null) {
                removePartido(addedPartido.getPartidoId());
            }
        }
    }
    @Test
    public void testAddInvalidPartido() {

        // Check partido title not null
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEquipoVisitante(null);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido title not empty
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEquipoVisitante(null);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido PrecioEntradas is not negative
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setPrecioEntradas(-1);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido PrecioEntradas <=MAX_PRICE
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setPrecioEntradas(MAX_PRICE+1);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido EntradasDisponibles in not negative
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEntradasDisponibles(-1);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido EntradasDisponibles<=MAX_SEATS
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEntradasDisponibles(MAX_SEATS+1);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido EntradasVendidas in not negative
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEntradasVendidas(-1);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

        // Check partido EntradasVendidas <=EntradasDisponibles
        assertThrows(InputValidationException.class, () -> {
            Partido partido = getValidPartido();
            partido.setEntradasVendidas(partido.getEntradasDisponibles()+1);
            Partido addedpartido = partidoService.darAltaPartido(partido);
            removePartido(addedpartido.getPartidoId());
        });

    }
    @Test
    public void testFindNonExistentPartido() {
        assertThrows(InstanceNotFoundException.class, () -> partidoService.buscarPartidoPorId(NON_EXISTENT_PARTIDO_ID));
    }

    @Test
    public void testBuyEntradaAndFindSale()
            throws InstanceNotFoundException, InputValidationException, NoSuficientesEntradasException, PartidoEmpezadoException {

        Partido partido = partidoService.darAltaPartido(getValidPartido());
        Sale sale = null;

        try {

            // Registrar partido
            LocalDateTime beforeBuyDate = LocalDateTime.now().withNano(0);

            sale = partidoService.comprarEntradas(USER_ID,partido.getPartidoId() , VALID_CREDIT_CARD_NUMBER,2);

            LocalDateTime afterBuyDate = LocalDateTime.now().withNano(0);

            // Find sale
            Sale foundSale = findSale(sale.getSaleId());

            // Check sale
            assertEquals(sale, foundSale);
            assertEquals(VALID_CREDIT_CARD_NUMBER, foundSale.getNumeroTarjetaBancaria());
            assertEquals(USER_ID, foundSale.getEmailUsuario());
            assertEquals(partido.getPartidoId(), foundSale.getPartidoId());
            assertTrue(partido.getPrecioEntradas() == foundSale.getPrecioEntrada());
            assertTrue((foundSale.getFechaHoraCompra().compareTo(beforeBuyDate) >= 0)
                    && (foundSale.getFechaHoraCompra().compareTo(afterBuyDate) <= 0));


        } finally {
            // Clear database: remove sale (if created) and Sale
            if (sale != null) {
                removeSale(sale.getSaleId());
            }
            removePartido(partido.getPartidoId());
        }

    }
    @Test
    public void testBuyInvalid() {

        //Check invalid credit card number
        assertThrows(InputValidationException.class, () -> {
            Partido partido = partidoService.darAltaPartido(getValidPartido());
            try {
                Sale sale = partidoService.comprarEntradas(USER_ID,partido.getPartidoId() , INVALID_CREDIT_CARD_NUMBER,4);
                removePartido(partido.getPartidoId());
            } finally {
                // Clear database
                removePartido(partido.getPartidoId());
            }
        });
        // Check partido numeroEntradas <=EntradasDisponibles
        assertThrows(NoSuficientesEntradasException.class, () -> {
            Partido partido = partidoService.darAltaPartido(getValidPartido());
            try {
                Sale sale = partidoService.comprarEntradas(USER_ID,partido.getPartidoId() , VALID_CREDIT_CARD_NUMBER,partido.getEntradasDisponibles()+1);
                removeSale(sale.getSaleId());
            } finally {
                // Clear database
                removePartido(partido.getPartidoId());

            }
        });
        // Check partido numeroEntradas >0
        assertThrows(InputValidationException.class, () -> {
            Partido partido = partidoService.darAltaPartido(getValidPartido());
            try {
                Sale sale = partidoService.comprarEntradas(USER_ID,partido.getPartidoId() , VALID_CREDIT_CARD_NUMBER,0/*buscarPartidoPorId(PartidoId())p.getEntradasDisponibles()+1*/);
                removeSale(sale.getSaleId());
            } finally {
                // Clear database
                removePartido(partido.getPartidoId());
            }
        });
        //Check invalidid partido
        assertThrows(InstanceNotFoundException.class, () -> {
            Sale sale = partidoService.comprarEntradas(USER_ID,NON_EXISTENT_PARTIDO_ID,  VALID_CREDIT_CARD_NUMBER,5);
            removeSale(sale.getSaleId());
        });
        //Chech invalid partido time
        assertThrows(PartidoEmpezadoException.class, () -> {
            Partido partido = partidoService.darAltaPartido(getValidPartido());
            partido.setFechaHoraCelebracion(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0));
            updatePartido(partido);
            try {
                Sale sale = partidoService.comprarEntradas(USER_ID,partido.getPartidoId() , VALID_CREDIT_CARD_NUMBER,4 );
                removeSale(sale.getSaleId());
            } finally {
                // Clear database
                removePartido(partido.getPartidoId());
            }
        });
    }

    @Test
    public void testBuscarPartidosEntreFechas() throws InputValidationException{
        // Define las fechas de inicio y fin para buscar partidos
        LocalDateTime fechaInicio = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2024, Month.JANUARY, 31, 23, 59);

        // Crea partidos de prueba para que se encuentren en el rango de fechas
        Partido partido1 = getValidPartido();
        Partido partido2 = getValidPartido();

        // Agrega los partidos a la base de datos
        Partido addedPartido1 = partidoService.darAltaPartido(partido1);
        Partido addedPartido2 = partidoService.darAltaPartido(partido2);

        // Llama al método buscarPartidosEntreFechas y obtén la lista de partidos
        List<Partido> partidosEncontrados = partidoService.buscarPartidosEntreFechas(fechaInicio,fechaFin);

        // Realiza aserciones para verificar si los partidos se encuentran en el rango de fechas
        for (Partido partido : partidosEncontrados) {
            LocalDateTime fechaPartido = partido.getFechaHoraCelebracion();
            assertTrue(fechaPartido.isAfter(fechaInicio) || fechaPartido.isEqual(fechaInicio));
            assertTrue(fechaPartido.isBefore(fechaFin) || fechaPartido.isEqual(fechaFin));
        }

        // Elimina los partidos creados en la prueba
        removePartido(addedPartido1.getPartidoId());
        removePartido(addedPartido2.getPartidoId());
    }
    @Test
    public void testBuscarPartidosEntreFechasFechaFinAnterior() {
        LocalDateTime fechaInicio = LocalDateTime.of(2025, Month.JANUARY, 31, 23, 59);
        LocalDateTime fechaFin = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0);
        assertThrows(InputValidationException.class, () -> {
            partidoService.buscarPartidosEntreFechas(fechaInicio, fechaFin);
        });
    }


    @Test
    void testObtenerComprasPorUsuario() throws InputValidationException, InstanceNotFoundException {
        // Crear un partido
        Partido partido = partidoService.darAltaPartido(getValidPartido());

        // Comprar entradas para el usuario
        Sale sale = null;
        try {
            sale = partidoService.comprarEntradas(USER_ID, partido.getPartidoId(), VALID_CREDIT_CARD_NUMBER, 2);
        } catch (NoSuficientesEntradasException | PartidoEmpezadoException e) {
            throw new RuntimeException(e);
        }

        try {
            // Obtener las compras del usuario
            List<Sale> compras = partidoService.obtenerComprasPorUsuario(USER_ID);

            // Verificar que la lista de compras no esté vacía
            assertFalse(compras.isEmpty());

            // Verificar que la compra que acabamos de realizar esté en la lista
            assertTrue(compras.contains(sale));
        } finally {
            // Limpiar la base de datos eliminando la compra y el partido
            if (sale != null) {
                removeSale(sale.getSaleId());
            }
            removePartido(partido.getPartidoId());
        }
    }
    @Test
    void testComprarEntradasUsuarioExcepciones() throws InputValidationException {
//
        assertEquals( partidoService.obtenerComprasPorUsuario("sdf@gmail.com").size(),0);

        assertThrows(InputValidationException.class, () -> {
            partidoService.obtenerComprasPorUsuario("");
        });

        // Limpiar la base de datos eliminando el partido

    }


    @Test
    public void testMarcarEntradasRecogidas() throws NoSuficientesEntradasException, InstanceNotFoundException, PartidoEmpezadoException, InputValidationException, TarjetaBancariaNoCoincideException, EntradasYaRecogidasException {
        // Configuración de datos de prueba
        Partido partido = partidoService.darAltaPartido(getValidPartido());

        // Comprar entradas para el usuario
        Sale sale = null;
        try {
            sale = partidoService.comprarEntradas(USER_ID, partido.getPartidoId(), VALID_CREDIT_CARD_NUMBER, 2);

            Long saleId = sale.getSaleId();

            sale.setEntradasRecogidas(false);

            Sale foundSale = findSale(saleId);

            assertEquals(foundSale, sale);
            partidoService.marcarEntradasRecogidas(foundSale.getSaleId(), VALID_CREDIT_CARD_NUMBER);

            // Recargar la venta desde la base de datos después de marcar las entradas como recogidas
            foundSale = findSale(saleId);

            assertTrue(foundSale.isEntradasRecogidas());
        } finally {
            // Clear Database (sale if it was created and Sale)
            if (sale != null) {
                removeSale(sale.getSaleId());
            }
            removePartido(partido.getPartidoId());
        }
    }
        @Test
    public void testMarcarEntradasRecogidasNonExistentSale() {
        assertThrows(InstanceNotFoundException.class, () -> partidoService.marcarEntradasRecogidas(NON_EXISTENT_SALE_ID,VALID_CREDIT_CARD_NUMBER));
    }
    @Test
    public void testMarcarEntradasRecogidasInvalidCreditCard() throws NoSuficientesEntradasException, InstanceNotFoundException, PartidoEmpezadoException, InputValidationException {
        Sale sale = null;
        Partido partido = partidoService.darAltaPartido(getValidPartido());
        sale = partidoService.comprarEntradas(USER_ID, partido.getPartidoId(), VALID_CREDIT_CARD_NUMBER, 2);
        Long saleId = sale.getSaleId();
        assertThrows(InputValidationException.class, () -> partidoService.marcarEntradasRecogidas(saleId,INVALID_CREDIT_CARD_NUMBER));
    }
    @Test
    public void testMarcarEntradasRecogidasEntradasYaRecogidas() throws NoSuficientesEntradasException, InstanceNotFoundException, PartidoEmpezadoException, InputValidationException, EntradasYaRecogidasException, TarjetaBancariaNoCoincideException {
        Sale sale = null;
        Partido partido = partidoService.darAltaPartido(getValidPartido());
        sale = partidoService.comprarEntradas(USER_ID, partido.getPartidoId(), VALID_CREDIT_CARD_NUMBER, 2);
        Long saleId = sale.getSaleId();
        String numeroTarjetaBancaria = VALID_CREDIT_CARD_NUMBER;

        partidoService.marcarEntradasRecogidas(saleId, numeroTarjetaBancaria);
        sale = findSale(saleId);
        assertThrows(EntradasYaRecogidasException.class, () -> partidoService.marcarEntradasRecogidas(saleId, numeroTarjetaBancaria));
    }

    @Test
    public void testMarcarEntradasRecogidasTarjetaNoCoincide() throws NoSuficientesEntradasException, InstanceNotFoundException, PartidoEmpezadoException, InputValidationException {
        Sale sale = null;
        Partido partido = partidoService.darAltaPartido(getValidPartido());
        sale = partidoService.comprarEntradas(USER_ID, partido.getPartidoId(), VALID_CREDIT_CARD_NUMBER, 2);
        sale.setEntradasRecogidas(false);
        Long saleId = sale.getSaleId();

        String numeroTarjetaBancaria = VALID_CREDIT_CARD_NUMBER2 ;

        assertThrows(TarjetaBancariaNoCoincideException.class, () -> partidoService.marcarEntradasRecogidas(saleId, numeroTarjetaBancaria));
    }
}