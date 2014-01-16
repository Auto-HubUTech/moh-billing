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
import org.openmrs.module.mohbilling.model.ImportedItem;

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
	public static List<ImportedItem> readFile(String filePath, int sheetAt,
			int rowAt) {

		FileInputStream file = null;
		List<ImportedItem> myList = new ArrayList<ImportedItem>();

		try {

			file = new FileInputStream(new File(filePath));

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(sheetAt);

			// Get iterator to all the rows in current sheet
			Iterator<HSSFRow> rowIterator = sheet.rowIterator();

			// Get iterator to all cells of current row
			int rowIndex = 0;

			while (rowIterator.hasNext()) {

				HSSFRow myRow = (HSSFRow) rowIterator.next();

				if (rowIndex >= rowAt - 1) {

					ImportedItem tariff = new ImportedItem(
							(String) getCellValue(myRow.getCell((short) 0)),
							(Double) getCellValue(myRow.getCell((short) 1)),
							(Double) getCellValue(myRow.getCell((short) 2)),
							(Double) getCellValue(myRow.getCell((short) 3)),
							(Double) getCellValue(myRow.getCell((short) 4)),
							((Double) getCellValue(myRow.getCell((short) 5)))
									.intValue(), true);
					myList.add(tariff);
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

		// TODO: code goes here...

		return null;
	}

	public List<BillableService> updateBillableServices(
			List<FacilityServicePrice> fspList) {

		// TODO: code goes here...

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

		// TODO: code goes here...

		return billablesList;
	}

	/**
	 * Gets the HSSFCell (from Excel file) value and converts it into Object
	 * 
	 * @param myCell
	 *            the Excel file cell to be returned as Object
	 * @return empty String object when the cell is empty, and Object object
	 *         otherwise
	 */
	private static Object getCellValue(HSSFCell myCell) {
		if (myCell != null) {
			if (myCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				return myCell.getStringCellValue();
			} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				return myCell.getNumericCellValue();
			} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				return myCell.getNumericCellValue();
			} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				return myCell.getBooleanCellValue();
			} else if (myCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				return myCell.getNumericCellValue();
			} else
				return myCell.toString();
		} else
			return "";
	}

	public static List<ImportedItem> getItemByConcept(String checkBox,
			List<ImportedItem> items, boolean chosen) {

		List<ImportedItem> itemsList = new ArrayList<ImportedItem>();
		itemsList.addAll((List<ImportedItem>) items);
		String[] temp = checkBox.split("chosen_");
		Integer conceptId = Integer.parseInt(temp[1]);

		System.out
				.println("____________________ CONCEPT ID got from the JSP page : "
						+ conceptId);

		if (itemsList.get(0) instanceof ImportedItem)
			for (ImportedItem item : itemsList) {
				if (item.getConceptId().intValue() == conceptId) {
					item.setChosen(chosen);
					break;
				}
			}
		return items;
	}
}
