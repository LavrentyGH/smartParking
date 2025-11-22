package com.parking.parkingmanagement.repository;

import com.parking.parkingmanagement.entity.Owner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class OwnerRepository {

    private final JdbcTemplate jdbcTemplate;

    public OwnerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Owner> rowMapper = (rs, rowNum) -> {
        Owner owner = new Owner();
        owner.setId(rs.getLong("id"));
        owner.setFullName(rs.getString("full_name"));
        owner.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return owner;
    };

    public List<Owner> findAll() {
        String sql = "SELECT * FROM owners ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Owner> findById(Long id) {
        String sql = "SELECT * FROM owners WHERE id = ?";
        List<Owner> owners = jdbcTemplate.query(sql, rowMapper, id);
        return owners.stream().findFirst();
    }

    public List<Owner> findByFullName(String fullName) {
        String sql = "SELECT * FROM owners WHERE full_name ILIKE ? ORDER BY full_name";
        return jdbcTemplate.query(sql, rowMapper, "%" + fullName + "%");
    }

    public Owner save(Owner owner) {
        if (owner.getId() == null) {
            String sql = "INSERT INTO owners (full_name, created_at) VALUES (?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    owner.getFullName(),
                    Timestamp.valueOf(owner.getCreatedAt()));
            owner.setId(id);
        } else {
            String sql = "UPDATE owners SET full_name = ? WHERE id = ?";
            jdbcTemplate.update(sql, owner.getFullName(), owner.getId());
        }
        return owner;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM owners WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByFullName(String fullName) {
        String sql = "SELECT COUNT(*) FROM owners WHERE full_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, fullName);
        return count != null && count > 0;
    }
}
