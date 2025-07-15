package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.time.LocalDateTime;

@XmlRootElement(name = "phoneCallBtwDates")
@XmlType(name="phoneCallBtwDatesType", propOrder = {"startDate", "duration", "destinationNumber"})
public class PhoneCallBtwDatesDtoJaxb {

    @XmlAttribute(required = true)
    private LocalDateTime startDate;

    @XmlAttribute(required = true)
    private Long duration;

    @XmlAttribute(required = true)
    private String destinationNumber;


    public PhoneCallBtwDatesDtoJaxb(){

    }

    public PhoneCallBtwDatesDtoJaxb(LocalDateTime startDate, Long duration, String destinationNumber){
        this.startDate = startDate;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
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

    @Override
    public String toString() {
        return "PhoneCallDtoJaxb{" +
                "startDate=" + startDate + '\'' +
                "duration=" + duration + '\'' +
                "destinationNumber=" + destinationNumber + '\'' +
                '}';
    }

}
