package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.client.service.dto.ClientSaleDto;
import es.udc.ws.app.thrift.ThriftCompraDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientSaleDtoToThriftSaleDtoConversor {
    public static ThriftCompraDto toThriftCompraDto(
            ClientSaleDto clientSaleDto) {

        Long compraId = clientSaleDto.getCompraId();

        return new ThriftCompraDto(
                compraId == null ? -1 : compraId.longValue(),
                clientSaleDto.getEmailUsuario(),
                clientSaleDto.getPartidoId(),
                clientSaleDto.getUltimosCuatroDigitosTarjeta(),
                clientSaleDto.getNumeroEntradasCompradas(),
                clientSaleDto.getFechaHoraCompra().toString(),
                clientSaleDto.isEntradasRecogidas());

    }

    public static List<ClientSaleDto> toThriftCompraDtos(List<ThriftCompraDto> compras){

        List<ClientSaleDto> clientSaleDtos = new ArrayList<>(compras.size());

        for (ThriftCompraDto compra : compras) {
            clientSaleDtos.add(toClientSaleDto(compra));
        }
        return clientSaleDtos;

    }

    private static ClientSaleDto toClientSaleDto(ThriftCompraDto compra) {

        return new ClientSaleDto(
                compra.getCompraId(),
                compra.getEmailUsuario(),
                compra.getPartidoId(),
                compra.getUltimosCuatroDigitosTarjeta(),
                compra.getNumeroEntradasCompradas(),
                LocalDateTime.parse(compra.getFechaHoraCompra()),
                compra.isEntradasRecogidas());

    }
}
