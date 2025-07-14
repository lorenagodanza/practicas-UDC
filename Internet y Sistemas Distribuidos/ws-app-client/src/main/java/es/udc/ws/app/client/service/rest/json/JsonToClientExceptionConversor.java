package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.ClientNoSuficientesEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoEmpezadoException;
import es.udc.ws.app.client.service.exceptions.EntradasYaRecogidasException;
import es.udc.ws.app.client.service.exceptions.TarjetaBancariaNoCoincideException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                //System.out.println("JSON: " + objectMapper.writeValueAsString(rootNode));

                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    if (errorType.equals("TarjetaBancariaNoCoincide")) {
                        return toTarjetaBancariaNoCoincideException(rootNode);
                    } else {
                        if (errorType.equals("EntradasYaRecogidas")){
                          return toEntradasYaRecogidasException(rootNode);
                        }else {
                            if (errorType.equals("NoSuficientesEntradas")){
                                return toNoSuficientesEntadasException(rootNode);
                            }else {
                                if(errorType.equals("PartidoYaEmpezado")){
                                    return toPartidoEmpezadoException(rootNode);
                                }else{
                                    throw new ParsingException("Unrecognized error type: " + errorType);
                                }

                            }
                        }
                    }
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else if (errorType.equals("TarjetaBancariaNoCoincide")) {
                    return toTarjetaBancariaNoCoincideException(rootNode);
                } else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("PartidoYaEmpezado")) {
                    return toPartidoEmpezadoException(rootNode);
                } else {
                       throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    private static EntradasYaRecogidasException toEntradasYaRecogidasException(JsonNode rootNode) {
        Long partidoId = rootNode.get("saleId").longValue();
        return new EntradasYaRecogidasException(partidoId);
    }
    private static TarjetaBancariaNoCoincideException toTarjetaBancariaNoCoincideException(JsonNode rootNode) {
        Long saleId = rootNode.get("saleId").longValue();
        return new TarjetaBancariaNoCoincideException(saleId);
    }
    private static ClientNoSuficientesEntradasException toNoSuficientesEntadasException(JsonNode rootNode) {
        Long partidoId = rootNode.get("partidoId").longValue();
        return new ClientNoSuficientesEntradasException(partidoId);
    }
    private static ClientPartidoEmpezadoException toPartidoEmpezadoException(JsonNode rootNode){
        Long partidoId = rootNode.get("partidoId").longValue();
        return new ClientPartidoEmpezadoException(partidoId);
    }


    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("type").textValue();
                throw new ParsingException("Unrecognized error type: " + errorType);
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
