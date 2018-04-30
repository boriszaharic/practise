package com.practise.team1.practiseapi.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.model.InventoryItem;
import com.practise.team1.practiseapi.web.dto.InventoryItemDTO;

@Component
public class InventoryItemToInventoryItemDTO implements Converter<InventoryItem, InventoryItemDTO> {

	@Override
	public InventoryItemDTO convert(InventoryItem item) {

		InventoryItemDTO dto = new InventoryItemDTO();

		dto.setId(item.getId());
		dto.setBarcode(item.getBarcode());
		dto.setDescription(item.getDescription());
		dto.setName(item.getName());
		dto.setOrderNumber(item.getOrderNumber());
		dto.setValue(item.getValue());
		dto.setCategoryId(item.getCategory().getId());
		dto.setCategoryName(item.getCategory().getName());

		return dto;
	}

	public List<InventoryItemDTO> convert(List<InventoryItem> items) {

		List<InventoryItemDTO> dtos = new ArrayList<>();

		for (InventoryItem item : items) {
			dtos.add(convert(item));
		}

		return dtos;

	}

}
