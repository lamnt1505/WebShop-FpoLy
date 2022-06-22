package edu.poly.shop.controller.site;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.poly.shop.domain.CartItem;
import edu.poly.shop.domain.Product;
import edu.poly.shop.model.ProductDto;
import edu.poly.shop.service.ProductService;
import edu.poly.shop.service.ShoppingCartService;
import edu.poly.shop.service.StorageService;

@Controller
@RequestMapping("site/shopping-cart")
public class ShoppingCartController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@GetMapping("view")
	public String list(Model model) {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems",cartItems);
		model.addAttribute("total",shoppingCartService.getAmount());
		model.addAttribute("NoOfItems",shoppingCartService.getCount());
		return "site/products/cart";
	}
	
	@GetMapping("add/{productId}")
	public String add(@PathVariable("productId") Long productId) {
		Optional<Product> product = productService.findById(productId);
		if(product!=null) {
			CartItem item = new CartItem();
//			BeanUtils.copyProperties(product, item);
			item.setProductId(product.get().getProductId());
			item.setName(product.get().getName());
			item.setUnitPrice(product.get().getUnitPrice());
			item.setQuantity(1);
			shoppingCartService.add(item);
		}
		return "redirect:/site/shopping-cart/view";
	}
	
	@GetMapping("remove/{productId}")
	public String remove(@PathVariable("productId") Long productId) {
		shoppingCartService.remove(productId);
		return "redirect:/site/shopping-cart/view";
	}
	@PostMapping("update")
	public String update(@RequestParam Long productId,@RequestParam int quantity) {
		shoppingCartService.update(productId, quantity);
		return "redirect:/site/shopping-cart/view";
	}
	@GetMapping("clear")
	public String clear() {
		shoppingCartService.clear();
		return "redirect:/site/shopping-cart/view";
	}
	
	
}
