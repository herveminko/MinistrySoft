/**
 *
 */
package jw.ministry.soft.modules.data.exchange.imports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;

import jw.ministry.soft.modules.data.dao.CongregationHome;
import jw.ministry.soft.modules.utils.HibernateUtil;
import jxl.write.WriteException;

/**
 * @author HervéClaude
 *
 */
public class InterestedListParser {

	private static final String sourceFileName = "interested.xlsx";

	private static final String targetFileName = "interested-all.xlsx";

	private static XSSFWorkbook sourceWorkbook;

	private static XSSFWorkbook targetWorkbook;

	public static void main(String[] args) throws JAXBException, IOException {

		exportTerritoryiesContacts();
		// HibernateUtil.shutdown();
		// System.exit(0);
	}

	public static void exportTerritoryiesContacts() {
		try {
			extractSpecificTerritoriesInterested("interested-291.xlsx", 291, null);
			extractSpecificTerritoriesInterested("interested-179.xlsx", 179, null);
			extractSpecificTerritoriesInterested(null, null, null);
			extractSpecificTerritoriesInterested("interested-PB.xlsx", null, Arrays.asList("Paderborn"));
			extractSpecificTerritoriesInterested("interested-BI.xlsx", null, Arrays.asList("Bielefeld"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Uses the JExcelAPI to create a spreadsheet
	 *
	 * @exception IOException
	 * @exception WriteException
	 */
	public static void overWriteHeaders(XSSFWorkbook workbook, XSSFSheet sheet) throws IOException, WriteException {

		XSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints(Short.valueOf("14"));
		font.setBold(true);
		style.setFont(font);

		Row row = sheet.createRow(0);
		// Code
		Cell cell = row.createCell(0);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Rue
		cell = row.createCell(1);
		cell.setCellValue("Rue");
		cell.setCellStyle(style);

		// Numéro maison
		cell = row.createCell(2);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Code postal
		cell = row.createCell(3);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Nom
		cell = row.createCell(4);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Prénom");
		cell = row.createCell(5);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Francophone?
		cell = row.createCell(6);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Appartement
		cell = row.createCell(7);
		cell.setCellValue("");
		cell.setCellStyle(style);

		// Notes
		cell = row.createCell(8);
		cell.setCellValue("Notes");
		cell.setCellStyle(style);

	}

	public static void extractAllTerritoriesInterested(XSSFSheet sheet) throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CongregationHome daoCongregation = new CongregationHome();

		// overWriteHeaders(sheet);

		XSSFSheet sourceSheet = sourceWorkbook.getSheet("Interessés");
		int rowsCount = sourceSheet.getLastRowNum();

		System.out.println("source sheet contains " + rowsCount + " rows!!!");

		Iterator<Row> iterator = sourceSheet.iterator();

		int i = 0;

		XSSFCellStyle orangeStyle = targetWorkbook.createCellStyle();
		orangeStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		orangeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = targetWorkbook.createFont();
		font.setColor(IndexedColors.BROWN.getIndex());
		font.setFontHeightInPoints(Short.valueOf("14"));
		orangeStyle.setFont(font);

		XSSFCellStyle defaultStyle = targetWorkbook.createCellStyle();

		Font defaultFont = targetWorkbook.createFont();
		defaultFont.setFontHeightInPoints(Short.valueOf("14"));
		defaultStyle.setFont(defaultFont);

		// Iterate over the source workbook rows
		while (iterator.hasNext()) {
			// The first row should be ignored
			// if (i > 1) {

			System.out.println("processing row number " + i);

			Row currentRow = iterator.next();
			Iterator<Cell> cellIterator = currentRow.iterator();

			// Row newRow = sheet.createRow(i-1);
			Row newRow = sheet.createRow(i);

			while (cellIterator.hasNext()) {

				Cell currentCell = cellIterator.next();
				int columnIndex = currentCell.getColumnIndex();
				String columnValue = "";

				if (currentCell.getCellTypeEnum() == CellType.STRING) {
					columnValue = currentCell.getStringCellValue();
				} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
					columnValue = String.format("%1$,.0f", currentCell.getNumericCellValue()).replace(".", "");
				}

				switch (columnIndex) {
				case 0:
					Cell codeCell = newRow.createCell(0);
					codeCell.setCellStyle(orangeStyle);
					codeCell.setCellValue(columnValue);
					break;
				case 2:
					Cell streetCell = newRow.createCell(1);
					streetCell.setCellValue(columnValue);
					streetCell.setCellStyle(defaultStyle);
					break;
				case 3:
					Cell houseCell = newRow.createCell(2);
					houseCell.setCellValue(columnValue);
					houseCell.setCellStyle(defaultStyle);
					break;
				case 4:
					Cell postalCodeCell = newRow.createCell(3);
					postalCodeCell.setCellValue(columnValue);
					postalCodeCell.setCellStyle(defaultStyle);
					break;
				case 5:
					Cell lastNameCell = newRow.createCell(4);
					lastNameCell.setCellValue(columnValue);
					lastNameCell.setCellStyle(defaultStyle);
					break;
				case 6:
					Cell firstNameCell = newRow.createCell(5);
					firstNameCell.setCellValue(columnValue);
					firstNameCell.setCellStyle(defaultStyle);
					break;
				case 7:
					Cell frenchFlagCell = newRow.createCell(6);
					frenchFlagCell.setCellValue(columnValue);
					frenchFlagCell.setCellStyle(defaultStyle);
					break;
				case 9:
					Cell appartmentCell = newRow.createCell(7);
					appartmentCell.setCellValue(columnValue);
					appartmentCell.setCellStyle(defaultStyle);
					break;
				case 11:
					Cell noteCell = newRow.createCell(8);
					noteCell.setCellValue(columnValue);
					noteCell.setCellStyle(defaultStyle);
					break;
				default:
					break;
				}

			}

			i++;

			// } else {
			// System.out.println("ignoring first row with all headers");
			// i++;
			// }
		}

		// update headers
		overWriteHeaders(targetWorkbook, sheet);

		// auto size columns
		autoSizeColumns(targetWorkbook);

	}

	public static void extractSpecificTerritoriesInterested(String outputFilename, Integer territoryCode,
			List<String> groups) throws WriteException, IOException {
		// overWriteHeaders(sheet);

		FileInputStream excelFile = new FileInputStream(new File(sourceFileName));
		sourceWorkbook = new XSSFWorkbook(excelFile);

		targetWorkbook = new XSSFWorkbook();
		String fileName = outputFilename != null ? outputFilename : targetFileName;
		FileOutputStream targetOutputStream = new FileOutputStream(fileName);

		XSSFSheet sheet = targetWorkbook.createSheet("TerritoriesInterested");

		XSSFSheet sourceSheet = sourceWorkbook.getSheet("Interessés");
		int rowsCount = sourceSheet.getLastRowNum();

		System.out.println("source sheet contains " + rowsCount + " rows!!!");

		Iterator<Row> iterator = sourceSheet.iterator();

		int i = 0;

		XSSFCellStyle orangeStyle = targetWorkbook.createCellStyle();
		orangeStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		orangeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = targetWorkbook.createFont();
		font.setColor(IndexedColors.BROWN.getIndex());
		font.setFontHeightInPoints(Short.valueOf("14"));
		orangeStyle.setFont(font);

		XSSFCellStyle defaultStyle = targetWorkbook.createCellStyle();

		Font defaultFont = targetWorkbook.createFont();
		defaultFont.setFontHeightInPoints(Short.valueOf("14"));
		defaultStyle.setFont(defaultFont);

		// Iterate over the source workbook rows
		while (iterator.hasNext()) {
			// The first row should be ignored
			Row currentRow = iterator.next();
			if (i > 0) {

				System.out.println("processing row number " + i);
				Iterator<Cell> cellIterator = currentRow.iterator();

				boolean generalFilter = currentRow.getCell(0) != null && currentRow.getCell(0).getCellTypeEnum() != null
						&& currentRow.getCell(0).getCellTypeEnum() == CellType.NUMERIC;

				boolean territoryCodeFilter = true;

				try {
					territoryCodeFilter = (territoryCode != null)
							? currentRow.getCell(0).getNumericCellValue() == Double.valueOf(territoryCode) : true;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"CodeFilter Error -- currentRow.getCell(0).getNumericCellValue() threw an exception . ignoring current record ");
					territoryCodeFilter = false;
				}

				boolean territoryGroupFilter = true;
				try {
					territoryGroupFilter = (groups != null && !groups.isEmpty())
							? groups.contains(currentRow.getCell(10).getStringCellValue()) : true;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"GroupFilter Error -- currentRow.getCell(10).getStringCellValue() threw an exception . ignoring current record ");
					territoryGroupFilter = false;
				}

				if (generalFilter && territoryCodeFilter && territoryGroupFilter) {

					// Row newRow = sheet.createRow(i-1);
					Row newRow = sheet.createRow(i);

					while (cellIterator.hasNext()) {

						Cell currentCell = cellIterator.next();
						int columnIndex = currentCell.getColumnIndex();
						String columnValue = "";

						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							columnValue = currentCell.getStringCellValue();
						} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							columnValue = String.format("%1$,.0f", currentCell.getNumericCellValue()).replace(".", "");
						}

						switch (columnIndex) {
						case 0:
							Cell codeCell = newRow.createCell(0);
							codeCell.setCellStyle(orangeStyle);
							codeCell.setCellValue(columnValue);
							break;
						case 2:
							Cell streetCell = newRow.createCell(1);
							streetCell.setCellValue(columnValue);
							streetCell.setCellStyle(defaultStyle);
							break;
						case 3:
							Cell houseCell = newRow.createCell(2);
							houseCell.setCellValue(columnValue);
							houseCell.setCellStyle(defaultStyle);
							break;
						case 4:
							Cell postalCodeCell = newRow.createCell(3);
							postalCodeCell.setCellValue(columnValue);
							postalCodeCell.setCellStyle(defaultStyle);
							break;
						case 5:
							Cell lastNameCell = newRow.createCell(4);
							lastNameCell.setCellValue(columnValue);
							lastNameCell.setCellStyle(defaultStyle);
							break;
						case 6:
							Cell firstNameCell = newRow.createCell(5);
							firstNameCell.setCellValue(columnValue);
							firstNameCell.setCellStyle(defaultStyle);
							break;
						case 7:
							Cell frenchFlagCell = newRow.createCell(6);
							frenchFlagCell.setCellValue(columnValue);
							frenchFlagCell.setCellStyle(defaultStyle);
							break;
						case 9:
							Cell appartmentCell = newRow.createCell(7);
							appartmentCell.setCellValue(columnValue);
							appartmentCell.setCellStyle(defaultStyle);
							break;
						case 11:
							Cell noteCell = newRow.createCell(8);
							noteCell.setCellValue(columnValue);
							noteCell.setCellStyle(defaultStyle);
							break;
						default:
							break;
						}

					}

					i++;

				} else {
					// i++;
				}

			} else {
				System.out.println("ignoring first row with all headers");
				Row newRow = sheet.createRow(i);
				i++;
			}
		}

		// update headers
		overWriteHeaders(targetWorkbook, sheet);

		// auto size columns
		autoSizeColumns(targetWorkbook);

		// auto filter table columns
		autoFilterColumns(targetWorkbook);

		targetWorkbook.write(targetOutputStream);

		targetWorkbook.close();
		sourceWorkbook.close();

	}

	/**
	 * Adapt the size of each workbook column automatically.
	 *
	 * @param workbook
	 */
	public static void autoSizeColumns(XSSFWorkbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			// sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum()
			// + 1,
			// 0, 14));
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}

	/**
	 * Adapt the size of each workbook column automatically.
	 *
	 * @param workbook
	 */
	public static void autoFilterColumns(XSSFWorkbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum() + 1, 0, 8));
		}
	}

}
