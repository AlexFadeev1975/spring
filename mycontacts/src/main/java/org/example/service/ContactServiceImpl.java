package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Contact;
import org.example.model.FillingError;
import org.example.model.dto.ContactDto;
import org.example.repository.JdbcContactRepository;
import org.example.service.mapper.ContactListToDtoMapper;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactListToDtoMapper mapper;

    private final JdbcContactRepository contactRepository;

    @Override
    public boolean save(Contact contact) {

        Contact result = contactRepository.save(contact);
        if (result != null) {
            log.info("Контакт " + result + " успешно сохранен.");

            return true;
        } else {
            log.info("Контакт с таким email уже существует");
            return false;
        }
    }

    @Override
    public void update(Contact contact) {

        contactRepository.update(contact);

    }

    @Override
    public List<ContactDto> findAll() {
        return mapper.listContactToListDto(contactRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {

        Contact result = contactRepository.deleteById(id);

        if (result != null) {
            log.info("Контакт " + result + "успешно удален");
        } else {
            log.info("Контакта с таким id не существует");
        }

    }

    @Override
    @EventListener(value = ApplicationStartedEvent.class)
    public void create() {

        contactRepository.create();

        log.info("База данных успешно создана");

    }

    @Override
    public Contact findById(Long id) {
        return contactRepository.findById(id);
    }

    @Override
    public FillingError validateData(String firstName, String lastName, String email, String phone) {

        Pattern patternName = Pattern.compile("[A-ЯЁа-яёa-zA-Z]+"
                , Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);


        Pattern patternNum = Pattern.compile("\\d{11}"
                , Pattern.UNICODE_CHARACTER_CLASS);

        Pattern patternEmail = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?``{|}~^.-]+@[a-zA-Z0-9.-]+$"
                , Pattern.UNICODE_CHARACTER_CLASS);

        FillingError errors = new FillingError();
        errors.setFnError(!patternName.matcher(firstName).matches());
        errors.setLnError(!patternName.matcher(lastName).matches());
        errors.setEmError(!patternEmail.matcher(email).matches());
        errors.setPhError(!patternNum.matcher(phone).matches());

        return errors;
    }
}
