package edu.poly.shop.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import edu.poly.shop.domain.CartItem;
import edu.poly.shop.repository.ProductRepository;
import edu.poly.shop.service.ShoppingCartService;


@Service
@SessionScope
public class ShoppingCartServiceImpl implements ShoppingCartService{
	private Map<Long, CartItem> map = new HashMap<Long, CartItem>();
	
	@Override
	public void add(CartItem item) {
		CartItem existedItem = map.get(item.getProductId());
		if(existedItem!=null) {
			existedItem.setQuantity(item.getQuantity()+existedItem.getQuantity());
		}else {
			map.put(item.getProductId(), item);
		}	
	}
	
	@Override
	public void remove(Long productId) {
		map.remove(productId);
	}
	
	@Override
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
	@Override
	public void clear() {
		map.clear();
	}
	
	@Override
	public void update(Long productId, int quantity) {
		CartItem item = map.get(productId);
		item.setQuantity(quantity);
		
		//neu sl<=0 thi xoa sp
		if(item.getQuantity()<=0) {
			map.remove(productId);
		}
	}
	@Override
	public double getAmount() {
		return map.values().stream().mapToDouble(item->item.getQuantity()*item.getUnitPrice()).sum();
	}
	//tra ve so mat hang co trong gio hang

	@Override
	public int getCount() {
		if(map.isEmpty()) {
			return 0;
		}
		return map.values().size();
	}
}
