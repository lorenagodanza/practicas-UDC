package es.udc.ws.app.model.compra;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSqlSaleDao implements SqlSaleDao{
    protected AbstractSqlSaleDao(){}

    @Override
    public Sale obtenerCompraPorId(Connection connection, Long saleId)
        throws InstanceNotFoundException {
        String queryString = "SELECT emailUsuario, partidoId, "
                + " numeroTarjetaBancaria, numeroEntradasCompradas, fechaHoraCompra, entradasRecogidas, precioEntradas FROM Compra WHERE saleId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, saleId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(saleId,
                        Sale.class.getName());
            }

            /* Get results. */
            //i = 1;
            String emailUsuario = resultSet.getString("emailUsuario");
            long partidoId = resultSet.getShort("partidoId");
            String numeroTarjetaBancaria = resultSet.getString("numeroTarjetaBancaria");
            int numeroEntradasCompradas = resultSet.getShort("numeroEntradasCompradas");
            LocalDateTime fechaHoraCompra=resultSet.getTimestamp("fechaHoraCompra").toLocalDateTime();
            boolean entradasRecogidas=resultSet.getBoolean("entradasRecogidas");
            float precioEntradas=resultSet.getFloat("precioEntradas");


            /* Return compra. */
            return new Sale(saleId,emailUsuario, partidoId, numeroTarjetaBancaria,numeroEntradasCompradas, fechaHoraCompra, entradasRecogidas,precioEntradas);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Sale> obtenerComprasPorUsuario(Connection connection,String emailUsuario) {
        String queryString="SELECT saleId FROM Compra WHERE emailUsuario = ?";
        LinkedList lista=new LinkedList<Sale>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, emailUsuario);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                lista.add(obtenerCompraPorId(connection,resultSet.getLong("saleId")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lista;

    }

    @Override
    public void actualizarCompra(Connection connection,Sale sale)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Compra"
                + " SET emailUsuario = ?, partidoId = ?, "
                + " numeroTarjetaBancaria = ?, numeroEntradasCompradas = ? , fechaHoraCompra = ? , entradasRecogidas = ?, precioEntradas = ? WHERE saleId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;

            preparedStatement.setString(i++, sale.getEmailUsuario());
            preparedStatement.setLong(i++, sale.getPartidoId());
            preparedStatement.setString(i++, sale.getNumeroTarjetaBancaria());
            preparedStatement.setInt(i++, sale.getNumeroEntradasCompradas());
            Timestamp date = sale.getFechaHoraCompra() != null ? Timestamp.valueOf(sale.getFechaHoraCompra()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setBoolean(i++, sale.isEntradasRecogidas());
            preparedStatement.setDouble(i++, sale.getPrecioEntrada());
            preparedStatement.setLong(i++, sale.getSaleId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(sale.getSaleId(),
                        Sale.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void eliminarCompra(Connection connection,Long saleId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Compra WHERE" + " saleId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, saleId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(saleId,Sale.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
