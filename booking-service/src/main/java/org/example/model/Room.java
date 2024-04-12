package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private String description;

    private Integer number;

    private Float price;

    private Integer population;

    private List<LocalDate> closedDates = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Hotel hotel;

    public String toString() {
        return " name " + roomName + " number " + number;
    }
}
