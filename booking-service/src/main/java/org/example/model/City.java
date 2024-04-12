package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityName;

    private String district;

    private String modifiedData;

    private String dataFileName;

    @OneToMany(mappedBy = "city", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Hotel> hotels;


    public City(String name, String district) {
        this.cityName = name;
        this.district = district;
    }

    public String toString() {

        return cityName + " " + district;
    }
}
