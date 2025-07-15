package es.udc.rs.telco.client.service.conversors;

import es.udc.rs.telco.client.service.clientDto.ClientCustomerDto;
import es.udc.rs.telco.client.service.rest.dto.CustomerType;
import es.udc.rs.telco.client.service.rest.dto.ObjectFactory;
import jakarta.xml.bind.JAXBElement;

public class toClientCustomerDtoConversor {

    public static JAXBElement<CustomerType> toJaxbCustomer(String name, String dni, String address, String phoneNumber) {
        CustomerType customerType = new CustomerType();
        customerType.setName(name);
        customerType.setDni(dni);
        customerType.setAddress(address);
        customerType.setPhoneNumber(phoneNumber);
        JAXBElement <CustomerType> jaxbElement = new ObjectFactory().createCustomer(customerType);
        return jaxbElement;
    }

    public static ClientCustomerDto toCustomerDto(CustomerType customerType) {
        return new ClientCustomerDto(
                customerType.getCustomerId(),
                customerType.getName(),
                customerType.getDni(),
                customerType.getAddress(),
                customerType.getPhoneNumber()
        );
    }

    public static CustomerType toCustomerType(String name, String dni, String address, String phoneNumber) {
        CustomerType customerType = new CustomerType();
        customerType.setName(name);
        customerType.setDni(dni);
        customerType.setAddress(address);
        customerType.setPhoneNumber(phoneNumber);
        return customerType;
    }
}
