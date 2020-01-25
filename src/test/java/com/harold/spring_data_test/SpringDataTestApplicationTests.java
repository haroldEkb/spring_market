package com.harold.spring_data_test;

import com.harold.spring_data_test.entities.Category;
import com.harold.spring_data_test.services.CategoryService;
import com.harold.spring_data_test.services.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.security.x509.CertAttrSet;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataTestApplicationTests {
	@Value("${paypal.clientId}")
	private String clientId;
	@Value("${paypal.clientSecret}")
	private String clientSecret;


	@Autowired
	private OrderService orderService;

	@Autowired
	private CategoryService categoryService;

	@Test
	public void contextLoads() {
		Assert.assertEquals("AYXwG1B1Eosb6oWaaB6KaCkY_O5XW2iug-m9poOA1yK7w-ZM9RSeNeNMAgj4OWZm-WHsERw5WCAtSSwM", clientId);
		Assert.assertNotNull(clientSecret);
	}

}
