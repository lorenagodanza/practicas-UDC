package es.udc.ws.app.model.Partido;

import java.sql.*;

public class Jdbc3CcSqlPartidoDao extends AbstractSqlPartidoDao  {
    @Override
    public Partido crearPartido(Connection connection, Partido partido) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Partido"
                + " (equipoVisitante, fechaHoraCelebracion, precioEntradas, entradasDisponibles, fechaHoraAlta,entradasVendidas)"
                + " VALUES (?, ?, ?, ?, ?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            preparedStatement.setString(i++, partido.getEquipoVisitante());
            Timestamp fechaHoraCelebracion = partido.getFechaHoraCelebracion() != null
                    ? Timestamp.valueOf(partido.getFechaHoraCelebracion())
                    : null;
            preparedStatement.setTimestamp(i++, fechaHoraCelebracion);
            preparedStatement.setDouble(i++, partido.getPrecioEntradas());
            preparedStatement.setInt(i++, partido.getEntradasDisponibles());
            Timestamp fechaHoraAlta = partido.getFechaHoraAlta() != null
                    ? Timestamp.valueOf(partido.getFechaHoraAlta())
                    : null;
            preparedStatement.setTimestamp(i++, fechaHoraAlta);
            preparedStatement.setInt(i++, partido.getEntradasVendidas());
            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long partidoId = resultSet.getLong(1);

            /* Return movie. */
            return new Partido(partidoId, partido.getEquipoVisitante(),
                    partido.getFechaHoraCelebracion(), partido.getPrecioEntradas(),
                    partido.getEntradasDisponibles(), partido.getFechaHoraAlta(),
                    partido.getEntradasVendidas());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
