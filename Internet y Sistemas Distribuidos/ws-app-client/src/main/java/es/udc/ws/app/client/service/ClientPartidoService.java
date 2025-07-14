package es.udc.ws.app.client.service;

import com.fasterxml.jackson.databind.exc.InvalidNullException;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.app.client.service.exceptions.ClientNoSuficientesEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoEmpezadoException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;
public interface ClientPartidoService {

    public Long darAltaPartido(ClientPartidoDto partido)
            throws InputValidationException;

    public ClientPartidoDto buscarPartidoPorId(Long partidoId)
            throws InstanceNotFoundException;

    public Long buyEntradas(Long partidoId,String emailUsuario, int numeroEntradas,String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException, ClientNoSuficientesEntradasException, ClientPartidoEmpezadoException;

    public List<ClientPartidoDto> buscarPartidosEntreFechas(String keywords) throws InputValidationException;
/*
    public void recogerEntradas(Long compraId, String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException;
    public List<ClientSaleDto> obtenerComprasUsuario(String keywords);*/
}
