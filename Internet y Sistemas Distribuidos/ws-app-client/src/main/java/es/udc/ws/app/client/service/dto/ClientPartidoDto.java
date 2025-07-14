package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientPartidoDto {

    private Long partidoId;
    private String equipoVisitante;

    private LocalDateTime fechaHoraCelebracion;
    private double precioEntradas;
    private int entradasDisponibles;
    private int entradasRestantes;


    public ClientPartidoDto(Long partidoId, String equipoVisitante, LocalDateTime fechaHoraCelebracion, double precioEntradas, int entradasDisponibles) {
        this.partidoId = partidoId;
        this.equipoVisitante = equipoVisitante;
        this.fechaHoraCelebracion = fechaHoraCelebracion;
        this.precioEntradas = precioEntradas;
        this.entradasDisponibles = entradasDisponibles;
        //this.entradasRestantes = entradasRestantes;
    }
    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public LocalDateTime getFechaHoraCelebracion() {
        return fechaHoraCelebracion;
    }

    public void setFechaHoraCelebracion(LocalDateTime fechaHoraCelebracion) {
        this.fechaHoraCelebracion = fechaHoraCelebracion;
    }

    public double getPrecioEntradas() {
        return precioEntradas;
    }

    public void setPrecioEntradas(double precioEntradas) {
        this.precioEntradas = precioEntradas;
    }

    public int getEntradasDisponibles() {
        return entradasDisponibles;
    }

    public void setEntradasDisponibles(int entradasDisponibles) {
        this.entradasDisponibles = entradasDisponibles;
    }

    public int getEntradasRestantes() {
        return entradasRestantes;
    }

    public void setEntradasRestantes(int entradasRestantes) {
        this.entradasRestantes = entradasRestantes;
    }

    @Override
    public String toString() {
        return "PartidoDto [partidoId=" + partidoId + ", equipoVisitante=" + equipoVisitante
                + ", fechaHoraCelebracion=" + fechaHoraCelebracion + " precioEntradas " + precioEntradas +
                 ", entradasDisponibles=" + entradasDisponibles  + "]";
    }
}
