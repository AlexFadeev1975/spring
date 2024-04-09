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
    @Column(name = "city_name")
    private String cityName;

    private String district;

    private String modifiedData;

    private String dataFileName;


    public City (String name, String district) {
        this.cityName = name;
        this.district = district;
    }

    public String toString() {

        return cityName + " " + district;
    }
}
