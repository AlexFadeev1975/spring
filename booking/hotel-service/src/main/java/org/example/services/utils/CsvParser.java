package org.example.services.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.example.statics.StaticData.target;


@Component
public class CsvParser {

    public List<CSVRecord> readFileFromUrl(String link) throws IOException {

        URL url = new URL(link);

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();

        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);

        List<CSVRecord> list = parser.getRecords();

        parser.close();

        return list;

    }

    public List<CSVRecord> readFileFromPath (String path) throws IOException {

        Reader reader = Files.newBufferedReader(Paths.get(path));

        CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withRecordSeparator(",,"));

        return parser.getRecords();
    }

    public List<String> getCities (String path) throws IOException {

         File file = new File(String.valueOf(Paths.get(target + path)));
         FileReader rdr =  new FileReader(file, Charset.forName("windows-1251"));

         BufferedReader reader = new BufferedReader(rdr);
         List<String> cities = new ArrayList<>();
         String pattern = "\"[а-я](?U)\\s[А-ЯЁа-яё]+\"";

        String line = reader.readLine();
        while (line != null) {
            line = reader.readLine();
            if (line == null) {break;}
            String[] arrData = line.split(";");
            if (arrData[6].matches(pattern)) {
                cities.add(arrData[0] +"-" + arrData[6]);
            }
        }

        return cities;
    }


}

