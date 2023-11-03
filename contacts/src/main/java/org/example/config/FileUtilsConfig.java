package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources( value = {
        @PropertySource("application-file.properties")
})
public class FileUtilsConfig {



}
