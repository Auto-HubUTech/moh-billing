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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohbilling.businesslogic.ImportUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Kamonyo
 * 
 *         This controller backs the /web/module/importBillableServicesForm.jsp
 *         page. This controller is tied to that jsp page in the
 *         /metadata/moduleApplicationContext.xml file
 * 
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
						.getParameter("columnHeader").trim()
						.compareToIgnoreCase("on") == 0) ? false : true;
				mav.addObject("fileToImport",
						request.getParameter("fileToImport"));
				mav.addObject("columnHeaderChecked", includeFirstRow);

				List<List<String>> result = ImportUtil.readFile(
						request.getParameter("fileToImport"), 0, 8);
				mav.addObject("result", result);
				mav.addObject("numberOfPatientsToImport", result.size());
				log.info(">>>>>>>> Finished!");

			} else
				mav.addObject("result", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

}
