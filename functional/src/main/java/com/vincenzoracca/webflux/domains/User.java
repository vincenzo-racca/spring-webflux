package com.vincenzoracca.webflux.domains;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document
@Data
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    @NotBlank
    private String surname;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

}
