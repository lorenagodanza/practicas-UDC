package es.udc.ws.app.model.partidoservice;

import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.compra.Sale;
import es.udc.ws.app.model.partidoservice.Exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


import java.time.LocalDateTime;
import java.util.List;

public interface PartidoService {
    Partido darAltaPartido(Partido partido) throws InputValidationException;
    Sale comprarEntradas(String emailUsuario, Long partidoId, String numeroTarjetaBancaria,int numeroEntradas)  throws InstanceNotFoundException, InputValidationException, NoSuficientesEntradasException, PartidoEmpezadoException;
    public List<Partido> buscarPartidosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws InputValidationException;

    Partido buscarPartidoPorId(Long partidoId) throws InstanceNotFoundException;
    public void marcarEntradasRecogidas(Long compraId, String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException, TarjetaBancariaNoCoincideException, EntradasYaRecogidasException;
    public List<Sale> obtenerComprasPorUsuario(String emailUsuario) throws InputValidationException;

}
