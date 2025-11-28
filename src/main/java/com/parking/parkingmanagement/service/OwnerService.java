package com.parking.parkingmanagement.service;

import com.parking.parkingmanagement.dto.owner.CreateOwnerRequest;
import com.parking.parkingmanagement.dto.owner.OwnerDTO;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.owner.UpdateOwnerRequest;

import java.util.List;

public interface OwnerService {

    PagedResponse<OwnerDTO> findAllPaged(int page, int size);
    List<OwnerDTO> getAllOwners();
    OwnerDTO getOwnerById(Long id);
    OwnerDTO createOwner(CreateOwnerRequest ownerDTO);
    OwnerDTO updateOwner(Long id, UpdateOwnerRequest ownerDetails);
    void deleteOwner(Long id);
    List<OwnerDTO> searchOwners(String fullName);
}
