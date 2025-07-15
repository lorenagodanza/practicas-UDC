package es.udc.rs.telco.model.exceptions;

public class PhoneCallAssociated extends Exception{
    private Long customerId;
    public PhoneCallAssociated(Long customerId) {
        super("The customer " +  customerId + " has elements phone calls associated, it needs to be empty to delete it" );
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}