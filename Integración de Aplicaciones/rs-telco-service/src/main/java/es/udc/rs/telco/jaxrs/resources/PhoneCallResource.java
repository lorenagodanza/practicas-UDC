package es.udc.rs.telco.jaxrs.resources;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.rs.telco.jaxrs.dto.PhoneCallBtwDatesDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.PhoneCallDtoJaxb;
import es.udc.rs.telco.jaxrs.util.PhoneCallToPhoneCallDtoJaxbConversor;
import es.udc.rs.telco.model.exceptions.InvalidStatus;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.telcoservice.TelcoServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("phoneCall")
public class PhoneCallResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Find a phone call",
            description = "This is a get statement between dates or by state pending, all the next parameters must be " +
                    "in the url as a query parameter, id, Inicio, Fin, tipo, indice, cantidad, ano, mes")
    @ApiResponse(responseCode = "200", description = "OK, parameters are valid",
            content = @Content(schema = @Schema(implementation = PhoneCall.class)))
    @ApiResponse(responseCode = "404",
            description = "ERROR, phone call not found, dates or state doesn't match",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    public Response findBetweenDatesAndPending(
            @QueryParam("id") final String customerId, @QueryParam("Inicio") final String fechaInicio,
            @QueryParam("Fin") final String fechaFin, @QueryParam("tipo") final String tipoLlamada,
            @QueryParam("indice") final int indice, @QueryParam("cantidad") final int cantidad,
            @QueryParam("ano") final Integer ano, @QueryParam("mes") final Integer mes) throws InputValidationException, InstanceNotFoundException {

        // Compruebo Id
        Long custId;
        try {
            custId = Long.valueOf(customerId);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Invalid Request: unable to parse customer id '" + customerId + "'");
        }

        List<PhoneCall> llamadas = new ArrayList<>();

        if (ano != null && mes != null) {
            try {
                llamadas = TelcoServiceFactory.getService().getPendingPhoneCallsByClientInMonth(custId, ano, mes);
                List<PhoneCallDtoJaxb> retorno = PhoneCallToPhoneCallDtoJaxbConversor.toPhoneCallDtoJaxb(llamadas);
                GenericEntity<List<PhoneCallDtoJaxb>> entity = new GenericEntity<List<PhoneCallDtoJaxb>>(retorno) {};
                return Response.ok(entity).build();
            }catch (Exception ex) {
                throw new InputValidationException("Internar Server Error: " + ex.getMessage());
            }
        }else{
            // Compruebo fechas
            LocalDateTime inicio;
            LocalDateTime fin;
            String nuevoInicio = fechaInicio.replace(" ", "T");
            String nuevofinal = fechaFin.replace(" ", "T");
            try {
                inicio = LocalDateTime.parse(nuevoInicio);
                fin = LocalDateTime.parse(nuevofinal);
            } catch (Exception ex) {
                throw new InputValidationException("Invalid Request: unable to parse dates. Use format 'yyyy-MM-ddTHH:mm:ss'");
            }


            try {
                llamadas = TelcoServiceFactory.getService().getCallsBetweenDates(custId, inicio, fin, tipoLlamada, indice, cantidad);
                List<PhoneCallBtwDatesDtoJaxb> retorno = PhoneCallToPhoneCallDtoJaxbConversor.toPhoneCallBtwDatesDtoJaxb(llamadas);

                GenericEntity<List<PhoneCallBtwDatesDtoJaxb>> entity = new GenericEntity<List<PhoneCallBtwDatesDtoJaxb>>(retorno) {};
                return Response.ok(entity).build();
            }catch (Exception ex) {
                throw new InputValidationException(ex.getMessage());
            }
        }
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Add a new phone call",
            description = "This is a post statement, the elements must be: customerId, startDate, duration, destinationNumber, phoneCallType and phoneCallStatus")
    @ApiResponse(responseCode = "201", description = "Created, a new phone has been added successfully",
            content = @Content(schema = @Schema(implementation = PhoneCall.class)))
    @ApiResponse(responseCode = "400",
            description = "ERROR of validation ,some input parameters are invalid, could be: customerId, startDate, duration, destinationNumber, phoneCallType or phoneCallStatus",
            content = @Content(schema = @Schema(implementation = InputValidationException.class)))
    public Response addPhoneCall(final PhoneCallDtoJaxb phoneCallDto, @Context final UriInfo ui) throws InputValidationException, InstanceNotFoundException {
        LocalDateTime fechaInicio = phoneCallDto.getStartDate();
        phoneCallDto.setStartDate(fechaInicio);

        PhoneCall phoneCall1 = PhoneCallToPhoneCallDtoJaxbConversor.toPhoneCall(phoneCallDto);
        PhoneCall phoneCall = TelcoServiceFactory.getService().addPhoneCall(phoneCall1.getCustomerId(),phoneCall1.getStartDate(),
                phoneCall1.getDuration(),phoneCall1.getDestinationNumber(),phoneCall1.getPhoneCallType());

        if (phoneCall.getPhoneCallId() == null) {
            throw new InputValidationException("Generated phoneCallId is null");
        }
        final PhoneCallDtoJaxb resultPhoneCallDto = PhoneCallToPhoneCallDtoJaxbConversor.toPhoneCallDtoJaxb(phoneCall);
        final String requestUri = ui.getRequestUri().toString();


        return Response.created(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + phoneCall.getPhoneCallId()))
                .entity(resultPhoneCallDto).build();

    }

    @PUT
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Update phone call status of a phone call",
            description = "This is a put statement, the elements must be: id,mes, ano and newStatus")
    @ApiResponse(responseCode = "201", description = "Created, a new phone has been added successfully",
            content = @Content(schema = @Schema(implementation = PhoneCall.class)))
    @ApiResponse(responseCode = "400",
            description = "ERROR of validation ,some input parameters are invalid, could be: customerId, startDate, duration, destinationNumber, phoneCallType or phoneCallStatus",
            content = @Content(schema = @Schema(implementation = InputValidationException.class)))
    @ApiResponse(responseCode = "403",
            description = "ERROR of validation, invalid status",
            content = @Content(schema = @Schema(implementation = InvalidStatus.class)))
    @ApiResponse(responseCode = "404",
            description = "ERROR, phone call not found, some parameters as id or mes or ano are not found",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    public Response updateCallStatus(@QueryParam("id") Long customerId, @QueryParam("mes") int mes,
                                     @QueryParam("ano") int ano, @QueryParam("newStatus") String newStatusStr) throws InvalidStatus, InstanceNotFoundException, InputValidationException {


        TelcoServiceFactory.getService().modifyCallStatus(customerId, mes, ano, newStatusStr.toUpperCase());
        return Response.ok().build();

    }
}
