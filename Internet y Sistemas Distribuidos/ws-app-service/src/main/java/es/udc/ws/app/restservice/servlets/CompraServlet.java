package es.udc.ws.app.restservice.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import es.udc.ws.app.model.compra.Sale;
import es.udc.ws.app.model.partidoservice.Exceptions.EntradasYaRecogidasException;
import es.udc.ws.app.model.partidoservice.Exceptions.NoSuficientesEntradasException;
import es.udc.ws.app.model.partidoservice.Exceptions.PartidoEmpezadoException;
import es.udc.ws.app.model.partidoservice.Exceptions.TarjetaBancariaNoCoincideException;
import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import es.udc.ws.app.restservice.dto.CompraToRestCompraDtoConversor;
import es.udc.ws.app.restservice.dto.RestCompraDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestSaleDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import java.util.List;

public class CompraServlet extends RestHttpServletTemplate{
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        String requestUri = req.getRequestURI();
        if (requestUri.endsWith("/compras")) {
            ServletUtils.checkEmptyPath(req);
            String email = ServletUtils.getMandatoryParameter(req,"emailusuario");
                Long partidoId = ServletUtils.getMandatoryParameterAsLong(req, "partidoId");
                String creditCardNumber = ServletUtils.getMandatoryParameter(req, "creditCardNumber");
                Long num_entradas = ServletUtils.getMandatoryParameterAsLong(req, "numeroEntradasCompradas");
                try {
                    Sale sale = PartidoServiceFactory.getService().comprarEntradas(email, partidoId, creditCardNumber, Math.toIntExact(num_entradas));
                    RestCompraDto saleDto = CompraToRestCompraDtoConversor.toRestSaleDto(sale);
                    String saleURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + sale.getSaleId().toString();
                    Map<String, String> headers = new HashMap<>(1);
                    headers.put("Location", saleURL);
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                            JsonToRestSaleDtoConversor.toObjectNode(saleDto), headers);
                } catch (NoSuficientesEntradasException e) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            AppExceptionToJsonConversor.toNoSuficientesEntradasExceptionJson(e),
                            null);
                    return;
                } catch (PartidoEmpezadoException e) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            AppExceptionToJsonConversor.toPartidoEmpezadoExceptionJson(e),
                            null);
                    return;
                }
        } else if(requestUri.endsWith("/compras/recoger")){
            Long compraId = ServletUtils.getMandatoryParameterAsLong(req,"compraId");
            String creditCardNumber = ServletUtils.getMandatoryParameter(req,"creditCardNumber");
            try {
            PartidoServiceFactory.getService().marcarEntradasRecogidas(compraId,creditCardNumber);
            Map<String, String> headers = new HashMap<>(1);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT,
                    null, headers);
            }catch(TarjetaBancariaNoCoincideException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        AppExceptionToJsonConversor.toTarjetaBancariaNoCoincideExceptionJson(e),
                        null);
                return;
            } catch ( EntradasYaRecogidasException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        AppExceptionToJsonConversor.toEntradasYaRecogidasExceptionJson(e),
                        null);
                return;
            }
        }
    }
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        String userEmail = req.getParameter("emailusuario");

        if (userEmail != null && !userEmail.isEmpty()) {
            try {
                // Lógica para obtener las compras del usuario con el correo electrónico proporcionado
                List<Sale> compras = PartidoServiceFactory.getService().obtenerComprasPorUsuario(userEmail);

                // Convertir las compras a DTO si es necesario
                List<RestCompraDto> compraDtos = CompraToRestCompraDtoConversor.toRestSaleDtos(compras);

                // Escribir la respuesta del servicio
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        JsonToRestSaleDtoConversor.toArrayNode(compraDtos), null);

            } catch (InputValidationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new InputValidationException("Parámetro 'emailUsuario' no proporcionado");
        }
    }

}
