package org.egov.pgr.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.egov.pgr.domain.service.ReceivingModeService;
import org.egov.pgr.persistence.entity.ReceivingMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceivingModeController.class)
public class ReceivingModeControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReceivingModeService mockReceivingModeService;

	@Test
	public void testGetReceivingCenters() throws Exception {
		String tenantId = "ap.public";
		List<ReceivingMode> recievingModes = new ArrayList<>(Collections.singletonList(new ReceivingMode()));
		when(mockReceivingModeService.getAllReceivingModes(tenantId)).thenReturn(recievingModes);

		mockMvc.perform(get("/receivingmode?tenantId=ap.public")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk());
	}

}