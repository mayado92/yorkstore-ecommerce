package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    //create address for a user
    @PostMapping("/create/address")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO createdAddressDTO = addressService.createAddress(addressDTO,user);

        return new ResponseEntity<>(createdAddressDTO, HttpStatus.CREATED);
    }


    //create address for a user
    @GetMapping("/admin/get/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addressDTOList = addressService.getAddresses();
        return new ResponseEntity<List<AddressDTO>>(addressDTOList, HttpStatus.CREATED);
    }

    //create address for a user
    @GetMapping("/get/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddresses(@PathVariable Long addressId){
        AddressDTO createdAddressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(createdAddressDTO, HttpStatus.OK);
    }


    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressList = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/update/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId
            , @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/delete/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
