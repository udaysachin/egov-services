/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.egf.persistence.entity.enums.BudgetAccountType;
import org.egov.egf.persistence.entity.enums.BudgetingType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude={"fiscalPeriodList"},callSuper=false)
@Table(name = "egf_financialyear")
@SequenceGenerator(name = FinancialYear.SEQ_FINANCIALYEAR, sequenceName = FinancialYear.SEQ_FINANCIALYEAR, allocationSize = 1)
public class FinancialYear extends AbstractAuditable {

    private static final long serialVersionUID = -1563670460427134487L;
    public static final String SEQ_FINANCIALYEAR = "seq_egf_financialyear";

    @Id
    @GeneratedValue(generator = SEQ_FINANCIALYEAR, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Length(min = 1, max = 25)
    @NotBlank
    @Column(name="FinancialYear")
    private String finYearRange;

  
    @NotNull
    private Date startingDate;

    @NotNull
    private Date endingDate;
    @NotNull
    private Boolean active;
    @NotNull
    private Boolean isActiveForPosting;
   
    private Boolean isClosed;
 
    private Boolean transferClosingBalance;
    
    @OneToMany(mappedBy = "financialYear", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC ")
    private List<FiscalPeriod> fiscalPeriodList = new ArrayList<FiscalPeriod>(0);
   
    public void setFiscalPeriod(final List<FiscalPeriod> fiscalPeriod) {
        this.fiscalPeriodList.clear();
        if (fiscalPeriod != null)
            this.fiscalPeriodList.addAll(fiscalPeriod);
    }
    
    public void addFiscalPeriod(final FiscalPeriod fiscalPeriod) {
    	this.fiscalPeriodList.add(fiscalPeriod);
    }
    @Override
    public Long getId()
    {
    	return this.id;
    }
}
