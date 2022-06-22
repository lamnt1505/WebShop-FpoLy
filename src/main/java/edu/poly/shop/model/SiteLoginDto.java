package edu.poly.shop.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteLoginDto {
	@NotEmpty
	private String phone;
	@NotEmpty
	private String password;
	
	private Boolean rememberMe = false;
}
