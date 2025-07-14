package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import es.udc.ws.app.client.service.rest.json.JsonToClientPartidoDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientSaleDtoConversor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


public class RestClientPartidoService implements ClientPartidoService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientPartidoService.endpointAddress";
    private String endpointAddress;
    @Override
    public Long darAltaPartido(ClientPartidoDto partido) throws InputValidationException {

        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "partidos").
                    bodyStream(toInputStream(partido), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDto(response.getEntity().getContent()).getPartidoId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //@Override
    public ClientPartidoDto buscarPartidoPorId(Long partidoId) throws InstanceNotFoundException {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "partidos/" + partidoId).execute()
                    .returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDto(response.getEntity().getContent());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientPartidoDto> buscarPartidosEntreFechas(String keywords) {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "partidos?fechaFin="
                            + URLEncoder.encode(keywords, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientPartidoDtoConversor.toClientPartidoDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   //@Override
    public Long buyEntradas(Long partidoId,String emailUsuario, int numeroEntradas,String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException {

        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "compras").
                    bodyForm(
                            Form.form().
                                    add("partidoId", Long.toString(partidoId)).
                                    add("emailusuario", emailUsuario).
                                    add("creditCardNumber", numeroTarjetaBancaria).
                                    add("numeroEntradasCompradas", String.valueOf(numeroEntradas)).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientSaleDtoConversor.toClientSaleDto(
                    response.getEntity().getContent()).getCompraId();

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Override
    public List<ClientSaleDto> obtenerComprasUsuario(String keywords) {
        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.get(getEndpointAddress() + "compras?emailusuario="
                            + URLEncoder.encode(keywords, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientSaleDtoConversor.toClientSaleDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Override
    public void recogerEntradas(Long compraId, String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException {

        try {

            ClassicHttpResponse response = (ClassicHttpResponse) Request.post(getEndpointAddress() + "compras/recoger").
                    bodyForm(
                            Form.form().
                                    add("compraId", Long.toString(compraId)).
                                    add("creditCardNumber", numeroTarjetaBancaria).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);


        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientPartidoDto partido) {
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientPartidoDtoConversor.toObjectNode(partido));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, ClassicHttpResponse response) throws Exception {

        try {

            int statusCode = response.getCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND -> throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_BAD_REQUEST -> throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_FORBIDDEN -> throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                        response.getEntity().getContent());
                case HttpStatus.SC_GONE -> throw JsonToClientExceptionConversor.fromGoneErrorCode(
                        response.getEntity().getContent());
                default -> throw new RuntimeException("HTTP error; status code = "
                        + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
