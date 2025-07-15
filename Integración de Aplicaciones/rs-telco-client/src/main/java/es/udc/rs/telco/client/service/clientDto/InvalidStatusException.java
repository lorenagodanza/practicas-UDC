package es.udc.rs.telco.client.service.clientDto;

public class InvalidStatusException extends Exception{
    private Long phoneCallId;
    public InvalidStatusException(Long phoneCallId) {
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
