package com.parking.parkingmanagement.controller;

import com.parking.parkingmanagement.dto.ApiResponse;
import com.parking.parkingmanagement.dto.owner.CreateOwnerRequest;
import com.parking.parkingmanagement.dto.owner.OwnerDTO;
import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.owner.UpdateOwnerRequest;
import com.parking.parkingmanagement.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "http://localhost:3000")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/paged")
    public ApiResponse<PagedResponse<OwnerDTO>> getOwnersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PagedResponse<OwnerDTO> owners = ownerService.findAllPaged(page, size);
        return ApiResponse.success(owners, "Владельцы получены успешно");
    }

    @GetMapping
    public ApiResponse<List<OwnerDTO>> getAllOwners() {
        List<OwnerDTO> owners = ownerService.getAllOwners();
        return ApiResponse.success(owners, "Владельцы получены успешно");
    }

    @GetMapping("/{id}")
    public ApiResponse<OwnerDTO> getOwnerById(@PathVariable Long id) {
        OwnerDTO owner = ownerService.getOwnerById(id);
        return ApiResponse.success(owner, "Владелец найден");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OwnerDTO>> createOwner(@RequestBody CreateOwnerRequest ownerDTO) {
        OwnerDTO owner = ownerService.createOwner(ownerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(owner, "Владелец создан успешно"));
    }

    @PutMapping("/{id}")
    public ApiResponse<OwnerDTO> updateOwner(@PathVariable Long id, @Valid @RequestBody UpdateOwnerRequest updateOwnerRequest) {
        OwnerDTO owner = ownerService.updateOwner(id, updateOwnerRequest);
        return ApiResponse.success(owner, "Владелец обновлен успешно");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ApiResponse.success(null, "Владелец удален успешно");
    }

    @GetMapping("/search")
    public ApiResponse<List<OwnerDTO>> searchOwners(@RequestParam String fullName) {
        List<OwnerDTO> owners = ownerService.searchOwners(fullName);
        return ApiResponse.success(owners, "Поиск владельцев завершен");
    }
}
