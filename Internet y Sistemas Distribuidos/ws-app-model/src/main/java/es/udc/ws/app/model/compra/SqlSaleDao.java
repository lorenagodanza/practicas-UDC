package es.udc.ws.app.model.compra;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.util.List;

public interface SqlSaleDao {
    Sale crearCompra(Connection connection,Sale sale);
    Sale obtenerCompraPorId(Connection connection,Long compraId) throws InstanceNotFoundException;
    List<Sale> obtenerComprasPorUsuario(Connection connection,String emailUsuario);
    void actualizarCompra(Connection connection,Sale sale) throws InstanceNotFoundException;
    void eliminarCompra(Connection connection,Long compraId) throws InstanceNotFoundException;
}
