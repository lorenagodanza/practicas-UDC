package es.udc.rs.telco.client.service.rest;

import com.fasterxml.jackson.core.util.JacksonFeature;
import es.udc.rs.telco.client.service.clientDto.*;
import es.udc.rs.telco.client.service.rest.dto.*;
import es.udc.rs.telco.client.service.rest.json.JaxbJsonContextResolver;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import es.udc.rs.telco.client.service.conversors.*;

import es.udc.rs.telco.client.service.ClientTelcoService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class RestClientTelcoService implements ClientTelcoService {

	private static jakarta.ws.rs.client.Client client = null;

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientTelcoService.endpointAddress";
	private WebTarget endPointWebTarget = null;
	private final static String endpointAddress =
			ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

	private final static String MEDIA_TYPE_PARAMETER = "RestClientTelcoService.mediaType";
	private MediaType mediaType = null;

	/*
	 * Client instances are expensive resources. It is recommended a configured
	 * instance is reused for the creation of Web resources. The creation of Web
	 * resources, the building of requests and receiving of responses are
	 * guaranteed to be thread safe. Thus a Client instance and WebTarget
	 * instances may be shared between multiple threads.
	 */
	private static Client getClient() {
		if (client == null) {
			client = ClientBuilder.newClient();
			client.register(JacksonFeature.class);
			client.register(JaxbJsonContextResolver.class);
		}
		return client;
	}

	private WebTarget getEndpointWebTarget() {
		if (endPointWebTarget == null) {
			endPointWebTarget = getClient()
					.target(ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER));
		}
		return endPointWebTarget;
	}
	
	protected abstract MediaType getMediaType();

	@Override
	public ClientPhoneCallDto addPhoneCall(Long customerId, String startDate, Long duration, String destinationNumber, String phoneCallType) throws InputValidationException, InstanceNotFoundException{
		LocalDateTime localDateTime = LocalDateTime.parse(startDate);
		WebTarget wt = getEndpointWebTarget().path("phoneCall");
			try (Response response = wt.request()
					.accept(this.getMediaType())
					.post(Entity.entity(
							toPhoneCallDtoJaxbConversor.toJaxbCall(customerId, startDate, duration, destinationNumber, phoneCallType, "PENDING"),
							this.getMediaType()))) {
				validateResponse(Response.Status.CREATED.getStatusCode(), response);
				PhoneCallType addedCall = response.readEntity(PhoneCallType.class);
				return toClientCallDtoConversor.toClientCallDtoJaxb(addedCall);
			} catch (InputValidationException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
	}

	@Override
	public ClientCustomerDto addCustomer(String name, String dni, String address, String phoneNumber) throws InputValidationException {
		WebTarget wt = getEndpointWebTarget().path("customers");
			try (Response response = wt.request()
					.accept(this.getMediaType())
					.post(Entity.entity(
							toClientCustomerDtoConversor.toJaxbCustomer(name, dni, address, phoneNumber),
							this.getMediaType()))) {
				validateResponse(Response.Status.CREATED.getStatusCode(), response);
				CustomerType resultCustomer = response.readEntity(CustomerType.class);
				return toClientCustomerDtoConversor.toCustomerDto(resultCustomer);
			} catch (InputValidationException ex) {
				throw ex;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
	}

	@Override
	public void deleteCustomer(Long customerId) throws InstanceNotFoundException {
		WebTarget webtarget = getEndpointWebTarget().path("customers/{id}").resolveTemplate("id",customerId);
			try (Response response = webtarget.request().accept(this.getMediaType()).delete()){
				validateResponse(Response.Status.OK.getStatusCode(), response);
			} catch (InstanceNotFoundException ex){
				throw ex;
			} catch (Exception ex){
				throw new RuntimeException(ex);
			}

	}

	@Override
	public void changeStatus(Long customerId, String mes, String ano, String status) throws InstanceNotFoundException {
		WebTarget webtarget = getEndpointWebTarget()
				.path("phoneCall")
				.queryParam("id", customerId)
				.queryParam("mes", mes)
				.queryParam("ano", ano)
				.queryParam("newStatus", status);
			try (Response response = webtarget.request()
					.accept(this.getMediaType())
					.put(Entity.entity(
							toChangeStatusDtoJaxbConversor.toJaxbChangeStatus(customerId, mes,ano,status),
							this.getMediaType()))){
				validateResponse(Response.Status.OK.getStatusCode(), response);
			} catch (InstanceNotFoundException ex){
				throw ex;
			} catch (Exception ex){
				throw new RuntimeException(ex);
			}
	}

	@Override
	public List<ClientPhoneCallsBtwDatesDto> findCallsBetweenDates(Long customerId, String fechaInicio, String fechaFin, String tipoLlamada, int indice, int cantidad) throws InputValidationException, InstanceNotFoundException {
		WebTarget webTarget = getEndpointWebTarget().path("phoneCall")
				.queryParam("id",customerId)
				.queryParam("Inicio",fechaInicio)
				.queryParam("Fin",fechaFin)
				.queryParam("tipo",tipoLlamada)
				.queryParam("indice",indice)
				.queryParam("cantidad",cantidad);
			try (Response response = webTarget.request().accept(this.getMediaType()).get()){
				validateResponse(Response.Status.OK.getStatusCode(), response);
				List<PhoneCallBtwDatesType> phonecalls = response.readEntity(new GenericType<List<PhoneCallBtwDatesType>>() {});
				return toPhoneCallDtoJaxbConversor.toclientPhoneCallsBtwDatesDtos(phonecalls);
			}catch (InputValidationException | InstanceNotFoundException ex){
				throw ex;
			} catch (Exception ex){
				throw new RuntimeException(ex);
			}

	}

	private void validateResponse(int expectedStatusCode, Response response)
			throws InputValidationException, InstanceNotFoundException, PhoneCallAssociatedException, InvalidStatusException {

		Response.Status statusCode = Response.Status.fromStatusCode(response.getStatus());

		/* Success? */
		if (statusCode.getStatusCode() == expectedStatusCode) {
			return;
		}

		String contentType = response.getMediaType() != null ? response.getMediaType().toString() : null;
		boolean expectedContentType = this.getMediaType().toString().equalsIgnoreCase(contentType);

		if (!expectedContentType && (statusCode != Response.Status.NO_CONTENT)) {
			throw new RuntimeException("HTTP error; status code = " + statusCode);
		}
		switch (statusCode){

			case BAD_REQUEST: {
				InputValidationExceptionType exception = response.readEntity(InputValidationExceptionType.class);
				throw ExceptionConversor.toInputValidationException(exception);
			}
			case NOT_FOUND: {
				InstanceNotFoundExceptionType exception = response.readEntity(InstanceNotFoundExceptionType.class);
				throw ExceptionConversor.toInstanceNotFoundException(exception);
			}case FORBIDDEN:{
				ErrorType exceptionDto = response.readEntity(ErrorType.class);

				if (exceptionDto.getErrorType().equals("InvalidStatusException")){
					throw ExceptionConversor.toInvalidStatusException(exceptionDto);
				}
				if (exceptionDto.getErrorType().equals("PhoneCallAssociated")){
					throw ExceptionConversor.toPhoneCallAssociatedException(exceptionDto);
				}
			}
			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);
		}
	}

}
