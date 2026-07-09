package com.example.order_service.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.order_service.Entity.Address;
import com.example.order_service.Repository.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // Add Address
    public Address addAddress(Address address) {

        address.setUserId(1L); // Temporary (Later JWT userId)

        return addressRepository.save(address);
    }

   

    // Update Address
    public Address updateAddress(Long id, Address addressRequest) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address Not Found"));

        address.setName(addressRequest.getName());
        address.setPhone(addressRequest.getPhone());
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPincode(addressRequest.getPincode());

        return addressRepository.save(address);
    }

    // Delete Address
    public String deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address Not Found"));

        addressRepository.delete(address);

        return "Address Deleted Successfully";
    }
}