package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.entity.Owner;

import java.util.List;

public interface OwnerService {

    List<Owner> getAllOwners();
    Owner getOwnerById(Long id);
    Owner createOwner(Owner owner);

    Owner updateOwner(Long id, Owner ownerDetails);

    void deleteOwner(Long id);

    List<Owner> searchOwners(String fullName);
}
