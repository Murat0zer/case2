package com.trendyol.Case2.product;

import com.trendyol.Case2.category.Category;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(value = 1, message = "{product.invalid.price}")
    private BigDecimal price;

    @NotNull
    @OneToMany
    private Set<Category> categories;

}
