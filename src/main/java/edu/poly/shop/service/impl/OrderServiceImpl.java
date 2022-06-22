package edu.poly.shop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import edu.poly.shop.domain.Order;
import edu.poly.shop.domain.OrderDetail;
import edu.poly.shop.repository.OrderDetailRepository;
import edu.poly.shop.repository.OrderRepository;
import edu.poly.shop.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderDetailRepository detailRepository;
	
	public <S extends Order> S save(S entity) {
		Set<OrderDetail> set = entity.getOrderDetails();
		
		entity.setOrderDetails(null);
		orderRepository.save(entity);
		
		set.forEach(item->{
			item.setOrder(entity);
		});
		
		detailRepository.saveAll(set);
		
		return entity;
	}

	public <S extends Order> Optional<S> findOne(Example<S> example) {
		return orderRepository.findOne(example);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	public List<Order> findAll(Sort sort) {
		return orderRepository.findAll(sort);
	}

	public List<Order> findAllById(Iterable<Long> ids) {
		return orderRepository.findAllById(ids);
	}

	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	public <S extends Order> List<S> saveAll(Iterable<S> entities) {
		return orderRepository.saveAll(entities);
	}

	public void flush() {
		orderRepository.flush();
	}

	public boolean existsById(Long id) {
		return orderRepository.existsById(id);
	}

	public <S extends Order> S saveAndFlush(S entity) {
		return orderRepository.saveAndFlush(entity);
	}

	public <S extends Order> List<S> saveAllAndFlush(Iterable<S> entities) {
		return orderRepository.saveAllAndFlush(entities);
	}

	public <S extends Order> Page<S> findAll(Example<S> example, Pageable pageable) {
		return orderRepository.findAll(example, pageable);
	}

	public void deleteInBatch(Iterable<Order> entities) {
		orderRepository.deleteInBatch(entities);
	}

	public <S extends Order> long count(Example<S> example) {
		return orderRepository.count(example);
	}

	public void deleteAllInBatch(Iterable<Order> entities) {
		orderRepository.deleteAllInBatch(entities);
	}

	public long count() {
		return orderRepository.count();
	}

	public <S extends Order> boolean exists(Example<S> example) {
		return orderRepository.exists(example);
	}

	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		orderRepository.deleteAllByIdInBatch(ids);
	}

	public void delete(Order entity) {
		orderRepository.delete(entity);
	}

	public <S extends Order, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return orderRepository.findBy(example, queryFunction);
	}

	public void deleteAllById(Iterable<? extends Long> ids) {
		orderRepository.deleteAllById(ids);
	}

	public void deleteAllInBatch() {
		orderRepository.deleteAllInBatch();
	}

	public Order getOne(Long id) {
		return orderRepository.getOne(id);
	}

	public void deleteAll(Iterable<? extends Order> entities) {
		orderRepository.deleteAll(entities);
	}

	public void deleteAll() {
		orderRepository.deleteAll();
	}

	public Order getById(Long id) {
		return orderRepository.getById(id);
	}

	public Order getReferenceById(Long id) {
		return orderRepository.getReferenceById(id);
	}

	public <S extends Order> List<S> findAll(Example<S> example) {
		return orderRepository.findAll(example);
	}

	public <S extends Order> List<S> findAll(Example<S> example, Sort sort) {
		return orderRepository.findAll(example, sort);
	}
	
	
}
