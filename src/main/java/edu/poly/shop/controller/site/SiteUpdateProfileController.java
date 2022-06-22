package edu.poly.shop.controller.site;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.Customer;
import edu.poly.shop.model.CustomerDto;
import edu.poly.shop.service.CustomerService;

@Controller
@RequestMapping("site/updateProfile")
public class SiteUpdateProfileController {
	@Autowired
	private CustomerService customerService;
	@GetMapping("view")
	public String viewString (ModelMap model) {
		model.addAttribute("customer",new CustomerDto());
		return "site/customers/profile";
	}
	//update profile
	@GetMapping("edit/{customerId}")
	public ModelAndView edit(ModelMap model, @PathVariable("customerId") Long customerId) {
		Optional<Customer> optional = customerService.findById(customerId);
		CustomerDto dto = new CustomerDto();
		if(optional.isPresent()) {
			Customer cus = optional.get();
			BeanUtils.copyProperties(cus, dto);
			dto.setIsEdit(true);
			model.addAttribute("customer",dto);
			return new ModelAndView("/site/customers/profile",model);
		}
		model.addAttribute("message","Customer is not existed");
		
		return new ModelAndView("forward:/slogin",model);
	}
}
