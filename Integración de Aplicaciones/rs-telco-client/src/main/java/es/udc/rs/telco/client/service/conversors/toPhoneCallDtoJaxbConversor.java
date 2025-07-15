package es.udc.rs.telco.client.service.conversors;

import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallsBtwDatesDto;
import es.udc.rs.telco.client.service.rest.dto.*;
import jakarta.xml.bind.JAXBElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class toPhoneCallDtoJaxbConversor {
    public static PhoneCallType toPhoneCallDtoJaxb(Long customerId, String startDate, Long duration, String destinationNumber, String phoneCallType, String status) {
        PhoneCallType serviceCall = new PhoneCallType();
        serviceCall.setCustomerId(customerId);
        LocalDateTime localDateTime = LocalDateTime.parse(startDate);
        serviceCall.setStartDate(localDateTime);
        serviceCall.setDuration(duration);
        serviceCall.setDestinationNumber(destinationNumber);
        serviceCall.setPhoneCallType(phoneCallType);
        serviceCall.setPhoneCallStatus(status);
        return serviceCall;
    }

    public static JAXBElement<PhoneCallType> toJaxbCall(Long customerId, String startDate, Long duration, String destinationNumber, String phoneCallType, String status) {
        PhoneCallType serviceCall = new PhoneCallType();
        LocalDateTime localDateTime = LocalDateTime.parse(startDate);
        serviceCall.setCustomerId(customerId);
        serviceCall.setStartDate(localDateTime);
        serviceCall.setDuration(duration);
        serviceCall.setDestinationNumber(destinationNumber);
        serviceCall.setPhoneCallType(phoneCallType);
        serviceCall.setPhoneCallStatus(status);
        JAXBElement <PhoneCallType> jaxbElement = new ObjectFactory().createPhoneCall(serviceCall);
        return jaxbElement;
    }

    public static List<ClientPhoneCallsBtwDatesDto> toclientPhoneCallsBtwDatesDtos (List<PhoneCallBtwDatesType> phonecalls){
        List<ClientPhoneCallsBtwDatesDto> llamadas = new ArrayList<>(phonecalls.size());
        for (PhoneCallBtwDatesType calls : phonecalls){
            llamadas.add(toclientPhoneCallsBtwDatesDto(calls));
        }
        return llamadas;
    }

    public static ClientPhoneCallsBtwDatesDto toclientPhoneCallsBtwDatesDto(PhoneCallBtwDatesType phoneCall) {
        return new ClientPhoneCallsBtwDatesDto(phoneCall.getStartDate().toString(),phoneCall.getDuration(),phoneCall.getDestinationNumber());
    }

}
