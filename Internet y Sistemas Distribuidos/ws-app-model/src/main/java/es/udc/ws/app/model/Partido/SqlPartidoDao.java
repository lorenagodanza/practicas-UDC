package es.udc.ws.app.model.Partido;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;
public interface SqlPartidoDao {
    public Partido crearPartido(Connection connection, Partido partido);

    public Partido obtenerPartidoPorId(Connection connection, Long partidoId)  throws InstanceNotFoundException;

    public List<Partido> obtenerPartidosEntreFechas(Connection connection,LocalDateTime fechaInicio, LocalDateTime fechaFin);

    public void actualizarPartido(Connection connection, Partido partido)
            throws InstanceNotFoundException;



    public void eliminarPartido(Connection connection, Long partidoId)
            throws InstanceNotFoundException, InstanceNotFoundException;

}
