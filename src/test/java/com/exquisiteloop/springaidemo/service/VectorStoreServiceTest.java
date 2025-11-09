package com.exquisiteloop.springaidemo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class VectorStoreServiceTest {

	@Autowired
	VectorStoreService vectorStoreService;
	
	@Test
	void testLoadDemoData() {
		vectorStoreService.loadDemoData();
	}

	@Test
	void testDeleteAllDocuments() {
		vectorStoreService.deleteAllDocuments();
	}
	
}
