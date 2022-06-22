package edu.poly.shop.controller.admin;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.poly.shop.domain.Order;
import edu.poly.shop.service.OrderService;

@Controller
@RequestMapping("admin/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
//	@GetMapping("delete/{orderId}")
//	public ModelAndView delete(ModelMap model, @PathVariable("orderId") Long orderId) {
//		
//			orderService.deleteById(orderId);
//	
//		model.addAttribute("message","Order is deleted!");
//		
//		return new ModelAndView("forward:/admin/orders/searchPaginated",model);
//	}

	@RequestMapping("searchPaginated")
	public String search(ModelMap model,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);// ngầm định page = 1
		int pageSize = size.orElse(5);// ngầm định size = 5

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(Direction.DESC,"orderDate"));
		Page<Order> resultPage = orderService.findAll(pageable);

		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);// ngầm định phân trang nhỏ nhất là 1
			int end = Math.min(currentPage + 2, totalPages);// ngầm định phân trang lớn nhất là tổng page

			if (totalPages > 5) {
				if (end == totalPages)
					start = end - 5;
				else if (start == 1)
					end = start + 5;
			}

			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("orderPage", resultPage);
		return "admin/orders/searchPaginated";
	}
}
