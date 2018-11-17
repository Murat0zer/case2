package com.trendyol.Case2.util;

import com.devskiller.jfairy.Fairy;
import com.trendyol.Case2.user.User;

import java.util.HashSet;
import java.util.Set;

public class UserGenerator {

    private UserGenerator() {
    }

    public static Set<User> generateRandomUsers(int count) {

        Fairy fairy = Fairy.create();
        Set<User> users = new HashSet<>();
        for (int i = 0; i < count; i++) {
            User user = User.builder()
                    .email(fairy.person().getEmail())
                    .username(fairy.person().getUsername())
                    .firstName(fairy.person().getFirstName())
                    .lastName(fairy.person().getLastName())
                    .password(fairy.person().getPassword())
                    .build();

            users.add(user);
        }
        return users;


    }
}
