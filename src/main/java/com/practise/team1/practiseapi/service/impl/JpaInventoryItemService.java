package com.practise.team1.practiseapi.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.practise.team1.practiseapi.model.InventoryItem;
import com.practise.team1.practiseapi.repository.InventoryItemRepository;
import com.practise.team1.practiseapi.service.InventoryItemService;

@Service
@Transactional
public class JpaInventoryItemService implements InventoryItemService {

	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	@Override
	public InventoryItem findOne(Long id) {
		if (inventoryItemRepository.existsById(id))
			return inventoryItemRepository.getOne(id);
		return null;
	}

	@Override
	public void save(InventoryItem item) {

		inventoryItemRepository.save(item);

	}

	@Override
	public void remove(Long id) {

		inventoryItemRepository.deleteById(id);

	}

	@Override
	public Page<InventoryItem> findByCategoryId(int pageNum, Long categoryId) {

		return inventoryItemRepository.findByCategoryId(PageRequest.of(pageNum, 10), categoryId);
	}

	@Override
	public List<InventoryItem> findByCategoryId(Long categoryId) {
		
		return inventoryItemRepository.findByCategoryId(categoryId);
	}

}
