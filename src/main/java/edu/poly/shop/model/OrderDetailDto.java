package edu.poly.shop.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto implements Serializable{
	private Long orderDetailId;
	private Long orderId;
	private Long productId;
	private int quantity;
	private double unitPrice;
	
	
	
}
