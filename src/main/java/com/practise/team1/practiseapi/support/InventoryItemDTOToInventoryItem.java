package com.practise.team1.practiseapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.InventoryItem;
import com.practise.team1.practiseapi.service.CategoryService;
import com.practise.team1.practiseapi.service.InventoryItemService;
import com.practise.team1.practiseapi.web.dto.InventoryItemDTO;

@Component
public class InventoryItemDTOToInventoryItem implements Converter<InventoryItemDTO, InventoryItem> {

	@Autowired
	InventoryItemService inventoryItemService;

	@Autowired
	CategoryService categoryService;

	@Override
	public InventoryItem convert(InventoryItemDTO dto) {

		InventoryItem item = new InventoryItem();

		if (dto.getId() != null) {
			item = inventoryItemService.findOne(dto.getId());

			if (item == null) {
				throw new IllegalStateException("Tried to modify a non-existant item");
			}
		}

		item.setId(dto.getId());
		item.setName(dto.getName());
		item.setDescription(dto.getDescription());
		item.setBarcode(dto.getBarcode());
		item.setOrderNumber(dto.getOrderNumber());
		item.setValue(dto.getValue());
		item.setCategory(categoryService.findOne(dto.getCategoryId()));

		return item;
	}

	public List<InventoryItem> convert(List<InventoryItemDTO> dtos) {

		List<InventoryItem> items = new ArrayList<>();

		for (InventoryItemDTO dto : dtos) {
			items.add(convert(dto));
		}

		return items;

	}

}
