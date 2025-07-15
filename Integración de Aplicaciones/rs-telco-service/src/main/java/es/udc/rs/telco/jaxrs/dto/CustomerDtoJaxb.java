package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.glassfish.jersey.internal.inject.Custom;

@XmlRootElement(name = "customer")
@XmlType(name = "customerType", propOrder = {"customerId","name","dni","address","phoneNumber"})
public class CustomerDtoJaxb {

    @XmlElement(required = true)
    private Long customerId;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String dni;

    @XmlElement(required = true)
    private String address;

    @XmlElement(required = true)
    private String phoneNumber;


    public CustomerDtoJaxb(){

    }

    public CustomerDtoJaxb(Long Id,String name, String dni, String address, String phoneNumber){
        this.customerId = Id;
        this.name = name;
        this.dni = dni;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public CustomerDtoJaxb(Long id, String name, String dni){
        this.customerId = id;
        this.name = name;
        this.dni = dni;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "CustomerDtoJaxb{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", dni='" + dni + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
