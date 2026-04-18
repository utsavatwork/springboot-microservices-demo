package com.utsav.inventory_service.repository;

import com.utsav.inventory_service.entity.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    boolean existsByProductId(Long productId);
}
