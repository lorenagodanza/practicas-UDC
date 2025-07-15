package es.udc.rs.telco.model.exceptions;

public class InvalidStatus extends Exception{
    private Long phoneCallId;
    public InvalidStatus(Long phoneCallId) {
        super("The phone call " +  phoneCallId + " has an invalid status" );
        this.phoneCallId = phoneCallId;
    }

    public Long getPhoneCallId() {
        return phoneCallId;
    }

    public void setPhoneCallId(Long phoneCallId) {
        this.phoneCallId = phoneCallId;
    }
}
