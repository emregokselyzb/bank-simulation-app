package com.cydeo.converter;

import com.cydeo.dto.AccountDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter implements Converter<String, AccountDTO> {
    @Override
    public AccountDTO convert(String source) {
        return null;
    }


}
