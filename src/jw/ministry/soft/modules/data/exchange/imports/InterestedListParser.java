/**
 *
 */
package jw.ministry.soft.modules.data.exchange.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import jw.ministry.soft.modules.utils.DateUtils;
import jxl.write.WriteException;

/**
 * @author HervéClaude
 *
 */
public class InterestedListParser {

	private static final String DEFAULT_CONTACT_NAME = "Nom manquant!!";
	private static final Double DEFAULT_LONGITUDE = Double.valueOf("8.521886");
	private static final Double DEFAULT_LATITUDE = Double.valueOf("52.03678");

	private static final String territoryHelperImportFileName = "TerritoryHelperContactsImport.xlsx";

	private static final String sourceFileName = "interested.xlsx";

	private static final String targetFileName = "interested-all.xlsx";

	private static XSSFWorkbook sourceWorkbook;

	private static XSSFWorkbook targetWorkbook;

	private static Map<String, Map<String, String>> geocodingCache = new HashMap<String, Map<String, String>>();

	static String geocodingCacheName = "geocodingCache.dat";

	private static SimpleDateFormat f = new SimpleDateFormat(DateUtils.getUiDateFormat());

	public static void main(String[] args) throws JAXBException, IOException {

		// downloadInterestedFile();
		exportTerritoryiesContacts();

		// geocodeAddress("Paderborner Straße 61, 32760 Detmold");
		// geocodeAddress("Arndtstr. 18, 32758 Detmold");
		// geocodeAddress("Benser Haide 23, Paderborn");
		// geocodeAddress("Uhlandstrasse 48, Paderborn");
		// geocodeAddress("Tempelhoferstr. 71-73, Paderborn");

		// HibernateUtil.shutdown();
		// System.exit(0);
	}

	public static void cacheGeocodedAddressData() {
		try {
			FileOutputStream fos = new FileOutputStream(geocodingCacheName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(geocodingCache);
			oos.close();
			fos.close();
			System.out.printf("Serialized HashMap geocoded address data is saved in geocodingCache.ser");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void readGeocodedAddressData() {

		try {
			FileInputStream fis = new FileInputStream(geocodingCacheName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			geocodingCache = (HashMap) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			System.out.println("no serialized cached geocoded addresses found!!!");
			geocodingCache = new HashMap<String, Map<String, String>>();

		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			geocodingCache = new HashMap<String, Map<String, String>>();
		}
	}

	public static void exportTerritoryiesContacts() {
		try {
			extractSpecificTerritoriesInterested("interested-291.xlsx", 291, null);
			extractSpecificTerritoriesInterested("interested-179.xlsx", 179, null);
			extractSpecificTerritoriesInterested(null, null, null);
			extractSpecificTerritoriesInterested("interested-PB.xlsx", null, Arrays.asList("Paderborn"));
			extractSpecificTerritoriesInterested("interested-BI.xlsx", null, Arrays.asList("Bielefeld"));

			createTerritoryHelperContactsListFromInterestedSheet(null, null, Arrays.asList("Paderborn", "Bielefeld"));

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
	public static void overWriteHeaders(XSSFWorkbook w, XSSFSheet s) throws IOException, WriteException {

		XSSFCellStyle style = w.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = w.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints(Short.valueOf("14"));
		font.setBold(true);
		style.setFont(font);

		Row row = s.createRow(0);
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

	/**
	 * Uses the JExcelAPI to create a spreadsheet
	 *
	 * @exception IOException
	 * @exception WriteException
	 */
	public static void overWriteTerritoryHelperHeaders(XSSFWorkbook w, XSSFSheet s) throws IOException, WriteException {

		XSSFCellStyle style = w.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Font font = w.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints(Short.valueOf("14"));
		font.setBold(true);
		style.setFont(font);

		/*
		 *
		 * Adresse Benser Haide 23, Paderborn Numéro 23 Rue Benser Haide
		 * Logement Hervé Minko Étage 1 Ville Paderborn Région Paderborn Code
		 * postal 33100 État NRW Code Pays Notes Notes... Dernière visite
		 * 2.4.2018 Dernière mise à jour 25.6.2018 Date de création 5.3.2018
		 */

		List<String> headers = Arrays.asList("Type de territoire", "Numéro de territoire", "Type d'emplacement",
				"Langue", "Statut", "Latitude", "Longitude", "Adresse", "Numéro", "Rue", "Logement", "Étage", "Ville",
				"Région", "Code postal", "État", "Code Pays", "Notes", "Dernière visite", "Dernière mise à jour",
				"Date de création");

		Row row = s.createRow(0);

		int cellIndex = 0;
		for (String header : headers) {
			// Code
			Cell cell = row.createCell(cellIndex++);
			cell.setCellValue(header);
			cell.setCellStyle(style);
		}

	}

	public static void extractSpecificTerritoriesInterested(String outputFilename, Integer territoryCode,
			List<String> groups) throws WriteException, IOException {
		// overWriteHeaders(sheet);

		// downloadInterestedFile();

		FileInputStream excelFile = new FileInputStream(new File(sourceFileName));
		sourceWorkbook = new XSSFWorkbook(excelFile);

		targetWorkbook = new XSSFWorkbook();
		String fileName = outputFilename != null ? outputFilename : targetFileName;

		String filePath = "fichiers_territoires" + File.separator + fileName;

		// Create output directory if needed...
		Path path = Paths.get("fichiers_territoires");
		// if directory exists?
		if (!Files.exists(path))
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				e.printStackTrace();
			}

		FileOutputStream targetOutputStream = new FileOutputStream(filePath);

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
			if (i <= 0) {
				System.out.println("ignoring first row with all headers");
				++i;
			} else {
				System.out.println("processing row number " + i);
				Iterator<Cell> cellIterator = currentRow.iterator();
				boolean generalFilter = currentRow.getCell(0) != null && currentRow.getCell(0).getCellTypeEnum() != null
						&& currentRow.getCell(0).getCellTypeEnum() == CellType.NUMERIC;
				boolean territoryCodeFilter = true;
				try {
					territoryCodeFilter = territoryCode == null
							|| currentRow.getCell(0).getNumericCellValue() == Double.valueOf(territoryCode);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"CodeFilter Error -- currentRow.getCell(0).getNumericCellValue() threw an exception . ignoring current record ");
					territoryCodeFilter = false;
				}
				boolean territoryGroupFilter = true;
				try {
					territoryGroupFilter = groups == null || groups.isEmpty()
							|| groups.contains(currentRow.getCell(10).getStringCellValue());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"GroupFilter Error -- currentRow.getCell(10).getStringCellValue() threw an exception . ignoring current record ");
					territoryGroupFilter = false;
				}
				if (generalFilter && territoryCodeFilter && territoryGroupFilter) {
					Row newRow = sheet.createRow(i);
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						int columnIndex = currentCell.getColumnIndex();
						String columnValue = "";
						if (currentCell.getCellTypeEnum() == CellType.STRING)
							columnValue = currentCell.getStringCellValue();
						else if (currentCell.getCellTypeEnum() == CellType.NUMERIC)
							columnValue = String.format("%1$,.0f", currentCell.getNumericCellValue()).replace(".", "");
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
					++i;
				}
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

	public static void createTerritoryHelperContactsListFromInterestedSheet(String outputFilename,
			Integer territoryCode, List<String> groups) throws WriteException, IOException {
		// overWriteHeaders(sheet);

		// downloadInterestedFile();
		readGeocodedAddressData();

		FileInputStream excelFile = new FileInputStream(new File(sourceFileName));
		sourceWorkbook = new XSSFWorkbook(excelFile);

		targetWorkbook = new XSSFWorkbook();
		String fileName = outputFilename != null ? outputFilename : territoryHelperImportFileName;

		String filePath = "fichiers_territoires" + File.separator + fileName;

		// Create output directory if needed...
		Path path = Paths.get("fichiers_territoires");
		// if directory exists?
		if (!Files.exists(path))
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				e.printStackTrace();
			}

		FileOutputStream targetOutputStream = new FileOutputStream(filePath);

		XSSFSheet sheet = targetWorkbook.createSheet("TerritoryHelperContacts");

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
		font.setFontHeightInPoints(Short.valueOf("12"));
		orangeStyle.setFont(font);

		XSSFCellStyle defaultStyle = targetWorkbook.createCellStyle();

		Font defaultFont = targetWorkbook.createFont();
		defaultFont.setFontHeightInPoints(Short.valueOf("12"));
		defaultStyle.setFont(defaultFont);

		// Iterate over the source workbook rows
		while (iterator.hasNext()) {
			// The first row should be ignored
			Row currentRow = iterator.next();
			if (i <= 0) {
				System.out.println("ignoring first row with all headers");
				++i;
			} else {
				// if (i == 10) {
				// break;
				// }
				System.out.println("processing row number " + i);
				Iterator<Cell> cellIterator = currentRow.iterator();
				boolean generalFilter = currentRow.getCell(0) != null && currentRow.getCell(0).getCellTypeEnum() != null
						&& currentRow.getCell(0).getCellTypeEnum() == CellType.NUMERIC;
				boolean territoryCodeFilter = true;
				try {
					territoryCodeFilter = territoryCode == null
							|| currentRow.getCell(0).getNumericCellValue() == Double.valueOf(territoryCode);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"CodeFilter Error -- currentRow.getCell(0).getNumericCellValue() threw an exception . ignoring current record ");
					territoryCodeFilter = false;
				}
				boolean territoryGroupFilter = true;
				try {
					String currentGroup = null;
					Cell groupCell = currentRow.getCell(10);
					try {
						currentGroup = groupCell.getStringCellValue();
					} catch (Exception e) {
						currentGroup = String.format("%1$,.0f", groupCell.getNumericCellValue()).replace(".", "");
					}
					territoryGroupFilter = groups == null || groups.isEmpty() || groups.contains(currentGroup);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"GroupFilter Error -- currentRow.getCell(10).getStringCellValue() threw an exception . NO Group filtering in use");
					territoryGroupFilter = false;
				}
				if (generalFilter && territoryCodeFilter && territoryGroupFilter) {
					String territoryType = null;
					String territoryNumber = null;
					String addressType = null;
					String language = "Inconnu";
					String status = "Inconnu";
					Double longitude = null;
					Double latitude = null;
					String address = null;
					String houseNumber = null;
					String streetName = null;
					String contactName = null;
					String firstName = null;
					String lastName = null;
					Integer floorNumber;
					String city = null;
					String region = null;
					String postalCode = null;
					String state = null;
					String countryCode = null;
					String notes = null;
					Date lastVisit = null;
					Date now = new Date();
					Date lastUpdate = now;
					Date creationDate = now;
					String frenchFlag = null;
					String appartement = null;
					Row newRow = sheet.createRow(i);
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						int columnIndex = currentCell.getColumnIndex();
						String columnValue = "";
						if (currentCell.getCellTypeEnum() == CellType.STRING)
							columnValue = currentCell.getStringCellValue();
						else if (currentCell.getCellTypeEnum() == CellType.NUMERIC)
							columnValue = String.format("%1$,.0f", currentCell.getNumericCellValue()).replace(".", "");
						else if (currentCell.getCellTypeEnum() == CellType.FORMULA)
							try {
								columnValue = currentCell.getStringCellValue();
							} catch (Exception e) {
								columnValue = String.format("%1$,.0f", currentCell.getNumericCellValue()).replace(".",
										"");
							}
						switch (columnIndex) {
						case 0:
							territoryNumber = columnValue;
							break;
						case 1:
							city = columnValue;
							break;
						case 2:
							streetName = columnValue;
							break;
						case 3:
							houseNumber = columnValue;
							break;
						case 4:
							postalCode = columnValue;
							break;
						case 5:
							lastName = columnValue;
							break;
						case 6:
							firstName = columnValue;
							break;
						case 7:
							frenchFlag = columnValue;
							break;
						case 9:
							appartement = columnValue;
							break;
						case 11:
							notes = columnValue;
							break;
						default:
							break;
						}
					}
					territoryType = "";
					addressType = "Maison";
					if (frenchFlag.equalsIgnoreCase("oui"))
						language = "Francais";
					Cell languageCell = newRow.createCell(6);
					languageCell.setCellValue(language);
					languageCell.setCellStyle(defaultStyle);
					if (appartement != null && !appartement.isEmpty())
						notes = "Appartement: " + appartement + ";  " + notes;
					if (notes != null && (
							notes.trim().toLowerCase().contains("pas revenir")
							|| notes.trim().toLowerCase().contains("ne veut pas")
							|| notes.trim().toLowerCase().contains("ne souhaite")
							|| notes.trim().toLowerCase().contains("pas interessé")
							|| notes.trim().toLowerCase().contains("pas du tout interessé")
							|| notes.trim().toLowerCase().contains("pas vraiment interessé")
							)
						)
						status = "Ne voulant Plus être Visité";
					else if (notes != null && notes.trim().toLowerCase().contains("visite"))
						status = "Nouvelle visite";
					// if (notes.trim().toLowerCase().contains("foyer
					// réfugiés"))
					// territoryType = "Ref";
					address = streetName + " " + houseNumber + ", " + postalCode + " " + city;
					contactName = firstName != null && !firstName.isEmpty() ? firstName + " " + lastName : lastName;
					if (contactName.trim().isEmpty()) {
						contactName = DEFAULT_CONTACT_NAME;
					}
					String addressToLocate = streetName + " " + houseNumber.split("-")[0].trim() + ", " + postalCode
							+ " " + city;

					Map<String, String> geocodedValues = geocodeAddress(addressToLocate.split("/")[0].trim());
					if (geocodedValues.get("longitude") != null) {
						longitude = Double.valueOf(geocodedValues.get("longitude"));
					}
					if (geocodedValues.get("latitude") != null) {
						latitude = Double.valueOf(geocodedValues.get("latitude"));
					}
					if (geocodedValues.get("state") != null) {
						state = geocodedValues.get("state");
					}
					if (geocodedValues.get("postcode") != null) {
						postalCode = geocodedValues.get("postcode");
					}
					if (geocodedValues.get("country") != null) {
						countryCode = geocodedValues.get("country");
					}

					int nextCellIndex = 0;
					Cell cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(territoryType);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(territoryNumber);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(addressType);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(language);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(status);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellStyle(defaultStyle);
					if (latitude != null) {
						cell.setCellValue(latitude);
					} else {
						cell.setCellValue(DEFAULT_LATITUDE);
					}
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellStyle(defaultStyle);
					if (longitude != null) {
						cell.setCellValue(longitude);
					} else {
						cell.setCellValue(DEFAULT_LONGITUDE);
						notes += ";LOCATION PAR DEFAUT!!!";
					}
					if (houseNumber == null || houseNumber.trim().isEmpty()) {
						houseNumber = "À retrouver!!";
					}
					if (streetName == null || streetName.trim().isEmpty()) {
						streetName = "À retrouver!!";
					}
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellValue(address);
					cell.setCellStyle(defaultStyle);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellValue(houseNumber);
					cell.setCellStyle(defaultStyle);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellValue(streetName);
					cell.setCellStyle(defaultStyle);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(contactName.trim());
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellType(CellType.NUMERIC);
					cell.setCellStyle(defaultStyle);
					// Étage
					cell.setCellValue(0);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(city);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(region);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(postalCode);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(state);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(countryCode);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					cell.setCellValue(notes);
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					// last visit
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					// last update
					cell.setCellValue(f.format(new Date()));
					cell = newRow.createCell(nextCellIndex++);
					cell.setCellStyle(defaultStyle);
					// creation date
					cell.setCellValue(f.format(new Date()));
					++i;
				}
			}
		}

		// update headers
		overWriteTerritoryHelperHeaders(targetWorkbook, sheet);

		// auto size columns
		autoSizeColumns(targetWorkbook);

		// auto filter table columns
		autoFilterColumnsTerritoryHelper(targetWorkbook);

		targetWorkbook.write(targetOutputStream);

		targetWorkbook.close();
		sourceWorkbook.close();

		cacheGeocodedAddressData();

	}

	public static Map<String, String> geocodeAddress(String address) {

		System.out.println("address = " + address);

		if (geocodingCache.get(address) != null) {
			System.out.println("reading address data from cache");
			return geocodingCache.get(address);
		}

		System.out.println("getting address data from web...");

		Map<String, String> mapResult = new HashMap<String, String>();
		String jsonResult = "";
		// Geodecoding URL:
		// http://photon.komoot.de/api/?q=Benser%20Haide%2025%2C%20Paderborn&limit=2

		String geocodeUrl = "http://photon.komoot.de/api/?q=";
		try {
			geocodeUrl += URLEncoder.encode(address, "UTF-8") + "&limit=20";
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block

			e1.printStackTrace();
		}

		URL url = null;
		try {
			url = new URL(geocodeUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
				content.append(inputLine);
			jsonResult = content.toString();
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// parsing file "JSONExample.json"
		Object obj;
		try {
			System.out.println(jsonResult);
			obj = new JSONParser().parse(jsonResult);
			// typecasting obj to JSONObject
			JSONObject jo = (JSONObject) obj;

			List features = (List) jo.get("features");

			for (Object oFeature : features) {
				JSONObject feature = (JSONObject) oFeature;
				JSONObject props = (JSONObject) feature.get("properties");
				String postcode = (String) props.get("postcode");
				String street = (String) props.get("street");
				String osmValue = (String) props.get("osm_value");
				String city = (String) props.get("city");
				if (osmValue.equals("residential")) {
					street = (String) props.get("name");
				}

				if (street == null) {
					String cityName = (String) props.get("name");
					if (cityName != null && address.contains(cityName)) {
						Map geometry = (Map) feature.get("geometry");
						JSONArray coords = (JSONArray) geometry.get("coordinates");
						mapResult.put("longitude", String.valueOf(coords.get(0)));
						mapResult.put("latitude", String.valueOf(coords.get(1)));
						mapResult.put("state", (String) props.get("state"));
						mapResult.put("postcode", (String) props.get("postcode"));
						mapResult.put("country", (String) props.get("country"));
						break;
					}

				} else if (street != null && city != null) {
					System.out.println("streetname = " + street);
					if (((postcode != null && address.contains(postcode))
							&& address.toLowerCase().contains(street.substring(0, 3).toLowerCase()))
							|| (address.contains(street) && address.contains(city))) {

						Map geometry = (Map) feature.get("geometry");
						JSONArray coords = (JSONArray) geometry.get("coordinates");
						mapResult.put("longitude", String.valueOf(coords.get(0)));
						mapResult.put("latitude", String.valueOf(coords.get(1)));
						mapResult.put("state", (String) props.get("state"));
						mapResult.put("postcode", (String) props.get("postcode"));
						mapResult.put("country", (String) props.get("country"));
						break;
					}
				}

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		con.disconnect();
		if (!mapResult.isEmpty()) {
			// System.out.println(Arrays.asList(mapResult));
			geocodingCache.put(address, mapResult);
		}
		return mapResult;

	}

	/**
	 * Adapt the size of each workbook column automatically.
	 *
	 * @param w
	 */
	public static void autoSizeColumns(XSSFWorkbook w) {
		int numberOfSheets = w.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; ++i) {
			XSSFSheet sheet = w.getSheetAt(i);
			// sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum()
			// + 1,
			// 0, 14));
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					sheet.autoSizeColumn(cellIterator.next().getColumnIndex());
				}
			}
		}
	}

	/**
	 * Adapt the size of each workbook column automatically.
	 *
	 * @param w
	 */
	public static void autoFilterColumns(XSSFWorkbook w) {
		int numberOfSheets = w.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; ++i) {
			XSSFSheet sheet = w.getSheetAt(i);
			sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum() + 1, 0, 8));
		}
	}

	/**
	 * Adapt the size of each workbook column automatically.
	 *
	 * @param w
	 */
	public static void autoFilterColumnsTerritoryHelper(XSSFWorkbook w) {
		int numberOfSheets = w.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; ++i) {
			XSSFSheet sheet = w.getSheetAt(i);
			sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum() + 1, 0, 20));
		}
	}

	public static void downloadInterestedFile() {

		try {
			// File f = new File("interested.xlsx");
			// String filePath = f.getAbsolutePath();
			// Path target = Paths.get(URI.create("file:///" + filePath));
			Path target = Paths.get(System.getProperty("user.home"), "workspace", "MinistrySoft", "interested.xlsx");
			File targetFile = new File(System.getProperty("user.home") + "/workspace/MinistrySoft/interested.xlsx");
			URL website = new URL("https://1drv.ms/x/s!ArIRj0umSSi4jFdh-Bs_yQ9_lx_d");
			FileUtils.copyURLToFile(website, targetFile);
			// try (InputStream in = website.openStream()) {
			// Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
			// System.out.println("Interested File successfully
			// downloaded!!!!");
			// }
			System.out.println("Interested File successfully downloaded!!!!");
		} catch (IOException e) {
			System.out.println("Interested File could not be downloaded!!!!");
		}
	}

}
