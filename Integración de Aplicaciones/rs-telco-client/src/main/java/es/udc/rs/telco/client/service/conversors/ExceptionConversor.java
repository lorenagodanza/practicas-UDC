package es.udc.rs.telco.client.service.conversors;

import es.udc.rs.telco.client.service.clientDto.InvalidStatusException;
import es.udc.rs.telco.client.service.clientDto.PhoneCallAssociatedException;
import es.udc.rs.telco.client.service.rest.dto.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class ExceptionConversor {

    public static InputValidationException toInputValidationException(InputValidationExceptionType exDto) {
        return new InputValidationException(exDto.getMessage());
    }

    public static InstanceNotFoundException toInstanceNotFoundException(InstanceNotFoundExceptionType exDto){
        return new InstanceNotFoundException(exDto.getInstanceId(),exDto.getInstanceType());
    }

    public static PhoneCallAssociatedException toPhoneCallAssociatedException(ErrorType exception){
        Long customerId = null;
        for (ErrorParamType exparamDto : exception.getParam()){
            switch (exparamDto.getKey()){
                case "customerId" : {
                    customerId = Long.valueOf(exparamDto.getValue());
                    break;
                }
            }
        }
        return new PhoneCallAssociatedException(customerId);
    }

    public static InvalidStatusException toInvalidStatusException(ErrorType exception){
        Long phoneCallId = null;
        for(ErrorParamType exparamDto : exception.getParam()){
            switch (exparamDto.getKey()){
                case "phoneCallId" : {
                    phoneCallId = Long.valueOf(exparamDto.getValue());
                    break;
                }
            }
        }
        return new InvalidStatusException(phoneCallId);
    }
}
