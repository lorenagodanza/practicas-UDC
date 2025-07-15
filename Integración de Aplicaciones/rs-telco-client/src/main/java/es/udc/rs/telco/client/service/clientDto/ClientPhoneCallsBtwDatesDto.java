package es.udc.rs.telco.client.service.clientDto;

public class ClientPhoneCallsBtwDatesDto {

    private String startDate;

    private Long duration;

    private String destinationNumber;


    public ClientPhoneCallsBtwDatesDto(){}

    public ClientPhoneCallsBtwDatesDto(String startDate, Long duration, String destinationNumber){
        this.startDate = startDate;
        this.duration = duration;
        this.destinationNumber = destinationNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
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
