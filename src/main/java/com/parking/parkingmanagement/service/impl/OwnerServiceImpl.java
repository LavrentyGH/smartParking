package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.entity.Owner;
import com.parking.parkingmanagement.repository.OwnerRepository;
import com.parking.parkingmanagement.service.OwnerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @Override
    public Owner getOwnerById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Владелец не найден с id: " + id));
    }

    @Override
    public Owner createOwner(Owner owner) {
        if (ownerRepository.existsByFullName(owner.getFullName())) {
            throw new RuntimeException("Владелец с таким ФИО уже существует");
        }
        return ownerRepository.save(owner);
    }

    @Override
    public Owner updateOwner(Long id, Owner ownerDetails) {
        Owner owner = getOwnerById(id);
        owner.setFullName(ownerDetails.getFullName());
        return ownerRepository.save(owner);
    }

    @Override
    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }

    @Override
    public List<Owner> searchOwners(String fullName) {
        return ownerRepository.findByFullName(fullName);
    }
}
