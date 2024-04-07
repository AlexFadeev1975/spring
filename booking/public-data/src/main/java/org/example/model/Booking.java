package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDate arrivalDate;

    LocalDate leavingDate;

    @ManyToOne(fetch = FetchType.EAGER)
    Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    User user;
}
