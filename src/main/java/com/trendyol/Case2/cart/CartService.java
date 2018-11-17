package com.trendyol.Case2.cart;


import com.trendyol.Case2.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartMessageProvider cartMessageProvider;

    @Transactional
    public ResponseMessage addItem(Cart cart, Set<CartItem> cartItems) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setStatus(true);

        responseMessage.setMessage(cartMessageProvider.getMessage("cart.products.added"));
        cart.getCartItems().addAll(cartItems);

        cartRepository.save(cart);
        return responseMessage;
    }
}
