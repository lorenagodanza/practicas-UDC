package es.udc.rs.telco.jaxrs.dto;


import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "changeStatus")
@XmlType(name = "changeStatusType", propOrder = { "customerId","mes", "ano", "status"})

public class ChangeStatusDtoJaxb {

    @XmlElement(required = true)
    private Long customerId;

    @XmlElement(required = true)
    private String mes;

    @XmlElement(required = true)
    private String ano;

    @XmlElement(required = true)
    private String status;

    public ChangeStatusDtoJaxb() {
    }

    public ChangeStatusDtoJaxb(Long customerId, String mes, String ano, String status) {
        this.customerId = customerId;
        this.mes = mes;
        this.ano = ano;
        this.status = status;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
