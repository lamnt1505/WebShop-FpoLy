package edu.poly.shop.controller.site;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.poly.shop.domain.Product;
import edu.poly.shop.service.ProductService;
import edu.poly.shop.service.ShoppingCartService;
import edu.poly.shop.service.StorageService;

@Controller
@RequestMapping("site/listProducts")
public class SiteListProductController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	StorageService storageService;
	
	@GetMapping("/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename){
		org.springframework.core.io.Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attchment; filename =\"" + file.getFilename()+ "\"").body(file);
	}
	
	@GetMapping("")
	public String listProducts(ModelMap model,@RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page,@RequestParam("size") Optional<Integer> size) {
		
		List<Product> list = productService.findAll();
		model.addAttribute("products", list);
		
		int currentPage = page.orElse(1);//ngầm định page = 1
		int pageSize = size.orElse(4);//ngầm định size = 4
		
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize,Sort.by("name"));
		Page<Product> resultPage = null;
		
		//khi name có giá trị
		if(org.springframework.util.StringUtils.hasText(name)) {
			resultPage= productService.findByNameContaining(name,pageable);
			model.addAttribute("name",name);
		}else {
			//không có giá trị trả về all
			resultPage = productService.findAll(pageable);
		}
		
		int totalPages = resultPage.getTotalPages();
		if(totalPages>0) {
			int start = Math.max(1, currentPage-2);//ngầm định phân trang nhỏ nhất là 1
			int end = Math.min(currentPage+2, totalPages);//ngầm định phân trang lớn nhất là tổng page
			
			if(totalPages >4) {
				if(end==totalPages) start = end-4;
				else if(start==1) end = start +4;
			}
			
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers",pageNumbers);
		}
		
		model.addAttribute("productPage",resultPage);
		return "site/products/listProduct";
	}
	
	
}
