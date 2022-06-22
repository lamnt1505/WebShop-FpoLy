package edu.poly.shop.model;

import java.io.Serializable;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable{
	private Long orderId;
	private Date orderDate = new Date();
	private double amount;
	private short status;
	private Long customerId;
}
