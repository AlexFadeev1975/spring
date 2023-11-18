package org.example.service.mapper;

import lombok.RequiredArgsConstructor;
import org.example.model.Contact;
import org.example.model.dto.ContactDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactDtoToContactMapper {

    private final ModelMapper mapper;

    public Contact mapToContact(String fn, String ln, String em, String ph) {

        ContactDto dto = new ContactDto(fn, ln, em, ph);
        return mapper.map(dto, Contact.class);
    }

    public Contact mapToContactWithId(String fn, String ln, String em, String ph, String id) {

        ContactDto dto = new ContactDto(fn, ln, em, ph, id);
        return mapper.map(dto, Contact.class);
    }


}
