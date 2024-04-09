package org.example.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.services.StatisticService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/statistic/api/v1/")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/download/reg")
    public ResponseEntity<Resource> getRegFile() {
        String filename = "registrations.csv";
        InputStreamResource file = new InputStreamResource(statisticService.loadRegMessageFile(Objects.requireNonNull(statisticService.getAllRegMessage().collectList().block())));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body((Resource) file);
    }

    @GetMapping("/download/booking")
    public ResponseEntity<Resource> getBookingFile() {
        String filename = "bookings.csv";
        InputStreamResource file = new InputStreamResource(statisticService.loadBookingMessageFile(Objects.requireNonNull(statisticService.getAllBookingMessage().collectList().block())));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body((Resource) file);
    }
}
