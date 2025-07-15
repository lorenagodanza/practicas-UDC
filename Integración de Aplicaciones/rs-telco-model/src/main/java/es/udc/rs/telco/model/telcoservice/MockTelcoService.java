package es.udc.rs.telco.model.telcoservice;

import java.time.LocalDateTime;
import java.util.*;

import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.exceptions.InvalidStatus;
import es.udc.rs.telco.model.exceptions.PhoneCallAssociated;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.validation.PropertyValidator;

public class MockTelcoService implements TelcoService {

	private Map<Long, Customer> clientsMap = new LinkedHashMap<Long, Customer>();
	private Map<Long,PhoneCall> phoneCallsMap = new LinkedHashMap<Long,PhoneCall>();
	private Map<Long,List<PhoneCall>> phoneCallsByUserMap = new LinkedHashMap<Long,List<PhoneCall>>();

	private long lastClientId = 0;
	private long lastPhoneCallId = 0;


	private synchronized long getNextClientId() {
		return ++lastClientId;
	}

	private synchronized long getNextPhoneCallId() {
		return ++lastPhoneCallId;
	}

	@Override
	public Customer addCustomer(String name, String dni, String address, String phonenumber) throws InputValidationException {
		Long customerid = getNextClientId();

		LocalDateTime creationdate = LocalDateTime.now().withNano(0);
		Customer customer = new Customer(customerid,name,dni,address,phonenumber,creationdate);

		validateCustomer(customer);

		clientsMap.put(customer.getCustomerId(),customer);

		return new Customer(customerid,name,dni,address,phonenumber,creationdate);
	}

	@Override
	public Customer findCustomerbyId(Long id) throws InstanceNotFoundException {
		Customer customer = clientsMap.get(id);

		if(customer == null){
			throw new InstanceNotFoundException(id,Customer.class.getName());
		}

		return new Customer(customer);
	}

	@Override
	public List<Customer> findbyKeywords(String name, int indice, int elementos){
		List<Customer> foundCustomers = new ArrayList<>();
		String searchedName = name !=null ? name.toLowerCase() : "";

		int numeroencontrados = 0;
		for (Customer c: clientsMap.values()){
			if(c.getName().toLowerCase().contains(searchedName)){
				foundCustomers.add(new Customer(c));
			}
		}

		if(foundCustomers.isEmpty()){
			return foundCustomers;
		}

		numeroencontrados = foundCustomers.size();

		if (indice > numeroencontrados | indice <= 0){
			throw new IndexOutOfBoundsException("Indice inexistente");
		}

		int finLista = Math.min(indice + elementos - 1, numeroencontrados);

		return foundCustomers.subList(indice - 1, finLista);
	}

	private static void validateCustomer (Customer c) throws InputValidationException{
		if(c == null){
			throw new InputValidationException("Customer cannot be null\n");
		}
		PropertyValidator.validateMandatoryString("name",c.getName());
		PropertyValidator.validateMandatoryString("dni",c.getDni());
		PropertyValidator.validateMandatoryString("address",c.getAddress());
		PropertyValidator.validateMandatoryString("phonenumber",c.getPhoneNumber());
		validatePhoneNumber(c.getPhoneNumber());
		validateDNI(c.getDni());
	}

	private static void validatePhoneNumber(String phoneNumber) throws InputValidationException {
		if (!phoneNumber.matches("^[0-9]{9}$")) {
			throw new InputValidationException("Invalid phone number. It must have 9 digits.\n");
		}
	}

	private static void validateDNI(String dni) throws InputValidationException {
		if (!dni.matches("^[0-9]{8}[A-Z]$")) {
			throw new InputValidationException("Invalid DNI. It must have 8 numbers followed by a letter.\n");
		}
	}

	private static void validateMes(int mes) throws InputValidationException {
		if ( mes < 1 || mes > 12){
			throw new InputValidationException("Invalid month. It must be between January and December.\n");
		}
	}

	@Override
	public PhoneCall addPhoneCall(Long clientId,LocalDateTime startDate,Long duration,String destinationNumber,PhoneCallType phoneCallType) throws InputValidationException, InstanceNotFoundException {
		Customer customer = clientsMap.get(clientId);
		if (customer == null) {
			throw new InstanceNotFoundException(clientId, Customer.class.getName());
		}
		Long phoneCallId = getNextPhoneCallId();
		PhoneCall phoneCall = new PhoneCall(phoneCallId,clientId, startDate, duration, destinationNumber, phoneCallType, null);
		if (phoneCall.getStartDate().isAfter(LocalDateTime.now())){
			throw new InputValidationException("La fecha no puede ser futura a la actual");
		}
		phoneCall.setPhoneCallStatus(PhoneCallStatus.PENDING);
		PhoneCallStatus phoneCallStatus = phoneCall.getPhoneCallStatus();
		validatePhoneCall(phoneCall);
		phoneCallsMap.put(phoneCallId,phoneCall);
		Long customerId = phoneCall.getCustomerId();
		phoneCallsByUserMap.computeIfAbsent(customerId, k -> new ArrayList<>()).add(phoneCall);
		return new PhoneCall(phoneCallId,clientId,startDate,duration,destinationNumber,phoneCallType,phoneCallStatus); //devolvemos copia para mais seguridad

	}
	public Map<Long, PhoneCall> getPhoneCallsMap() {
		return phoneCallsMap;
	}



	private static void validatePhoneCall(PhoneCall phoneCall) throws InputValidationException {
		if (phoneCall == null) {
			throw new InputValidationException("PhoneCall cannot be null\n");
		}

		PropertyValidator.validateMandatoryString("destinationNumber", phoneCall.getDestinationNumber());
		PropertyValidator.validateMandatoryString("startDate", phoneCall.getStartDate().toString());
		PropertyValidator.validateLong("duration", phoneCall.getDuration(), 0, Integer.MAX_VALUE);
		if (phoneCall.getPhoneCallType() == null) {
			throw new InputValidationException("phoneCallType cannot be null\n");
		}

	}

	@Override
	public  void  modifyCallStatus(Long clientId,int mes, int ano, String newStatus) throws InstanceNotFoundException, InputValidationException,InvalidStatus{

		Customer customer = findCustomerbyId(clientId);
		Boolean cambio = false;

		List <PhoneCall> phoneCalls = phoneCallsByUserMap.get(clientId);
		int mes_actual=LocalDateTime.now().getMonthValue();
		int ano_actual= LocalDateTime.now().getYear();

		validateMes(mes);

		if (mes>=mes_actual && ano>=ano_actual) {
			throw new InputValidationException("PhoneCall can't be modified until the month ends");
		}

		if (PhoneCallStatus.valueOf(newStatus) == null) {
			throw new InputValidationException("Estatus inválido");
		}

		for (PhoneCall phoneCall : phoneCalls) {
			if(phoneCall.getStartDate().getYear() == ano && phoneCall.getStartDate().getMonthValue() == mes) {
				if (phoneCall.getPhoneCallStatus() == PhoneCallStatus.PENDING && PhoneCallStatus.valueOf(newStatus) == PhoneCallStatus.BILLED) {
					phoneCall.setPhoneCallStatus(PhoneCallStatus.valueOf(newStatus));
					phoneCallsByUserMap.replace(clientId, phoneCalls);
					cambio = true;
				} else if (phoneCall.getPhoneCallStatus() == PhoneCallStatus.BILLED && PhoneCallStatus.valueOf(newStatus) == PhoneCallStatus.PAID) {
					phoneCall.setPhoneCallStatus(PhoneCallStatus.valueOf(newStatus));
					phoneCallsByUserMap.replace(clientId, phoneCalls);
					cambio = true;

				} else {
					throw new InvalidStatus(phoneCall.getPhoneCallId());
				}
			}
		}


	}

	public List<PhoneCall> getPendingPhoneCallsByClientInMonth(Long clientId, int year, int month) throws InstanceNotFoundException, InputValidationException, InvalidStatus {
		// Verificar si o cliente existe
		if (!clientsMap.containsKey(clientId)) {
			throw new InstanceNotFoundException(clientId, "El cliente que menciona no existe");
		}

		// Verificar que ano e anterior ao actual
		if (year > LocalDateTime.now().getYear()) {
			throw new InputValidationException("El año no puede ser mayor al actual");
		}

		List<PhoneCall> result = new ArrayList<>();
		List<PhoneCall> phoneCalls = phoneCallsByUserMap.get(clientId);
		boolean allPending = true;

		if (phoneCalls != null) {
			for (PhoneCall phoneCall : phoneCalls) {
				LocalDateTime callDate = phoneCall.getStartDate();

				// Comprobar si a chamada pertenece ao mes e ano que nos dan
				if (callDate.getYear() == year && callDate.getMonthValue() == month) {
					result.add(phoneCall);

					// Comprobar si a chamada está pending
					if (phoneCall.getPhoneCallStatus() != PhoneCallStatus.PENDING) {
						allPending = false;
						throw new InvalidStatus(phoneCall.getPhoneCallId());
					}
				}
			}
		}



		return result;
	}



	public void clear(){
		clientsMap.clear();
		phoneCallsMap.clear();
		phoneCallsByUserMap.clear();
		//Reiniciar IDs
		lastClientId = 0;
		lastPhoneCallId = 0;
	}

	@Override
	public void deleteClient(Long Id) throws InstanceNotFoundException, PhoneCallAssociated{

		List<PhoneCall> asociadas = new ArrayList<>();

		if ((clientsMap.get(Id) == null) || (!clientsMap.containsKey(Id))){
			throw new InstanceNotFoundException(Id,Customer.class.getName());
		}

		asociadas = phoneCallsByUserMap.get(Id);

		if(asociadas != null){
			throw new PhoneCallAssociated(Id);
		}

		clientsMap.remove(Id);
	}

	@Override
	public Customer findCustomerByDni(String DNI) throws InstanceNotFoundException{

		Customer usuario = null;

		for (Map.Entry<Long, Customer> entry : clientsMap.entrySet()){
			if (entry.getValue().getDni().equalsIgnoreCase(DNI)){
				usuario = entry.getValue();
			}
		}
		if (usuario == null){throw new InstanceNotFoundException(DNI,Customer.class.getName());}

		return usuario;
	}

	@Override
	public void updateClient(Long id, String nombreCompleto, String dni, String dir) throws InputValidationException, InstanceNotFoundException {
		Customer cliente = clientsMap.get(id);

		validateDNI(dni);

		if (cliente == null) {
			throw new InstanceNotFoundException(id, Customer.class.getName());
		}
		cliente.setName(nombreCompleto);
		cliente.setDni(dni);
		cliente.setAddress(dir);
		clientsMap.replace(id, cliente);
	}

	@Override
	public List<PhoneCall> getCallsBetweenDates(Long clientId, LocalDateTime fechaIni, LocalDateTime fechaFin, String tipoCall, int indice, int cantidad) throws InstanceNotFoundException {

		List<PhoneCall> phoneCallsList = new ArrayList<>();

		//Vemos si existe el cliente solicitante
		if (!phoneCallsByUserMap.containsKey(clientId)){
			throw new InstanceNotFoundException(clientId,"El cliente que menciona no existe");
		}

		//Filtramos lladas por "tipo" y "rango de fechas"
		for (PhoneCall call : phoneCallsByUserMap.get(clientId)) {
			if (tipoCall != null) {
				if ((Objects.equals(call.getPhoneCallType().toString(), tipoCall)) &&
						(!call.getStartDate().isBefore(fechaIni)) && (!call.getStartDate().isAfter(fechaFin))) {
					phoneCallsList.add(new PhoneCall(call));
				}
			} else {
				if ((!call.getStartDate().isBefore(fechaIni)) && (!call.getStartDate().isAfter(fechaFin))) {
					phoneCallsList.add(new PhoneCall(call));
				}
			}
		}
		//Vemos si nos pasan un índice con sentido que esté dentro del nº de llamadas
		int nObj = phoneCallsList.size();
		if (indice > nObj | indice <= 0){
			throw new IndexOutOfBoundsException("Indice inexistente");
		}

		//Vemos el índice del último elemento que se devolvería, bien un índice previo a final, o bien el fin de la lista en sí
		int finLista = Math.min(indice +cantidad-1, nObj);

		return phoneCallsList.subList(indice -1, finLista);
	}


}
