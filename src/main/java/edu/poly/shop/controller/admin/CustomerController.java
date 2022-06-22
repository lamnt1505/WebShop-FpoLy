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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.Customer;

import edu.poly.shop.service.CustomerService;
import edu.poly.shop.service.ProductService;
import edu.poly.shop.service.StorageService;

@Controller
@RequestMapping("admin/customers")
public class CustomerController {
	@Autowired
	CustomerService customerService;

	@Autowired
	ProductService productService;

	@Autowired
	StorageService storageService;

//	@GetMapping("add")
//	public String add(Model model) {
//		CustomerDto dto = new CustomerDto();
//		dto.setIsEdit(false);
//		model.addAttribute("customer", dto);
//		return "admin/customers/addOrEdit";
//	}

	@RequestMapping("")
	public String list(ModelMap model) {
		List<Customer> list = customerService.findAll();
		model.addAttribute("customers", list);
		return "admin/customers/list";
	}

	@RequestMapping("searchPaginated")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);// ngầm định page = 1
		int pageSize = size.orElse(5);// ngầm định size = 5

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<Customer> resultPage = null;

		// khi name có giá trị
		if (org.springframework.util.StringUtils.hasText(name)) {
			resultPage = customerService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		} else {
			// không có giá trị trả về all
			resultPage = customerService.findAll(pageable);
		}

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

		model.addAttribute("customerPage", resultPage);
		return "admin/customers/searchPaginated";
	}

	@GetMapping("delete/{customerId}")
	public ModelAndView delete(ModelMap model, @PathVariable("customerId") Long customerId) {

		customerService.deleteById(customerId);

		model.addAttribute("message", "Customer is deleted!");

		return new ModelAndView("forward:/admin/customers/searchPaginated", model);
	}
	// lựa chọn danh mục category
//	@ModelAttribute("categories")
//	public List<CustomerDto> getCategories(){
//		return categoryService.findAll().stream().map(item->{
//			CustomerDto dto = new CustomerDto();
//			BeanUtils.copyProperties(item, dto);
//			return dto;
//		}).toList();
//	}
//	

//	@GetMapping("edit/{customerId}")
//	public ModelAndView edit(ModelMap model, @PathVariable("customerId") Long customerId) {
//		Optional<Product> optional = customerService.findById(customerId);
//		CustomerDto dto = new CustomerDto();
//		
//		if(optional.isPresent()) {
//			Product entity = optional.get();
//			BeanUtils.copyProperties(entity, dto);
//			
//			dto.setCustomerId(entity.getCustomer().getCustomerId());
//			dto.setIsEdit(true);
//			
//			model.addAttribute("customer",dto);
//			return new ModelAndView("admin/customers/addOrEdit",model);
//		}
//		model.addAttribute("message","Product is not existed");
//		
//		return new ModelAndView("forward:/admin/customers/searchPaginated",model);
//	}
//	
//	@GetMapping("/images/{filename:.+}")
//	@ResponseBody
//	public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename){
//		org.springframework.core.io.Resource file = storageService.loadAsResource(filename);
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//				"attchment; filename =\"" + file.getFilename()+ "\"").body(file);
//	}
//	
//	@GetMapping("delete/{customerId}")
//	public ModelAndView delete(ModelMap model, @PathVariable("customerId") Long customerId) throws IOException {
//		
//			Optional<Product> optional = customerService.findById(customerId);
//			
//			if(optional.isPresent()) {
//				if(!org.springframework.util.StringUtils.isEmpty(optional.get().getImage())) {
//					storageService.delete(optional.get().getImage());
//				}
//				customerService.delete(optional.get());
//				model.addAttribute("message","Product is deleted!");
//			}else {
//				model.addAttribute("message","Product is not found!");
//			}
//		
//		
//		return new ModelAndView("forward:/admin/customers/searchPaginated",model);
//	}

//	@GetMapping("search")
//	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
//		List<Product> list = null;
//		//khi name có giá trị
//		if(org.springframework.util.StringUtils.hasText(name)) {
//			list= customerService.findByNameContaining(name);
//		}else {
//			//không có giá trị trả về all
//			list = customerService.findAll();
//		}
//		model.addAttribute("customers",list);
//		return "admin/customers/search";
//	}

}
