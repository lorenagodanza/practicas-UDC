package es.udc.ws.app.model.partidoservice.Exceptions;

public class EntradasYaRecogidasException extends Exception{
    private Long saleId;

    public EntradasYaRecogidasException(Long saleId) {
        super("The tickets from sale with id=\"" + saleId + "\n have already been collected");
        this.saleId = saleId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long movieId) {
        this.saleId = saleId;
    }

}
