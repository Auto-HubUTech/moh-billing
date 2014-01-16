/**
 * 
 */
package org.openmrs.module.mohbilling.web.controller.importbillables;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohbilling.model.ImportedItem;
//import org.openmrs.module.importpatientidentification.utils.FileReading;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author kamonyo
 * 
 */
public class ProcessBillableServicesImportController extends
		ParameterizableViewController {

	private Log log = LogFactory
			.getLog(ProcessBillableServicesImportController.class);

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (Context.getAuthenticatedUser() == null)
			return new ModelAndView(new RedirectView(request.getContextPath()
					+ "/login.htm"));

		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		Date startTime = new Date();
		List<ImportedItem> items = new ArrayList<ImportedItem>();
		items = (List<ImportedItem>) request.getSession().getAttribute("itemsList");
		for(ImportedItem item:items)
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<< My list is of size: "
				+ item.isChosen() + " >>>>>>>>>>>>>>>>>>>>>");

		String fileName = "";

		try {
			boolean includeFirstRow = (request.getParameter("includeFirstRow") == null || request
					.getParameter("includeFirstRow").trim().compareTo("false") == 0) ? false
					: true;

			int indexFrom = 0;

			if (includeFirstRow)
				indexFrom = 0;
			else {
				indexFrom = 1;
			}

			for (int index = indexFrom; index < Integer.valueOf(request
					.getParameter("numberOfRecords")); index++) {

				log.info(">>>>>>>>>>> Trying to create patient at rowIndex#"
						+ index);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
					"An error occured, please contact your administrator.");
		}

		mav.addObject("reportFileName", fileName);
		Date endTime = new Date();
		mav.addObject("starttime", startTime);
		mav.addObject("endtime", endTime);

		long secs = (endTime.getTime() - startTime.getTime()) / 1000;
		int hours = (int) secs / 3600;
		secs = secs % 3600;
		int mins = (int) secs / 60;
		secs = secs % 60;

		String timeOfProcess = "";
		timeOfProcess += (hours > 0) ? hours + " hour(s) " : "";
		timeOfProcess += (mins > 0 || hours > 0) ? mins + " minute(s) " : "";
		timeOfProcess += secs + " seconds";

		mav.addObject("timeOfProcess", timeOfProcess);

		return mav;
	}

}
