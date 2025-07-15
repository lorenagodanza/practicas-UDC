package es.udc.rs.telco.jaxrs.resources;

import java.net.URI;
import java.util.List;

import es.udc.rs.telco.jaxrs.dto.CustomerByKeywordsDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.telco.jaxrs.util.CustomertoCustomerDtoJaxbConversor;
import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.exceptions.PhoneCallAssociated;
import es.udc.rs.telco.model.telcoservice.TelcoServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("customers")
@OpenAPIDefinition(
        info = @Info(
                title = "ia-13",
                version = "2.2.15",
                description = "RS JAX-RS Documentacion"),
        servers = {
                @Server(
                        description = "Localhost server",
                        url = "http://localhost:7070/rs-telco-service")
        })

public class CustomerResource {

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Get the information of a customer", description = "This is a get statement by its ID, the ID must be in the url")
    @ApiResponse(responseCode = "200", description = "OK, id is valid so information about this customer is delayed",
            content = @Content(schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "400",
            description = "ERROR of validation, invalid request, id is not valid",
            content = @Content(schema = @Schema(implementation = InputValidationException.class)))
    @ApiResponse(responseCode = "404",
            description = "ERROR, customer not found, ID doesn't match",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    public CustomerDtoJaxb findCustomersById(@PathParam("id") final String id) throws InputValidationException, InstanceNotFoundException {
        Long customerId;

        try {
            customerId = Long.valueOf(id);
        } catch (final NumberFormatException ex){
            throw new InputValidationException("Invalid Request: unable to parse customer id '" + id + "'" );
        }

        return CustomertoCustomerDtoJaxbConversor.toCustomerDtoJaxb(TelcoServiceFactory.getService().findCustomerbyId(customerId));
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Add a new customer", description = "This is a post statement, the elements must be: name, dni, address and phoneNumber")
    @ApiResponse(responseCode = "201", description = "Created, a new customer has been added successfully",
            content = @Content(schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "400",
            description = "ERROR of validation ,some input parameters are invalid, could be: name, dni, address or the phoneNumber",
            content = @Content(schema = @Schema(implementation = InputValidationException.class)))
    public Response addCustomer(final CustomerDtoJaxb customerDto, @Context final UriInfo ui) throws InputValidationException{

        Customer customer = CustomertoCustomerDtoJaxbConversor.toCustomer(customerDto);
        customer = TelcoServiceFactory.getService().addCustomer(customer.getName(),customer.getDni(),customer.getAddress(),customer.getPhoneNumber());

        final CustomerDtoJaxb resultCustomerDto = CustomertoCustomerDtoJaxbConversor.toCustomerDtoJaxb(customer);
        final String requestUri = ui.getRequestUri().toString();

        System.out.println("Request received: " + customerDto);


        return Response.created(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + customer.getCustomerId()))
                .entity(resultCustomerDto).build();

    }

    @PATCH
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Add a new customer", description = "This is a patch statement, the ID must be in the url and the elements must be: name, dni, address and phoneNumber")
    @ApiResponse(responseCode = "200", description = "OK, the customer was modified correctly",
            content = @Content(schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "400",
            description = "ERROR of validation ,some input parameters are invalid, could be: name, dni, address",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    @ApiResponse(responseCode = "404",
            description = "ERROR, customer not found, ID doesn't match",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    public Response updateClient(@PathParam("id") final String id, final CustomerDtoJaxb customerDto, @Context final UriInfo ui) throws InputValidationException, InstanceNotFoundException {
        Long customerId;

        try {
            customerId = Long.valueOf(id);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: unable to parse customer id '" + id + "'");
        }

        Customer customer = TelcoServiceFactory.getService().findCustomerbyId(customerId);

        if (customerDto.getName() != null) {
            customer.setName(customerDto.getName());
        }
        if (customerDto.getDni() != null) {
            customer.setDni(customerDto.getDni());
        }
        if (customerDto.getAddress() != null) {
            customer.setAddress(customerDto.getAddress());
        }
        if (customerDto.getPhoneNumber() != null) {
            customer.setPhoneNumber(customerDto.getPhoneNumber());
        }

        TelcoServiceFactory.getService().updateClient(customerId, customer.getName(), customer.getDni(), customer.getAddress());

        final CustomerDtoJaxb resultCustomerDto = CustomertoCustomerDtoJaxbConversor.toCustomerDtoJaxb(TelcoServiceFactory.getService().findCustomerbyId(customerId));
        final String requestUri = ui.getRequestUri().toString();

        return Response.ok(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + customerId))
                .entity(resultCustomerDto).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Delete a customer by its ID" , description = "This is a delete statement, the ID must be in the url")
    @ApiResponse(responseCode = "200", description = "OK, the customer was deleted correctly",
            content = @Content(schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "400", description = "ERROR of validation, invalid request, id is not valid",
            content = @Content(schema = @Schema(implementation = InputValidationException.class)))
    @ApiResponse(responseCode = "403", description = "ERROR forbidden, can not delete customers with phone calls associated",
            content = @Content(schema = @Schema(implementation = PhoneCallAssociated.class)))
    @ApiResponse(responseCode = "404", description = "ERROR, customer not found, ID doesn't match",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    public Response deleteClient(@PathParam("id") final String id) throws InputValidationException, InstanceNotFoundException, PhoneCallAssociated {
        Long customerId;

        try {
            customerId = Long.valueOf(id);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: unable to parse customer id '" + id + "'");
        }
        CustomerDtoJaxb customerDto = CustomertoCustomerDtoJaxbConversor.toCustomerDtoJaxb(TelcoServiceFactory.getService().findCustomerbyId(customerId));

        TelcoServiceFactory.getService().deleteClient(customerId);

        return Response.ok(customerDto).build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Operation(summary = "Get the information of a customer", description = "This is a get statement by keywords or by dni, the keywords or dni and index and count must be in the url as a query parameter")
    @ApiResponse(responseCode = "200", description = "OK, keywords or dni are valid so information about this customer is delayed",
            content = @Content(schema = @Schema(implementation = Customer.class)))
    @ApiResponse(responseCode = "404",
            description = "ERROR, customer not found, keywords or dni doesn't match",
            content = @Content(schema = @Schema(implementation = InstanceNotFoundException.class)))
    public Response findClient(@QueryParam("dni") final String dni, @QueryParam("keywords") final String keywords, @QueryParam("index") final Integer index, @QueryParam("count") final Integer count) throws InstanceNotFoundException {
        if (dni != null) {
            CustomerDtoJaxb customer = CustomertoCustomerDtoJaxbConversor.toCustomerDtoJaxb(TelcoServiceFactory.getService().findCustomerByDni(dni));
            return Response.ok(customer).build();
        } else if (keywords != null && index != null && count != null) {
            List<Customer> customers = TelcoServiceFactory.getService().findbyKeywords(keywords, index, count);
            List<CustomerByKeywordsDtoJaxb> customerDtos = CustomertoCustomerDtoJaxbConversor.toCustomersDtoByKeywordsJaxbs(customers);
            return Response.ok(customerDtos).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing required query parameters").build();
        }
    }

}
