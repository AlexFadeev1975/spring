package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.model.kafka.BookingMessage;
import org.example.model.kafka.RegMessage;
import org.example.repository.BookingMessageRepository;
import org.example.repository.RegMessageRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticService {

    private final RegMessageRepository regMessageRepository;

    private final BookingMessageRepository bookingMessageRepository;

    private static final String[] REG_HEADERS = {"Id", "userId"};
    private static final String[] BOOKING_HEADERS = {"Id", "userId", "ArrivalDate", "LeavingDate"};
    private static final CSVFormat REG_FORMAT = CSVFormat.DEFAULT.withHeader(REG_HEADERS);
    private static final CSVFormat BOOKING_FORMAT = CSVFormat.DEFAULT.withHeader(BOOKING_HEADERS);


    public Mono<RegMessage> saveRegMessage(RegMessage message) {

        return regMessageRepository.save(message);
    }


    public Flux<RegMessage> getAllRegMessage() {

        return regMessageRepository.findAll();
    }

    public Mono<BookingMessage> saveBookingMessage(BookingMessage message) {

        return bookingMessageRepository.save(message);
    }


    public Flux<BookingMessage> getAllBookingMessage() {

        return bookingMessageRepository.findAll();
    }

    public ByteArrayInputStream loadRegMessageFile(final List<RegMessage> messages) {

        log.info("Writing data to the csv printer");
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), REG_FORMAT)) {
            for (final RegMessage message : messages) {
                final List<String> data = Arrays.asList(
                        String.valueOf(message.getId()),
                        message.getUserId());

                printer.printRecord(data);
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Csv writing error: " + e.getMessage());
        }
    }

    public ByteArrayInputStream loadBookingMessageFile(final List<BookingMessage> messages) {

        log.info("Writing data to the csv printer");
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), BOOKING_FORMAT)) {
            for (final BookingMessage message : messages) {
                final List<String> data = Arrays.asList(
                        String.valueOf(message.getId()),
                        message.getUserId(),
                        message.getArrivalDate(),
                        message.getLeavingDate());

                printer.printRecord(data);
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Csv writing error: " + e.getMessage());
        }

    }
}
