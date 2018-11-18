package com.trendyol.case2.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class User implements Serializable {

    @Id
    private String id;

    @Size(min = 3)
    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

}
