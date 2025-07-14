package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.restservice.dto.PartidoToRestPartidoDtoConversor;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.app.restservice.json.JsonToRestPartidoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.udc.ws.util.servlet.ServletUtils.normalizePath;

public class PartidoServlet extends RestHttpServletTemplate {



    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestPartidoDto partidoDto = JsonToRestPartidoDtoConversor.toRestPartidoDto(req.getInputStream());
        Partido partido = PartidoToRestPartidoDtoConversor.toPartido(partidoDto);

        partido = PartidoServiceFactory.getService().darAltaPartido(partido);

        partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);
        String partidoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + partido.getPartidoId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", partidoURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), headers);
    }

   @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
           InputValidationException, InstanceNotFoundException {

       String path = normalizePath(req.getPathInfo());
       if (path != null && path.length() != 0) {
           String idAsString = path.substring(1);

           try {
              Long partidoId = Long.valueOf(idAsString);
               Partido partido = PartidoServiceFactory.getService().buscarPartidoPorId(partidoId);
               RestPartidoDto partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);
               ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                       JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), null);
           } catch (NumberFormatException var6) {
               throw new InputValidationException("Invalid Request: invalid partidoId" + idAsString + "'");
           }
       } else {
           LocalDateTime keyWords = LocalDateTime.parse(req.getParameter("fechaFin"));

           List<Partido> partidos = PartidoServiceFactory.getService().buscarPartidosEntreFechas(LocalDateTime.now(),keyWords);

           List<RestPartidoDto> partidoDtos = PartidoToRestPartidoDtoConversor.toRestPartidoDtos(partidos);
           ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                   JsonToRestPartidoDtoConversor.toArrayNode(partidoDtos), null);
       }
   }

}
