package es.udc.ws.app.thriftservice;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.model.compra.Sale;
import es.udc.ws.app.model.partidoservice.Exceptions.NoSuficientesEntradasException;
import es.udc.ws.app.model.partidoservice.Exceptions.PartidoEmpezadoException;
import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import es.udc.ws.app.thrift.*;
import es.udc.ws.app.thrift.ThriftPartidoService;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDateTime;
import java.util.List;

public class ThriftPartidoServiceImpl implements ThriftPartidoService.Iface {

    @Override
    public ThriftPartidoDto darAltaPartido(ThriftPartidoDto partidoDto) throws ThriftInputValidationException {

        Partido partido = PartidoToThriftPartidoDtoConversor.toPartido(partidoDto);

        try {
            Partido addedPartido = PartidoServiceFactory.getService().darAltaPartido(partido);
            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDto(addedPartido);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    @Override
    public List<ThriftPartidoDto> buscarPartidosEntreFechas(String keywords) throws ThriftInputValidationException{
        try{
            LocalDateTime fecha=LocalDateTime.parse(keywords);
            List<Partido> partidos = PartidoServiceFactory.getService().buscarPartidosEntreFechas(LocalDateTime.now(),fecha);

            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDtos(partidos);
        }catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }
    @Override
    public ThriftPartidoDto buscarPartidoPorId(long partidoId) throws ThriftInstanceNotFoundException {
        try {
            // Llama al servicio para buscar un partido por su ID
            Partido foundPartido = PartidoServiceFactory.getService().buscarPartidoPorId(partidoId);

            // Convierte el resultado a ThriftPartidoDto y devuelve
            return PartidoToThriftPartidoDtoConversor.toThriftPartidoDto(foundPartido);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),e.getInstanceType().toString());
        }
    }
    @Override
    public ThriftCompraDto buyEntradas(long partidoId, String emailUsuario, String numeroTarjetaBancaria, int numeroEntradas)
            throws ThriftInstanceNotFoundException, ThriftInputValidationException, ThriftoNoSuficientesEntradasException, ThriftoPartidoEmpezadoException{
        try {
            // Llama al servicio para comprar entradas

            Sale boughtPartido = PartidoServiceFactory.getService().comprarEntradas( emailUsuario, partidoId, numeroTarjetaBancaria, numeroEntradas);

            // Convierte el resultado a ThriftPartidoDto y devuelve
            return CompraToThriftCompraDtoConversor.toThriftSaleDto(boughtPartido);
        } catch (InstanceNotFoundException e) {
            // Lanza una excepción ThriftInstanceNotFoundException con los valores obtenidos
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().toString());
        } catch (InputValidationException e) {
            // Lanza una excepción ThriftInputValidationException con el mensaje obtenido
            throw new ThriftInputValidationException(e.getMessage());
        } catch (NoSuficientesEntradasException e) {
            // Lanza una excepción ThriftoNoSuficientesEntradasException con el partidoId obtenido
            throw new ThriftoNoSuficientesEntradasException(e.getPartidoId());
        } catch (PartidoEmpezadoException e) {
            // Lanza una excepción ThriftoPartidoEmpezadoException con el partidoId obtenido
            throw new ThriftoPartidoEmpezadoException(e.getPartidoId());
        }
    }


}

