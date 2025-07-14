package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import es.udc.ws.app.restservice.dto.RestPartidoDto;
public class JsonToRestPartidoDtoConversor {
    public static ObjectNode toObjectNode(RestPartidoDto partido) {

        ObjectNode partidoObject = JsonNodeFactory.instance.objectNode();

        partidoObject.put("partidoId", partido.getPartidoId()).
                put("equipoVisitante", partido.getEquipoVisitante()).
                put("precio", partido.getPrecioEntradas()).
                put("fecha", partido.getFechaHoraCelebracion().toString()).
                put("disponibles", partido.getEntradasDisponibles()).
                put("vendidas",partido.getEntradasVendidas());


        return partidoObject;
    }

    public static ArrayNode toArrayNode(List<RestPartidoDto> partidos) {

        ArrayNode partidosNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < partidos.size(); i++) {
            RestPartidoDto partidoDto = partidos.get(i);
            ObjectNode partidoObject = toObjectNode(partidoDto);
            partidosNode.add(partidoObject);
        }

        return partidosNode;
    }

    public static RestPartidoDto toRestPartidoDto(InputStream jsonPartido) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonPartido);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode partidoObject = (ObjectNode) rootNode;

                JsonNode partidoIdNode = partidoObject.get("partidoId");
                Long partidoId = (partidoIdNode != null) ? partidoIdNode.longValue() : null;

                String equipoVisitante = partidoObject.get("equipoVisitante").textValue().trim();
                double precio =  partidoObject.get("precio").doubleValue();
                //Cambiar tipo de variable fecha??
                String fechaString = partidoObject.get("fecha").textValue().trim();
                LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ISO_DATE_TIME);
                int disponibles=partidoObject.get("disponibles").intValue();
                JsonNode vendidasNode = partidoObject.get("vendidas");
                int vendidas = (vendidasNode != null) ? vendidasNode.intValue() : 0;

                return new RestPartidoDto(partidoId, equipoVisitante, fecha, precio , disponibles,vendidas);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
