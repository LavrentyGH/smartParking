package com.parking.parkingmanagement.repository;

import com.parking.parkingmanagement.entity.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

        // Для JOIN запросов
        try {
            car.setOwnerFullName(rs.getString("owner_name"));
        } catch (Exception e) {
            // Игнорируем если колонки нет
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

    public List<Car> findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM cars WHERE license_plate ILIKE ? ORDER BY license_plate";
        return jdbcTemplate.query(sql, rowMapper, "%" + licensePlate + "%");
    }

    public Car save(Car car) {
        if (car.getId() == null) {
            String sql = "INSERT INTO cars (license_plate, owner_id) VALUES (?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    car.getLicensePlate(), car.getOwnerId());
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

    public Optional<Car> findByLicensePlateExact(String licensePlate) {
        String sql = "SELECT * FROM cars WHERE license_plate = ?";
        List<Car> cars = jdbcTemplate.query(sql, rowMapper, licensePlate);
        return cars.stream().findFirst();
    }
}
