package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.partidoservice.Exceptions.EntradasYaRecogidasException;
import es.udc.ws.app.model.partidoservice.Exceptions.NoSuficientesEntradasException;
import es.udc.ws.app.model.partidoservice.Exceptions.PartidoEmpezadoException;
import es.udc.ws.app.model.partidoservice.Exceptions.TarjetaBancariaNoCoincideException;

import java.lang.reflect.Method;

public class AppExceptionToJsonConversor {
    public static ObjectNode toEntradasYaRecogidasExceptionJson(EntradasYaRecogidasException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "EntradasYaRecogidas");
        exceptionObject.put("message", ex.getMessage());
        exceptionObject.put("saleId", (ex.getSaleId() != null) ? ex.getSaleId() : null);
        return exceptionObject;
    }
    public static ObjectNode toNoSuficientesEntradasExceptionJson(NoSuficientesEntradasException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "NoSuficientesEntradas");
        exceptionObject.put("message", ex.getMessage());
        exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
        return exceptionObject;
    }
    public static ObjectNode toPartidoEmpezadoExceptionJson(PartidoEmpezadoException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "PartidoYaEmpezado");
        exceptionObject.put("message", ex.getMessage());
        exceptionObject.put("partidoId", (ex.getPartidoId() != null) ? ex.getPartidoId() : null);
        return exceptionObject;
    }
    public static ObjectNode toTarjetaBancariaNoCoincideExceptionJson(TarjetaBancariaNoCoincideException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "TarjetaBancariaNoCoincide");
        exceptionObject.put("message", ex.getMessage());
        exceptionObject.put("saleId", (ex.getSaleId() != null) ? ex.getSaleId() : null);
        return exceptionObject;
    }
}
