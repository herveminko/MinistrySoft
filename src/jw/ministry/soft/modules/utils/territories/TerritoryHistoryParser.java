/**
 * 
 */
package jw.ministry.soft.modules.utils.territories;

import java.util.HashMap;
import java.util.Map;

import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dao.TerritoryHome;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territory;

import org.hibernate.Session;

/**
 * @author HervéClaude
 *
 */
public class TerritoryHistoryParser {

	private Map<String, Integer> publishersMappingTable = new HashMap<String, Integer>();

	public TerritoryHistoryParser() {
		publishersMappingTable.put("Abdou, F.", 0);
		publishersMappingTable.put("Abdou, J.", 1);
		publishersMappingTable.put("Adjevi, K.", 2);
		publishersMappingTable.put("Alongo, N.", 3);
		publishersMappingTable.put("Alongo,J.", 4);
		publishersMappingTable.put("Andrianasolo, H.", 5);
		publishersMappingTable.put("Andrianasolo, S.", 6);
		publishersMappingTable.put("Andrianasolo,Maeva", 7);
		publishersMappingTable.put("Babang, E.", 8);
		publishersMappingTable.put("Babela, Ph.", 9);
		publishersMappingTable.put("Babela. E.", 10);
		publishersMappingTable.put("Bergmann, I.", 11);
		publishersMappingTable.put("Böhmer, B.", 12);
		publishersMappingTable.put("Bolongo, P.", 13);
		publishersMappingTable.put("Desanti, D.", 14);
		publishersMappingTable.put("Desanti, P.", 15);
		publishersMappingTable.put("Djonga, F.", 16);
		publishersMappingTable.put("Djonga, L.", 17);
		publishersMappingTable.put("Fritsch, A.", 18);
		publishersMappingTable.put("Gado, A.", 19);
		publishersMappingTable.put("Groupe Bielefeld", null);
		publishersMappingTable.put("Groupe Osnabrück", null);
		publishersMappingTable.put("Groupe Paderborn", null);
		publishersMappingTable.put("Hebhang, H.", 20);
		publishersMappingTable.put("Hellebrandt, D.", 21);
		publishersMappingTable.put("Helmreich, A.", 22);
		publishersMappingTable.put("Helmreich, K.", 23);
		publishersMappingTable.put("Hoffmann, G.", 24);
		publishersMappingTable.put("Kamanda, F.", 25);
		publishersMappingTable.put("Lange, H.", 26);
		publishersMappingTable.put("Lavodrama, B.", 27);
		publishersMappingTable.put("Minko, A.", 28);
		publishersMappingTable.put("Minko, H.", 29);
		publishersMappingTable.put("Ngansop Gaelle", 30);
		publishersMappingTable.put("Ngassop Hervé", 31);
		publishersMappingTable.put("Ngiakialla, G.", 32);
		publishersMappingTable.put("Noubi, G.", 33);
		publishersMappingTable.put("Obiang, D.", 34);
		publishersMappingTable.put("Rabearisoa, R.", 35);
		publishersMappingTable.put("Rolland, E.", 36);
		publishersMappingTable.put("Rolland, M.", 37);
		publishersMappingTable.put("Stroop, A", 38);
		publishersMappingTable.put("Stroop, N", 39);
		publishersMappingTable.put("Tambyrajah, M.", 40);
		publishersMappingTable.put("Tambyrajah, P.", 41);
		publishersMappingTable.put("Thamar, Carola", 42);
		publishersMappingTable.put("Thamar, Jean", 43);
		publishersMappingTable.put("Verloren", null);
		publishersMappingTable.put("Vnocsek, D.", 44);
		publishersMappingTable.put("Vnocsek, M.", 45);
		publishersMappingTable.put("", 47);
		publishersMappingTable.put("Groupe Osnabrück", 48);
		publishersMappingTable.put("Groupe Paderborn", 49);
		publishersMappingTable.put("Groupe Bielefeld", 50);

	}
	
	
	public Publisher getDestinationDbPublisher(Session session, int publisherId) {
		
		PublisherHome publishersDao = new PublisherHome();
		
		Publisher dbPublisher = publishersDao.findById(session, publisherId);
		
		return dbPublisher;
	}
	
	
	public Territory getDestinationDbTerritory(Session session, String teritoryName, String territoryCode) {
		
		TerritoryHome territoriesDao = new TerritoryHome();
		Territory instance = new Territory();
		instance.setCode(territoryCode);
		instance.setName(teritoryName);
	
		Territory dbTerritory = territoriesDao.findByExample(session, instance).get(0);
		
		return dbTerritory;
	}


	/**
	 * @return the publishersMappingTable
	 */
	public Map<String, Integer> getPublishersMappingTable() {
		return publishersMappingTable;
	}
	
	
	
	
	
}
