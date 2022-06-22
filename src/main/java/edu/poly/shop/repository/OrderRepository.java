package edu.poly.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import edu.poly.shop.domain.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
}
