package com.trendyol.Case2.cart;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component
public class CartMessageProvider {

    @Resource(name = "cartProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * cart.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}