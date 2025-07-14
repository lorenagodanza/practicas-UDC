package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestCompraDto;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;


public class JsonToRestSaleDtoConversor {
    public static ObjectNode toObjectNode(RestCompraDto sale) {

        ObjectNode saleNode = JsonNodeFactory.instance.objectNode();

        if (sale.getCompraId() != null) {
            saleNode.put("compraId", sale.getCompraId());
        }
        saleNode. put("emailusuario", sale.getEmailUsuario()).
                  put("partidoId", sale.getPartidoId()).
                  put("ultimosCuatroDigitosTarjeta",sale.getUltimosCuatroDigitosTarjeta() ).
                  put("numeroEntradasCompradas", sale.getNumeroEntradasCompradas()).
                  put("fechaHoraCompra",sale.getFechaHoraCompra().toString()).
                  put("entradasRecogidas",sale.isEntradasRecogidas());
        return saleNode;
    }

    public static ArrayNode toArrayNode(List<RestCompraDto> compras) {

        ArrayNode comprasNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < compras.size(); i++) {
            RestCompraDto compraDto = compras.get(i);
            ObjectNode compraObject = toObjectNode(compraDto);
            comprasNode.add(compraObject);
        }

        return comprasNode;
    }

}
