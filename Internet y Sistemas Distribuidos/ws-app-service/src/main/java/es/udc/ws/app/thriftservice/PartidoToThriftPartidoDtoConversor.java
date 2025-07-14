package es.udc.ws.app.thriftservice;
import es.udc.ws.app.model.Partido.Partido;
import es.udc.ws.app.thrift.ThriftPartidoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidoToThriftPartidoDtoConversor {
    public static List<ThriftPartidoDto> toThriftPartidoDtos(List<Partido> partidos) {
        List<ThriftPartidoDto> partidoDtos = new ArrayList<>();
        for (Partido partido:partidos) {
            partidoDtos.add(toThriftPartidoDto(partido));
        }
        return partidoDtos;
    }

    public static ThriftPartidoDto toThriftPartidoDto(Partido partido) {
        return new ThriftPartidoDto(partido.getPartidoId(), partido.getEquipoVisitante(), partido.getFechaHoraCelebracion().toString(), partido.getPrecioEntradas(),
                partido.getEntradasDisponibles(),partido.getEntradasVendidas());
    }
    public static Partido toPartido(ThriftPartidoDto partido) {
        return new Partido(partido.getPartidoId(), partido.getEquipoVisitante(), LocalDateTime.parse(partido.getFechaHoraCelebracion()), partido.getPrecioEntradas(),
                partido.getEntradasDisponibles(),partido.getEntradasVendidas());
    }



}
