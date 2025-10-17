package edu.ukma.restaurant.repository;

import edu.ukma.restaurant.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {}