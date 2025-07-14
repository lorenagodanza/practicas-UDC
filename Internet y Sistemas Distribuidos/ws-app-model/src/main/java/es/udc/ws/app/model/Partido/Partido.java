package es.udc.ws.app.model.Partido;

import java.time.LocalDateTime;
import java.util.Objects;

public class Partido {
    private Long partidoId;
    private String equipoVisitante;
    private LocalDateTime fechaHoraCelebracion;
    private double precioEntradas;
    private int entradasDisponibles;
    private LocalDateTime fechaHoraAlta;
    private int entradasVendidas;


    public Partido(Long partidoId, String equipoVisitante, LocalDateTime fechaHoraCelebracion, double precioEntradas, int entradasDisponibles, LocalDateTime fechaHoraAlta, int entradasVendidas) {
        this.partidoId = partidoId;
        this.equipoVisitante = equipoVisitante;
        this.fechaHoraCelebracion = (fechaHoraCelebracion != null) ? fechaHoraCelebracion.withNano(0) : null;
        this.precioEntradas = precioEntradas;
        this.entradasDisponibles = entradasDisponibles;
        this.fechaHoraAlta = (fechaHoraAlta != null) ? fechaHoraAlta.withNano(0) : null;
        this.entradasVendidas = entradasVendidas;
    }
    public Partido(Long partidoId, String equipoVisitante, LocalDateTime fechaHoraCelebracion, double precioEntradas, int entradasDisponibles, int entradasVendidas) {
        this.partidoId = partidoId;
        this.equipoVisitante = equipoVisitante;
        this.fechaHoraCelebracion = (fechaHoraCelebracion != null) ? fechaHoraCelebracion.withNano(0) : null;
        this.precioEntradas = precioEntradas;
        this.entradasDisponibles = entradasDisponibles;
        this.fechaHoraAlta = (fechaHoraAlta != null) ? fechaHoraAlta.withNano(0) : null;
        this.entradasVendidas = entradasVendidas;
    }
    public Partido( String equipoVisitante, LocalDateTime fechaHoraCelebracion, double precioEntradas, int entradasDisponibles, int entradasVendidas) {
        this.equipoVisitante = equipoVisitante;
        this.fechaHoraCelebracion = (fechaHoraCelebracion != null) ? fechaHoraCelebracion.withNano(0) : null;
        this.precioEntradas = precioEntradas;
        this.entradasDisponibles = entradasDisponibles;
        this.entradasVendidas = entradasVendidas;
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
        this.fechaHoraCelebracion = (fechaHoraCelebracion != null) ? fechaHoraCelebracion.withNano(0) : null;
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

    public LocalDateTime getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(LocalDateTime fechaHoraAlta) {
        this.fechaHoraAlta = (fechaHoraAlta != null) ? fechaHoraAlta.withNano(0) : null;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partido partido = (Partido) o;

        if (Double.compare(precioEntradas, partido.precioEntradas) != 0) return false;
        if (entradasDisponibles != partido.entradasDisponibles) return false;
        if (entradasVendidas != partido.entradasVendidas) return false;
        if (!Objects.equals(partidoId, partido.partidoId)) return false;
        if (!Objects.equals(equipoVisitante, partido.equipoVisitante))
            return false;
        if (!Objects.equals(fechaHoraCelebracion, partido.fechaHoraCelebracion))
            return false;
        return Objects.equals(fechaHoraAlta, partido.fechaHoraAlta);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = partidoId != null ? partidoId.hashCode() : 0;
        result = 31 * result + (equipoVisitante != null ? equipoVisitante.hashCode() : 0);
        result = 31 * result + (fechaHoraCelebracion != null ? fechaHoraCelebracion.hashCode() : 0);
        temp = Double.doubleToLongBits(precioEntradas);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + entradasDisponibles;
        result = 31 * result + (fechaHoraAlta != null ? fechaHoraAlta.hashCode() : 0);
        result = 31 * result + entradasVendidas;
        return result;
    }
}

