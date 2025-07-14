package es.udc.ws.app.model.partidoservice.Exceptions;

public class PartidoEmpezadoException extends Exception{
    private Long partidoId;
    public PartidoEmpezadoException(Long partidoId) {
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
