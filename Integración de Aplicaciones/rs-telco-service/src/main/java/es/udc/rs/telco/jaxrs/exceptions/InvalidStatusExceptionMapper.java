package es.udc.rs.telco.jaxrs.exceptions;

import es.udc.rs.telco.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.InvalidStatusDtoJaxb;
import es.udc.rs.telco.model.exceptions.InvalidStatus;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidStatusExceptionMapper implements ExceptionMapper<InvalidStatus> {
    @Override
    public Response toResponse(InvalidStatus invalidstatus) {
        ExceptionDtoJaxb exception = new ExceptionDtoJaxb("InvalidStatusException");
        exception.addParam("message", invalidstatus.getMessage());
        return Response.status(Response.Status.FORBIDDEN)
                .entity(exception).build();
    }
}



