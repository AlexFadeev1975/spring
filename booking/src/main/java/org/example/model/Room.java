package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    @Column(name = "room_name")
    private String roomName;

    private String description;

    private Integer number;

    private Float price;

    private Integer population;

    @Column(name = "closed_dates")
    private List<LocalDate> closedDates;

    @ManyToOne(fetch = FetchType.EAGER)
    private Hotel hotel;

    public String toString () {
        return "id " + id + " name " + roomName + " description " + description
        + " number " + number + " price " + price + " population " + population;
    }
}
