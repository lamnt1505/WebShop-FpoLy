package edu.poly.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.poly.shop.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	@Query(value = "select o from Customer o where o.phone = ?1")
	Optional<Customer> findByPhone(String phone);
	Customer findByEmail(String email);
	Page<Customer> findByNameContaining(String name, Pageable pageable);

}
