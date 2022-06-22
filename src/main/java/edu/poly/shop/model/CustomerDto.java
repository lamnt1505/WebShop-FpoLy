package edu.poly.shop.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable{
	private Long customerId;
	@NotEmpty
	@Length(min = 3)
	private String name;
	@NotEmpty
	@Length(min = 3)
	private String password;
	@NotEmpty
	@Email
	private String email;
	@NotEmpty
	private String phone;
	private Date registeredDate = new Date();
	private short status;
	
	private Boolean isEdit = false;
	
}
