/**
 *
 */
package jw.ministry.soft.modules.utils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.gui.views.territories.model.TerritoryHistoryModel;
import jw.ministry.soft.modules.gui.views.territories.model.TerritoryModel;

/**
 * @author HervéClaude
 *
 */
public class MailUtils {

	public static void testMail() throws IOException {

		ResourceBundle bundle = Main.getMainBundle();

		final String username = bundle.getString("mail_user");
		final String password = bundle.getString("mail_password");
		final String smtpHost = bundle.getString("mail_smtp_host");
		final String smtpPort = bundle.getString("mail_smtp_port");

		final String mailSender = bundle.getString("mail_sender");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mailSender));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
					+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean sendTerritoryWorkStatusMailToPublisher(
			Publisher publisher) throws IOException {

		org.hibernate.Session session = HibernateUtil.getSessionFactory()
				.openSession();
		PublisherHome dao = new PublisherHome();
		Publisher p = (dao.findByExample(session, publisher)).get(0);
		return sendTerritoryWorkStatusMailToDatabasePublisher(p);
	}


	/**
	 * Send a mail to a database publisher instance, requesting the coverage status of
	 * of his territories.
	 *
	 * @param publisher
	 *            is a publisher instance from the database with all his join information.
	 * @return true or false.
	 * @throws IOException
	 */
	public static boolean sendTerritoryWorkStatusMailToDatabasePublisher(
			Publisher publisher)  throws IOException {
		String territoriesInfo = "";

		for (Territoriesassignments ass : publisher
				.getTerritoriesassignmentses()) {
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();

				TerritoryModel model = new TerritoryModel(ter);
				List<TerritoryHistoryModel> histories = model.getHistoryData();
				Collections.sort(histories);

				String lastWorkDateText = "--> Jamais travaillé";
				if (!histories.isEmpty()) {
					Date lastWorkDate = histories.get(0).getHistoryActionDate();
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());
					String lastWorkDateString = dateTimeFormatter.format(DateUtils.asLocalDate(lastWorkDate));

					lastWorkDateText = " --> Travaillé la dernière fois le " + lastWorkDateString;
				}
				territoriesInfo += ter.getCode() + " - " + ter.getName() + " " + lastWorkDateText + "\n";

			}
		}

		if (publisher.getContact().getEmail() != null
				&& !publisher.getContact().getEmail().isEmpty()) {

			ResourceBundle bundle = Main.getMainBundle();

			final String mailSender = bundle.getString("mail_sender");

			String mailReceiver = publisher.getContact().getEmail();
			String mailSubject = "Travail de ton (tes) territoire(s) ";

			String title = publisher.getSexe().getSexe()
					.equalsIgnoreCase("masculin") ? "Cher "
					+ publisher.getFirstName() + ",\n" : "Chère "
					+ publisher.getFirstName() + ",\n";
			String mailContent = title
					+ "\n\n"
					+ "Notre semaine spéciale approche à très grands pas! Nous avons à nouveau besoin de quelques informations de ta part, afin de préparer le dossier des territoires pour notre surveillant itinérant\n\n"
					+ "D'après notre liste tu travailles actuellement le(s) territoire(s) suivant(s):\n\n"
					+ territoriesInfo
					+ "\n\n"
					+ "Pourrais-tu s'il te plait nous dire quand tu les as travaillés (complètement!) la dernière fois?\nSi tu ne les as plus travaillé depuis la dernière fois, veuille quand même nous en informer (la dernière date sera retenue).\n\n";
					//+ "N.B: Ne disposant plus de beaucoup de temps, nous aurons besoin s'il te plait de ta réponse au plus tard Vendredi cette semaine\n";
			String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
			String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
			String mailReturnInfo = "";
			String returnNames = "";
			if (mailResponsesNames.length > 1) {
				returnNames += "Tes frères\n";
			} else {
				returnNames += "Ton frère\n";
			}

			for (int i = 0; i < mailResponsesAddresses.length; i++) {
				mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
				if (i == 0) {
					returnNames +=  mailResponsesNames[i];
				} else 	if (i == (mailResponsesAddresses.length - 1)) {
					returnNames += " & " + mailResponsesNames[i];
				} else {
					returnNames += ", " + mailResponsesNames[i];
				}
			}

			String mailRemark = "\n\nVeuille répondre à l'une des adresses email suivantes:\n"
					+ mailReturnInfo
					+ "ou tout simplement nous contacter dès que possible par un autre moyen.\n";
			String signature = "\n\nMerci d'avance pour ta coopération et tes efforts.\n\n" + returnNames;
			mailContent += mailRemark;
			mailContent += signature;

			System.out.println("Sending mail to " + mailReceiver + " ...");
			sendMail(mailSender, mailReceiver, mailSubject, mailContent);

			return true;

		} else {
			// mail could not be sent because this publisher does not have a
			// mail address.
			return false;

		}

	}

	public static boolean sendTerritoryWorkStatusRemainderMailToDatabasePublisher(
			Publisher publisher)  throws IOException {
		String territoriesInfo = "";

		for (Territoriesassignments ass : publisher
				.getTerritoriesassignmentses()) {
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();

				TerritoryModel model = new TerritoryModel(ter);
				List<TerritoryHistoryModel> histories = model.getHistoryData();
				Collections.sort(histories);

				String lastWorkDateText = "--> Jamais travaillé";
				if (!histories.isEmpty()) {
					Date lastWorkDate = histories.get(0).getHistoryActionDate();
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());
					String lastWorkDateString = dateTimeFormatter.format(DateUtils.asLocalDate(lastWorkDate));

					lastWorkDateText = " --> Travaillé la dernière fois le " + lastWorkDateString;
				}
				territoriesInfo += ter.getCode() + " - " + ter.getName() + " " + lastWorkDateText + "\n";

			}
		}

		if (publisher.getContact().getEmail() != null
				&& !publisher.getContact().getEmail().isEmpty()) {

			ResourceBundle bundle = Main.getMainBundle();

			final String mailSender = bundle.getString("mail_sender");

			String mailReceiver = publisher.getContact().getEmail();
			String mailSubject = "Rappel - Travail de ton (tes) territoire(s) ";

			String title = publisher.getSexe().getSexe()
					.equalsIgnoreCase("masculin") ? "Cher "
					+ publisher.getFirstName() + ",\n" : "Chère "
					+ publisher.getFirstName() + ",\n";
			String mailContent = title
					+ "\n\n"
					+ "Ceci est un rappel collectif. Si tu as déja transmis les informations requises ci-dessous, veuille s'il te plait, ignorer cet Email! Dans le cas contraire, veuille s'il te plait nous les transmettre au plus tard Vendredi cette semaine. Merci beaucoup pour ta coopération. "
					+ "\n\n"
					+ "Notre semaine spéciale approche à très grands pas! Nous avons à nouveau besoin de quelques informations de ta part, afin de préparer le dossier des territoires pour notre surveillant itinérant\n\n"
					+ "D'après notre liste tu travailles actuellement le(s) territoire(s) suivant(s):\n\n"
					+ territoriesInfo
					+ "\n\n"
					+ "Pourrais-tu s'il te plait nous dire quand tu les as travaillés (complètement!) la dernière fois?\nSi tu ne les as plus travaillé depuis la dernière fois, veuille quand même nous en informer (la dernière date sera retenue).\n\n";
					//+ "N.B: Ne disposant plus de beaucoup de temps, nous aurons besoin s'il te plait de ta réponse au plus tard Vendredi cette semaine\n";
			String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
			String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
			String mailReturnInfo = "";
			String returnNames = "";
			if (mailResponsesNames.length > 1) {
				returnNames += "Tes frères\n";
			} else {
				returnNames += "Ton frère\n";
			}

			for (int i = 0; i < mailResponsesAddresses.length; i++) {
				mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
				if (i == 0) {
					returnNames +=  mailResponsesNames[i];
				} else 	if (i == (mailResponsesAddresses.length - 1)) {
					returnNames += " & " + mailResponsesNames[i];
				} else {
					returnNames += ", " + mailResponsesNames[i];
				}
			}

			String mailRemark = "\n\nVeuille répondre à l'une des adresses email suivantes:\n"
					+ mailReturnInfo
					+ "ou tout simplement nous contacter dès que possible par un autre moyen.\n";
			String signature = "\n\nMerci d'avance pour ta coopération et tes efforts.\n\n" + returnNames;
			mailContent += mailRemark;
			mailContent += signature;

			System.out.println("Sending mail to " + mailReceiver + " ...");
			sendMail(mailSender, mailReceiver, mailSubject, mailContent);

			return true;

		} else {
			// mail could not be sent because this publisher does not have a
			// mail address.
			return false;

		}

	}

	/**
	 * Send a mail to a database publisher instance, requesting the list of interested addresses
	 * of his territory.
	 *
	 * @param publisher
	 *            is a publisher instance from the database with all his join information.
	 * @return true or false.
	 * @throws IOException
	 */
	public static boolean sendTerritoryAddressesMailToDatabasePublisher(
			Publisher publisher) throws IOException {

		String territoriesInfo = "";

		for (Territoriesassignments ass : publisher
				.getTerritoriesassignmentses()) {
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();
				territoriesInfo += ter.getCode() + " - " + ter.getName() + "\n";
			}
		}
		if (publisher.getContact().getEmail() != null
				&& !publisher.getContact().getEmail().isEmpty()) {

			ResourceBundle bundle = Main.getMainBundle();

			final String mailSender = bundle.getString("mail_sender");

			String mailReceiver = publisher.getContact().getEmail();
			String mailSubject = "Liste d'adresses de ton (tes) territoire(s) ";

			String title = publisher.getSexe().getSexe()
					.equalsIgnoreCase("masculin") ? "Cher "
					+ publisher.getFirstName() + ",\n" : "Chère "
					+ publisher.getFirstName() + ",\n";
			String mailContent = title
					+ "\n\n"
					+ "D'après notre liste tu travailles actuellement les territoires suivants:\n\n"
					+ territoriesInfo
					+ "\n\n"
					+ "Pourrais-tu s'il te plait nous transmettre la(les) liste(s) actuelle(s) des adresses de ce(s) territoire(s) afin que nous complétions le fichier central d'adresses francophones de la congrégation?";
			String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
			String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
			String mailReturnInfo = "";
			String returnNames = "";
			if (mailResponsesNames.length > 1) {
				returnNames += "Tes frères\n";
			} else {
				returnNames += "Ton frère\n";
			}

			for (int i = 0; i < mailResponsesAddresses.length; i++) {
				mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
				if (i == 0) {
					returnNames +=  mailResponsesNames[i];
				} else 	if (i == (mailResponsesAddresses.length - 1)) {
					returnNames += " & " + mailResponsesNames[i];
				} else {
					returnNames += ", " + mailResponsesNames[i];
				}
			}

			String mailRemark = "\n\nVeuille répondre à l'une des adresses email suivantes:\n"
					+ mailReturnInfo
					+ "ou tout simplement nous contacter dès que possible par un autre moyen.\n"
					+ "N'hésite pas à nous contacter, si tu as besoin d'informations supplémentaires.\n\n"
					+ "Si tu nous as déja fait parvenir tes adresses ces dernières semaines, tu peux ignorer cet email ou simplement nous le rappeler.\n";
			String signature = "\n\nMerci d'avance pour ta coopération et tes efforts.\n\n" + returnNames;
			mailContent += mailRemark;
			mailContent += signature;

			System.out.println("Sending mail to " + mailReceiver + " ...");
			sendMail(mailSender, mailReceiver, mailSubject, mailContent);

			return true;

		} else {
			// mail could not be sent because this publisher does not have a
			// mail address.
			return false;

		}

	}

	/**
	 * Send a mail to a publisher, requesting the list of interested addresses
	 * of his territory.
	 *
	 * @param publisher
	 *            is a publisher instance meeting certain criteria.
	 * @return true or false.
	 * @throws IOException
	 */
	public static boolean sendTerritoryAddressesMailToPublisher(
			Publisher publisher) throws IOException {
		org.hibernate.Session session = HibernateUtil.getSessionFactory()
				.openSession();
		PublisherHome dao = new PublisherHome();
		Publisher p = (dao.findByExample(session, publisher)).get(0);
		return sendTerritoryAddressesMailToDatabasePublisher(p);

	}

	/**
	 * Sending an email to a given address.
	 *
	 * @param mailSender
	 * @param mailReceiver
	 * @param mailSubject
	 * @param mailContent
	 * @throws IOException
	 */
	public static void sendMail(String mailSender, String mailReceiver,
			String mailSubject, String mailContent) throws IOException {

		ResourceBundle bundle = Main.getMainBundle();

		final String username = bundle.getString("mail_user");
		final String password = bundle.getString("mail_password");
		final String smtpHost = bundle.getString("mail_smtp_host");
		final String smtpPort = bundle.getString("mail_smtp_port");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		session.setDebug(true);

		try {

			System.out.println("Sending email to " + mailReceiver);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mailReceiver));
			message.setSubject(mailSubject);
			message.setText(mailContent);

			Transport.send(message);

			System.out.println("Done - Email sending.");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
