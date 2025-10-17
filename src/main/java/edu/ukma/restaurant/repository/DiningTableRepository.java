package edu.ukma.restaurant.repository;

import edu.ukma.restaurant.domain.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    Optional<DiningTable> findByCode(String code);
}