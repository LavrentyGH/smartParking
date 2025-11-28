package com.parking.parkingmanagement.mapper;

import com.parking.parkingmanagement.dto.owner.OwnerDTO;
import com.parking.parkingmanagement.entity.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public OwnerDTO toOwnerDTO(Owner owner) {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setId(owner.getId());
        ownerDTO.setFullName(owner.getFullName());
        ownerDTO.setCreatedAt(owner.getCreatedAt());
        return ownerDTO;
    }
}
