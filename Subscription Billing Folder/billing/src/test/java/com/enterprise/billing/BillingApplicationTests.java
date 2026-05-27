package com.enterprise.billing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Explicitly pointing to the correct main class prevents initialization failures
@SpringBootTest(classes = BillingSystemApplication.class)
class BillingSystemApplicationTests {

    @Test
    void contextLoads() {
        // This method validates that the Spring Context, Database Connections, 
        // and Security conf