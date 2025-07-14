package es.udc.ws.app.model.compra;

import java.time.LocalDateTime;
import java.util.Objects;

public class Sale {
    private Long saleId;
    private String emailUsuario;
    private Long partidoId;
    private String numeroTarjetaBancaria;
    private int numeroEntradasCompradas;
    private LocalDateTime fechaHoraCompra;
    private boolean entradasRecogidas;
    private double precioEntradas;

    public Sale(String emailUsuario, Long partidoId, String numeroTarjetaBancaria, int numeroEntradasCompradas, LocalDateTime fechaHoraCompra, boolean entradasRecogidas, double precioEntradas) {
        this.emailUsuario = emailUsuario;
        this.partidoId = partidoId;
        this.numeroTarjetaBancaria = numeroTarjetaBancaria;
        this.numeroEntradasCompradas = numeroEntradasCompradas;
        this.fechaHoraCompra = (fechaHoraCompra != null) ? fechaHoraCompra.withNano(0) : null;
        this.entradasRecogidas = entradasRecogidas;
        this.precioEntradas = precioEntradas;
    }

    public Sale(Long saleId, String emailUsuario, Long partidoId, String numeroTarjetaBancaria, int numeroEntradasCompradas, LocalDateTime fechaHoraCompra, boolean entradasRecogidas, double precioEntradas) {
        this.saleId = saleId;
        this.emailUsuario = emailUsuario;
        this.partidoId = partidoId;
        this.numeroTarjetaBancaria = numeroTarjetaBancaria;
        this.numeroEntradasCompradas = numeroEntradasCompradas;
        this.fechaHoraCompra = (fechaHoraCompra != null) ? fechaHoraCompra.withNano(0) : null;        this.entradasRecogidas = entradasRecogidas;
        this.precioEntradas = precioEntradas;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }

    public String getNumeroTarjetaBancaria() {
        return numeroTarjetaBancaria;
    }

    public void setNumeroTarjetaBancaria(String numeroTarjetaBancaria) {
        this.numeroTarjetaBancaria = numeroTarjetaBancaria;
    }

    public int getNumeroEntradasCompradas() {
        return numeroEntradasCompradas;
    }

    public void setNumeroEntradasCompradas(int numeroEntradasCompradas) {
        this.numeroEntradasCompradas = numeroEntradasCompradas;
    }

    public LocalDateTime getFechaHoraCompra() {return fechaHoraCompra;}

    public void setFechaHoraCompra(LocalDateTime fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }

    public boolean isEntradasRecogidas() {
        return entradasRecogidas;
    }

    public void setEntradasRecogidas(boolean entradasRecogidas) {
        this.entradasRecogidas = entradasRecogidas;
    }

    public double getPrecioEntrada() {
        return precioEntradas;
    }

    public void setPrecioEntrada(float precioEntrada) {
        this.precioEntradas = precioEntrada;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sale sale = (Sale) o;

        if (numeroEntradasCompradas != sale.numeroEntradasCompradas) return false;
        if (entradasRecogidas != sale.entradasRecogidas) return false;
        if (Double.compare(sale.precioEntradas, precioEntradas) != 0) return false;
        if (!Objects.equals(saleId, sale.saleId)) return false;
        if (!Objects.equals(emailUsuario, sale.emailUsuario)) return false;
        if (!Objects.equals(partidoId, sale.partidoId)) return false;
        if (!Objects.equals(numeroTarjetaBancaria, sale.numeroTarjetaBancaria))
            return false;
        return Objects.equals(fechaHoraCompra, sale.fechaHoraCompra);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = saleId != null ? saleId.hashCode() : 0;
        result = 31 * result + (emailUsuario != null ? emailUsuario.hashCode() : 0);
        result = 31 * result + (partidoId != null ? partidoId.hashCode() : 0);
        result = 31 * result + (numeroTarjetaBancaria != null ? numeroTarjetaBancaria.hashCode() : 0);
        result = 31 * result + numeroEntradasCompradas;
        result = 31 * result + (fechaHoraCompra != null ? fechaHoraCompra.hashCode() : 0);
        result = 31 * result + (entradasRecogidas ? 1 : 0);
        temp = Double.doubleToLongBits(precioEntradas);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

