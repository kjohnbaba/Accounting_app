package com.cydeo.converter;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.service.ClientVendorService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ClientVendorDTOConverter implements Converter<String, ClientVendorDto> {

    ClientVendorService clientVendorService;

    public ClientVendorDTOConverter(@Lazy ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    public ClientVendorDto convert(String source) {
        if (source == null || source.equals("")) {
            return null;
        }

        return clientVendorService.findById(Long.parseLong(source));
    }
}
