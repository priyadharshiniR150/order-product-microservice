package com.example.order_service.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.order_service.Entity.Address;
import com.example.order_service.Service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // Add Address
    @PostMapping
    public Address addAddress(@RequestBody Address address) {

        return addressService.addAddress(address);
    }

    // Get All Addresses
   

    // Update Address
    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id,
                                 @RequestBody Address address) {

        return addressService.updateAddress(id, address);
    }

    // Delete Address
    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable Long id) {

        return addressService.deleteAddress(id);
    }
}