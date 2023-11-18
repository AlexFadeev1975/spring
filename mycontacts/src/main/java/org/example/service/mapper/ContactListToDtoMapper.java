package org.example.service.mapper;

import lombok.RequiredArgsConstructor;
import org.example.model.Contact;
import org.example.model.dto.ContactDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContactListToDtoMapper {

    private final ModelMapper mapper;

    public List<ContactDto> listContactToListDto(List<Contact> contacts) {

        return (!contacts.isEmpty()) ? contacts.stream().map(this::contactToContactDto).toList() : new ArrayList<>();

    }

    public ContactDto contactToContactDto(Contact contact) {

        return (contact != null) ? mapper.map(contact, ContactDto.class) : null;
    }

    public ContactDto contactToContactDtoAndAction(Contact contact, String action) {

        ContactDto dto = mapper.map(contact, ContactDto.class);
        dto.setAction(action);

        return dto;
    }

}
