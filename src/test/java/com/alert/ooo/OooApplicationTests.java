package com.alert.ooo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OooApplicationTests {

    @Autowired
    private EtaSchedulerService etaSchedulerService;

	@Test
	void contextLoads() {
	}

    @Test
    void testCheckEta() {
        assertDoesNotThrow(() -> {
            etaSchedulerService.checkEta();
        });
    }
}
