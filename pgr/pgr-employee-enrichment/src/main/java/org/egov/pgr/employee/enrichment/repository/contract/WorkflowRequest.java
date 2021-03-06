package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.egov.pgr.employee.enrichment.consumer.contract.RequestInfo;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Getter
@Builder
public class WorkflowRequest {

    public enum Action {
        CREATE("create"), END("close");

        private String strValue;

        Action(String strValue) {
            this.strValue = strValue;
        }

        public static String forComplaintStatus(String complaintStatus) {
            if (complaintStatus.equals("REGISTERED")) {
                return CREATE.strValue;
            } else if (complaintStatus.equals("COMPLETED")) {
                return END.strValue;
            }
            return CREATE.strValue;
        }
    }

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("type")
    private String type;

    @JsonProperty("description")
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    @JsonProperty("created_Date")
    private Date createdDate;

    @JsonProperty("last_updated")
    private Date lastupdatedSince;

    @JsonProperty("status")
    private String status;

    @JsonProperty("action")
    private String action;

    @JsonProperty("business_key")
    private String businessKey;

    @JsonProperty("assignee")
    private Long assignee;

    @JsonProperty("group")
    private String group;

    @JsonProperty("sender_name")
    private String senderName;

    @JsonProperty("values")
    private Map<String, Attribute> values;

    public String getValueForKey(String key) {
        if (Objects.nonNull(values.get(key)))
            return values.get(key).getValues().get(0).getName();
        return "";
    }

}
