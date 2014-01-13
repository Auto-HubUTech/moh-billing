/**
 * 
 */
package org.openmrs.module.mohbilling.businesslogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;
import org.openmrs.*;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.*;

import java.io.*;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kamonyo
 *
 */
public class ImportUtil {
	private static Log log = LogFactory.getLog(ImportUtil.class);

	/**
	 * @param filePath
	 * @param includeFirstRow
	 * @param request
	 * @return
	 */
	public static List<List<String>> init(String filePath,
			boolean includeFirstRow, HttpServletRequest request) {
		try {
			return contentReading(filePath, includeFirstRow, request);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * @param fileName
	 * @param includeFirstRow
	 * @param request
	 * @return
	 */
	public static List<List<String>> contentReading(String fileName,
			boolean includeFirstRow, HttpServletRequest request) {
		FileInputStream myInput = null;
		List<List<String>> myList = new ArrayList<List<String>>();
		try {
			myInput = new FileInputStream(fileName);

			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

			Iterator rowIter = mySheet.rowIterator();

			int rowIndex = 0, columnIndex = 0;

			while (rowIter.hasNext()) {
				List<String> rowValues = new ArrayList<String>();
				HSSFRow myRow = (HSSFRow) rowIter.next();
				for (columnIndex = 0; columnIndex < 16; columnIndex++) {
					HSSFCell myCell = myRow.getCell((short) columnIndex);
					rowValues.add((myCell != null) ? myCell.toString() : "");
				}

				if (includeFirstRow || rowIndex > 0) {
					myList.add(rowValues);

				}
				rowIndex++;
			}
			return myList;
//		} catch (XmlFileException ofe) {
//			ofe.printStackTrace();
//			request
//					.getSession()
//					.setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
//							"The file you are trying to import is invalid, please check and try again.");
//			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				myInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param row
	 * @param loc
	 * @param tracnet
	 * @param localId
	 * @param arvId
	 * @param rowIndex
	 * @return
	 * @throws Exception
	 */
	private static Collection<PatientIdentifier> createPatientIdentifiers(
			HSSFRow row, Location loc, PatientIdentifierType tracnet,
			PatientIdentifierType localId, PatientIdentifierType arvId,
			Integer rowIndex, BufferedWriter out) throws Exception {

		Collection<PatientIdentifier> identifiers = new ArrayList<PatientIdentifier>();
		String tracnetId = "";
		try {
			HSSFCell tracnetCell = row.getCell((short) 1);
			tracnetId = (tracnetCell != null) ? ""
					+ tracnetCell.getNumericCellValue() : "";
			tracnetId = tracnetId.trim().substring(0,
					tracnetId.trim().indexOf("."));
			if (tracnetId.length() != 6) {
				out.newLine();
				out.write(">> The patient at row number " + row.getRowNum()
						+ " cannot be created because TRACNET [" + tracnetId
						+ "] is invalid.");
				tracnetId = null;
				return null;
			}
		} catch (Exception e) {
			out.newLine();
			out.write(">> The patient at row number " + row.getRowNum()
					+ " cannot be created because TRACNET [" + row.getCell((short) 1).getStringCellValue()
					+ "] is invalid.");
			tracnetId = null;
			return null;
		}

		HSSFCell localIdCell = row.getCell((short) 0);
		String localIdentifier = (localIdCell != null) ? localIdCell.toString()
				: "";

		HSSFCell arvIdCell = row.getCell((short) 2);
		String arvIdentifier = (arvIdCell != null) ? "" + arvIdCell.toString()
				: "";

		if (tracnetId != null)
			identifiers.add(new PatientIdentifier(tracnetId, tracnet, loc));

		if (localId != null) {
			if (localIdentifier.trim().compareTo("") != 0)
				identifiers.add(new PatientIdentifier(localIdentifier, localId,
						loc));
		}

		if (arvId != null) {
			if (arvIdentifier.trim().compareTo("") != 0) {
				if (arvIdentifier.contains("."))
					arvIdentifier = arvIdentifier.substring(0, arvIdentifier
							.indexOf("."));

				identifiers
						.add(new PatientIdentifier(arvIdentifier, arvId, loc));
			}
		}

		return identifiers;
	}

	/**
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private static PersonAddress createPatientAddress(HSSFRow row,
			BufferedWriter out) throws Exception {

		PersonAddress pa = new PersonAddress();
		pa.setCountry("Rwanda");
		try {
			HSSFCell districtCell = row.getCell((short) 7);
			String district = (districtCell != null) ? districtCell.toString()
					: null;
			district = (null != district) ? district.trim() : null;

			HSSFCell sectorCell = row.getCell((short) 8);
			String sector = (sectorCell != null) ? sectorCell.toString() : null;
			sector = (null != sector) ? sector.trim() : null;

			HSSFCell cellCell = row.getCell((short) 9);
			String cell = (cellCell != null) ? cellCell.toString() : null;
			cell = (cell != null) ? cell.trim() : null;

			HSSFCell umuduguduCell = row.getCell((short) 10);
			String umudugudu = (umuduguduCell != null) ? umuduguduCell
					.toString() : null;
			umudugudu = (umudugudu != null) ? umudugudu.trim() : null;

			if (district != null) {
				pa.setCountyDistrict(district);
			}
			if (sector != null) {
				pa.setCityVillage(sector);
			}
			if (cell != null) {
				pa.setNeighborhoodCell(cell);
			}
			if (umudugudu != null) {
				pa.setAddress1(umudugudu);
			}
		} catch (Exception e) {
			out.newLine();
			out.write(">> The patient at row number " + row.getRowNum()
					+ " has an invalid address.");
		}
		return pa;
	}

	
	public static BufferedWriter createReportFile(String fileName) {
		BufferedWriter out = null;

		FileWriter fstream = null;
		log.info(">>>>>>>>> trying to create file : " + fileName);
		try {

			// Create file

			boolean created = createReportFolderIfNotExists();
			log.info(">>>>>>>>>>>>>>> "
					+ OpenmrsUtil.getApplicationDataDirectory()
					+ "PATIENT_IMPORTATION_REPORT\\" + fileName + created);

			fstream = new FileWriter(OpenmrsUtil.getApplicationDataDirectory()
					+ "PATIENT_IMPORTATION_REPORT\\" + fileName);

			out = new BufferedWriter(fstream);

			out.write("REPORT");
			out.newLine();
			out.write("______");
			out.newLine();

			out.flush();

			log.info(">>>>>>>>> Report File in process of creation...");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return out;
	}

	private static boolean createReportFolderIfNotExists() {
		File dir = new File(OpenmrsUtil.getApplicationDataDirectory(),
				"PATIENT_IMPORTATION_REPORT");
		return dir.mkdirs();
	}

	/**
	 * Close the file writer
	 * 
	 * @param out
	 * @throws Exception
	 */
	public static void closeReportFile(BufferedWriter out) throws Exception {
		if (null != out) {
			try {
				out.close();
				log.info(">>>>>>>>> Report File created successfully !");
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void downloadFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String filePath = OpenmrsUtil.getApplicationDataDirectory()
				+ "PATIENT_IMPORTATION_REPORT/"
				+ request.getParameter("fileName");
		ServletOutputStream outputStream = null;
		try {
			File f = new File(filePath);
			if (f != null && f.exists()) {
				String text = OpenmrsUtil.getFileAsString(f);
				outputStream = response.getOutputStream();

				response.setContentType("text/plain");
				response.setHeader("Content-Disposition",
						"attachment; filename=\""
								+ request.getParameter("fileName") + "\"");

				outputStream.println(text);

				request.setAttribute("report", text);

				outputStream.flush();

			} else {
				request.getSession().setAttribute(
						WebConstants.OPENMRS_ERROR_ATTR,
						"Please specify a valid file path.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
}
