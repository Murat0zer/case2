package com.trendyol.Case2.product;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component
public class ProductMessageProvider {

    @Resource(name = "productProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * product.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
