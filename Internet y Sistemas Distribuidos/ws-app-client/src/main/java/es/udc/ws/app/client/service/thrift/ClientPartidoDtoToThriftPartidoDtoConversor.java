package es.udc.ws.app.client.service.thrift;

import java.time.LocalDateTime;
import java.util.ArrayList;
import es.udc.ws.app.client.service.dto.ClientPartidoDto;
import es.udc.ws.app.thrift.ThriftPartidoDto;
import java.util.List;

public class ClientPartidoDtoToThriftPartidoDtoConversor {
    public static ThriftPartidoDto toThriftPartidoDto(
            ClientPartidoDto clientPartidoDto) {

        Long partidoId = clientPartidoDto.getPartidoId();

        return new ThriftPartidoDto(
                partidoId == null ? -1 : partidoId.longValue(),
                clientPartidoDto.getEquipoVisitante(),
                clientPartidoDto.getFechaHoraCelebracion().toString(),
                clientPartidoDto.getPrecioEntradas(),
                clientPartidoDto.getEntradasDisponibles(),
                clientPartidoDto.getEntradasRestantes());

    }

    public static List<ClientPartidoDto> toClientPartidoDtos(List<ThriftPartidoDto> partidos){

        List<ClientPartidoDto> clientPartidoDtos = new ArrayList<>(partidos.size());

        for (ThriftPartidoDto partido : partidos) {
            clientPartidoDtos.add(toClientPartidoDto(partido));
        }
        return clientPartidoDtos;

    }

    public static ClientPartidoDto toClientPartidoDto(ThriftPartidoDto partido) {

        return new ClientPartidoDto(
                partido.getPartidoId(),
                partido.getEquipoVisitante(),
                LocalDateTime.parse(partido.getFechaHoraCelebracion()),
                partido.getPrecioEntradas(),
                partido.getEntradasDisponibles());

    }

}
