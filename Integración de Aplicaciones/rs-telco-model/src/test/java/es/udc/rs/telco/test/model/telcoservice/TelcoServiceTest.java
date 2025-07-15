package es.udc.rs.telco.test.model.telcoservice;

import es.udc.rs.telco.model.customer.Customer;
import es.udc.rs.telco.model.exceptions.InvalidStatus;
import es.udc.rs.telco.model.exceptions.PhoneCallAssociated;
import es.udc.rs.telco.model.phonecall.PhoneCall;
import es.udc.rs.telco.model.phonecall.PhoneCallStatus;
import es.udc.rs.telco.model.phonecall.PhoneCallType;
import es.udc.rs.telco.model.telcoservice.MockTelcoService;
import es.udc.rs.telco.model.telcoservice.TelcoService;
import es.udc.rs.telco.model.telcoservice.TelcoServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TelcoServiceTest {

    private static TelcoService telcoService = null;

    @BeforeAll
    public static void init() {
        telcoService = TelcoServiceFactory.getService();

    }


    private Customer customer1 = new Customer("Raul Lopez", "12345678A", "Zas", "123456789");
    private Customer customer2 = new Customer("Raul2 Lopez", "12345678b", "Zas", "123456781");

    @Test
    public void testAddCustomerAndFindCustomer() throws InputValidationException, InstanceNotFoundException {
        Customer customer = customer1;
        Customer newcustomer;
        Customer foundcustomer;
        try{
            LocalDateTime beforeCreationDate =LocalDateTime.now().withNano(0);
            newcustomer = telcoService.addCustomer(customer.getName(),customer.getDni(),customer.getAddress(), customer.getPhoneNumber());
            LocalDateTime afterCreationDate =LocalDateTime.now().withNano(0);
            foundcustomer = telcoService.findCustomerbyId(newcustomer.getCustomerId());

            assertEquals(newcustomer.getName(),foundcustomer.getName());
            assertEquals(newcustomer.getDni(),foundcustomer.getDni());
            assertEquals(newcustomer.getAddress(),foundcustomer.getAddress());
            assertEquals(newcustomer.getPhoneNumber(),foundcustomer.getPhoneNumber());
            assertTrue((foundcustomer.getCreationDate().compareTo(beforeCreationDate) >= 0) && (foundcustomer.getCreationDate().compareTo(afterCreationDate) <= 0) );

        }finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testFindCustomerByDNI() throws InputValidationException, InstanceNotFoundException {
        Customer customer = telcoService.addCustomer(customer1.getName(), customer1.getDni(),customer1.getAddress(),customer1.getPhoneNumber());
        Customer foundcustomer;
        Customer newcustomer;

        try{
            foundcustomer = telcoService.findCustomerByDni(customer.getDni());
            assertEquals(customer.getName(),foundcustomer.getName());
            assertEquals(customer.getDni(),foundcustomer.getDni());
            assertEquals(customer.getAddress(),foundcustomer.getAddress());
            assertEquals(customer.getPhoneNumber(),foundcustomer.getPhoneNumber());
            assertEquals(customer.getCustomerId(),foundcustomer.getCustomerId());

        }finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testAddandFindInvalidCustomer(){

        try {
            assertThrows(InputValidationException.class, () ->{
                Customer customer = new Customer(null,"12345678A","Zas","123456789");
                telcoService.addCustomer(customer.getName(),customer.getDni(),customer.getAddress(), customer.getPhoneNumber());
            });

            assertThrows(InputValidationException.class, () ->{
                Customer customer = new Customer("Raul Lopez","","Zas","123456789");
                telcoService.addCustomer(customer.getName(),customer.getDni(),customer.getAddress(), customer.getPhoneNumber());
            });

            assertThrows(InstanceNotFoundException.class, () ->
                    telcoService.findCustomerbyId((long)5));
        }
        finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testAddandFindCustomersbyKeywords() throws InstanceNotFoundException,InputValidationException{

        Customer customer1 = new Customer("Raul Lopez", "12345678A", "Zas", "123456789");
        Customer customer2 = new Customer("Lucas Redondo", "78945612B", "Coruna", "987654321");

        Customer customer3 = new Customer("Raul Lopez","99638521E","Vilar","666777888");

        List<Customer> expectedCustomers = new ArrayList<>();
        List<Customer> foundCustomers = new ArrayList<>();

        Customer firstcustomer;
        Customer secondcustomer;
        Customer thirdcustomer;

        firstcustomer = telcoService.addCustomer(customer1.getName(),customer1.getDni(),customer1.getAddress(), customer1.getPhoneNumber());
        expectedCustomers.add(firstcustomer);
        secondcustomer = telcoService.addCustomer(customer2.getName(),customer2.getDni(),customer2.getAddress(), customer2.getPhoneNumber());
        expectedCustomers.add(secondcustomer);
        thirdcustomer = telcoService.addCustomer(customer3.getName(),customer3.getDni(),customer3.getAddress(), customer3.getPhoneNumber());
        expectedCustomers.add(thirdcustomer);



        try{
            foundCustomers = telcoService.findbyKeywords("Raul lopez",1,2);
            assertEquals(expectedCustomers.get(0).getName(),foundCustomers.get(0).getName());
            assertEquals(expectedCustomers.get(2).getName(),foundCustomers.get(1).getName());
            assertEquals(2,foundCustomers.size());

            foundCustomers.clear();

            foundCustomers = telcoService.findbyKeywords("Lucas redondo",1,1);
            assertEquals(expectedCustomers.get(1).getName(),foundCustomers.getFirst().getName());
            assertEquals(1,foundCustomers.size());

            foundCustomers.clear();
            foundCustomers = telcoService.findbyKeywords("Lorena",1,0);
            assertEquals(0,foundCustomers.size());
        }
        finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testDeleteCustomer() throws InputValidationException,InstanceNotFoundException, PhoneCallAssociated {
        Customer customer = new Customer(customer1);
        Customer nuevocustomer;

        try{
            nuevocustomer = telcoService.addCustomer(customer.getName(),customer.getDni(),customer.getAddress(),customer.getPhoneNumber());
            telcoService.deleteClient(nuevocustomer.getCustomerId());
            assertEquals(telcoService.findbyKeywords("Raul",1,1), Collections.EMPTY_LIST);

        }
        finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testDeleteCustomerwithPhoneCall() throws InstanceNotFoundException,PhoneCallAssociated{

        try{
            assertThrows(PhoneCallAssociated.class, () -> {
                Customer customer = new Customer("Raul Lopez","12345678A","Zas","123456789");
                telcoService.addCustomer(customer.getName(),customer.getDni(),customer.getAddress(),customer.getPhoneNumber());
                Customer foundcustomer = telcoService.findCustomerByDni("12345678A");
                PhoneCall phonecall = new PhoneCall(customer.getCustomerId(), LocalDateTime.now(), 90L, "123456789", PhoneCallType.LOCAL);
                telcoService.addPhoneCall(foundcustomer.getCustomerId(),phonecall.getStartDate(),phonecall.getDuration(),phonecall.getDestinationNumber(),phonecall.getPhoneCallType());
                telcoService.deleteClient(foundcustomer.getCustomerId());
            });

        }finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testAddPhoneCallValid() throws InputValidationException, InstanceNotFoundException {
        Customer customer = customer1;
        Customer newcustomer = telcoService.addCustomer(customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber());
        Long idcliente = newcustomer.getCustomerId();
        PhoneCall phoneCall1 = new PhoneCall(idcliente, LocalDateTime.now(), 90L, "123456789", PhoneCallType.LOCAL);
        PhoneCall phoneCall;
        try {
            phoneCall = telcoService.addPhoneCall(phoneCall1.getCustomerId(), phoneCall1.getStartDate(), phoneCall1.getDuration(), phoneCall1.getDestinationNumber(), phoneCall1.getPhoneCallType());
            assertNotNull(phoneCall);
            assertNotNull(phoneCall.getPhoneCallId());
            assertEquals(phoneCall1.getCustomerId(), phoneCall.getCustomerId());
            assertEquals(phoneCall1.getStartDate(), phoneCall.getStartDate());
            assertEquals(phoneCall1.getDuration(), phoneCall.getDuration());
            assertEquals(phoneCall1.getDestinationNumber(), phoneCall.getDestinationNumber());
            assertEquals(phoneCall1.getPhoneCallType(), phoneCall.getPhoneCallType());
            assertEquals(phoneCall.getPhoneCallStatus(), PhoneCallStatus.PENDING);


            List<PhoneCall> phoneCallsList = telcoService.getCallsBetweenDates(idcliente, LocalDateTime.now().minusYears(1), LocalDateTime.now().plusYears(1), null, 1, 10);
            assertTrue(phoneCallsList.stream().anyMatch(call -> call.getPhoneCallId().equals(phoneCall.getPhoneCallId())));

        } finally {
            ((MockTelcoService) telcoService).clear();
        }
    }

    @Test
    public void testAddPhoneCallInvalid() throws InputValidationException {
        Customer customer = customer1;
        Customer newcustomer;
        newcustomer = telcoService.addCustomer(customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber());
        Long idcliente = newcustomer.getCustomerId();
        try {

            // Duración negativa
            assertThrows(InputValidationException.class, () -> {
                telcoService.addPhoneCall(idcliente, LocalDateTime.now(), -1L, "123456789", PhoneCallType.LOCAL);
            });

            // Número de destino vacío
            assertThrows(InputValidationException.class, () -> {
                telcoService.addPhoneCall(idcliente, LocalDateTime.now(), 90L, "", PhoneCallType.LOCAL);
            });

            // Tipo de llamada nulo
            assertThrows(InputValidationException.class, () -> {
                telcoService.addPhoneCall(idcliente, LocalDateTime.now(), 90L, "123456789", null);
            });

        } finally {
            ((MockTelcoService) telcoService).clear();
        }
    }

    @Test
    public void testModifyCallStatus() throws InputValidationException, InstanceNotFoundException, InvalidStatus {
        Customer customer = telcoService.addCustomer("John Doe", "12345678A", "123 Main St", "123456789");
        // Buscar el cliente por DNI
        Customer foundCustomer = telcoService.findCustomerByDni("12345678A");

        Long idcliente = customer.getCustomerId();
        List<PhoneCall> phoneCallsList;

        try {
            PhoneCall phoneCall = new PhoneCall(idcliente, LocalDateTime.now().minusMonths(2), 90L, "123456789", PhoneCallType.LOCAL);
            PhoneCall phoneCall1 = telcoService.addPhoneCall(phoneCall.getCustomerId(), phoneCall.getStartDate(), phoneCall.getDuration(), phoneCall.getDestinationNumber(), phoneCall.getPhoneCallType());
            PhoneCall phoneCall2 = telcoService.addPhoneCall(phoneCall.getCustomerId(), phoneCall.getStartDate(), phoneCall1.getDuration(), phoneCall1.getDestinationNumber(), phoneCall1.getPhoneCallType());


            // PENDING a BILLED
            telcoService.modifyCallStatus(idcliente, phoneCall1.getStartDate().getMonthValue(),phoneCall1.getStartDate().getYear(), "BILLED");
            phoneCallsList=telcoService.getCallsBetweenDates(idcliente,LocalDateTime.now().minusYears(10),LocalDateTime.now(), null,1,100);
            assertSame(PhoneCallStatus.BILLED, phoneCallsList.get(0).getPhoneCallStatus());
            //System.out.println(phoneCallsList);


            // BILLED a PAID
            telcoService.modifyCallStatus(idcliente, phoneCall2.getStartDate().getMonthValue(),phoneCall2.getStartDate().getYear(), "PAID");
            phoneCallsList=telcoService.getCallsBetweenDates(idcliente,LocalDateTime.now().minusYears(10),LocalDateTime.now(), null,1,100);
            assertSame(PhoneCallStatus.PAID, phoneCallsList.get(0).getPhoneCallStatus());
        } finally {
            // Limpiar el servicio mock
            ((MockTelcoService) telcoService).clear();
        }
    }

    @Test
    public void testModifyCallStatusInvalid() throws InstanceNotFoundException, InputValidationException {
        Customer customer = customer1;
        Customer newCustomer = telcoService.addCustomer(customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber());
        Long clientId = newCustomer.getCustomerId();

        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime twoMonthsAgo = now.minusMonths(2);

            // Add phone calls
            telcoService.addPhoneCall(clientId, twoMonthsAgo, 120L, "555-1111", PhoneCallType.LOCAL);
            telcoService.addPhoneCall(clientId, twoMonthsAgo, 60L, "555-2222", PhoneCallType.LOCAL);


            assertThrows(InvalidStatus.class, () -> {
                telcoService.modifyCallStatus(clientId, twoMonthsAgo.getMonthValue(), twoMonthsAgo.getYear(), "PAID");
            });

            // Change status from PENDING to BILLED
            telcoService.modifyCallStatus(clientId, twoMonthsAgo.getMonthValue(), twoMonthsAgo.getYear(), "BILLED");

            // Attempt to change status from BILLED to PENDING
            assertThrows(InvalidStatus.class, () -> {
                telcoService.modifyCallStatus(clientId, twoMonthsAgo.getMonthValue(), twoMonthsAgo.getYear(), "PENDING");
            });

        } catch (InvalidStatus e) {
            throw new RuntimeException(e);
        } finally {
            ((MockTelcoService) telcoService).clear();
        }
    }


    @Test
    public void testgetPendingPhoneCallsByClientInMonth() throws InputValidationException, InstanceNotFoundException, InvalidStatus {
        Customer customer = customer1;
        Customer newcustomer;
        newcustomer = telcoService.addCustomer(customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber());
        Long idcliente = newcustomer.getCustomerId();
        telcoService.addPhoneCall(idcliente, LocalDateTime.of(2022, Month.JANUARY, 5, 10, 0), 120L, "555-1111", PhoneCallType.LOCAL);
        telcoService.addPhoneCall(idcliente, LocalDateTime.of(2022, Month.JANUARY, 15, 11, 0), 150L, "555-2222", PhoneCallType.LOCAL);
        try {
            // Obtener llamadas para enero de 2022
            List<PhoneCall> calls = telcoService.getPendingPhoneCallsByClientInMonth(idcliente, 2022, 1);

            assertEquals(2, calls.size());
            assertTrue(calls.stream().allMatch(call -> call.getPhoneCallStatus() == PhoneCallStatus.PENDING));
        } finally {
            ((MockTelcoService) telcoService).clear();
        }

    }

    //verificar que pasa cando hay alguna chamada que non está PENDING
    @Test
    public void testgetPendingPhoneCallsByClientInMonthInvalid() throws InputValidationException, InstanceNotFoundException, InvalidStatus {
        Customer customer = customer1;
        Customer newcustomer;
        newcustomer = telcoService.addCustomer(customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber());
        Long idcliente = newcustomer.getCustomerId();
        PhoneCall ph1 = telcoService.addPhoneCall(idcliente, LocalDateTime.of(2022, Month.JANUARY, 5, 10, 0), 120L, "555-1111", PhoneCallType.LOCAL);

        // Marcar una llamada como billed
        telcoService.modifyCallStatus(idcliente, ph1.getStartDate().getMonthValue(), ph1.getStartDate().getYear(), "BILLED");

        try {
            assertThrows(InvalidStatus.class, () -> {
                telcoService.getPendingPhoneCallsByClientInMonth(idcliente, 2022, 1);
            });


        } finally {
            ((MockTelcoService) telcoService).clear();
        }
    }
    @Test
    public void testUpdateClient() throws InputValidationException, InstanceNotFoundException {
        Customer customer = telcoService.addCustomer(customer1.getName(), customer1.getDni(),customer1.getAddress(),customer1.getPhoneNumber());
        String newAddres = "Enrique Dequidt";
        telcoService.updateClient(customer.getCustomerId(), customer.getName(),customer.getDni(),newAddres);
        Customer foundCustomer = telcoService.findCustomerbyId(customer.getCustomerId());

        try{
            assertEquals(foundCustomer.getAddress(),newAddres);

        }finally {
            ((MockTelcoService)telcoService).clear();
        }
    }

    @Test
    public void testGetCallsBetweenDates() throws InstanceNotFoundException, InputValidationException {
        Customer customer = customer1;
        Customer newcustomer = telcoService.addCustomer(customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber());
        Long idcliente = newcustomer.getCustomerId();

        // Añadir algunas llamadas telefónicas al cliente en distintas fechas
        telcoService.addPhoneCall(idcliente, LocalDateTime.of(2022, Month.JANUARY, 5, 10, 0), 120L, "555-1111", PhoneCallType.LOCAL);
        telcoService.addPhoneCall(idcliente, LocalDateTime.of(2022, Month.JANUARY, 15, 11, 0), 150L, "555-2222", PhoneCallType.LOCAL);
        telcoService.addPhoneCall(idcliente, LocalDateTime.of(2022, Month.FEBRUARY, 10, 9, 30), 90L, "555-3333", PhoneCallType.LOCAL);

        try {
            //Buscar llamadas dentro de un rango de fechas válido (enero de 2022)
            List<PhoneCall> result = telcoService.getCallsBetweenDates(idcliente,
                    LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0),
                    LocalDateTime.of(2022, Month.JANUARY, 31, 23, 59),
                    "LOCAL", 1, 10);
            assertEquals(2, result.size());
            assertEquals("555-1111", result.get(0).getDestinationNumber());
            assertEquals("555-2222", result.get(1).getDestinationNumber());

            //Buscar llamadas fuera del rango (febrero de 2022)
            result = telcoService.getCallsBetweenDates(idcliente,
                    LocalDateTime.of(2022, Month.FEBRUARY, 1, 0, 0),
                    LocalDateTime.of(2022, Month.FEBRUARY, 28, 23, 59),
                    "LOCAL", 1, 10);
            assertEquals(1, result.size());
            assertEquals("555-3333", result.get(0).getDestinationNumber());

            //Buscar con un cliente inexistente
            assertThrows(InstanceNotFoundException.class, () -> {
                telcoService.getCallsBetweenDates(999L,
                        LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0),
                        LocalDateTime.of(2022, Month.JANUARY, 31, 23, 59),
                        "LOCAL", 1, 10);
            });

            //Probar con índices y cantidad fuera de rango
            assertThrows(IndexOutOfBoundsException.class, () -> {
                telcoService.getCallsBetweenDates(idcliente,
                        LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0),
                        LocalDateTime.of(2022, Month.JANUARY, 31, 23, 59),
                        "LOCAL", 5, 10); // Índice fuera de rango
            });

        } finally {
            // Limpiar datos del servicio mock después de la prueba
            ((MockTelcoService) telcoService).clear();
        }
    }

}