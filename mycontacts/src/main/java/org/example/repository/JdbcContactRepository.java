package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Contact;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcContactRepository implements ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create() {

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS contacts " +
                "(id SERIAL PRIMARY KEY, " +
                "first_name VARCHAR(255), " +
                "last_name VARCHAR(255), " +
                "email VARCHAR(255), " +
                "phone CHAR(11))");
    }

    @Override
    public Contact findByEmail(String email) {
        Contact contact;
        try {
            contact = jdbcTemplate.queryForObject("SELECT * FROM contacts WHERE email = ?",
                    new BeanPropertyRowMapper<Contact>(Contact.class), email);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
        return contact;
    }

    @Override
    public Contact save(Contact contact) {

        if (findByEmail(contact.getEmail()) == null) {

            int num = jdbcTemplate.update("INSERT INTO contacts (phone, first_name, last_name, email) VALUES (?, ?, ?, ?)",
                    contact.getPhone(), contact.getFirstName(), contact.getLastName(), contact.getEmail());

            return (num > 0) ? findByEmail(contact.getEmail()) : null;
        } else {

            return null;
        }
    }

    @Override
    public Contact update(Contact contact) {


        int num = jdbcTemplate.update("UPDATE contacts SET (phone, first_name, last_name, email) = (?, ?, ?, ?) WHERE id = ?",
                contact.getPhone(), contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getId());

        return findById(contact.getId());


    }

    @Override
    public Contact findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM contacts WHERE id = ?",
                new BeanPropertyRowMapper<Contact>(Contact.class), id);
    }


    @Override
    public List<Contact> findAll() {
        return jdbcTemplate.query("SELECT * FROM contacts ORDER BY id",
                new BeanPropertyRowMapper<Contact>(Contact.class));
    }

    @Override
    public Contact deleteById(Long id) {

        Contact contact = findById(id);
        if (contact != null) {
            jdbcTemplate.update("DELETE FROM contacts WHERE id = ?", id);

            return contact;
        }
        return null;
    }
}
