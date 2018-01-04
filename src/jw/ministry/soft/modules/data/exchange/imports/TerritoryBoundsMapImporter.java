/**
 *
 */
package jw.ministry.soft.modules.data.exchange.imports;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibernate.Session;

import jw.ministry.soft.modules.data.dao.TerritoryHome;
import jw.ministry.soft.modules.data.dao.TerritoryhistoryHome;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.data.dto.Territoryhistory;
import jw.ministry.soft.modules.utils.HibernateUtil;
import jw.ministry.soft.modules.utils.territories.TerritoryHistoryParser;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author HervéClaude
 *
 */
public class TerritoryBoundsMapImporter {

	private static final String DATE_IMPORT_PATTERN = "dd/MM/yyyy";
	private static final String TERRITORY_BOUNDS_DIRECTORY = "D:\\Projects\\TMS\\TerritoryBounds";

	public static void main(String[] args) throws JAXBException, IOException {


		read();
//		HibernateUtil.shutdown();

	}


	public static void importTerritoryHistories(String xmlFilePath) throws JAXBException, IOException  {


		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext
				.newInstance(TerritoryHistoriesRecords.class);

		// get variables from our xml file, created before
		System.out.println("Output from our XML File: ");
		Unmarshaller um = context.createUnmarshaller();

		Session session = HibernateUtil.getSessionFactory().openSession();

		TerritoryHistoriesRecords histRecords = (TerritoryHistoriesRecords) um
				.unmarshal(new FileReader(xmlFilePath));

		TerritoryHistoryParser parser = new TerritoryHistoryParser();

		Territory dbTerritory = parser.getDestinationDbTerritory(session,
				new String(histRecords.getTerritoryName().getBytes(), "UTF-8"), null);

		List<Territoryhistory> listOfImportHistories = new ArrayList<Territoryhistory>();

		for (ExternalTerritoryHistory t : histRecords.getHistoriesList()) {
			System.out.println("Territory: " + histRecords.getTerritoryName()
					+ " - Code = " + histRecords.getTerritoryCode() + " - "
					+ t.getActionName());
			String pubName = new String(t.getPubisherName().getBytes(), "UTF-8");
			Integer i = parser.getPublishersMappingTable().get(
					pubName);
			Publisher p = parser.getDestinationDbPublisher(session, i);

			System.out.println(" DB Publisher = " + p.getFirstName() + " "
					+ p.getLastName());

			Territoryhistory hist = new Territoryhistory();
			SimpleDateFormat dateFormater = new SimpleDateFormat(
					DATE_IMPORT_PATTERN);
			Date d = null;
			try {
				d = dateFormater.parse(t.getActionDateString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hist.setActionDate(d);
			hist.setActionDescrption(t.getActionName());
			hist.setPublisher(p);
			hist.setTerritory(dbTerritory);

			listOfImportHistories.add(hist);
		}

		TerritoryhistoryHome daoHome = new TerritoryhistoryHome();
		daoHome.persist(session, listOfImportHistories);

		session.close();
	}

	public static void read() throws IOException  {
		String inputFile = TERRITORY_BOUNDS_DIRECTORY + File.separator + "Territory Link List.XLS";
	    File inputWorkbook = new File(inputFile);
	    Workbook w;
	    try {
	      w = Workbook.getWorkbook(inputWorkbook);
	      // Get the first sheet
	      Sheet sheet = w.getSheet(0);
	      // Loop over first 10 column and lines
	        for (int j = 0; j < sheet.getRows(); j++) {
//	        for (int i = 0; i < sheet.getColumns(); i++) {
//	          Cell cell = sheet.getCell(i,j);
//	          CellType type = cell.getType();
//	          if (type == CellType.LABEL) {
//	            System.out.println("I got a label "
//	                + cell.getContents());
//	          }
//
//	          if (type == CellType.NUMBER) {
//	            System.out.println("I got a number "
//	                + cell.getContents());
//	          }

	          String code = sheet.getCell(0,j).getContents();
	          String url = sheet.getCell(2,j).getContents();

	          System.out.print("");
	          Session session = HibernateUtil.getSessionFactory().openSession();


	          TerritoryHome territoryDao = new TerritoryHome();

	          Territory instance = new Territory();
	          instance.setCode(code);
	          List<Territory> list = territoryDao.findByExample(session, instance);

	          if (!list.isEmpty()) {
	        	  Territory dbTerritory = list.get(0);
	        	  dbTerritory.setBounds(url);
	        	  territoryDao.update(session, dbTerritory);
	          }

	          session.close();

//	        }

	      }
	    } catch (BiffException e) {
	      e.printStackTrace();
	    }
	  }



}
