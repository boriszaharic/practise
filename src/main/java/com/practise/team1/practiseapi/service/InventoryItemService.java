package com.practise.team1.practiseapi.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.practise.team1.practiseapi.model.InventoryItem;

public interface InventoryItemService {

	Page<InventoryItem> findByCategoryId(int pageNum, Long categoryId);

	InventoryItem findOne(Long id);

	void save(InventoryItem item);

	void remove(Long id);

	List<InventoryItem> findByCategoryId(Long categoryId);
}
