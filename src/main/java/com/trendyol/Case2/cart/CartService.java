package com.trendyol.Case2.cart;


import com.google.common.collect.Lists;
import com.trendyol.Case2.category.Campaign;
import com.trendyol.Case2.category.DiscountType;
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
    public ResponseMessage saveCart(Cart cart) {
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setStatus(true);

        responseMessage.setMessage(cartMessageProvider.getMessage("cart.saved.success"));

        cartRepository.save(cart);
        return responseMessage;
    }

    public void applyDiscounts(Cart cart, Set<Campaign> campaigns) {

        Set<CartItem> cartItems = cart.getCartItems();
        double totalCartItemPriceBeforeDiscount;
        double maxDiscountForCartItem;
        double discount;
        double totalDiscount = 0;
        double totalAmountBeforeDiscount = 0.0;

        for (CartItem cartItem : cartItems) {
            maxDiscountForCartItem = 0;
            double productPrice = cartItem.getProduct().getPrice().doubleValue();
            int quantity = cartItem.getQuantity();
            totalCartItemPriceBeforeDiscount = productPrice * quantity;
            totalAmountBeforeDiscount += totalCartItemPriceBeforeDiscount;
            String campCategoryTitle = Lists.newArrayList(cartItem.getProduct().getCategories()).get(0).getTitle();
            for (Campaign campaign : campaigns) {

                boolean exp1 = campaign.getCategoryId().equals(campCategoryTitle);
                boolean exp2 = quantity >= campaign.getNecessaryQuantity();
                boolean exp3 = campaign.getDiscountType() == DiscountType.RATE;
                if (exp1 && exp2 && exp3) {
                    discount = totalCartItemPriceBeforeDiscount * campaign.getDiscountValue() / 100.0;
                } else if (exp1 && exp2) {
                    discount = campaign.getDiscountValue();
                } else
                    continue;
                maxDiscountForCartItem = discount > maxDiscountForCartItem ? discount : maxDiscountForCartItem;
            }

            totalDiscount += maxDiscountForCartItem;

        }
        cart.setCampaignDiscount(totalDiscount);
        cart.setTotalAmountAfterDiscounts(totalAmountBeforeDiscount - totalDiscount);

    }
}
