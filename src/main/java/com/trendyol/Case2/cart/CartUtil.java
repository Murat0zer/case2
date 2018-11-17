package com.trendyol.Case2.cart;

import com.google.common.collect.Lists;
import com.trendyol.Case2.category.Campaign;
import com.trendyol.Case2.category.DiscountType;

import java.util.HashSet;
import java.util.Set;

public class CartUtil {

    private CartUtil() {
    }

    public static double calculateTotalPrice(Cart cart) {
        double totalPrice = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            int price = cartItem.getProduct().getPrice().intValue();
            int quantity = cartItem.getQuantity();
            totalPrice += price * quantity;
        }
        return totalPrice;
    }

    static void applyDiscounts(Cart cart, Set<Campaign> campaigns) {

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

                boolean isCampaignAvailableForCategory = campaign.getCategoryId().equals(campCategoryTitle);
                boolean isProvideNecessaryQuantity = quantity >= campaign.getNecessaryQuantity();
                boolean isDiscountTypeRate = campaign.getDiscountType() == DiscountType.RATE;

                if (isCampaignAvailableForCategory && isProvideNecessaryQuantity && isDiscountTypeRate) {
                    discount = totalCartItemPriceBeforeDiscount * campaign.getDiscountValue() / 100.0;

                } else if (isCampaignAvailableForCategory && isProvideNecessaryQuantity) {
                    discount = campaign.getDiscountValue();

                } else
                    continue;
                maxDiscountForCartItem = discount > maxDiscountForCartItem ? discount : maxDiscountForCartItem;
            }
            totalDiscount += maxDiscountForCartItem;
        }
        cart.setCampaignDiscount(cart.getCampaignDiscount() + totalDiscount);
        cart.setTotalAmountAfterDiscounts(totalAmountBeforeDiscount - totalDiscount);
    }


    static void applyCoupon(Cart cart, Coupon coupon) {

        double totalCartAmount = calculateTotalPrice(cart);

        if (totalCartAmount >= coupon.getNecessaryValue()) {

            double discount = coupon.getDiscountType() == DiscountType.AMOUNT ? coupon.getDiscountValue()
                    : cart.getTotalAmountAfterDiscounts() * coupon.getDiscountValue() / 100.0;

            cart.setTotalAmountAfterDiscounts(cart.getTotalAmountAfterDiscounts() - discount);
            cart.setCouponDiscount(cart.getCouponDiscount() + discount);
        }
    }

    static Set<Campaign> returnAvailableCampaigns(Cart cart, Set<Campaign> allCampaigns) {
        Set<Campaign> availableCampaigns = new HashSet<>();

        cart.getCartItems().forEach(cartItem -> cartItem.getProduct().getCategories()
                .forEach(category -> {
                    allCampaigns.stream()
                            .filter(campaign -> campaign.getCategoryId().equals(category.getId()))
                            .forEach(availableCampaigns::add);
                }));
        return availableCampaigns;
    }
}
