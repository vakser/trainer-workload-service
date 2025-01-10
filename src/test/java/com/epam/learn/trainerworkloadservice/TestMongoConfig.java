package com.epam.learn.trainerworkloadservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class TestMongoConfig {
    private final MongoTemplate mongoTemplate;

    public TestMongoConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    @Primary
    public MongoTemplate testMongoTemplate() {
        // Drop all collections and recreate them when the context is initialized
        mongoTemplate.getDb().drop();
        return mongoTemplate;
    }
}
