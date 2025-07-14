package es.udc.ws.app.client.service.exceptions;

public class ClientPartidoEmpezadoException extends Exception {
    private Long partidoId;
    public ClientPartidoEmpezadoException(Long partidoId) {
        super("Partido with id=\"" + partidoId + "\n has already started");
        this.partidoId = partidoId;
    }

    public Long getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Long partidoId) {
        this.partidoId = partidoId;
    }
}
