package es.udc.rs.telco.client.service.clientDto;

public class ClientCustomerDto {

    private Long customerId;
    private String name;
    private String dni;
    private String address;
    private String phoneNumber;

    public ClientCustomerDto() {
    }

    public ClientCustomerDto(Long customerId, String name, String dni, String address, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.dni = dni;
        this.address = address;
        this.phoneNumber = phoneNumber;
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
        return "ClientCustomerDto{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", dni='" + dni + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}