package com.trendyol.Case2.category;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "{category.title.blank}")
    @Column(unique = true)
    private String title;

    @ElementCollection(targetClass = String.class)
    private Set<String> parentCategories;
}
