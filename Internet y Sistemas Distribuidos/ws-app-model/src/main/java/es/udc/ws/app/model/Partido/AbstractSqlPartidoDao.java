package es.udc.ws.app.model.Partido;



import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractSqlPartidoDao implements SqlPartidoDao {

    protected AbstractSqlPartidoDao() {
    }

    @Override
    public Partido obtenerPartidoPorId(Connection connection, Long partidoId)
            throws InstanceNotFoundException {
        String queryString = "SELECT equipovisitante, fechaHoraCelebracion,  "
                + " precioEntradas, entradasDisponibles, fechaHoraAlta, entradasVendidas FROM Partido WHERE partidoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, partidoId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(partidoId,Partido.class.getName());
            }

            /* Get results. */
            i = 1;
            String equipoVisitante = resultSet.getString(i++);
            Timestamp creationDateAsTimestam = resultSet.getTimestamp(i++);
            LocalDateTime fechaHoraCelebracion = creationDateAsTimestam.toLocalDateTime();
            float precioEntradas = resultSet.getFloat(i++);
            int entradasDisponibles = resultSet.getShort(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaHoraAlta = creationDateAsTimestamp.toLocalDateTime();
            int entradasVendidas = resultSet.getShort(i++);


            /* Return partido */
            return new Partido(partidoId,equipoVisitante,fechaHoraCelebracion,precioEntradas,entradasDisponibles,fechaHoraAlta,entradasVendidas);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Partido> obtenerPartidosEntreFechas(Connection connection,LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        String queryString = "SELECT partidoId, equipoVisitante, fechaHoraCelebracion, precioEntradas, entradasDisponibles, fechaHoraAlta, entradasVendidas FROM Partido WHERE fechaHoraCelebracion BETWEEN ? AND ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(fechaInicio));
            preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(fechaFin));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Partido> partidos = new ArrayList<>();

            while (resultSet.next()) {
                Long partidoId = resultSet.getLong("partidoId");
                String equipoVisitante = resultSet.getString("equipoVisitante");
                LocalDateTime fechaHoraCelebracion = resultSet.getTimestamp("fechaHoraCelebracion").toLocalDateTime();
                double precioEntradas = resultSet.getDouble("precioEntradas");
                int entradasDisponibles = resultSet.getInt("entradasDisponibles");
                LocalDateTime fechaHoraAlta = resultSet.getTimestamp("fechaHoraAlta").toLocalDateTime();
                int entradasVendidas = resultSet.getInt("entradasVendidas");

                Partido partido = new Partido(partidoId, equipoVisitante, fechaHoraCelebracion, precioEntradas, entradasDisponibles, fechaHoraAlta, entradasVendidas);
                partidos.add(partido);
            }

            return partidos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void actualizarPartido(Connection connection, Partido partido) throws InstanceNotFoundException {
        String queryString = "UPDATE Partido SET equipoVisitante = ?, fechaHoraCelebracion = ?, precioEntradas = ?, entradasDisponibles = ?, fechaHoraAlta = ?, entradasVendidas = ? WHERE partidoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setString(i++, partido.getEquipoVisitante());
            preparedStatement.setTimestamp(i++, java.sql.Timestamp.valueOf(partido.getFechaHoraCelebracion()));
            preparedStatement.setDouble(i++, partido.getPrecioEntradas());
            preparedStatement.setInt(i++, partido.getEntradasDisponibles());
            preparedStatement.setTimestamp(i++, java.sql.Timestamp.valueOf(partido.getFechaHoraAlta()));
            preparedStatement.setInt(i++, partido.getEntradasVendidas());
            preparedStatement.setLong(i++, partido.getPartidoId());

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(partido.getPartidoId(), Partido.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void eliminarPartido(Connection connection, Long partidoId) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Partido WHERE partidoId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, partidoId);

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(partidoId,Partido.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}

