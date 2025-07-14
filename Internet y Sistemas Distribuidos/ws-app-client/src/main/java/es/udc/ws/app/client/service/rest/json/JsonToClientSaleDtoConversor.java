package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Level.parse;

public class JsonToClientSaleDtoConversor {
    public static ClientSaleDto toClientSaleDto(JsonNode saleNode) throws ParsingException {
            if (saleNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode partidoObject = (ObjectNode) saleNode;

                JsonNode saleIdNode = partidoObject.get("compraId");
                JsonNode emailUsuarioNode = partidoObject.get("emailusuario");
                String emailUsuario = (emailUsuarioNode != null) ? emailUsuarioNode.textValue().trim() : null;

                Long saleId = (saleIdNode != null) ? saleIdNode.longValue() : null;
                Long partidoId = partidoObject.get("partidoId").longValue();
                String tarjeta = partidoObject.get("ultimosCuatroDigitosTarjeta").textValue().trim();

                String fechaHoraCompra = partidoObject.get("fechaHoraCompra").textValue().trim();
                boolean entradasRecogidas=partidoObject.get("entradasRecogidas").asBoolean();
                int numeroEntradasCompradas= partidoObject.get("numeroEntradasCompradas").asInt();


                return new ClientSaleDto(saleId, emailUsuario, partidoId, tarjeta, numeroEntradasCompradas,LocalDateTime.parse(fechaHoraCompra),entradasRecogidas);

            }
    }


    public static ClientSaleDto toClientSaleDto(InputStream jsonSale) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonSale);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientSaleDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientSaleDto> toClientSaleDtos(InputStream jsonPartidos) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartidos);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode salesArray = (ArrayNode) rootNode;
                List<ClientSaleDto>saleDtos = new ArrayList<>(salesArray.size());
                for (JsonNode saleNode : salesArray) {
                    saleDtos.add(toClientSaleDto(saleNode));
                }
                return saleDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
