package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;


public class ClientSaleDto {
    private Long compraId;
    private String emailUsuario;
    private Long partidoId;
    private String ultimosCuatroDigitosTarjeta;
    private int numeroEntradasCompradas;
    private LocalDateTime fechaHoraCompra;
    private boolean entradasRecogidas;

    public ClientSaleDto(Long compraId, String emailUsuario, Long partidoId, String ultimosCuatroDigitosTarjeta, int numeroEntradasCompradas, LocalDateTime fechaHoraCompra, boolean entradasRecogidas) {
        this.compraId = compraId;
        this.emailUsuario = emailUsuario;
        this.partidoId = partidoId;
        this.ultimosCuatroDigitosTarjeta = ultimosCuatroDigitosTarjeta;
        this.numeroEntradasCompradas = numeroEntradasCompradas;
        this.fechaHoraCompra = fechaHoraCompra;
        this.entradasRecogidas = entradasRecogidas;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
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

    public String getUltimosCuatroDigitosTarjeta() {
        return ultimosCuatroDigitosTarjeta;
    }

    public void setUltimosCuatroDigitosTarjeta(String ultimosCuatroDigitosTarjeta) {
        this.ultimosCuatroDigitosTarjeta = ultimosCuatroDigitosTarjeta;
    }

    public int getNumeroEntradasCompradas() {
        return numeroEntradasCompradas;
    }

    public void setNumeroEntradasCompradas(int numeroEntradasCompradas) {
        this.numeroEntradasCompradas = numeroEntradasCompradas;
    }

    public LocalDateTime getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public void setFechaHoraCompra(LocalDateTime fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }

    public boolean isEntradasRecogidas() {
        return entradasRecogidas;
    }

    public void setEntradasRecogidas(boolean entradasRecogidas) {
        this.entradasRecogidas = entradasRecogidas;
    }

    @Override
    public String toString() {
        return "ClientSaleDto [compraId=" + compraId + ", emailUsuario=" + emailUsuario
                + ", partidoId=" + partidoId + ", ultimosCuatroDigitosTarjeta=" + ultimosCuatroDigitosTarjeta
                + ", numeroEntradasCompradas=" + numeroEntradasCompradas + ", fechaHoraCompra=" + fechaHoraCompra
                + ", entradasRecogidas=" + entradasRecogidas + "]";
    }


}
