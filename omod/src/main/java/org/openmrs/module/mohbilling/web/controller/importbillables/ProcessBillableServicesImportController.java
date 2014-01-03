/**
 * 
 */
package org.openmrs.module.mohbilling.web.controller.importbillables;

import java.io.BufferedWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
//import org.openmrs.module.importpatientidentification.utils.FileReading;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Yves GAKUBA
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
		int numberOfPatientCreatedSuccessFully = 0, numberOfPatientNotCreatedSuccessFully = 0;

		String fileName = "";
		BufferedWriter out = null;

		try {
			boolean includeFirstRow = (request.getParameter("includeFirstRow") == null || request
					.getParameter("includeFirstRow").trim().compareTo("false") == 0) ? false
					: true;
			String sourceFile = request.getParameter("sourceFile");
			Location loc = Context.getLocationService().getLocation(
					Integer.valueOf(request.getParameter("location")));

			PatientIdentifierType tracnetIdType = Context.getPatientService()
					.getPatientIdentifierType(
							Integer.valueOf(request
									.getParameter("tracnetIdentifierType")));

			PatientIdentifierType localIdType = (request
					.getParameter("includeLocalIdentifierType") != null && request
					.getParameter("includeLocalIdentifierType").trim()
					.compareToIgnoreCase("on") == 0) ? Context
					.getPatientService().getPatientIdentifierType(
							Integer.valueOf(request
									.getParameter("localIdentifierType")))
					: null;

			PatientIdentifierType arvIdType = (request
					.getParameter("includeArvIdentifierType") != null && request
					.getParameter("includeArvIdentifierType").trim()
					.compareToIgnoreCase("on") == 0) ? Context
					.getPatientService().getPatientIdentifierType(
							Integer.valueOf(request
									.getParameter("arvIdentifierType"))) : null;

			int indexFrom = 0, i = 0;

			if (includeFirstRow)
				indexFrom = 0;
			else {
				indexFrom = 1;
			}
			
			fileName = createReportFileName(sourceFile, request);
//			out = FileReading.createReportFile(fileName);
			for (int index = indexFrom; index < Integer.valueOf(request
					.getParameter("numberOfRecords")); index++) {
				Patient p = null;//FileReading.createPatient(sourceFile, index,
//						request, loc, tracnetIdType, localIdType, arvIdType,
//						out);

				log.info(">>>>>>>>>>> Trying to create patient at rowIndex#"
						+ index);

				if (null != p) {
					numberOfPatientCreatedSuccessFully += 1;
				} else {
					numberOfPatientNotCreatedSuccessFully += 1;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
					"An error occured, please contact your administrator.");
		} finally {
//			if (null != out)
//				FileReading.closeReportFile(out);
		}
		mav.addObject("recordCreatedSuccessfully",
				numberOfPatientCreatedSuccessFully);
		mav.addObject("recordNotCreatedSuccessfully",
				numberOfPatientNotCreatedSuccessFully);

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

	private String createReportFileName(String filePath,
			HttpServletRequest request) {

		String t = new Date().toString();
		t = t.replace(" ", "_");
		t = t.replace(":", "_");

		String fileName = "report_file_" + (t + ".txt");

		return fileName;
	}

}
