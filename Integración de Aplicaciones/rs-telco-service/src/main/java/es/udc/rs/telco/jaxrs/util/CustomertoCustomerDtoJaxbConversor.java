package es.udc.rs.telco.jaxrs.util;

import es.udc.rs.telco.jaxrs.dto.CustomerByKeywordsDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.telco.jaxrs.dto.PhoneCallBtwDatesDtoJaxb;
import es.udc.rs.telco.model.customer.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomertoCustomerDtoJaxbConversor {

    public static List<CustomerDtoJaxb> toCustomerDtoJaxb(List<Customer> customers){
        List<CustomerDtoJaxb> customersdto = new ArrayList<>(customers.size());

        for(Customer cust: customers){
            customersdto.add(toCustomerDtoJaxb(cust));
        }

        return customersdto;
    }

    public static CustomerDtoJaxb toCustomerDtoJaxb(Customer customer){
        return new CustomerDtoJaxb(customer.getCustomerId(),customer.getName(),customer.getDni(),customer.getAddress(),customer.getPhoneNumber());
    }

    public static Customer toCustomer(CustomerDtoJaxb customerDto){
        return new Customer(customerDto.getCustomerId(),customerDto.getName(),customerDto.getDni(),customerDto.getAddress(),customerDto.getPhoneNumber());
    }

    public static CustomerByKeywordsDtoJaxb toCustomerDtoByKeywordsJaxb(Customer customer){
        return new CustomerByKeywordsDtoJaxb(customer.getCustomerId(),customer.getName(),customer.getDni());
    }

    public static List<CustomerByKeywordsDtoJaxb> toCustomersDtoByKeywordsJaxbs (List<Customer> customers){
        List<CustomerByKeywordsDtoJaxb> customersDtos = new ArrayList<>(customers.size());
        for (Customer customer : customers){
            customersDtos.add(toCustomerDtoByKeywordsJaxb(customer));
        }
        return customersDtos;
    }

}
