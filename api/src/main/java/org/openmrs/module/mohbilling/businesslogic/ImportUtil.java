/**
 * 
 */
package org.openmrs.module.mohbilling.businesslogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohbilling.model.BillableService;
import org.openmrs.module.mohbilling.model.FacilityServicePrice;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Kamonyo
 * 
 *         Useful to handle importing Billable Service or Facility Services
 *         Prices action: this can be done in order to create new ones or update
 *         existing ones...
 */
public class ImportUtil {
	private static Log log = LogFactory.getLog(ImportUtil.class);

	/**
	 * Reads content of a given File by specifying its path on the System...
	 * 
	 * @param filePath
	 *            the path where the file can be found on the system (e.g.
	 *            "C:\\test.xls" or "/home/user/text.xls")
	 * @param sheetAt
	 *            the number of the Sheet that will be read
	 * @param rowAt
	 *            see if you can precise the row to start from... Normally the
	 *            default value should be <code>TRUE</code>
	 * @return myList the list of the Excel File content (LiCo)
	 */
	public static List<List<String>> readFile(String filePath, int sheetAt,
			int rowAt) {

		FileInputStream file = null;
		List<List<String>> myList = new ArrayList<List<String>>();
		try {

			file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(sheetAt);

			// Get iterator to all the rows in current sheet
			Iterator<HSSFRow> rowIterator = sheet.rowIterator();

			// Get iterator to all cells of current row
			int rowIndex = 0, columnIndex = 0;

			while (rowIterator.hasNext()) {

				List<String> rowValues = new ArrayList<String>();
				HSSFRow myRow = (HSSFRow) rowIterator.next();

				for (columnIndex = 0; columnIndex < 6; columnIndex++) {
					HSSFCell myCell = myRow.getCell((short) columnIndex);

					if (myCell != null) {
						if (myCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							rowValues.add(myCell.getStringCellValue());
						} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							rowValues.add(""
									+ ((Double) myCell.getNumericCellValue())
											.longValue());
						} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
							rowValues.add("" + myCell.getStringCellValue());
						} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
							rowValues.add("" + myCell.getBooleanCellValue());
						} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
							rowValues.add(""
									+ ((Double) myCell.getNumericCellValue())
											.longValue());
						} else
							rowValues.add(myCell.toString());
					} else
						rowValues.add("");

				}

				if (rowIndex >= rowAt - 1) {
					myList.add(rowValues);
				}
				rowIndex++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return myList;
	}

	/**
	 * Gets the Tariff Items as Facility Service Price List...
	 * 
	 * @param tariffList
	 *            the list of items from the web pages (those read from Excel
	 *            file)
	 * @return FacilityServicePrices list generated from the List Chosen by the
	 *         User
	 */
	public List<FacilityServicePrice> getNewClinicalServices(
			List<Object[]> tariffList) {

		List<FacilityServicePrice> billablesList = new ArrayList<FacilityServicePrice>();

		for (Object[] row : tariffList) {

			double tariffB;
			String clinicalService;
			Concept concept;

			clinicalService = (String) row[0];
			tariffB = (Double) row[2];
			concept = Context.getConceptService().getConcept((Integer) row[5]);

			// Create a Facility Service Price object:
			FacilityServicePrice service = new FacilityServicePrice();

			service.setConcept(concept);
			service.setCreatedDate(new Date());
			service.setCreator(Context.getAuthenticatedUser());
			service.setDescription(clinicalService);
			service.setName(clinicalService);
			service.setLocation(Context.getLocationService()
					.getDefaultLocation());
			service.setFullPrice(BigDecimal.valueOf(tariffB));
			service.setRetired(false);
			service.setShortName(clinicalService);
			service.setStartDate(new Date());
			service.setEndDate(InsurancePolicyUtil.addYears(new Date(), 10));

			FacilityServicePriceUtil.createFacilityService(service);

			billablesList.add(service);
		}

		return billablesList;
	}

	public List<BillableService> createNewBillableServices(
			List<FacilityServicePrice> fspList) {
		
		//TODO: code goes here...

		return null;
	}

	public List<BillableService> updateBillableServices(
			List<FacilityServicePrice> fspList) {
		
		//TODO: code goes here...

		return null;
	}
	
	/**
	 * Gets the Tariff Items as Facility Service Price List...
	 * 
	 * @param tariffList
	 *            the list of items from the web pages (those read from Excel
	 *            file)
	 * @return FacilityServicePrices list generated from the List Chosen by the
	 *         User
	 */
	public List<FacilityServicePrice> getUpdatedClinicalServices(
			List<Object[]> tariffList) {

		List<FacilityServicePrice> billablesList = new ArrayList<FacilityServicePrice>();
		
		//TODO: code goes here...
		
		return billablesList;
	}
}
