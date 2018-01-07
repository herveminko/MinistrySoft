/**
 *
 */
package jw.ministry.soft.modules.data.exchange.exports;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.bind.JAXBException;

import jw.ministry.soft.modules.data.dao.TerritoriesassignmentsHome;
import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.gui.views.publishers.model.PublisherModel;
import jw.ministry.soft.modules.utils.DateUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.hibernate.Session;

/**
 * @author HervéClaude
 *
 */
public class TerritoriesAssignmentsExporter {

	private static final String fileName = "TMSTerritoriesAssignmentsExport.xls";

	private static final String extendedfileName = "BI-TerritoriesAssignments.xls";

	private static WritableWorkbook workbook;

	public static void main(String[] args) throws JAXBException, IOException {

		exportTerritoriAssignments();

		exportPubishersTerritoriesAssignments();

		HibernateUtil.shutdown();
	}

	public static void exportTerritoriAssignments() {

		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			workbook = Workbook.createWorkbook(new File(fileName), ws);
			// WritableWorkbook workbook = Workbook.createWorkbook(new
			// File("output.xls"));
			WritableSheet sheet = workbook.createSheet("Territories Assignments Sheet", 0);
			exportAllTerritoriesAssignmentsPretty(sheet);

			workbook.write();
			workbook.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	public static void exportPubishersTerritoriesAssignments() {

		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			workbook = Workbook.createWorkbook(new File(extendedfileName), ws);
			// WritableWorkbook workbook = Workbook.createWorkbook(new
			// File("output.xls"));
			WritableSheet sheet = workbook.createSheet("Publishers Sheet", 0);
			PublishersExporter.exportAllPublishersPretty(sheet, 0);

			sheet = workbook.createSheet("Territories Sheet", 1);
			TerritoriesExporter.exportAllTerritoriesPretty(sheet);


		    sheet = workbook.createSheet("Territories Assignments Sheet", 2);
			exportAllTerritoriesAssignmentsPretty(sheet);



			workbook.write();
			workbook.close();

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
	public static void write(WritableSheet sheet,
			Territoriesassignments assign, int rowIndex) throws IOException,
			WriteException {

		// Numéro territoire
		jxl.write.Number number = new jxl.write.Number(0, rowIndex, Double
				.valueOf(assign.getTerritory().getTerritoryId()).doubleValue());
		sheet.addCell(number);

		// Numéro proclamateur
		number = new jxl.write.Number(1, rowIndex, Double.valueOf(
				assign.getPublisher().getPublisherId()).doubleValue());
		sheet.addCell(number);

		SimpleDateFormat format = new SimpleDateFormat(
				DateUtils.getUiDateFormat());
		// Date début
		String begin = format.format(assign.getAssignmentDate());
		Label label = new Label(2, rowIndex, begin);
		sheet.addCell(label);

		// Date fin
		String end = assign.getReturnDate() != null ? format.format(assign
				.getReturnDate()) : "";
		label = new Label(3, rowIndex, end);
		sheet.addCell(label);

		// Numéro campagne
		label = new Label(4, rowIndex, "");
		sheet.addCell(label);

		// Situation
		label = new Label(5, rowIndex, "");
		sheet.addCell(label);

		// Commentaires
		label = new Label(5, rowIndex, "");
		sheet.addCell(label);

	}


	/**
	 * Uses the JExcelAPI to create a spreadsheet
	 *
	 * @exception IOException
	 * @exception WriteException
	 */
	public static void writeHeaders(WritableSheet sheet) throws IOException,
			WriteException {

		// Numéro territoire
		Label label = new Label(0, 0, "ID Territoire");
		sheet.addCell(label);

	    // Code territoire
	    label = new Label(1, 0, "Groupe");
	    sheet.addCell(label);

	    // Code territoire
	    label = new Label(2, 0, "Code");
	    sheet.addCell(label);

	    // Nom territoire
	    label = new Label(3, 0, "Description Territoire");
	    sheet.addCell(label);

		// Numéro proclamateur
		label = new Label(4, 0, "Assigné à");
		sheet.addCell(label);

		// Date assignation
		label = new Label(5, 0, "Date assignation");
		sheet.addCell(label);

	}



	/**
	 * Uses the JExcelAPI to create a spreadsheet
	 *
	 * @exception IOException
	 * @exception WriteException
	 */
	public static void writeExtended(WritableSheet sheet,
			Territoriesassignments assign, int rowIndex) throws IOException,
			WriteException {
		Territory ter = assign.getTerritory();
		Publisher pub = assign.getPublisher();

		// Numéro territoire
		jxl.write.Number number = new jxl.write.Number(0, rowIndex, Double
				.valueOf(ter.getTerritoryId()).doubleValue());
		sheet.addCell(number);

	    // Description territoire
	    Label label = new Label(1, rowIndex, ter.getGroupName());
	    sheet.addCell(label);

	    // Code territoire
	    number = new jxl.write.Number(2,rowIndex, Double.valueOf(ter.getCode()).doubleValue());
	    sheet.addCell(number);

	    // Description territoire
	    label = new Label(3, rowIndex, ter.getName());
	    sheet.addCell(label);

		// Proclamateur
		label = new Label(4, rowIndex, pub.getFirstName() + " " + pub.getLastName());
		sheet.addCell(label);


		SimpleDateFormat format = new SimpleDateFormat(
				DateUtils.getUiDateFormat());
		// Date assignation
		String begin = format.format(assign.getAssignmentDate());
	    label = new Label(5, rowIndex, begin);
		sheet.addCell(label);


	}



	public static void exportAllTerritoriesAssignments(WritableSheet sheet)
			throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		TerritoriesassignmentsHome daoAssignments = new TerritoriesassignmentsHome();

		List<Territoriesassignments> assignMents = daoAssignments
				.getAllTerritoriesAssignments(session); // dao.getAllPublishersInCongregation(selectedCongregation.session);

		int count = assignMents.size();
		int exportedAssignments = 0;

		for (int i = 0; i < count; i++) {

			boolean doNotSkipTerritory = assignMents.get(i).getTerritory().getStatus() == null || (assignMents.get(i).getTerritory().getStatus() != null && !assignMents.get(i).getTerritory().getStatus().contains("Archivé"));
			//boolean skipPublisher = assignMents.get(i).getPublisher().getStatus() == null || (assignMents.get(i).getPublisher().getStatus() != null && assignMents.get(i).getPublisher().getStatus().getStatus() != null && assignMents.get(i).getPublisher().getStatus().getStatus().contains("Archivé"));
			if (doNotSkipTerritory && assignMents.get(i).getReturnDate() == null) {
				write(sheet, assignMents.get(i), exportedAssignments);
				exportedAssignments ++;
			}
		}

		System.out.println(assignMents.size() + " assignments (total)");

		System.out.println(exportedAssignments + " exported assignments");

		session.close();

	}

	public static void exportAllTerritoriesAssignmentsPretty(WritableSheet sheet)
			throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		TerritoriesassignmentsHome daoAssignments = new TerritoriesassignmentsHome();

		List<Territoriesassignments> assignMents = daoAssignments
				.getAllTerritoriesAssignments(session); // dao.getAllPublishersInCongregation(selectedCongregation.session);

		int count = assignMents.size();
		int exportedAssignments = 0;

		writeHeaders(sheet);

		for (int i = 0; i < count; i++) {

			boolean doNotSkipTerritory = assignMents.get(i).getTerritory().getStatus() == null || (assignMents.get(i).getTerritory().getStatus() != null && !assignMents.get(i).getTerritory().getStatus().contains("Archivé"));
			//boolean skipPublisher = assignMents.get(i).getPublisher().getStatus() == null || (assignMents.get(i).getPublisher().getStatus() != null && assignMents.get(i).getPublisher().getStatus().getStatus() != null && assignMents.get(i).getPublisher().getStatus().getStatus().contains("Archivé"));
			if (doNotSkipTerritory && assignMents.get(i).getReturnDate() == null) {
				writeExtended(sheet, assignMents.get(i), exportedAssignments+2);
				exportedAssignments ++;
			}
		}

		System.out.println(assignMents.size() + " assignments (total)");

		System.out.println(exportedAssignments + " exported assignments");

		session.close();

	}


}
