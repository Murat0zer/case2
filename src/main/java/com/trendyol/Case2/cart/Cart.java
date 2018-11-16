package com.trendyol.Case2.cart;


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
public class Cart implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @NotBlank
    private Long userId;

    @ElementCollection(targetClass = CartItem.class)
    private Set<CartItem> cartItems;

}
