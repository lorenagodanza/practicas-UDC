package es.udc.rs.telco.jaxrs.dto;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "customerByKeywords")
@XmlType(name = "customerByKeywordsDto", propOrder = {"customerId","name","dni"})
public class CustomerByKeywordsDtoJaxb {

    @XmlElement(required = true)
    private Long customerId;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private String dni;

    public CustomerByKeywordsDtoJaxb(){

    }

    public CustomerByKeywordsDtoJaxb(Long customerId, String name, String dni) {
        this.customerId = customerId;
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

    @Override
    public String toString() {
        return "CustomerByKeywordsDtoJaxb{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", dni='" + dni + '\'' +
                '}';
    }
}
