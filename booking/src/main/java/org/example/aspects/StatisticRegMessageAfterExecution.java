package org.example.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.example.dto.UserDto;
import org.example.model.kafka.RegMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticRegMessageAfterExecution {

    @Value(value = "${spring.kafka.kafkaRegMessageTopic}")
    private String staticTopic;


    private final KafkaTemplate<String, RegMessage> kafkaTemplate;


    public void sendRegMessageAfterCreatingBooking(JoinPoint joinPoint, UserDto result) {

        log.debug("logAfterReturning running .....");
        log.debug("Enter: {}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        kafkaTemplate.send(staticTopic, new RegMessage(result.getId().toString()));

    }
}
