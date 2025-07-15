package es.udc.rs.telco.client.service.conversors;

import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.rest.dto.PhoneCallType;

public class toClientCallDtoConversor {
    public static ClientPhoneCallDto toClientCallDtoJaxb(PhoneCallType phoneCallDto) {
        ClientPhoneCallDto clientPhoneCallDto = new ClientPhoneCallDto();
        clientPhoneCallDto.setPhoneCallId(phoneCallDto.getPhoneCallId());
        clientPhoneCallDto.setCustomerId(phoneCallDto.getCustomerId());
        clientPhoneCallDto.setStartDate(phoneCallDto.getStartDate().toString());
        clientPhoneCallDto.setDuration(phoneCallDto.getDuration());
        clientPhoneCallDto.setDestinationNumber(phoneCallDto.getDestinationNumber());
        clientPhoneCallDto.setPhoneCallType(phoneCallDto.getPhoneCallType());
        clientPhoneCallDto.setPhoneCallStatus(phoneCallDto.getPhoneCallStatus());
        return clientPhoneCallDto;
    }
}
