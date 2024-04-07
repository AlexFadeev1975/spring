package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.example.model.City;
import org.example.repository.CityRepository;
import org.example.search.filters.CityFilterBuilder;
import org.example.search.utils.Filter;
import org.example.search.utils.SpecificationBuilder;
import org.example.services.utils.CsvParser;
import org.example.services.utils.FileLoaderFromURI;
import org.example.statics.StaticData;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.example.statics.StaticData.target;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityService {


    private final FileLoaderFromURI loader;
    private final CsvParser csvParser;
    private final CityRepository cityRepository;
    private final CityFilterBuilder cityFilterBuilder;
    private final SpecificationBuilder specificationBuilder;
    private Boolean isModified = false;
    private City city;

    // @Scheduled(cron = "@monthly")
    //@Scheduled(fixedDelay = 70000)
    private void checkVersionData() throws IOException {
        /** Список городов РФ хранится в БД и заполняется из официальных данных Росстата (загрузка справочника ОКАТО)
            Список регионов неизменный хранится в папке districts **/

        List<City> listCity = cityRepository.findByCityName("г Моква");
        City savedCity = new City();
        if (listCity.isEmpty()) {
            City newCity = new City();
            newCity.setCityName("г Москва");
            savedCity = cityRepository.save(newCity);
        } else {
            savedCity = listCity.get(0);
        }

        city = savedCity;

        // Парсим данные с сайта Росстата о наличии обновлений списка ОКАТО //
        List<CSVRecord> recordList = csvParser.readFileFromUrl(StaticData.metaDataUrl);

        recordList.forEach(strings -> {
            Map<String, String> str = strings.toMap();

            if (str.get("property").equals("modified") &
                    !str.get("value").equals(city.getModifiedData())) {
                city.setModifiedData(str.get("value"));
                isModified = true;
            }
            if (str.get("property").contains("data") & isModified) {
                try {
                    loader.loadFile(str.get("property"), str.get("value")); // скачивание нового списка ОКАТО .csv
                    city.setDataFileName(str.get("property"));
                    isModified = false;
                    cityRepository.save(city); // сохраняем данные обновления
                    updateCities();            // обновляем города
                    FileUtils.cleanDirectory(new File(target));


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    public void updateCities() throws IOException {

        City checkedCity = cityRepository.findByCityName("г Москва").get(0);

        List<String> stringCities = csvParser.getCities(checkedCity.getDataFileName());

        Map<String, String> regions = getDistricts(); // получаем мапу код региона - название региона

        List<City> cityList = stringCities.stream().map(c -> {
            String[] arrString = c.split("-");
            City city = new City();
            city.setCityName(arrString[1].replaceAll("\"", ""));

            String dictrict = arrString[0].replaceAll("\"", "");
            String dis = Integer.valueOf(dictrict).toString();
            city.setDistrict(regions.get(dis));                   // подставляем в City название региона вместо кода
            city.setModifiedData(checkedCity.getModifiedData());
            city.setDataFileName(checkedCity.getDataFileName());
            return city;
        }).toList();
        cityRepository.deleteAll();
        cityRepository.saveAll(cityList);  // сохраняем новый список прежде удалив старый
        log.info("База данных городов обновлена");
    }

    private Map<String, String> getDistricts() throws IOException {

        List<CSVRecord> recordList = csvParser.readFileFromPath(StaticData.districts); // парсим список регионов из папки districts .csv

        Map<String, String> regions = new HashMap<>();

        recordList.forEach(strings -> {
            Map<String, String> str = strings.toMap();
            String code = str.get("region_code");
            String region = str.get("name_with_type");

            regions.put(code, region);

        });
        return regions;
    }

    public City searchCity (String city, String district) {

        if (city == null || district == null) { return null;}

     List<Filter> filters = cityFilterBuilder.createFilter(city, district);

        Specification<City> specification = (Specification<City>) specificationBuilder.getSpecificationFromFilters(filters);

        List<City> cities = cityRepository.findAll(specification);

        return cities.stream().findFirst().orElseThrow(() -> new NoSuchElementException("Такой город не найден"));
    }
}



