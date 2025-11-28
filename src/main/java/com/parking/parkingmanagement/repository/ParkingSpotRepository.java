package com.parking.parkingmanagement.repository;

import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.dto.parkingspot.ParkingSpotDTO;
import com.parking.parkingmanagement.entity.ParkingSpot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ParkingSpotRepository {

    private final JdbcTemplate jdbcTemplate;

    public ParkingSpotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ParkingSpot> rowMapper = (rs, rowNum) -> {
        ParkingSpot spot = new ParkingSpot();
        spot.setId(rs.getLong("id"));
        spot.setSpotNumber(rs.getString("spot_number"));
        spot.setIsAvailable(rs.getBoolean("is_available"));
        spot.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return spot;
    };

    private final RowMapper<ParkingSpotDTO> spotDTORowMapper = (rs, rowNum) -> {
        ParkingSpotDTO spot = new ParkingSpotDTO();
        spot.setId(rs.getLong("id"));
        spot.setSpotNumber(rs.getString("spot_number"));
        spot.setIsAvailable(rs.getBoolean("is_available"));
        spot.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return spot;
    };

    public PagedResponse<ParkingSpot> findAllPaged(int page, int size) {
        int offset = page * size;

        String sql = "SELECT * FROM parking_spots ORDER BY spot_number LIMIT ? OFFSET ?";
        List<ParkingSpot> content = jdbcTemplate.query(sql, rowMapper, size, offset);

        String countSql = "SELECT COUNT(*) FROM parking_spots";
        Long totalItems = jdbcTemplate.queryForObject(countSql, Long.class);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return new PagedResponse<>(content, page, totalPages, totalItems, size);
    }

    public List<ParkingSpot> findAll() {
        String sql = "SELECT * FROM parking_spots ORDER BY spot_number";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ParkingSpot> findById(Long id) {
        String sql = "SELECT * FROM parking_spots WHERE id = ?";
        List<ParkingSpot> spots = jdbcTemplate.query(sql, rowMapper, id);
        return spots.stream().findFirst();
    }

    public Optional<ParkingSpot> findBySpotNumber(String spotNumber) {
        String sql = "SELECT * FROM parking_spots WHERE spot_number = ?";
        List<ParkingSpot> spots = jdbcTemplate.query(sql, rowMapper, spotNumber);
        return spots.stream().findFirst();
    }

    public List<ParkingSpot> findAvailableSpots() {
        String sql = """
            SELECT ps.* FROM parking_spots ps
            WHERE ps.is_available = true
            AND ps.id NOT IN (
                SELECT r.spot_id FROM reservations r
                WHERE r.status = 'ACTIVE'
            )
            ORDER BY ps.spot_number
            """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public ParkingSpot save(ParkingSpot spot) {
        if (spot.getId() == null) {
            String sql = "INSERT INTO parking_spots (spot_number, is_available, created_at) VALUES (?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    spot.getSpotNumber(), spot.getIsAvailable(), Timestamp.valueOf(spot.getCreatedAt()));
            spot.setId(id);
        } else {
            String sql = "UPDATE parking_spots SET spot_number = ?, is_available = ? WHERE id = ?";
            jdbcTemplate.update(sql, spot.getSpotNumber(), spot.getIsAvailable(), spot.getId());
        }
        return spot;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM parking_spots WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsBySpotNumber(String spotNumber) {
        String sql = "SELECT COUNT(*) FROM parking_spots WHERE spot_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, spotNumber);
        return count != null && count > 0;
    }

    public void updateAvailability(Long id, Boolean isAvailable) {
        String sql = "UPDATE parking_spots SET is_available = ? WHERE id = ?";
        jdbcTemplate.update(sql, isAvailable, id);
    }
}
