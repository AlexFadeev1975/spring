package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotelName;

    private String header;

    private String address;

    private Float remotion;

    private Float rating;

    private Integer ratesCount;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private City city;


}





