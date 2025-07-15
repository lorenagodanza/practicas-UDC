package es.udc.rs.telco.jaxrs.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.udc.rs.telco.jaxrs.dto.PhoneCallBtwDatesDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.PhoneCallDtoJaxb;
import es.udc.rs.telco.model.phonecall.PhoneCall;

public class PhoneCallToPhoneCallDtoJaxbConversor {

    public static List<PhoneCallDtoJaxb> toPhoneCallDtoJaxb(List<PhoneCall> phoneCalls) {
        List<PhoneCallDtoJaxb> phoneCallsDtos = new ArrayList<>(phoneCalls.size());
        for (PhoneCall call : phoneCalls) {
            phoneCallsDtos.add(toPhoneCallDtoJaxb(call));
        }
        return phoneCallsDtos;
    }

    public static List<PhoneCallBtwDatesDtoJaxb> toPhoneCallBtwDatesDtoJaxb(List<PhoneCall> phoneCalls) {
        List<PhoneCallBtwDatesDtoJaxb> phoneCallsDtos = new ArrayList<>(phoneCalls.size());
        for (PhoneCall call : phoneCalls) {
            phoneCallsDtos.add(toPhoneCallBtwDatesDtoJaxb(call));
        }
        return phoneCallsDtos;
    }

    public static PhoneCallDtoJaxb toPhoneCallDtoJaxb(PhoneCall phoneCall) {
        return new PhoneCallDtoJaxb(phoneCall.getPhoneCallId(), phoneCall.getCustomerId(),
                phoneCall.getStartDate(),phoneCall.getDuration(),phoneCall.getDestinationNumber(),
                phoneCall.getPhoneCallType().toString(), phoneCall.getPhoneCallStatus().toString());
    }

    public static PhoneCallBtwDatesDtoJaxb toPhoneCallBtwDatesDtoJaxb(PhoneCall phoneCall) {
        return new PhoneCallBtwDatesDtoJaxb(phoneCall.getStartDate(),phoneCall.getDuration(),phoneCall.getDestinationNumber());
    }

    public static PhoneCall toPhoneCall(PhoneCallDtoJaxb phoneCallDto){
        return new PhoneCall(phoneCallDto.getPhoneCallId(),phoneCallDto.getCustomerId(),
                phoneCallDto.getStartDate(),
                phoneCallDto.getDuration(),phoneCallDto.getDestinationNumber(),
                phoneCallDto.getPhoneCallType(), "PENDING");
    }

}
