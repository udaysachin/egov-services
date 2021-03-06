package org.egov.pgr.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.egov.pgr.Resources;
import org.egov.pgr.TestResourceReader;
import org.egov.pgr.domain.exception.InvalidComplaintException;
import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complainant;
import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.ComplaintLocation;
import org.egov.pgr.domain.model.ComplaintSearchCriteria;
import org.egov.pgr.domain.model.ComplaintType;
import org.egov.pgr.domain.model.Coordinates;
import org.egov.pgr.domain.model.Role;
import org.egov.pgr.domain.model.UserType;
import org.egov.pgr.domain.service.ComplaintService;
import org.egov.pgr.domain.service.UserService;
import org.egov.pgr.persistence.queue.contract.SevaRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ComplaintController.class)
public class ComplaintControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private Resources resources = new Resources();

	@MockBean
	private UserService userService;

	@MockBean
	private ComplaintService complaintService;

	@Test
	public void test_should_return_error_response_when_tenant_id_is_not_present_on_creating_a_complaint()
			throws Exception {
		when(userService.getUser("authToken")).thenReturn(getCitizen());
		Complaint invalidComplaint = getComplaintWithNoTenantId();
		doThrow(new InvalidComplaintException(invalidComplaint)).when(complaintService).save(any(Complaint.class),
				any(SevaRequest.class));

		mockMvc.perform(post("/seva").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(resources.getFileContents("createComplaintRequest.json"))).andExpect(status().isBadRequest())
				.andExpect(content().json(resources.getFileContents("createComplaintErrorResponse.json")));
	}

	public Complaint getComplaintWithNoTenantId() {
		final ComplaintLocation complaintLocation = ComplaintLocation.builder()
				.coordinates(new Coordinates(11.22d,12.22d)).build();
		final Complainant complainant = Complainant.builder().userId("userId").build();
		return Complaint.builder().complainant(complainant).authenticatedUser(getCitizen())
				.complaintLocation(complaintLocation).tenantId(null).description("description")
				.complaintType(new ComplaintType(null, "complaintCode")).build();
	}

	public AuthenticatedUser getCitizen() {
		return AuthenticatedUser.builder().id(1).type(UserType.CITIZEN).build();
	}

	@Test
	public void testGetServiceRequests() throws Exception {
		String crn = "1234";
		String complaintType = "abc";
		String complainant = "kumar";
		String receivingMode = "MANUAL";
		String receivingCenter = "Commissioner Office";
		String location = "Election Ward No 1";
		String childLocation = "Gadu Veedhi";
		String stateId = "1";
		String assigneeId = "2";
		String departmentName = "Accounts";
		String address=null;
		List<String> mediaUrls=new ArrayList<>();
        mediaUrls.add(null);
        mediaUrls.add(null);
        String jurisdictionId="1";
        String description=null;
        String mobileNumber = null;
        String emailId = null;
        String name = "kumar";
        Integer id =67;
        boolean anonymousUser = false;
        List<Role> roles=new ArrayList<>();
        roles.add(new Role(1L,"Citizen"));
        roles.add(new Role(2L,"Employee"));

		final HashMap<String, String> additionalValues = new HashMap<>();
		additionalValues.put("ReceivingMode", receivingMode);
		additionalValues.put("LocationId", location);
		additionalValues.put("ChildLocationId", childLocation);
		additionalValues.put("ReceivingCenter", receivingCenter);
		additionalValues.put("StateId", stateId);
		additionalValues.put("AssigneeId", assigneeId);
		additionalValues.put("DepartmentName", departmentName);
		AuthenticatedUser user=AuthenticatedUser.builder().mobileNumber(mobileNumber).emailId(emailId).name(name).id(id).anonymousUser(anonymousUser).roles(roles).type(UserType.CITIZEN).build();
		Complaint complaint=Complaint.builder()
		        .authenticatedUser(user)
		        .crn(crn)
		         .complaintType(new ComplaintType("abc","complaintCode"))
		         .additionalValues(additionalValues)
		         .address(address)
		         .mediaUrls(mediaUrls)
		         .complaintLocation(new ComplaintLocation(new Coordinates(0.0,0.0),null,"34"))
		         .complainant(new Complainant("kumar",null,null,"mico layout","user"))
		         .tenantId(jurisdictionId)
		         .description(description).build();
		ComplaintSearchCriteria criteria=ComplaintSearchCriteria.builder().assignmentId(10L).endDate(null).lastModifiedDatetime(null).serviceCode("serviceCode_123").serviceRequestId("serid_123").startDate(null).status("registered").userId(10L).build();


		List<Complaint> complaints = new ArrayList<>(Collections.singletonList(complaint));
		when(complaintService.findAll(criteria)).thenReturn(complaints);

		String content = new TestResourceReader().readResource("getServiceRequests.txt");
		String expectedContent = String.format(content, crn, complaintType, complainant, receivingMode, receivingCenter,
				location, childLocation);

		mockMvc.perform(get("/seva?jurisdiction_id=1&service_request_id=serid_123&service_code=serviceCode_123&status=registered&assignment_id=10&user_id=10")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")).header("api_id", "api_id")
				.header("ver", "ver").header("ts", "ts").header("action", "action").header("did", "did")
				.header("msg_id", "msg_id").header("requester_id", "requester_id").header("auth_token", "auth_token"))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedContent));
	}

	@Test
	public void testGetServiceRequestsFailsWithoutJurisdictionId() throws Exception {
		mockMvc.perform(get("/seva").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				.header("api_id", "api_id").header("ver", "ver").header("ts", "ts").header("action", "action")
				.header("did", "did").header("msg_id", "msg_id").header("requester_id", "requester_id")
				.header("auth_token", "auth_token")).andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testGetServiceRequestsFailsWithoutRequiredHeaders() throws Exception {
		mockMvc.perform(
				get("/seva?jurisdiction_id=1").accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						.header("ts", "ts").header("action", "action").header("did", "did").header("msg_id", "msg_id")
						.header("auth_token", "auth_token"))
				.andExpect(status().isNotFound());
	}
}