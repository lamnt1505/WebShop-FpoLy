package edu.poly.shop.controller.site;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;


import edu.poly.shop.domain.Customer;

import edu.poly.shop.model.CustomerDto;
import edu.poly.shop.model.MailInfo;
import edu.poly.shop.service.CustomerService;
import edu.poly.shop.service.MailerService;

@Controller
@RequestMapping("customers")
public class SiteCustomerController {
	@Autowired
	CustomerService customerService;
	
	@Autowired
	MailerService mailerService;
	
	@Autowired
	HttpSession session;
	@GetMapping("registered")
	public String add(Model model) {
		CustomerDto dto = new CustomerDto();
//		dto.setIsEdit(false);
		model.addAttribute("customer", dto);
		return "site/customers/registeredCustomer";
	}
	
	//dang ky
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Validated @ModelAttribute("customer") CustomerDto dto,BindingResult result) {
		
		if(result.hasErrors()) {
			return new ModelAndView("site/customers/addOrEdit");
		}
		
		Customer entity = new Customer();
		BeanUtils.copyProperties(dto, entity);//copy dto sang entity
		
		customerService.save(entity);//lưu
		model.addAttribute("message","You is registered successful!");
		
		return new ModelAndView( "forward:/site/customers/login/",model);
	}
	
	//quen mat khau 
	@GetMapping("forgotPass")
	public String mailer(ModelMap model) {
		model.addAttribute("customers", new MailInfo());
		return "site/customers/forgotPassword";
	}
	
	@PostMapping("forgotPass")
	public String sendMail(Model model,@RequestParam("email") String email) {
		try {
			Customer customer = customerService.findByEmail(email);
			if(email!=null) {
				mailerService.send(email, "Password", customer.getPassword());
				model.addAttribute("message","Send is seccussful");
			}else {
				model.addAttribute("message","Send is not seccussful");
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/slogin";
	}
}
