/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.mohbilling.web.controller.importbillables;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
//import org.openmrs.module.importpatientidentification.utils.FileReading;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This controller backs the /web/module/importPatientIdentification.jsp page.
 * This controller is tied to that jsp page in the
 * /metadata/moduleApplicationContext.xml file
 */
public class ImportBillableServicesFormController extends
		ParameterizableViewController {

	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) {
		
		if (Context.getAuthenticatedUser() == null)
			return new ModelAndView(new RedirectView(request.getContextPath()
					+ "/login.htm"));
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());

		try {
			if (null != request.getParameter("fileToImport")
					&& request.getParameter("fileToImport").trim()
							.compareTo("") != 0) {
				log.info(">>>>>>>> Starting reading file '"
						+ request.getParameter("fileToImport") + "'....");

				boolean includeFirstRow = (request.getParameter("columnHeader") != null && request
						.getParameter("columnHeader").trim().compareToIgnoreCase("on") == 0) ? false
						: true;
				mav.addObject("fileToImport", request
						.getParameter("fileToImport"));
				mav.addObject("columnHeaderChecked", includeFirstRow);

//				List<List<String>> result = FileReading
//						.init(request.getParameter("fileToImport"),
//								includeFirstRow, request);
//				mav.addObject("result", result);
//				mav.addObject("numberOfPatientsToImport", result.size());
				log.info(">>>>>>>> Finished!");

				mav.addObject("pIdentifierTypes",
						getPatientIdentifierTypeList());
				mav.addObject("locations", getLocationList());
			} else
				mav.addObject("result", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	private HashMap<Integer, String> getLocationList() {
		HashMap<Integer, String> locationMap = new HashMap<Integer, String>();

		for (Location loc : Context.getLocationService().getAllLocations())
			locationMap.put(loc.getLocationId(), loc.getName());

		return locationMap;
	}

	private HashMap<Integer, String> getPatientIdentifierTypeList() {
		HashMap<Integer, String> pIdentifierTypeMap = new HashMap<Integer, String>();

		for (PatientIdentifierType pit : Context.getPatientService()
				.getAllPatientIdentifierTypes())
			pIdentifierTypeMap.put(pit.getPatientIdentifierTypeId(), pit
					.getName());

		return pIdentifierTypeMap;
	}

}
