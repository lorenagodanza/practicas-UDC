package es.udc.rs.telco.client.service.clientDto;

public class PhoneCallAssociatedException extends Exception{

    private Long customerId;

    public PhoneCallAssociatedException(Long customerId){
        super("The customer with id: " + customerId + " has phone calls associated");
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
