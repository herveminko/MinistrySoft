/**
 *
 */
package jw.ministry.soft.modules.data.exchange.exports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.bind.JAXBException;

import jw.ministry.soft.modules.data.dao.CongregationHome;
import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dao.TerritoryHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Contact;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.gui.views.publishers.model.PublisherModel;
import jw.ministry.soft.modules.utils.HibernateUtil;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.hibernate.Session;

/**
 * @author HervéClaude
 *
 */
public class PublishersExporter {

	private static final String fileName = "TMSPublishersExport.xls";

	private static WritableWorkbook workbook;

	public static void main(String[] args) throws JAXBException, IOException {

		exportPublishersFromCongregaton(0);
		HibernateUtil.shutdown();
		// System.exit(0);
	}

	public static void exportPublishersFromCongregaton(int congregationIndex) {
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			workbook = Workbook.createWorkbook(new File(fileName), ws);
			// WritableWorkbook workbook = Workbook.createWorkbook(new
			// File("output.xls"));
			WritableSheet sheet = workbook.createSheet("Publishers Sheet", 0);
			exportAllPublishers(sheet, congregationIndex);

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
	public static void write(WritableSheet sheet, Publisher pub, int rowIndex) throws IOException, WriteException {

		// Numéro
		// jxl.write.Number number = new jxl.write.Number(0,rowIndex,
		// Double.valueOf(ter.getCode()).doubleValue());
		// sheet.addCell(number);
		jxl.write.Number number = new jxl.write.Number(0, rowIndex, Double.valueOf(pub.getPublisherId()).doubleValue());
		sheet.addCell(number);

		// Prénom
		Label label = new Label(1, rowIndex, pub.getFirstName());
		sheet.addCell(label);

		// Nom
		label = new Label(2, rowIndex, pub.getLastName());
		sheet.addCell(label);

		// INactif?
		String statusString = pub.getStatus().getStatus().toLowerCase();
		boolean notActif = statusString.contains("inactif") || statusString.contains("excommunié")
				|| statusString.contains("archivé");
		int exPortStatus = notActif ? 1 : 0;
		number = new jxl.write.Number(3, rowIndex, exPortStatus);
		sheet.addCell(number);

		Contact cont = pub.getContact();
		if (cont != null) {
			// Téléphone
			label = new Label(4, rowIndex, cont.getPhone());
			sheet.addCell(label);

			// Mobile
			label = new Label(5, rowIndex, cont.getCellPhone());
			sheet.addCell(label);

			// Fax
			label = new Label(6, rowIndex, "NO FAX");
			sheet.addCell(label);

			// Téléphone bureau
			label = new Label(7, rowIndex, "NO WORK PHONE");
			sheet.addCell(label);

			// Mail
			label = new Label(8, rowIndex, cont.getEmail());
			sheet.addCell(label);

		}

	}

	/**
	 * Uses the JExcelAPI to create a spreadsheet
	 *
	 * @exception IOException
	 * @exception WriteException
	 */
	public static void writeHeaders(WritableSheet sheet) throws IOException, WriteException {

		// Numéro
		Label label = new Label(0, 0, "ID");
		sheet.addCell(label);

		// Prénom
		label = new Label(1, 0, "Prénom");
		sheet.addCell(label);

		// Nom
		label = new Label(2, 0, "Nom");
		sheet.addCell(label);

		// INactif?
		label = new Label(3, 0, "Status");
		sheet.addCell(label);

		// Téléphone
		label = new Label(4, 0, "Téléphone");
		sheet.addCell(label);

		// Mobile
		label = new Label(5, 0, "Téléphone portable");
		sheet.addCell(label);

		// Fax
		label = new Label(6, 0, "Fax");
		sheet.addCell(label);

		// Téléphone bureau
		label = new Label(7, 0, "Téléphone bureau");
		sheet.addCell(label);

		// Mail
		label = new Label(8, 0, "Adresse Email");
		sheet.addCell(label);

		// }

	}

	/**
	 * Uses the JExcelAPI to create a spreadsheet
	 *
	 * @exception IOException
	 * @exception WriteException
	 */
	public static void writeExtended(WritableSheet sheet, Publisher pub, int rowIndex)
			throws IOException, WriteException {

		// Numéro
		// jxl.write.Number number = new jxl.write.Number(0,rowIndex,
		// Double.valueOf(ter.getCode()).doubleValue());
		// sheet.addCell(number);
		jxl.write.Number number = new jxl.write.Number(0, rowIndex, Double.valueOf(pub.getPublisherId()).doubleValue());
		sheet.addCell(number);

		// Prénom
		Label label = new Label(1, rowIndex, pub.getFirstName());
		sheet.addCell(label);

		// Nom
		label = new Label(2, rowIndex, pub.getLastName());
		sheet.addCell(label);

		// Status
		label = new Label(3, rowIndex, pub.getStatus().getStatus());
		sheet.addCell(label);

		Contact cont = pub.getContact();
		if (cont != null) {
			// Téléphone
			label = new Label(4, rowIndex, cont.getPhone());
			sheet.addCell(label);

			// Mobile
			label = new Label(5, rowIndex, cont.getCellPhone());
			sheet.addCell(label);

			// Fax
			label = new Label(6, rowIndex, "");
			sheet.addCell(label);

			// Téléphone bureau
			label = new Label(7, rowIndex, "");
			sheet.addCell(label);

			// Mail
			label = new Label(8, rowIndex, cont.getEmail());
			sheet.addCell(label);

		}

	}

	public static void exportAllPublishers(WritableSheet sheet, int congregationIndex)
			throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CongregationHome daoCongregation = new CongregationHome();
		Congregation selectedCongregation = daoCongregation.getAllCongregations(session).get(congregationIndex);
		Set<Publisher> dbPublishers = selectedCongregation.getPublishers(); // dao.getAllPublishersInCongregation(selectedCongregation.session);

		List<PublisherModel> publishersModelList = new ArrayList<PublisherModel>();
		for (Publisher p : dbPublishers) {
			publishersModelList.add(new PublisherModel(p));
		}

		int count = dbPublishers.size();

		for (int i = 0; i < count; i++) {

			write(sheet, publishersModelList.get(i).getPublisherRecord(), i);
		}

		System.out.println(dbPublishers.size() + " publishers");

		session.close();

	}

	public static void exportAllPublishersPretty(WritableSheet sheet, int congregationIndex)
			throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CongregationHome daoCongregation = new CongregationHome();
		Congregation selectedCongregation = daoCongregation.getAllCongregations(session).get(congregationIndex);
		Set<Publisher> dbPublishers = selectedCongregation.getPublishers(); // dao.getAllPublishersInCongregation(selectedCongregation.session);

		List<PublisherModel> publishersModelList = new ArrayList<PublisherModel>();
		for (Publisher p : dbPublishers) {
			publishersModelList.add(new PublisherModel(p));
		}

		writeHeaders(sheet);

		int count = dbPublishers.size();

		for (int i = 0; i < count; i++) {

			writeExtended(sheet, publishersModelList.get(i).getPublisherRecord(), i + 1);
		}

		System.out.println(dbPublishers.size() + " publishers");

		session.close();

	}

}
