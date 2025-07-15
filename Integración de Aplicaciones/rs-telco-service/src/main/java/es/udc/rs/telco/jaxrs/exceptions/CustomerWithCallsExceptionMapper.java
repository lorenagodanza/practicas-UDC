package es.udc.rs.telco.jaxrs.exceptions;

import es.udc.rs.telco.jaxrs.dto.CustomerWithCallsDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.telco.model.exceptions.InvalidStatus;
import es.udc.rs.telco.model.exceptions.PhoneCallAssociated;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomerWithCallsExceptionMapper implements ExceptionMapper<PhoneCallAssociated> {

    @Override
    public Response toResponse(PhoneCallAssociated phoneCallAssociated) {
        ExceptionDtoJaxb exception = new ExceptionDtoJaxb("PhoneCallAssociated");
        exception.addParam("message", phoneCallAssociated.getMessage());
        return Response.status(Response.Status.FORBIDDEN)
                .entity(exception).build();
    }
}
