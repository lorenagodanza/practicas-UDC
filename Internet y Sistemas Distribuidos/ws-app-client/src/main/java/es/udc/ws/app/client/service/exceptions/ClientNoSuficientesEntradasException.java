package es.udc.ws.app.client.service.exceptions;

public class ClientNoSuficientesEntradasException extends Exception {
    private Long partidoId;

    public ClientNoSuficientesEntradasException(Long partidoId) {
        super("Partido with id=\"" + partidoId + "\n has not enought tickets for sale");
        this.partidoId = partidoId;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }
}
