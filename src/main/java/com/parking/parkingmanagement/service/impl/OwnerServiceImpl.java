package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.dto.owner.CreateOwnerRequest;
import com.parking.parkingmanagement.dto.owner.OwnerDTO;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.owner.UpdateOwnerRequest;
import com.parking.parkingmanagement.entity.Owner;
import com.parking.parkingmanagement.exception.OwnerAlreadyExistsException;
import com.parking.parkingmanagement.exception.OwnerNotFoundException;
import com.parking.parkingmanagement.mapper.OwnerMapper;
import com.parking.parkingmanagement.repository.OwnerRepository;
import com.parking.parkingmanagement.service.OwnerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    @Override
    public PagedResponse<OwnerDTO> findAllPaged(int page, int size) {
        PagedResponse<Owner> ownerPage = ownerRepository.findAll(page, size);
        List<OwnerDTO> ownerDTOs = ownerPage.getContent().stream()
                .map(ownerMapper::toOwnerDTO)
                .toList();

        return new PagedResponse<>(ownerDTOs,
                ownerPage.getCurrentPage(),
                ownerPage.getTotalPages(),
                ownerPage.getTotalItems(),
                ownerPage.getPageSize());
    }

    @Override
    public List<OwnerDTO> getAllOwners() {
        List<Owner> owners = ownerRepository.findAllWithoutPagination();
        if (owners.isEmpty()) {
            throw new OwnerNotFoundException("Владельцы не найдены");
        }
        return owners.stream().map(ownerMapper::toOwnerDTO).toList();
    }

    @Override
    public OwnerDTO getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
        return ownerMapper.toOwnerDTO(owner);
    }

    @Override
    public OwnerDTO createOwner(CreateOwnerRequest ownerDTO) {
        if (ownerRepository.existsByFullName(ownerDTO.getFullName())) {
            throw new OwnerAlreadyExistsException(ownerDTO.getFullName());
        }

        Owner owner = new Owner();
        owner.setFullName(ownerDTO.getFullName());
        owner.setCreatedAt(LocalDateTime.now());

        Owner savedOwner = ownerRepository.save(owner);
        return ownerMapper.toOwnerDTO(savedOwner);
    }

    @Override
    public OwnerDTO updateOwner(Long id, UpdateOwnerRequest ownerDetails) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));

        if (!owner.getFullName().equals(ownerDetails.getFullName()) &&
            ownerRepository.existsByFullName(ownerDetails.getFullName())) {
            throw new OwnerAlreadyExistsException(ownerDetails.getFullName());
        }

        owner.setFullName(ownerDetails.getFullName());
        Owner updatedOwner = ownerRepository.save(owner);
        return ownerMapper.toOwnerDTO(updatedOwner);
    }

    @Override
    public void deleteOwner(Long id) {
        if (ownerRepository.findById(id).isEmpty()) {
            throw new OwnerNotFoundException(id);
        }
        ownerRepository.deleteById(id);
    }

    @Override
    public List<OwnerDTO> searchOwners(String fullName) {
        // Нужно добавить метод в репозиторий для поиска по части имени
        List<Owner> owners = ownerRepository.findByFullNameContaining(fullName);
        if (owners.isEmpty()) {
            throw new OwnerNotFoundException("Владельцы с именем содержащим: " + fullName + " не найдены");
        }
        return owners.stream().map(ownerMapper::toOwnerDTO).toList();
    }
}
