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
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import jw.ministry.soft.modules.data.dao.TerritoryHome;
import jw.ministry.soft.modules.data.dao.TerritoryhistoryHome;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.data.dto.Territoryhistory;
import jw.ministry.soft.modules.utils.HibernateUtil;
import jw.ministry.soft.modules.utils.territories.TerritoryHistoryParser;

import org.hibernate.Session;

/**
 * @author HervéClaude
 *
 */
public class TerritoryHistoryImporter {

	private static final String DATE_IMPORT_PATTERN = "dd/MM/yyyy";
	
	private static final String TERRITORY_HISTORIES_DIRECTORY = "D:\\Projects\\TMS\\TerritoryHistories";

	private static final String TERRITORY_HISTORY_XML = "./territory_histories.xml";

	public static void main(String[] args) throws JAXBException, IOException {
//
//		ExternalTerritoryHistory ter = new ExternalTerritoryHistory();
//		ter.setActionName("Travaillé complètement");
//		ter.setActionDateString("06/04/2003");
//		ter.setPubisherName("Fritsch, A.");
//
//		ExternalTerritoryHistory ter2 = new ExternalTerritoryHistory();
//		ter2.setActionName("Travaillé complètement");
//		ter2.setActionDateString("31/12/2004");
//		ter2.setPubisherName("Desanti, D.");
//
//		TerritoryHistoriesRecords historiesRecords = new TerritoryHistoriesRecords();
//		historiesRecords.setTerritoryCode("1");
//		historiesRecords.setTerritoryName("OS - Westerberg");
//		historiesRecords.getHistoriesList().add(ter);
//		historiesRecords.getHistoriesList().add(ter2);
//
//		// create JAXB context and instantiate marshaller
//		JAXBContext context = JAXBContext
//				.newInstance(TerritoryHistoriesRecords.class);
//		Marshaller m = context.createMarshaller();
//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// Write to System.out
//		m.marshal(historiesRecords, System.out);

//		// Write to File
//		m.marshal(historiesRecords, new File(TERRITORY_HISTORY_XML));
//
//		importTerritoryHistories(TERRITORY_HISTORY_XML);
		createTerritoryHistoriesTemplates();
		HibernateUtil.shutdown();

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
	
	public static void createTerritoryHistoriesTemplates() throws JAXBException, IOException  {
		Session session = HibernateUtil.getSessionFactory().openSession();
		TerritoryHome territoryDao = new TerritoryHome();
		
		List<Territory> territories = territoryDao.getAllTerritories(session);
		
		for (Territory t : territories) {
			
			TerritoryHistoriesRecords historiesRecords = new TerritoryHistoriesRecords();
			historiesRecords.setTerritoryCode(t.getCode());
			//String name = new String (t.getName().getBytes(), "UTF-8");
			historiesRecords.setTerritoryName(t.getName());

			// create JAXB context and instantiate marshaller
			JAXBContext context = JAXBContext
					.newInstance(TerritoryHistoriesRecords.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			m.marshal(historiesRecords, System.out);
			
		//	name.replaceAll("/", "_");

			// Write to File
			m.marshal(historiesRecords, new File(TERRITORY_HISTORIES_DIRECTORY + "\\Territory " + t.getCode() + " - " + " Imported History.xml"));
		}
		
		session.close();
	}

		
}
