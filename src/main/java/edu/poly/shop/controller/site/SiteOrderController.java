package edu.poly.shop.controller.site;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.poly.shop.domain.Customer;
import edu.poly.shop.domain.Order;
import edu.poly.shop.domain.OrderDetail;
import edu.poly.shop.domain.Product;
import edu.poly.shop.model.CustomerDto;
import edu.poly.shop.model.OrderDto;
import edu.poly.shop.service.CustomerService;
import edu.poly.shop.service.OrderService;
import edu.poly.shop.service.ShoppingCartService;

@Controller
@RequestMapping("site/ordersSite")
public class SiteOrderController {
	@Autowired
	OrderService orderService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	private ShoppingCartService cartService;
	
	@GetMapping("views")
	public String view(Model model) {
		OrderDto dto = new OrderDto();
		model.addAttribute("order", dto);
		return "site/ordersSite/orderProduct";
	}
	@GetMapping("addOrder/{customerId}")
	public String addOrder(Model model,@PathVariable("customerId") Long customerId) {
		Optional< Customer> optional = customerService.findById(customerId);
		OrderDto dto = new OrderDto();
		
		Order order = new Order();

			Customer customer = optional.get();
			customer.setCustomerId(optional.get().getCustomerId());
		
		
		order.setCustomer(customer);
		order.setAmount(cartService.getAmount());
		order.setOrderDate(new Date());
		order.setStatus((short)0);
		
		Set<OrderDetail> set = new HashSet<OrderDetail>();
		cartService.getCartItems().forEach(item->{
			OrderDetail orderDetail = new OrderDetail();
			Product product = new Product();
			product.setProductId(item.getProductId());
			orderDetail.setProduct(product);
			orderDetail.setQuantity(item.getQuantity());
			orderDetail.setUnitPrice(item.getUnitPrice());
			
			set.add(orderDetail);
		});
		
		order.setOrderDetails(set);
		orderService.save(order);
		
		
//		dto.setIsEdit(false);
		model.addAttribute("order", dto);
		return "site/ordersSite/orderProduct";
	}
	
}
