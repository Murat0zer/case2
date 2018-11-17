package com.trendyol.Case2.category;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
@CompoundIndexes(
        @CompoundIndex(def = "{'id':1}")
)
public class Category implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "{category.title.blank}")
    private String title;

    private Set<String> parentCategories;
}
