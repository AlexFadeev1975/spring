package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.model.Contact;
import org.example.model.CreatingError;
import org.example.model.FillingError;
import org.example.model.dto.ContactDto;
import org.example.service.ContactService;
import org.example.service.mapper.ContactDtoToContactMapper;
import org.example.service.mapper.ContactListToDtoMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    private final ContactDtoToContactMapper mapper;
    private final ContactListToDtoMapper dtoMapper;

    private boolean isExist;

    @ModelAttribute("contacts")
    public List<ContactDto> contacts() {

        return contactService.findAll();
    }

    @ModelAttribute("creatingError")
    public CreatingError creatingError() {

        return (isExist) ? new CreatingError() : new CreatingError("");
    }


    @GetMapping("/")
    public String getMainPage(Model model) {

        isExist = false;

        return "index";
    }

    @GetMapping("/contacts/create")
    public String getContactPage(Model model) {

        ContactDto dto = new ContactDto("", "", "", "", "0");

        model.addAttribute("contactDto", dto);

        return "contact";
    }

    @PostMapping("/contacts/create")
    public String createContact(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("email") String email,
                                @RequestParam("phone") String phone,
                                Model model) {

        FillingError error = contactService.validateData(firstName, lastName, email, phone);

        if (!error.isEmError() & !error.isLnError() & !error.isFnError() & !error.isPhError()) {

            if (!contactService.save(mapper.mapToContact(firstName, lastName, email, phone))) {

                isExist = true;
            }
            return "redirect:/";
        } else {

            ContactDto dto = new ContactDto(firstName, lastName, email, phone,
                    error.isFnError(), error.isLnError(), error.isEmError(), error.isPhError());
            model.addAttribute("contactDto", dto);

            return "contact";
        }
    }

    @GetMapping("/contacts/update/{id}")
    public String getUpdatePage(@PathVariable("id") String id, Model model) {

        Contact contact = contactService.findById(Long.valueOf(id));
        model.addAttribute("contactDto", dtoMapper.contactToContactDtoAndAction(contact, "update"));

        return "contact";

    }

    @PostMapping("/contacts/update")
    public String updateContact(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("email") String email,
                                @RequestParam("phone") String phone,
                                @RequestParam("id") String id,
                                Model model) {

        contactService.update(mapper.mapToContactWithId(firstName, lastName, email, phone, id));

        return "redirect:/";
    }

    @GetMapping("/contacts/delete/{id}")
    public String deleteContact(@PathVariable("id") String id) {

        contactService.deleteById(Long.parseLong(id));

        return "redirect:/";
    }
}
