package com.trendyol.Case2.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 3)
    @NotBlank
    @Column(unique = true)
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

}
