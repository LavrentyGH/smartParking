package com.parking.parkingmanagement.service.impl;

import com.parking.parkingmanagement.entity.Car;
import com.parking.parkingmanagement.entity.ParkingSpot;
import com.parking.parkingmanagement.entity.Reservation;
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

    public ReservationServiceImpl(ReservationRepository reservationRepository, CarService carService, ParkingSpotService parkingSpotService) {
        this.reservationRepository = reservationRepository;
        this.carService = carService;
        this.parkingSpotService = parkingSpotService;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAllActiveWithCarAndOwner();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено с id: " + id));
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        // Проверяем существование автомобиля
        Car car = carService.getCarById(reservation.getCarId());

        // Проверяем существование парковочного места
        ParkingSpot parkingSpot = parkingSpotService.getParkingSpotById(reservation.getSpotId());

        // Проверяем доступность места
        if (!parkingSpot.getIsAvailable()) {
            throw new RuntimeException("Парковочное место недоступно для бронирования");
        }

        // Проверяем нет ли активного бронирования для этого автомобиля
        reservationRepository.findByCarIdAndStatus(car.getId(), "ACTIVE")
                .ifPresent(existingReservation -> {
                    throw new RuntimeException("У этого автомобиля уже есть активное бронирование");
                });

        // Устанавливаем связи
        reservation.setCarId(car.getId());
        reservation.setSpotId(parkingSpot.getId());
        reservation.setStartTime(LocalDateTime.now());
        if (reservation.getCreatedAt() == null) {
            reservation.setCreatedAt(LocalDateTime.now());
        }
        reservation.setStatus("ACTIVE");
        reservation.setIsPaid(false);

        // Помечаем место как занятое
        parkingSpot.setIsAvailable(false);
        parkingSpotService.updateParkingSpot(reservation.getSpotId(), parkingSpot);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation markAsPaid(Long id) {
        Reservation reservation = getReservationById(id);

        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new RuntimeException("Можно оплатить только активное бронирование");
        }

        reservation.setIsPaid(true);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation freeSpot(Long id) {
        Reservation reservation = getReservationById(id);

        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new RuntimeException("Можно освободить только активное бронирование");
        }

        // Освобождаем парковочное место
        ParkingSpot parkingSpot = parkingSpotService.getParkingSpotById(reservation.getSpotId());
        parkingSpot.setIsAvailable(true);
        parkingSpotService.updateParkingSpot(reservation.getSpotId(), parkingSpot);

        // Завершаем бронирование
        reservation.setEndTime(LocalDateTime.now());
        reservation.setStatus("COMPLETED");

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> searchByCarLicensePlate(String licensePlate) {
        return reservationRepository.findByCarLicensePlate(licensePlate);
    }

    @Override
    public List<Reservation> searchByOwnerFullName(String ownerName) {
        return reservationRepository.findByOwnerFullName(ownerName);
    }

    @Override
    public List<Reservation> getActiveReservations() {
        return reservationRepository.findByStatus("ACTIVE");
    }

    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = getReservationById(id);

        if ("ACTIVE".equals(reservation.getStatus())) {
            // Освобождаем место при отмене активного бронирования
            ParkingSpot parkingSpot = parkingSpotService.getParkingSpotById(reservation.getSpotId());
            parkingSpot.setIsAvailable(true);
            parkingSpotService.updateParkingSpot(reservation.getSpotId(), parkingSpot);
        }

        reservation.setStatus("CANCELLED");
        reservation.setEndTime(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
