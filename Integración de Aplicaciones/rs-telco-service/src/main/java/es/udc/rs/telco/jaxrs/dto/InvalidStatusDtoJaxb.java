package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "InvalidStatus")
@XmlType(name = "InvalidStatusType")
public class InvalidStatusDtoJaxb {

    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(required = true)
    private Long phoneCallId;


    public InvalidStatusDtoJaxb() {
    }

    public InvalidStatusDtoJaxb(Long phoneCallId) {
        this.errorType = "Invalid status";
        this.phoneCallId = phoneCallId;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }



    @Override
    public String toString() {
        return "ExceptionDtoJaxb{" +
                "errorType='" + errorType + '\'' +
                ", phoneCallId=" + phoneCallId +
                '}';
    }

    public Long getPhoneCallId() {
        return phoneCallId;
    }

    public void setPhoneCallId(Long phoneCallId) {
        this.phoneCallId = phoneCallId;
    }
}