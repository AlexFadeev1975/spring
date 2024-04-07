package org.example.model.kafka;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NotBlank
@Document(collection = "users")
public class RegMessage implements Serializable {
    @Id
    String id;

    String userId;

    public RegMessage (String userId) {

        this.userId = userId;
    }
}
