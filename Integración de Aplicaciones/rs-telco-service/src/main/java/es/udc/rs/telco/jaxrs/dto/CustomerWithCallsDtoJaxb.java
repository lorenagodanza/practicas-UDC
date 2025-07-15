package es.udc.rs.telco.jaxrs.dto;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "customerWithCalls")
@XmlType(name = "customerWithCallsType")
public class CustomerWithCallsDtoJaxb {

    @XmlAttribute(required = true)
    private String erroryType;
    @XmlElement(required = true)
    private Long customerId;


    public CustomerWithCallsDtoJaxb(){

    }

    public CustomerWithCallsDtoJaxb(Long customerId){
        this.erroryType = "Customer with phone calls associated";
        this.customerId = customerId;
    }

    public String getErroryType() {
        return erroryType;
    }

    public void setErroryType(String erroryType) {
        this.erroryType = erroryType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerWithCallsDtoJaxb{" +
                "erroryType='" + erroryType + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
