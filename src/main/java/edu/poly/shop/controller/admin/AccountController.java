package edu.poly.shop.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.Account;
import edu.poly.shop.model.AccountDto;
import edu.poly.shop.service.AccountService;


@Controller
@RequestMapping("admin/accounts")
public class AccountController {
	@Autowired
	AccountService accountService;
	
	@GetMapping("add")
	public String add(Model model) {
		model.addAttribute("account", new AccountDto());
		return "admin/accounts/addOrEdit";
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model,
			@Validated @ModelAttribute("account") AccountDto dto,BindingResult result) {
		
		if(result.hasErrors()) {
			return new ModelAndView("admin/accounts/addOrEdit");
		}
		
		Account entity = new Account();
		BeanUtils.copyProperties(dto, entity);//copy dto sang entity
		accountService.save(entity);//lưu
		model.addAttribute("message","Account is saved!");
		
		return new ModelAndView( "redirect:/admin/accounts/searchPaginated",model);
	}
	
	@RequestMapping("searchPaginated")
	public String search(ModelMap model, @RequestParam(name = "username", required = false) String username,
			@RequestParam("page") Optional<Integer> page,@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);//ngầm định page = 1
		int pageSize = size.orElse(5);//ngầm định size = 5
		
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize,Sort.by("username"));
		Page<Account> resultPage = null;
		
		//khi name có giá trị
		if(org.springframework.util.StringUtils.hasText(username)) {
			resultPage= accountService.findByUsernameContaining(username,pageable);
			model.addAttribute("username",username);
		}else {
			//không có giá trị trả về all
			resultPage = accountService.findAll(pageable);
		}
		
		int totalPages = resultPage.getTotalPages();
		if(totalPages>0) {
			int start = Math.max(1, currentPage-2);//ngầm định phân trang nhỏ nhất là 1
			int end = Math.min(currentPage+2, totalPages);//ngầm định phân trang lớn nhất là tổng page
			
			if(totalPages >5) {
				if(end==totalPages) start = end-5;
				else if(start==1) end = start +5;
			}
			
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers",pageNumbers);
		}
		
		model.addAttribute("accountPage",resultPage);
		return "admin/accounts/searchPaginated";
	}
	
	@GetMapping("edit/{username}")
	public ModelAndView edit(ModelMap model, @PathVariable("username") String username) {
		
		Optional<Account> optional = accountService.findById(username);
		AccountDto dto = new AccountDto();
		
		if(optional.isPresent()) {
			
			Account entity = optional.get();
			
			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
			
			dto.setPassword("");
			
			model.addAttribute("account",dto);
			return new ModelAndView("admin/accounts/addOrEdit",model);
		}
		model.addAttribute("message","Account is not existed");
		
		return new ModelAndView("forward:/admin/accounts/searchPaginated",model);
	}
	@GetMapping("delete/{username}")
	public ModelAndView delete(ModelMap model, @PathVariable("username") String username) {
		
			accountService.deleteById(username);
	
		model.addAttribute("message","Account is deleted!");
		
		return new ModelAndView("redirect:/admin/accounts/searchPaginated",model);
	}
//	
//	@RequestMapping("")
//	public String list(ModelMap model) {
//	List<Account> list = accountService.findAll();
//	model.addAttribute("accounts",list);
//		return "admin/accounts/list";
//	}
//	@GetMapping("search")
//	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
//		List<Account> list = null;
//		//khi name có giá trị
//		if(org.springframework.util.StringUtils.hasText(name)) {
//			list= accountService.findByNameContaining(name);
//		}else {
//			//không có giá trị trả về all
//			list = accountService.findAll();
//		}
//		model.addAttribute("accounts",list);
//		return "admin/accounts/search";
//	}

}
