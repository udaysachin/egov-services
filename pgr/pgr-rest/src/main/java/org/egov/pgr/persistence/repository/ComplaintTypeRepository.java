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
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.persistence.repository;

import org.egov.pgr.persistence.entity.Complaint;
import org.egov.pgr.persistence.entity.ComplaintType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComplaintTypeRepository {

    private ComplaintTypeJpaRepository complaintTypeJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ComplaintTypeRepository(ComplaintTypeJpaRepository complaintTypeJpaRepository) {
        this.complaintTypeJpaRepository = complaintTypeJpaRepository;
    }

    public List<ComplaintType> findActiveComplaintTypesByCategory(Long categoryId) {
        return complaintTypeJpaRepository.findByIsActiveTrueAndCategoryIdOrderByNameAsc(categoryId);
    }

    public List<ComplaintType> getFrequentlyFiledComplaints(Integer count) {

        DateTime previousDate = new DateTime();
        final DateTime currentDate = new DateTime();
        previousDate = previousDate.minusMonths(1);

        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Complaint.class, "complaint");
        criteria.createAlias("complaint.complaintType", "compType");
        criteria.setProjection(Projections.projectionList().add(Projections.property("complaint.complaintType"))
                .add(Projections.count("complaint.complaintType").as("count"))
                .add(Projections.groupProperty("complaint.complaintType")));
        criteria.add(Restrictions.between("complaint.createdDate", previousDate.toDate(), currentDate.toDate()));
        criteria.add(Restrictions.eq("compType.isActive", Boolean.TRUE));
        criteria.setMaxResults(count).addOrder(Order.desc("count"));
        final List<Object> resultList = criteria.list();
        final List<ComplaintType> complaintTypeList = new ArrayList<ComplaintType>();

        for (final Object row : resultList) {
            final Object[] columns = (Object[]) row;
            complaintTypeList.add((ComplaintType) columns[0]);
        }
        return complaintTypeList;

    }

    public ComplaintType getComplaintType(String complaintTypeCode) {
        return complaintTypeJpaRepository.findByCode(complaintTypeCode);
    }
    
    public List<ComplaintType> getAllComplaintTypes(){
    	return complaintTypeJpaRepository.findAll();
    }
}
