/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.workflow.persistence.entity;

import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "EG_WF_MATRIX")
@SequenceGenerator(name = WorkFlowMatrix.SEQ_WF_MATRIX, sequenceName = WorkFlowMatrix.SEQ_WF_MATRIX, allocationSize = 1)
public class WorkFlowMatrix extends AbstractPersistable<Long> implements Cloneable {

    public static final String SEQ_WF_MATRIX = "SEQ_EG_WF_MATRIX";
    private static final long serialVersionUID = 4954386159285858993L;
    @Id
    @GeneratedValue(generator = SEQ_WF_MATRIX, strategy = GenerationType.SEQUENCE)
    private Long id;

    @SafeHtml
    private String department;

    @NotNull
    @SafeHtml
    private String objectType;

    @SafeHtml
    private String currentState;

    @SafeHtml
    private String currentStatus;

    @SafeHtml
    private String pendingActions;

    @SafeHtml
    private String currentDesignation;

    @SafeHtml
    private String additionalRule;

    @SafeHtml
    private String nextState;

    @SafeHtml
    private String nextAction;

    @SafeHtml
    private String nextDesignation;

    @SafeHtml
    private String nextStatus;

    @SafeHtml
    private String validActions;

    private BigDecimal fromQty;

    private BigDecimal toQty;

    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    private Date toDate;

    public WorkFlowMatrix() {

    }

    public WorkFlowMatrix(final String department, final String objectType, final String currentState, final String currentStatus,
                          final String pendingActions, final String currentDesignation, final String additionalRule, final String nextState,
                          final String nextAction, final String nextDesignation, final String nextStatus,
                          final String validActions, final BigDecimal fromQty, final BigDecimal toQty, final Date fromDate, final Date toDate) {
        super();
        this.department = department;
        this.objectType = objectType;
        this.currentState = currentState;
        this.currentStatus = currentStatus;
        this.pendingActions = pendingActions;
        this.currentDesignation = currentDesignation;
        this.additionalRule = additionalRule;
        this.nextState = nextState;
        this.nextAction = nextAction;
        this.nextDesignation = nextDesignation;
        this.nextStatus = nextStatus;
        this.validActions = validActions;
        this.fromQty = fromQty;
        this.toQty = toQty;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public WorkFlowMatrix clone() {
        return new WorkFlowMatrix(department, objectType, currentState, currentStatus, pendingActions, currentDesignation, additionalRule, nextState,
                nextAction, nextDesignation, nextStatus,
                validActions, fromQty, toQty, fromDate, toDate);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(final String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(final String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getPendingActions() {
        return pendingActions;
    }

    public void setPendingActions(final String pendingActions) {
        this.pendingActions = pendingActions;
    }

    public String getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(final String currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public String getAdditionalRule() {
        return additionalRule;
    }

    public void setAdditionalRule(final String additionalRule) {
        this.additionalRule = additionalRule;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(final String nextState) {
        this.nextState = nextState;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(final String nextAction) {
        this.nextAction = nextAction;
    }

    public String getNextDesignation() {
        return nextDesignation;
    }

    public void setNextDesignation(final String nextDesignation) {
        this.nextDesignation = nextDesignation;
    }

    public String getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(final String nextStatus) {
        this.nextStatus = nextStatus;
    }

    public String getValidActions() {
        return validActions;
    }

    public void setValidActions(final String validActions) {
        this.validActions = validActions;
    }

    public BigDecimal getFromQty() {
        return fromQty;
    }

    public void setFromQty(final BigDecimal fromQty) {
        this.fromQty = fromQty;
    }

    public BigDecimal getToQty() {
        return toQty;
    }

    public void setToQty(final BigDecimal toQty) {
        this.toQty = toQty;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

}