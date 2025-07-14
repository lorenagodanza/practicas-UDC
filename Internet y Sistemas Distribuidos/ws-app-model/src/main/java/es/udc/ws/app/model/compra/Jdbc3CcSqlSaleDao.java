package es.udc.ws.app.model.compra;

import java.sql.*;

public class Jdbc3CcSqlSaleDao extends AbstractSqlSaleDao{
    @Override
    public Sale crearCompra(Connection connection, Sale sale) {
        String queryString = "INSERT INTO Compra"
                + " (emailUsuario, partidoId, precioEntradas, numeroTarjetaBancaria, numeroEntradasCompradas, fechaHoraCompra, entradasRecogidas)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";



        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setString(i++, sale.getEmailUsuario());

            preparedStatement.setLong(i++, sale.getPartidoId());

            preparedStatement.setDouble(i++, sale.getPrecioEntrada());

            preparedStatement.setString(i++, sale.getNumeroTarjetaBancaria());

            preparedStatement.setInt(i++, sale.getNumeroEntradasCompradas());

            Timestamp date = sale.getFechaHoraCompra() != null ? Timestamp.valueOf(sale.getFechaHoraCompra()) : null;
            preparedStatement.setTimestamp(i++, date);

            preparedStatement.setBoolean(i++, sale.isEntradasRecogidas());

            preparedStatement.executeUpdate();


            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long saleId = resultSet.getLong(1);

            return new Sale(saleId,sale.getEmailUsuario(),sale.getPartidoId(),sale.getNumeroTarjetaBancaria(),sale.getNumeroEntradasCompradas(),
                    sale.getFechaHoraCompra(),sale.isEntradasRecogidas(),sale.getPrecioEntrada());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
