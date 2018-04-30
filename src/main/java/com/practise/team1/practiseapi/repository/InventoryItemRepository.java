package com.practise.team1.practiseapi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.practise.team1.practiseapi.model.InventoryItem;
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

	Page<InventoryItem> findByCategoryId(Pageable pageRequest , Long categoryId);

	List<InventoryItem> findByCategoryId(Long categoryId);

}
