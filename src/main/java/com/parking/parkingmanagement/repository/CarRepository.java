package com.parking.parkingmanagement.repository;

import com.parking.parkingmanagement.dto.PagedResponse;
import com.parking.parkingmanagement.entity.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository {
    private final JdbcTemplate jdbcTemplate;

    public CarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Car> rowMapper = (rs, rowNum) -> {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setLicensePlate(rs.getString("license_plate"));
        car.setOwnerId(rs.getLong("owner_id"));
        car.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        try {
            car.setOwnerFullName(rs.getString("full_name"));
        } catch (Exception ignored) {
        }

        return car;
    };

    public List<Car> findAllWithOwner() {
        String sql = """
            SELECT c.*, o.full_name as owner_name
            FROM cars c
            LEFT JOIN owners o ON c.owner_id = o.id
            ORDER BY c.created_at DESC
            """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Car> findById(Long id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        List<Car> cars = jdbcTemplate.query(sql, rowMapper, id);
        return cars.stream().findFirst();
    }

    public Optional<Car> findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM cars WHERE license_plate = ?";
        List<Car> cars = jdbcTemplate.query(sql, rowMapper, licensePlate);
        return cars.stream().findFirst();
    }

    public Car save(Car car) {
        if (car.getId() == null) {
            String sql = "INSERT INTO cars (license_plate, owner_id, created_at) VALUES (?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    car.getLicensePlate(), car.getOwnerId(), Timestamp.valueOf(car.getCreatedAt()));
            car.setId(id);
        } else {
            String sql = "UPDATE cars SET license_plate = ?, owner_id = ? WHERE id = ?";
            jdbcTemplate.update(sql, car.getLicensePlate(), car.getOwnerId(), car.getId());
        }
        return car;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        String sql = "SELECT COUNT(*) FROM cars WHERE license_plate = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, licensePlate);
        return count != null && count > 0;
    }

    public PagedResponse<Car> findAllPaged(int page, int size) {
        int offset = page * size;

        String sql = """
            SELECT c.*, o.full_name
            FROM cars c
            LEFT JOIN owners o ON c.owner_id = o.id
            ORDER BY c.created_at DESC LIMIT ? OFFSET ?
            """;
        List<Car> content = jdbcTemplate.query(sql, rowMapper, size, offset);

        String countSql = "SELECT COUNT(*) FROM cars";
        Long totalItems = jdbcTemplate.queryForObject(countSql, Long.class);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        return new PagedResponse<>(content, page, totalPages, totalItems, size);
    }
}
