package com.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OrderApplication.class)
@ActiveProfiles("test")
class OrderApplicationTests {

	@Test
	void contextLoads() {
	}
}

