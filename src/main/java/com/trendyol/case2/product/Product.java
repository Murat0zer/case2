package com.trendyol.case2.product;

import com.trendyol.case2.category.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Document(collection = "products")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class Product implements Serializable {

    @Id
    private String id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(value = 1, message = "{product.invalid.price}")
    private BigDecimal price;

    @NotEmpty
    private Set<Category> categories;

}
