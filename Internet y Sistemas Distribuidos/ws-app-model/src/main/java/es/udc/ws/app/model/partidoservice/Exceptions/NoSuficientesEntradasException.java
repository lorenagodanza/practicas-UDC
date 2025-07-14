package es.udc.ws.app.model.partidoservice.Exceptions;

public class NoSuficientesEntradasException extends Exception{
    private Long partidoId;

    public NoSuficientesEntradasException(Long partidoId) {
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
