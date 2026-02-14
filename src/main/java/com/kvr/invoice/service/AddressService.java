package com.kvr.invoice.service;

import com.kvr.invoice.model.Address;
import com.kvr.invoice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    
    public Address getAddressByEntity(String entityType, Long entityId) {
        return addressRepository.findByEntityTypeAndEntityId(entityType, entityId)
                .orElseGet(() -> {
                    Address address = new Address();
                    address.setEntityType(entityType);
                    address.setEntityId(entityId);
                    address.setCountry("India");
                    return address;
                });
    }
    
    @Transactional
    public Address saveAddress(Address address, Long userId) {
        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            address.setCountry("India");
        }
        if (address.getId() == null) {
            address.setCreatedBy(userId);
        } else {
            address.setUpdatedBy(userId);
        }
        return addressRepository.save(address);
    }
}
