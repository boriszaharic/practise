package com.practise.team1.practiseapi.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.practise.team1.practiseapi.web.dto.EmailDTO;
@Component
public class EmailDTOToEmail implements Converter<EmailDTO, String> {

	@Override
	public String convert(EmailDTO source) {
		
		return source.getEmail();
	}

}
