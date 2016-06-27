/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version 2 only
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

/**
 * Builds a VulnNaiAuditDetails object from result sets fetched from the NAI
 * Audit Details table.
 *
 * @author sbillings
 *
 */
public class VulnNaiAuditDetailsMapper implements
RowMapper<VulnNaiAuditDetails> {

	/**
	 * Create a VulnNaiAuditDetails object from a result set.
	 */
	@Override
	public VulnNaiAuditDetails mapRow(final ResultSet rs, final int rowNum)
			throws SQLException {

		final AppCompVulnKey key = new AppCompVulnKey(rs.getString("application_id"), rs.getString("componentuse_id"),
				rs.getString("component_id"), rs.getString("vulnerability_id"));
		final VulnNaiAuditDetails result = new VulnNaiAuditDetails(key,
				rs.getString("nai_audit_status"),
				rs.getString("nai_audit_comment"));
		return result;
	}

}
