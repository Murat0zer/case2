package com.trendyol.case2;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = {"com.trendyol.case2"})
@Profile({"prod", "dev"})
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${case2.mongo.host-name}")
    private String hostName;

    @Value("${case2.mongo.port}")
    private int port;

    @Value("${case2.mongo.password}")
    private String password;

    @Value("${case2.mongo.username}")
    private String username;

    @Value("${case2.mongo.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }


    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.trendyol.case2");
    }


    @Override
    public MongoClient mongoClient() {
        MongoCredential mongoCredential = MongoCredential
                .createCredential(username, "admin", password.toCharArray());

        ServerAddress serverAddress = new ServerAddress(hostName, port);

        return new MongoClient(serverAddress, (mongoCredential), MongoClientOptions.builder().build());
    }
}
