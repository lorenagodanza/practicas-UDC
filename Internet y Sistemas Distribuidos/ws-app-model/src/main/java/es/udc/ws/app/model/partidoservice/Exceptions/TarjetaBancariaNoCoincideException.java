package es.udc.ws.app.model.partidoservice.Exceptions;

public class TarjetaBancariaNoCoincideException extends Exception{
    private Long saleId;

    public TarjetaBancariaNoCoincideException(Long saleId) {
        super("The credit card number of sale with id=\"" + saleId + "\n doesn't match with the entered card number");
        this.saleId = saleId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long movieId) {
        this.saleId = saleId;
    }

}
