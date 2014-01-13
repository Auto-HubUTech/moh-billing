/**
 * 
 */
package org.openmrs.module.mohbilling.businesslogic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kamonyo
 * 
 */
public class ImportUtil {
	private static Log log = LogFactory.getLog(ImportUtil.class);

	/**
	 * @param filePath
	 *            the path where the file can be found on the system (e.g.
	 *            "C:\\test.xls" or "/home/user/text.xls")
	 * @param sheetAt
	 *            the number of the Sheet that will be read
	 * @param includeFirstRow
	 *            see if you can precise the row to start from... Normally the
	 *            default value should be <code>TRUE</code>
	 * @return
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
			// } catch (XmlFileException ofe) {
			// ofe.printStackTrace();
			// request
			// .getSession()
			// .setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			// "The file you are trying to import is invalid, please check and try again.");
			// return null;
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

}
