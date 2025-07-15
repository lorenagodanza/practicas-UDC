package es.udc.rs.telco.client.service;

import es.udc.rs.telco.client.service.clientDto.ClientCustomerDto;
import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallDto;
import es.udc.rs.telco.client.service.clientDto.ClientPhoneCallsBtwDatesDto;
import es.udc.rs.telco.client.service.clientDto.PhoneCallAssociatedException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientTelcoService {
    ClientPhoneCallDto addPhoneCall (Long customerId, String startDate, Long duration, String destinationNumber, String phoneCallType) throws InputValidationException, InstanceNotFoundException;
    ClientCustomerDto addCustomer(String name, String dni, String address, String phoneNumber) throws InputValidationException;
    void deleteCustomer(Long customerId) throws InstanceNotFoundException, PhoneCallAssociatedException;
    void changeStatus(Long phoneCallId, String mes, String ano, String status) throws InstanceNotFoundException;
    List<ClientPhoneCallsBtwDatesDto> findCallsBetweenDates(Long customerId,String fechaInicio, String fechaFin, String tipoLlamada, int indice, int cantidad) throws InputValidationException, InstanceNotFoundException;
}
