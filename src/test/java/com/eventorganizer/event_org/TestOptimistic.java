package com.eventorganizer.event_org;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(classes = EventOrgApplication.class)
@AutoConfigureMockMvc
public class TestOptimistic {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRaceCondition() throws InterruptedException {

        System.out.println("=== Starting Race Condition Test ===");
        System.out.println("Firing 2 simultaneous JOIN requests for event 28...");
        System.out.println("====================================");

        Thread userA = new Thread(() -> {
            try {
                var result = mockMvc.perform(post("/events/join/28")
                                .with(user("haziq").roles("USER")))  // ← no JWT needed
                        .andReturn();
                int status = result.getResponse().getStatus();
                String body = result.getResponse().getContentAsString();
                System.out.println("------------------------------------");
                System.out.println("User A (haziq)");
                System.out.println("Status  : " + status);
                System.out.println("Response: " + body);
                System.out.println("------------------------------------");
            } catch (Exception e) {
                System.out.println("User A → Exception: " + e.getMessage());
            }
        });

        Thread userB = new Thread(() -> {
            try {
                var result = mockMvc.perform(post("/events/join/28")
                                .with(user("ali").roles("USER")))  // ← no JWT needed
                        .andReturn();
                int status = result.getResponse().getStatus();
                String body = result.getResponse().getContentAsString();
                System.out.println("------------------------------------");
                System.out.println("User B (ali)");
                System.out.println("Status  : " + status);
                System.out.println("Response: " + body);
                System.out.println("------------------------------------");
            } catch (Exception e) {
                System.out.println("User B → Exception: " + e.getMessage());
            }
        });

        userA.start();
        userB.start();

        userA.join();
        userB.join();

        System.out.println("====================================");
        System.out.println("=== Race Condition Test Complete ===");
        System.out.println("====================================");
    }
}