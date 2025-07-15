package es.udc.rs.telco.client.service.conversors;

import es.udc.rs.telco.client.service.rest.dto.ChangeStatusType;
import es.udc.rs.telco.client.service.rest.dto.ObjectFactory;
import es.udc.rs.telco.client.service.rest.dto.PhoneCallType;
import jakarta.xml.bind.JAXBElement;

public class toChangeStatusDtoJaxbConversor {
    public static ChangeStatusType toChangeStatusDtoJaxb(Long customerId, String mes, String ano, String status) {
        ChangeStatusType changestatus = new ChangeStatusType();
        changestatus.setCustomerId(customerId);
        changestatus.setMes(mes);
        changestatus.setAno(ano);
        changestatus.setStatus(status);
        return changestatus;
    }

    public static JAXBElement<ChangeStatusType> toJaxbChangeStatus(Long customerId,String mes, String ano, String status) {
        ChangeStatusType changestatus = new ChangeStatusType();
        changestatus.setCustomerId(customerId);
        changestatus.setMes(mes);
        changestatus.setAno(ano);
        changestatus.setStatus(status);
        JAXBElement <ChangeStatusType> jaxbElement = new ObjectFactory().createChangeStatus(changestatus);
        return jaxbElement;
    }
}
