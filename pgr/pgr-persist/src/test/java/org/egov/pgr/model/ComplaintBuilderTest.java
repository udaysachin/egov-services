package org.egov.pgr.model;

import org.egov.pgr.contracts.grievance.SevaRequest;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.repository.PositionRepository;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.EscalationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ComplaintBuilderTest {

    @Mock
    private ComplaintTypeService complaintTypeService;
    @Mock
    private ComplaintStatusService complaintStatusService;
    @Mock
    private EscalationService escalationService;
    @Mock
    private PositionRepository positionRepository;
    private String tenantId = "ap.public";

    @Test
    public void testNewComplaintShouldBeCreatedWithPassedInServiceRequest() throws Exception {
        setupMocks();
        SevaRequest sevaRequest = successSevaRequest();
        ComplaintBuilder complaintBuilder = complaintBuilderWithComplaint(new Complaint(), sevaRequest);
        Complaint builtComplaint = complaintBuilder.build();

        Calendar c = Calendar.getInstance();
        c.setTime(builtComplaint.getEscalationDate());
        assertEquals("AA-01892-AP", builtComplaint.getCrn());
        assertEquals(15.232, builtComplaint.getLat(), 0.0);
        assertEquals(18.232, builtComplaint.getLng(), 0.0);
        assertEquals("complaint details", builtComplaint.getDetails());
        assertEquals("landmark", builtComplaint.getLandmarkDetails());
        assertEquals("complainant address", builtComplaint.getComplainant().getAddress());
        assertEquals("raju@maildrop.cc", builtComplaint.getComplainant().getEmail());
        assertEquals("9988776655", builtComplaint.getComplainant().getMobile());
        assertEquals("raju", builtComplaint.getComplainant().getName());
        assertEquals(ReceivingMode.WEBSITE, builtComplaint.getReceivingMode());
        assertEquals("magical powers", builtComplaint.getComplaintType().getName());
        assertEquals("PROCESSING", builtComplaint.getStatus().getName());
        assertEquals(Long.valueOf(18), builtComplaint.getAssignee());
        assertEquals(Long.valueOf(101), builtComplaint.getLocation());
        assertEquals(Long.valueOf(201), builtComplaint.getChildLocation());
        assertEquals("jhumritalaiyya", builtComplaint.getLocationName());
        assertEquals(Long.valueOf(88), builtComplaint.getStateId());
        assertEquals(Long.valueOf(11), builtComplaint.getDepartment());
        assertEquals(Long.valueOf(22), builtComplaint.getCreatedBy());
        assertEquals(Long.valueOf(22), builtComplaint.getLastModifiedBy());
        assertNotNull(builtComplaint.getCreatedDate());
        assertNotNull(builtComplaint.getLastModifiedDate());
        assertEquals(28, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(2, c.get(Calendar.MONTH));
        assertEquals(2017, c.get(Calendar.YEAR));
    }

    @Test
    public void testThatInvalidStatusIsIgnoredWhileUpdatingComplaint() throws Exception {
        SevaRequest sevaRequest = successSevaRequest();
        String invalidStatus = "INVALID_STATUS";
        sevaRequest.getServiceRequest().getValues().put("status", invalidStatus);
        Complaint complaint = new Complaint();
        org.egov.pgr.entity.ComplaintStatus originalStatus = new org.egov.pgr.entity.ComplaintStatus();
        originalStatus.setName(ComplaintStatus.PROCESSING.toString());
        complaint.setStatus(originalStatus);
        ComplaintBuilder complaintBuilder = complaintBuilderWithComplaint(complaint, sevaRequest);
        Complaint builtComplaint = complaintBuilder.build();

        verify(complaintStatusService, never()).getByName(invalidStatus);
        assertEquals(originalStatus, builtComplaint.getStatus());
    }

    private SevaRequest successSevaRequest() {
        HashMap<String, Object> sevaRequestMap = new HashMap<>();
        Map<String, Object> serviceRequestMap = new HashMap<>();
        Map<String, Object> requestInfoMap = new HashMap<>();
        Map<String, String> valuesMap = new HashMap<>();
        sevaRequestMap.put("RequestInfo", requestInfoMap);
        requestInfoMap.put("requester_id", "22");
        valuesMap.put("complainantAddress", "complainant address");
        valuesMap.put("receivingMode", "Website");
        valuesMap.put("status", "PROCESSING");
        valuesMap.put("assignment_id", "18");
        valuesMap.put("location_id", "101");
        valuesMap.put("child_location_id", "201");
        valuesMap.put("location_name", "jhumritalaiyya");
        valuesMap.put("state_id", "88");
        serviceRequestMap.put("values", valuesMap);
        serviceRequestMap.put("tenantId", tenantId);
        serviceRequestMap.put("service_request_id", "AA-01892-AP");
        serviceRequestMap.put("lat", 15.232D);
        serviceRequestMap.put("lng", 18.232D);
        serviceRequestMap.put("address", "landmark");
        serviceRequestMap.put("description", "complaint details");
        serviceRequestMap.put("first_name", "raju");
        serviceRequestMap.put("email", "raju@maildrop.cc");
        serviceRequestMap.put("phone", "9988776655");
        serviceRequestMap.put("service_code", "MAGIC");
        sevaRequestMap.put("ServiceRequest", serviceRequestMap);
        return new SevaRequest(sevaRequestMap);
    }

    private void setupMocks() {
        long designationId = 12L;
        long assigneeId = 18L;
        Long departmentId = 11L;
        Date escalationDate = getDateFor(2017, 2, 28);
        String complaintStatus = "PROCESSING";
        setupStatusMock();
        setupComplaintTypeMock();
        setupPositionMock(assigneeId, designationId, departmentId);
        setupEscalationDateMock(escalationDate, designationId);
        setupComplaintStatusMock(complaintStatus);
    }

    private void setupComplaintStatusMock(String statusName) {
        org.egov.pgr.entity.ComplaintStatus complaintStatus = new org.egov.pgr.entity.ComplaintStatus();
        complaintStatus.setName(statusName);
        when(complaintStatusService.getByName(statusName)).thenReturn(complaintStatus);
    }

    private void setupEscalationDateMock(Date expectedDate, long designationId) {
        when(escalationService.getExpiryDate(any(Complaint.class), eq(designationId))).thenReturn(expectedDate);
    }

    private void setupPositionMock(Long assigneeId, Long designationId, Long departmentId) {
        when(positionRepository.designationIdForAssignee(tenantId, assigneeId)).thenReturn(designationId);
        when(positionRepository.departmentIdForAssignee(tenantId, assigneeId)).thenReturn(departmentId);
    }

    private void setupComplaintTypeMock() {
        ComplaintType complaintType = new ComplaintType();
        complaintType.setName("magical powers");
        when(complaintTypeService.findByCode("MAGIC")).thenReturn(complaintType);
    }

    private void setupStatusMock() {
        String processing = "PROCESSING";
        org.egov.pgr.entity.ComplaintStatus processingStatus = new org.egov.pgr.entity.ComplaintStatus();
        processingStatus.setName(processing);
        when(complaintStatusService.getByName(processing)).thenReturn(processingStatus);
    }

    private ComplaintBuilder complaintBuilderWithComplaint(Complaint complaint, SevaRequest sevaRequest) {
        return new ComplaintBuilder(complaint, sevaRequest, complaintTypeService, complaintStatusService,
                escalationService, positionRepository);
    }

    private Date getDateFor(int year, int month, int date) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, date, 0, 0);
        return c.getTime();
    }
}