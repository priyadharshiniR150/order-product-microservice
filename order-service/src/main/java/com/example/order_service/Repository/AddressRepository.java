package com.example.order_service.Repository;


	import java.util.List;

	import org.springframework.data.jpa.repository.JpaRepository;
	import org.springframework.stereotype.Repository;

	import com.example.order_service.Entity.Address;

	@Repository
	public interface AddressRepository extends JpaRepository<Address, Long> {

	    List<Address> findByUserId(Long userId);
	    Address findByUserIdAndIsDefaultTrue(Long userId);

	}

