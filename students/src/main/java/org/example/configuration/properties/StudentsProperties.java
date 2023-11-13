package org.example.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("preload")
@Component
@Getter
@Setter
public class StudentsProperties {

    @Value("${spring.load_from_file}")
    boolean isLoaded;

}
