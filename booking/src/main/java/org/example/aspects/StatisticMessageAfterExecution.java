package org.example.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.model.Booking;
import org.example.model.kafka.BookingMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticMessageAfterExecution {

    @Value(value = "${spring.kafka.kafkaBookingMessageTopic}")
    private String staticTopic;

    private final KafkaTemplate<String, BookingMessage> kafkaTemplate;

  //  @AfterReturning(pointcut = "execution(* org.example.services.BookingService.createBookingRoom(..))", returning = "result")

    public void sendBookingMessageAfterCreatingBooking(JoinPoint joinPoint, Booking result) {

        log.debug("logAfterReturning running .....");
        log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        kafkaTemplate.send(staticTopic, new BookingMessage(result.getUser().getId().toString(),
                result.getArrivalDate().toString(), result.getLeavingDate().toString()));

    }
}
