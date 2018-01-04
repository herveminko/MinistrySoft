/**
 * 
 */
package jw.ministry.soft.modules.gui.views.publishers.model;

import java.util.Comparator;

import jw.ministry.soft.modules.data.dto.Publisher;

/**
 * @author HervéClaude
 *
 */
public class PublisherNameComparator implements Comparator<Publisher> {

	@Override
	public int compare(Publisher p1, Publisher p2) {
		if (p1.getLastName() == null && p2.getLastName() == null) {
			return 0;
		}
		if (p1.getLastName() == null) {
			return 1;
		}
		if (p2.getLastName() == null) {
			return -1;
		}
		int returnValue = p1.getLastName().compareTo(p2.getLastName());
		
		if (returnValue == 0) {
			return p1.getFirstName().compareTo(p2.getFirstName());
		} else {
			return returnValue;
		}
	}

}
