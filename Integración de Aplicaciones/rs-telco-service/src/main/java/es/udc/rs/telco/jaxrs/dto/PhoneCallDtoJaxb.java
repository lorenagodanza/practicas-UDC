package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;

import java.time.LocalDateTime;

@XmlRootElement(name = "phoneCall")
@XmlType(name="phoneCallType", propOrder = {"phoneCallId", "customerId", "startDate",
        "duration", "destinationNumber", "phoneCallType", "phoneCallStatus"})
public class PhoneCallDtoJaxb {

    @XmlElement(required = false)
    private Long phoneCallId;

    @XmlElement(required = true)
    private Long customerId;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private LocalDateTime startDate;

    @XmlElement(required = true)
    private Long duration;

    @XmlElement(required = true)
    private String destinationNumber;

    @XmlElement(required = true)
    private String phoneCallType;

    @XmlElement(required = true)
    private String phoneCallStatus;


    public PhoneCallDtoJaxb(){

    }

    public PhoneCallDtoJaxb(Long phoneCallId, Long clientId, LocalDateTime startDate, Long duration, String destinationNumber,
                            String phoneCallType, String phoneCallStatus){
        this.phoneCallId = phoneCallId;
        this.customerId = clientId;
        this.startDate = startDate;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
        this.phoneCallType = phoneCallType;
        this.phoneCallStatus = phoneCallStatus;
    }

    public Long getPhoneCallId() {
        return phoneCallId;
    }

    public void setPhoneCallId(Long phoneCallId) {
        this.phoneCallId = phoneCallId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public String getPhoneCallType() {
        return phoneCallType;
    }

    public void setPhoneCallType(String phoneCallType) {
        this.phoneCallType = phoneCallType;
    }

    public String getPhoneCallStatus() {
        return phoneCallStatus;
    }

    public void setPhoneCallStatus(String phoneCallStatus) {
        this.phoneCallStatus = phoneCallStatus;
    }

    @Override
    public String toString() {
        return "PhoneCallDtoJaxb{" +
                "phoneCallId=" + phoneCallId + '\'' +
                "customerId=" + customerId + '\'' +
                "startDate=" + startDate + '\'' +
                "duration=" + duration + '\'' +
                "destinationNumber=" + destinationNumber + '\'' +
                "phoneCallType=" + phoneCallType + '\'' +
                "phoneCallStatus=" + phoneCallStatus + '\'' +
                '}';
    }

}
