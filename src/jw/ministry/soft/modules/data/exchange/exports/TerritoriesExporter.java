/**
 *
 */
package jw.ministry.soft.modules.data.exchange.exports;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import jw.ministry.soft.modules.data.dao.TerritoryHome;
import jw.ministry.soft.modules.data.dto.Territory;
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
public class TerritoriesExporter {


	private static final String fileName = "TMSTerritoriesExport.xls";

	private static WritableWorkbook workbook;


	public static void main(String[] args) throws JAXBException, IOException {

		exportTerritories();
		HibernateUtil.shutdown();

	}

	public static void exportTerritories() {
		try {
		    WorkbookSettings ws = new WorkbookSettings();
		    ws.setLocale(new Locale("en", "EN"));
		    workbook = Workbook.createWorkbook(new File(fileName), ws);
//		    WritableWorkbook workbook = Workbook.createWorkbook(new File("output.xls"));
			WritableSheet sheet = workbook.createSheet("Territories Sheet", 0);
			exportAllTerritoriesPretty(sheet);

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
	  public static void write(WritableSheet sheet, Territory ter, int rowIndex) throws IOException, WriteException
	  {

		// Numéro
//	    jxl.write.Number number = new jxl.write.Number(0,rowIndex, Double.valueOf(ter.getCode()).doubleValue());
//	    sheet.addCell(number);
	    jxl.write.Number number = new jxl.write.Number(0,rowIndex, Double.valueOf(ter.getTerritoryId()).doubleValue());
	    sheet.addCell(number);

	    //Région
	    Label label = new Label(1, rowIndex, ter.getCity());
	    sheet.addCell(label);

	    // Description
	    label = new Label(2, rowIndex, ter.getCode() + " # " + ter.getName());
	    sheet.addCell(label);

	    // Commercant?
	    number = new jxl.write.Number(3,rowIndex, 0);
	    sheet.addCell(number);

	    // Commentaire
	    label = new Label(4, rowIndex, ter.getTerritoryType());
	    sheet.addCell(label);

	    //b
	    label = new Label(5, rowIndex, "");
	    sheet.addCell(label);


	  }

	  /**
	   * Uses the JExcelAPI to create a spreadsheet
	   *
	   * @exception IOException
	   * @exception WriteException
	   */
	  public static void writeHeaders(WritableSheet sheet) throws IOException, WriteException
	  {


	    //Numéro
	    Label label = new Label(0, 0, "ID");
	    sheet.addCell(label);

	    //Région
	    label = new Label(1, 0, "Région");
	    sheet.addCell(label);

	    // Code
	    label = new Label(2, 0, "Code");
	    sheet.addCell(label);

	    // Nom
	    label = new Label(3, 0, "Nom");
	    sheet.addCell(label);

	    // Description
	    label = new Label(4, 0, "Description");
	    sheet.addCell(label);

	    // Commentaire
	    label = new Label(5, 0, "Type");
	    sheet.addCell(label);

	    // Commentaire
	    label = new Label(6, 0, "Groupe");
	    sheet.addCell(label);

	  }


	  /**
	   * Uses the JExcelAPI to create a spreadsheet
	   *
	   * @exception IOException
	   * @exception WriteException
	   */
	  public static void writeExtended(WritableSheet sheet, Territory ter, int rowIndex) throws IOException, WriteException
	  {

		// Numéro
//	    jxl.write.Number number = new jxl.write.Number(0,rowIndex, Double.valueOf(ter.getCode()).doubleValue());
//	    sheet.addCell(number);
	    jxl.write.Number number = new jxl.write.Number(0,rowIndex, Double.valueOf(ter.getTerritoryId()).doubleValue());
	    sheet.addCell(number);

	    // Région
	    Label label = new Label(1, rowIndex, ter.getCity());
	    sheet.addCell(label);

	    // Code
	    number = new jxl.write.Number(2,rowIndex, Double.valueOf(ter.getCode()).doubleValue());
	    sheet.addCell(number);


	    // Nom
	    label = new Label(3, rowIndex, ter.getName());
	    sheet.addCell(label);

	    // Description
	    label = new Label(4, rowIndex, ter.getCode() + " # " + ter.getName());
	    sheet.addCell(label);

	    // Type
	    label = new Label(5, rowIndex, ter.getTerritoryType());
	    sheet.addCell(label);

	    // Groupe
	    label = new Label(6, rowIndex, ter.getGroupName());
	    sheet.addCell(label);




	  }




	public static void exportAllTerritories(WritableSheet sheet) throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		TerritoryHome daoTerritories = new TerritoryHome();
		List<Territory> dbTerritories = daoTerritories.getAllTerritories(session);

		int count = dbTerritories.size();

		int exportedTerritoriesCount = 0;

		for (int i = 0; i < count; i++) {
			if (dbTerritories.get(i).getStatus() == null || !dbTerritories.get(i).getStatus().contains("Archivé")) {
				write(sheet,dbTerritories.get(i), exportedTerritoriesCount);
				exportedTerritoriesCount++;
			}
		}

		System.out.println(dbTerritories.size() + " territories" );
		System.out.println("Exported territories count = " + exportedTerritoriesCount );

		session.close();

	}

	public static void exportAllTerritoriesPretty(WritableSheet sheet) throws WriteException, IOException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		TerritoryHome daoTerritories = new TerritoryHome();
		List<Territory> dbTerritories = daoTerritories.getAllTerritories(session);

		int count = dbTerritories.size();

		writeHeaders(sheet);

		int exportedTerritoriesCount = 0;

		for (int i = 0; i < count; i++) {
			if (dbTerritories.get(i).getStatus() == null || !dbTerritories.get(i).getStatus().contains("Archivé")) {
				writeExtended(sheet,dbTerritories.get(i), exportedTerritoriesCount+1 );
				exportedTerritoriesCount++;
			}
		}

		System.out.println(dbTerritories.size() + " territories" );
		System.out.println("Exported territories count = " + exportedTerritoriesCount );

		session.close();

	}





}
