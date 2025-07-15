package es.udc.rs.telco.model.customer;

import java.time.LocalDateTime;

public class Customer {

    private Long customerId;
    private String name;
    private String dni;
    private String address;
    private LocalDateTime creationDate;
    private String phoneNumber;
    

	public Customer(String name, String dni, String address, String phoneNumber) {
		super();
		this.name = name;
		this.dni = dni;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public Customer(Long id, String name,String dni, String address, String phoneNumber){
		this.customerId = id;
		this.name = name;
		this.dni = dni;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public Customer(Long id, String name,String dni, String address, String phoneNumber, LocalDateTime creationDate){
		this(id,name,dni,address,phoneNumber);
		this.creationDate = creationDate != null ? creationDate.withNano(0) : null;
	}



	public Customer(Customer customer){
		this(customer.getCustomerId(),customer.getName(), customer.getDni(), customer.getAddress(), customer.getPhoneNumber(), customer.getCreationDate());
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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"customerId=" + customerId +
				", name='" + name + '\'' +
				", dni='" + dni + '\'' +
				", address='" + address + '\'' +
				", creationDate=" + creationDate +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}

}