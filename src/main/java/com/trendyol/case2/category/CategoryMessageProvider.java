package com.trendyol.case2.category;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component
public class CategoryMessageProvider {

    @Resource(name = "categoryProperties")
    private Properties properties;

    public Properties getProp() {
        return properties;
    }

    /**
     * category.properties dosyasinda bulunan mesajlara ulasmamizi saglayan metot
     *
     * @param key anahtar deger
     * @return mesaj
     */
    public String getMessage(String key) {
        return properties.getProperty(key);
    }
}
