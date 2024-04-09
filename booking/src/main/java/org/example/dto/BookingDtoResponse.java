package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Room;
import org.example.model.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoResponse {

    Long id;

    LocalDate arrivalDate;

    LocalDate leavingDate;

    Room room;

   User user;
}

