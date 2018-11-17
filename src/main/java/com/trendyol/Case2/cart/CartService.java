package com.trendyol.Case2.cart;


import com.trendyol.Case2.category.Campaign;
import com.trendyol.Case2.category.Category;
import com.trendyol.Case2.category.CategoryRepository;
import com.trendyol.Case2.response.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartMessageProvider cartMessageProvider;

    private final CategoryRepository categoryRepository;

    @Transactional
    public ResponseMessage saveCart(Cart cart) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setStatus(true);

        responseMessage.setMessage(cartMessageProvider.getMessage("cart.saved.success"));

        cartRepository.save(cart);
        return responseMessage;
    }

    public void applyDiscounts(Cart cart, Set<Campaign> campaigns) {

        CartUtil.applyDiscounts(cart, campaigns);
        cartRepository.save(cart);
    }

    public void applyCoupon(Cart cart, Coupon coupon) {

        Set<Campaign> allCampaigns = new HashSet<>();
        for (Category category : categoryRepository.findAll()) {
            if(Objects.nonNull(category.getCampaigns()))
                allCampaigns.addAll(category.getCampaigns());
        }

        this.applyDiscounts(cart, CartUtil.returnAvailableCampaigns(cart, allCampaigns));
        CartUtil.applyCoupon(cart, coupon);
        cartRepository.save(cart);

    }
}
