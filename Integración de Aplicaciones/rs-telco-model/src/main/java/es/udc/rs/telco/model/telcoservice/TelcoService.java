package es.udc.rs.telco.model.telcoservice;

import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.exceptions.InvalidStatus;
import es.udc.rs.telco.model.exceptions.PhoneCallAssociated;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface TelcoService {
    Customer addCustomer(String name, String dni, String address, String phonenumber) throws InputValidationException;

    Customer findCustomerbyId(Long id) throws InstanceNotFoundException;

    PhoneCall addPhoneCall(Long clientId, LocalDateTime startDate, Long duration, String destinationNumber, PhoneCallType phoneCallType) throws InputValidationException, InstanceNotFoundException;

    void deleteClient(Long Id) throws InstanceNotFoundException, PhoneCallAssociated;

    Customer findCustomerByDni(String DNI) throws InstanceNotFoundException;
    void updateClient(Long Id,String nombreCompleto, String dni, String dir) throws InputValidationException,InstanceNotFoundException;

    void  modifyCallStatus(Long clientId, int mes, int ano, String newStatus) throws InstanceNotFoundException, InputValidationException,InvalidStatus;

    List<PhoneCall> getPendingPhoneCallsByClientInMonth(Long clientId, int year, int month) throws InstanceNotFoundException, InputValidationException, InvalidStatus;

    public List<Customer> findbyKeywords(String name, int indice, int elementos);

    List<PhoneCall> getCallsBetweenDates(Long clietnId, LocalDateTime fechaIni, LocalDateTime fechaFin, String tipoCall, int Indice, int cantidad) throws InstanceNotFoundException;
}