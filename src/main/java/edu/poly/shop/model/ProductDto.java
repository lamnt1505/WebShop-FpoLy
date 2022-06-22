package edu.poly.shop.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable{
	private Long productId;
	@NotEmpty
	private String name;
	@Min(1)
	private int quantity;
	@Min(0)
	private double unitPrice;
	private String image;
	private MultipartFile imageFile;
	private String description;
	@Min(0)
	private double discount;
	private Date enteredDate = new Date();
	private short status;
	private Long categoryId;
	
	private Boolean isEdit;
}
