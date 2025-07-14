package es.udc.ws.app.client.service.thrift;
import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.exceptions.ClientNoSuficientesEntradasException;
import es.udc.ws.app.client.service.exceptions.ClientPartidoEmpezadoException;
import es.udc.ws.app.thrift.*;
import es.udc.ws.app.client.service.ClientPartidoService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class ThriftClientPartidoService implements ClientPartidoService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientPartidoService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
    @Override
    public Long darAltaPartido(ClientPartidoDto partido) throws InputValidationException {

        ThriftPartidoService.Client client = getClient();


        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return client.darAltaPartido(ClientPartidoDtoToThriftPartidoDtoConversor.toThriftPartidoDto(partido)).getPartidoId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientPartidoDto buscarPartidoPorId(Long partidoId) throws InstanceNotFoundException {
        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDto(client.buscarPartidoPorId(partidoId));

        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long buyEntradas(Long partidoId, String emailUsuario, int numeroEntradas, String numeroTarjetaBancaria)
            throws InstanceNotFoundException, InputValidationException,
            ClientNoSuficientesEntradasException, ClientPartidoEmpezadoException {

        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return client.buyEntradas(partidoId, emailUsuario, numeroTarjetaBancaria, numeroEntradas).getPartidoId();

        } catch (ThriftoNoSuficientesEntradasException e) {
            //throw new RuntimeException(e);
            throw new ClientNoSuficientesEntradasException(e.getPartidoId());
        } catch (ThriftoPartidoEmpezadoException e) {
            //throw new RuntimeException(e);
            throw new ClientPartidoEmpezadoException(e.getPartidoId());
        }catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType().toString());
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<ClientPartidoDto> buscarPartidosEntreFechas(String keywords) throws InputValidationException{

        ThriftPartidoService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {

            transport.open();

            return ClientPartidoDtoToThriftPartidoDtoConversor.toClientPartidoDtos(client.buscarPartidosEntreFechas(keywords));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ThriftPartidoService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftPartidoService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}
