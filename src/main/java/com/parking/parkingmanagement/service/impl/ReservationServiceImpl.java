package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.parkingspot.ParkingSpotDTO;
import com.parking.parkingmanagement.dto.reservation.CreateReservationRequest;
import com.parking.parkingmanagement.dto.reservation.ReservationDTO;
import com.parking.parkingmanagement.entity.Reservation;
import com.parking.parkingmanagement.exception.ParkingSpotNotAvailableException;
import com.parking.parkingmanagement.exception.ReservationAlreadyExistsException;
import com.parking.parkingmanagement.exception.ReservationNotFoundException;
import com.parking.parkingmanagement.mapper.ReservationMapper;
import com.parking.parkingmanagement.repository.ReservationRepository;
import com.parking.parkingmanagement.service.CarService;
import com.parking.parkingmanagement.service.ParkingSpotService;
import com.parking.parkingmanagement.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarService carService;
    private final ParkingSpotService parkingSpotService;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  CarService carService,
                                  ParkingSpotService parkingSpotService,
                                  ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.carService = carService;
        this.parkingSpotService = parkingSpotService;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public PagedResponse<ReservationDTO> findAllPaged(int page, int size) {
        // Используем существующий метод, но с пагинацией
        List<Reservation> reservations = reservationRepository.findAllActiveWithCarAndOwner();

        // Применяем пагинацию вручную (или можно добавить в репозиторий)
        int start = page * size;
        int end = Math.min(start + size, reservations.size());
        List<Reservation> pagedContent = reservations.subList(start, end);

        List<ReservationDTO> reservationDTOs = pagedContent.stream()
                .map(reservationMapper::toDTO)
                .toList();

        int totalPages = (int) Math.ceil((double) reservations.size() / size);

        return new PagedResponse<>(reservationDTOs, page, totalPages, (long) reservations.size(), size);
    }

    @Override
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAllActiveWithCarAndOwner();
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Бронирования не найдены");
        }
        return reservations.stream().map(reservationMapper::toDTO).toList();
    }

    @Override
    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        return reservationMapper.toDTO(reservation);
    }

    @Override
    public ReservationDTO createReservation(CreateReservationRequest request) {
        // Проверяем существование автомобиля
        carService.getCarById(request.getCarId());

        // Проверяем существование парковочного места
        ParkingSpotDTO parkingSpot = parkingSpotService.getParkingSpotById(request.getSpotId());

        // Проверяем доступность места
        if (!parkingSpot.getIsAvailable()) {
            throw new ParkingSpotNotAvailableException("Парковочное место недоступно для бронирования");
        }

        // Проверяем нет ли активного бронирования для этого автомобиля
        reservationRepository.findByCarIdAndStatus(request.getCarId(), "ACTIVE")
                .ifPresent(existingReservation -> {
                    throw new ReservationAlreadyExistsException("У этого автомобиля уже есть активное бронирование");
                });

        // Создаем бронирование
        Reservation reservation = new Reservation();
        reservation.setCarId(request.getCarId());
        reservation.setSpotId(request.getSpotId());
        reservation.setStartTime(LocalDateTime.now());
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setStatus("ACTIVE");
        reservation.setIsPaid(false);

        // Помечаем место как занятое
        parkingSpot.setIsAvailable(false);
        parkingSpotService.updateAvailability(request.getSpotId(), false);

        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO markAsPaid(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new IllegalStateException("Можно оплатить только активное бронирование");
        }

        reservation.setIsPaid(true);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(updatedReservation);
    }

    @Override
    public ReservationDTO freeSpot(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new IllegalStateException("Можно освободить только активное бронирование");
        }

        // Освобождаем парковочное место
        parkingSpotService.updateAvailability(reservation.getSpotId(), true);

        // Завершаем бронирование
        reservation.setEndTime(LocalDateTime.now());
        reservation.setStatus("COMPLETED");

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDTO(updatedReservation);
    }

    @Override
    public List<ReservationDTO> searchByCarLicensePlate(String licensePlate) {
        List<Reservation> reservations = reservationRepository.findByCarLicensePlate(licensePlate);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Бронирования для автомобиля с номером " + licensePlate + " не найдены");
        }
        return reservations.stream().map(reservationMapper::toDTO).toList();
    }

    @Override
    public List<ReservationDTO> searchByOwnerFullName(String ownerName) {
        List<Reservation> reservations = reservationRepository.findByOwnerFullName(ownerName);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Бронирования для владельца " + ownerName + " не найдены");
        }
        return reservations.stream().map(reservationMapper::toDTO).toList();
    }

    @Override
    public List<ReservationDTO> getActiveReservations() {
        List<Reservation> reservations = reservationRepository.findByStatus("ACTIVE");
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException("Активные бронирования не найдены");
        }
        return reservations.stream().map(reservationMapper::toDTO).toList();
    }

    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if ("ACTIVE".equals(reservation.getStatus())) {
            // Освобождаем место при отмене активного бронирования
            parkingSpotService.updateAvailability(reservation.getSpotId(), true);
        }

        reservation.setStatus("CANCELLED");
        reservation.setEndTime(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
