package com.parking.parkingmanagement.repository;

import com.parking.parkingmanagement.entity.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setCarId(rs.getLong("car_id"));
        reservation.setSpotId(rs.getLong("spot_id"));
        reservation.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());

        if (rs.getTimestamp("end_time") != null) {
            reservation.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        }

        reservation.setIsPaid(rs.getBoolean("is_paid"));
        reservation.setStatus(rs.getString("status"));
        reservation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Для JOIN запросов
        try {
            reservation.setCarLicensePlate(rs.getString("license_plate"));
            reservation.setOwnerFullName(rs.getString("owner_name"));
            reservation.setSpotNumber(rs.getString("spot_number"));
        } catch (Exception e) {
            // Игнорируем если колонки нет
        }

        return reservation;
    };

    public List<Reservation> findAllActiveWithCarAndOwner() {
        String sql = """
            SELECT r.*, c.license_plate, o.full_name as owner_name, ps.spot_number
            FROM reservations r
            JOIN cars c ON r.car_id = c.id
            JOIN owners o ON c.owner_id = o.id
            JOIN parking_spots ps ON r.spot_id = ps.id
            WHERE r.status = 'ACTIVE'
            ORDER BY r.created_at DESC
            """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, rowMapper, id);
        return reservations.stream().findFirst();
    }

    public List<Reservation> findByCarLicensePlate(String licensePlate) {
        String sql = """
            SELECT r.*, c.license_plate, o.full_name as owner_name, ps.spot_number
            FROM reservations r
            JOIN cars c ON r.car_id = c.id
            JOIN owners o ON c.owner_id = o.id
            JOIN parking_spots ps ON r.spot_id = ps.id
            WHERE c.license_plate ILIKE ? AND r.status = 'ACTIVE'
            ORDER BY r.created_at DESC
            """;
        return jdbcTemplate.query(sql, rowMapper, "%" + licensePlate + "%");
    }

    public List<Reservation> findByOwnerFullName(String ownerName) {
        String sql = """
            SELECT r.*, c.license_plate, o.full_name as owner_name, ps.spot_number
            FROM reservations r
            JOIN cars c ON r.car_id = c.id
            JOIN owners o ON c.owner_id = o.id
            JOIN parking_spots ps ON r.spot_id = ps.id
            WHERE o.full_name ILIKE ? AND r.status = 'ACTIVE'
            ORDER BY r.created_at DESC
            """;
        return jdbcTemplate.query(sql, rowMapper, "%" + ownerName + "%");
    }

    public Optional<Reservation> findByCarIdAndStatus(Long carId, String status) {
        String sql = "SELECT * FROM reservations WHERE car_id = ? AND status = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, rowMapper, carId, status);
        return reservations.stream().findFirst();
    }

    public List<Reservation> findByStatus(String status) {
        String sql = "SELECT * FROM reservations WHERE status = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, status);
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            String sql = """
                INSERT INTO reservations (car_id, spot_id, start_time, end_time, is_paid, status) 
                VALUES (?, ?, ?, ?, ?, ?) RETURNING id
                """;
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    reservation.getCarId(), reservation.getSpotId(),
                    reservation.getStartTime(), reservation.getEndTime(),
                    reservation.getIsPaid(), reservation.getStatus());
            reservation.setId(id);
        } else {
            String sql = """
                UPDATE reservations 
                SET car_id = ?, spot_id = ?, start_time = ?, end_time = ?, is_paid = ?, status = ? 
                WHERE id = ?
                """;
            jdbcTemplate.update(sql,
                    reservation.getCarId(), reservation.getSpotId(),
                    reservation.getStartTime(), reservation.getEndTime(),
                    reservation.getIsPaid(), reservation.getStatus(),
                    reservation.getId());
        }
        return reservation;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}