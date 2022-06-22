package edu.poly.shop.service;


import java.util.Collection;

import edu.poly.shop.domain.CartItem;

public interface ShoppingCartService {

	void add(CartItem item);

	void remove(Long productId);

	Collection<CartItem> getCartItems();

	void clear();

	void update(Long productId, int quantity);

	double getAmount();

	int getCount();
	

}
