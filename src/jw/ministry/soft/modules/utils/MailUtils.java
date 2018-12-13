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

import com.itextpdf.text.DocumentException;

import jw.ministry.soft.application.Main;
import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.gui.views.territories.TerritoriesController;
import jw.ministry.soft.modules.gui.views.territories.model.TerritoryHistoryModel;
import jw.ministry.soft.modules.gui.views.territories.model.TerritoryModel;

/**
 * @author Herv�Claude
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

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailSender));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," + "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean sendTerritoryWorkStatusMailToPublisher(Publisher p) throws IOException {

		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		return sendTerritoryWorkStatusMailToDatabasePublisher(((new PublisherHome()).findByExample(session, p)).get(0));
	}

	/**
	 * Send a mail to a database publisher instance, requesting the coverage
	 * status of of his territories.
	 *
	 * @param p
	 *            is a publisher instance from the database with all his join
	 *            information.
	 * @return true or false.
	 * @throws IOException
	 */
	public static boolean sendTerritoryWorkStatusMailToDatabasePublisher(Publisher p) throws IOException {
		String territoriesInfo = "";
		for (Territoriesassignments ass : p.getTerritoriesassignmentses()) {
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();
				TerritoryModel model = new TerritoryModel(ter);
				List<TerritoryHistoryModel> histories = model.getHistoryData();
				Collections.sort(histories);
				String lastWorkDateText = "--> Jamais travaill�";
				if (!histories.isEmpty()) {
					Date lastWorkDate = histories.get(0).getHistoryActionDate();
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());
					String lastWorkDateString = dateTimeFormatter.format(DateUtils.asLocalDate(lastWorkDate));
					lastWorkDateText = " --> Travaill� la derni�re fois le " + lastWorkDateString;
				}
				territoriesInfo += ter.getCode() + " - " + ter.getName() + " " + lastWorkDateText + "\n";
			}
		}
		if (p.getContact().getEmail() == null || p.getContact().getEmail().isEmpty())
			return false;
		ResourceBundle bundle = Main.getMainBundle();
		final String mailSender = bundle.getString("mail_sender");
		String mailReceiver = p.getContact().getEmail();
		String mailSubject = "Travail de ton (tes) territoire(s) ";
		String title = p.getSexe().getSexe().equalsIgnoreCase("masculin") ? "Cher " + p.getFirstName() + ",\n"
				: "Ch�re " + p.getFirstName() + ",\n";
		String mailContent = title + "\n\n"
				+ "Notre semaine sp�ciale approche � tr�s grands pas! Nous avons � nouveau besoin de quelques informations de ta part, afin de pr�parer le dossier des territoires pour notre surveillant itin�rant\n\n"
				+ "D'apr�s notre liste tu travailles actuellement le(s) territoire(s) suivant(s):\n\n" + territoriesInfo
				+ "\n\n"
				+ "Pourrais-tu s'il te plait nous dire quand tu les as travaill�s (compl�tement!) la derni�re fois?\nSi tu ne les as plus travaill� depuis la derni�re fois, veuille quand m�me nous en informer (la derni�re date sera retenue).\n\n";
		String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
		String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
		String mailReturnInfo = "";
		String returnNames = "";
		if (mailResponsesNames.length > 1) {
			returnNames += "Tes fr�res\n";
		} else {
			returnNames += "Ton fr�re\n";
		}
		for (int i = 0; i < mailResponsesAddresses.length; i++) {
			mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
			if (i == 0) {
				returnNames += mailResponsesNames[i];
			} else if (i == (mailResponsesAddresses.length - 1)) {
				returnNames += " & " + mailResponsesNames[i];
			} else {
				returnNames += ", " + mailResponsesNames[i];
			}
		}
		String mailRemark = "\n\nVeuille r�pondre � l'une des adresses email suivantes:\n" + mailReturnInfo
				+ "ou tout simplement nous contacter d�s que possible par un autre moyen.\n";
		String signature = "\n\nMerci d'avance pour ta coop�ration et tes efforts.\n\n" + returnNames;
		mailContent += mailRemark;
		mailContent += signature;
		System.out.println("Sending mail to " + mailReceiver + " ...");
		sendMail(mailSender, mailReceiver, mailSubject, mailContent);
		return true;
	}

	public static boolean sendTerritoryWorkStatusRemainderMailToDatabasePublisher(Publisher p) throws IOException {
		String territoriesInfo = "";
		for (Territoriesassignments ass : p.getTerritoriesassignmentses())
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();
				TerritoryModel model = new TerritoryModel(ter);
				List<TerritoryHistoryModel> histories = model.getHistoryData();
				Collections.sort(histories);
				String lastWorkDateText = "--> Jamais travaill�";
				if (!histories.isEmpty()) {
					Date lastWorkDate = histories.get(0).getHistoryActionDate();
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());
					String lastWorkDateString = dateTimeFormatter.format(DateUtils.asLocalDate(lastWorkDate));
					lastWorkDateText = " --> Travaill� la derni�re fois le " + lastWorkDateString;
				}
				territoriesInfo += ter.getCode() + " - " + ter.getName() + " " + lastWorkDateText + "\n";
			}
		if (p.getContact().getEmail() == null || p.getContact().getEmail().isEmpty())
			return false;
		ResourceBundle bundle = Main.getMainBundle();
		final String mailSender = bundle.getString("mail_sender");
		String mailReceiver = p.getContact().getEmail();
		String mailSubject = "Rappel - Travail de ton (tes) territoire(s) ";
		String title = (p.getSexe().getSexe().equalsIgnoreCase("masculin") ? "Cher " : "Ch�re ") + p.getFirstName()
				+ ",\n";
		String mailContent = title + "\n\n"
				+ "Ceci est un rappel collectif. Si tu as d�ja transmis les informations requises ci-dessous, veuille s'il te plait, ignorer cet Email! Dans le cas contraire, veuille s'il te plait nous les transmettre au plus tard � la prochaine r�union. Merci beaucoup pour ta coop�ration. "
				+ "\n\n"
				+ "Notre semaine sp�ciale approche � tr�s grands pas! Nous avons � nouveau besoin de quelques informations de ta part, afin de pr�parer le dossier des territoires pour notre surveillant itin�rant\n\n"
				+ "D'apr�s notre liste tu travailles actuellement le(s) territoire(s) suivant(s):\n\n" + territoriesInfo
				+ "\n\n"
				+ "Pourrais-tu s'il te plait nous dire quand tu les as travaill�s (compl�tement!) la derni�re fois?\nSi tu ne les as plus travaill� depuis la derni�re fois, veuille quand m�me nous en informer (la derni�re date sera retenue).\n\n";
		String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
		String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
		String mailReturnInfo = "";
		String returnNames = "";
		returnNames += mailResponsesNames.length > 1 ? "Tes fr�res\n" : "Ton fr�re\n";
		for (int i = 0; i < mailResponsesAddresses.length; ++i) {
			mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
			if (i == 0)
				returnNames += mailResponsesNames[i];
			else
				returnNames += i == (mailResponsesAddresses.length - 1) ? " & " + mailResponsesNames[i]
						: ", " + mailResponsesNames[i];
		}
		String mailRemark = "\n\nVeuille r�pondre � l'une des adresses Email suivantes:\n" + mailReturnInfo
				+ "ou tout simplement nous contacter d�s que possible par un autre moyen.\n";
		String signature = "\n\nMerci d'avance pour ta coop�ration et tes efforts.\n\n" + returnNames;
		mailContent += mailRemark;
		mailContent += signature;
		System.out.println("Sending mail to " + mailReceiver + " ...");
		sendMail(mailSender, mailReceiver, mailSubject, mailContent);
		try {
			TerritoriesController.createPdf(p.getFirstName() + "_" + p.getLastName(), mailContent);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean sendTerroriesReturnRequestMailToDatabasePublisher(Publisher p) throws IOException {
		String territoriesInfo = "";
		for (Territoriesassignments ass : p.getTerritoriesassignmentses())
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();
				TerritoryModel model = new TerritoryModel(ter);
				List<TerritoryHistoryModel> histories = model.getHistoryData();
				Collections.sort(histories);
				territoriesInfo += ter.getCode() + " - " + ter.getName()  + "\n";
			}
		if (p.getContact().getEmail() == null || p.getContact().getEmail().isEmpty())
			return false;
		ResourceBundle bundle = Main.getMainBundle();
		final String mailSender = bundle.getString("mail_sender");
		String mailReceiver = p.getContact().getEmail();
		String mailSubject = "Rappel Urgent - Retour de tous les territoires de Bielefeld francophone!!";
		String title = (p.getSexe().getSexe().equalsIgnoreCase("masculin") ? "Cher " : "Ch�re ") + p.getFirstName()
				+ ",\n";
		String mailContent = title + "\n\n"
				+ "Ceci est un rappel collectif. Si tu as d�ja rendu TOUS tes territoires aux responsables, tu peux ignorer cet Email! Dans le cas contraire, veuille s'il te plait nous les remettre URGEMMENT d�s la prochaine r�union."
				+ "\n\n"
				+ "D'apr�s notre liste tu travailles actuellement le(s) territoire(s) suivant(s):\n\n" + territoriesInfo
				+ "\n\n"
				+ "Si tu poss�des encore des adresses de ces territoires non transmises aux responsables, veuille absolument les noter sur tes cartes de terriroires correspondantes avant de la remettre.\n\n"
				+ "\n\n"
				+ "Des d�tails suivront en temps voulu, pour ceux qui se poseraient des questions...\n\n";
		String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
		String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
		String mailReturnInfo = "";
		String returnNames = "";
		returnNames += mailResponsesNames.length > 1 ? "Tes fr�res\n" : "Ton fr�re\n";
		for (int i = 0; i < mailResponsesAddresses.length; ++i) {
			mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
			if (i == 0)
				returnNames += mailResponsesNames[i];
			else
				returnNames += i == (mailResponsesAddresses.length - 1) ? " & " + mailResponsesNames[i]
						: ", " + mailResponsesNames[i];
		}
//		String mailRemark = "\n\nVeuille r�pondre � l'une des adresses Email suivantes:\n" + mailReturnInfo
//				+ "ou tout simplement nous contacter d�s que possible par un autre moyen.\n";
		String signature = "\n\nMerci d'avance pour ta coop�ration et tes efforts.\n\n" + returnNames;
//		mailContent += mailRemark;
		mailContent += signature;
		System.out.println("Sending mail to " + mailReceiver + " ...");
		sendMail(mailSender, mailReceiver, mailSubject, mailContent);
		try {
			TerritoriesController.createPdf("return_mail_" + p.getFirstName() + "_" + p.getLastName() , mailContent);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Send a mail to a database publisher instance, requesting the list of
	 * interested addresses of his territory.
	 *
	 * @param p
	 *            is a publisher instance from the database with all his join
	 *            information.
	 * @return true or false.
	 * @throws IOException
	 */
	public static boolean sendTerritoryAddressesMailToDatabasePublisher(Publisher p) throws IOException {
		String territoriesInfo = "";
		for (Territoriesassignments ass : p.getTerritoriesassignmentses()) {
			if (ass.getReturnDate() == null) {
				Territory ter = ass.getTerritory();
				territoriesInfo += ter.getCode() + " - " + ter.getName() + "\n";
			}
		}
		if (p.getContact() == null || p.getContact().getEmail() == null || p.getContact().getEmail().isEmpty())
			return false;
		ResourceBundle bundle = Main.getMainBundle();
		final String mailSender = bundle.getString("mail_sender");
		String mailReceiver = p.getContact().getEmail();
		String mailSubject = "Liste d'adresses de ton (tes) territoire(s) ";
		String title = p.getSexe().getSexe().equalsIgnoreCase("masculin") ? "Cher " + p.getFirstName() + ",\n"
				: "Ch�re " + p.getFirstName() + ",\n";
		String mailContent = title + "\n\n"
				+ "D'apr�s notre liste tu travailles actuellement les territoires suivants:\n\n" + territoriesInfo
				+ "\n\n"
				+ "Pourrais-tu s'il te plait nous transmettre la(les) liste(s) actuelle(s) des adresses de ce(s) territoire(s) afin que nous compl�tions le fichier central d'adresses francophones de la congr�gation?";
		String[] mailResponsesAddresses = bundle.getString("mail_response_adresses").split(";");
		String[] mailResponsesNames = bundle.getString("mail_response_names").split(";");
		String mailReturnInfo = "";
		String returnNames = "";
		if (mailResponsesNames.length > 1) {
			returnNames += "Tes fr�res\n";
		} else {
			returnNames += "Ton fr�re\n";
		}
		for (int i = 0; i < mailResponsesAddresses.length; i++) {
			mailReturnInfo += mailResponsesNames[i] + ": " + mailResponsesAddresses[i] + "\n";
			if (i == 0) {
				returnNames += mailResponsesNames[i];
			} else if (i == (mailResponsesAddresses.length - 1)) {
				returnNames += " & " + mailResponsesNames[i];
			} else {
				returnNames += ", " + mailResponsesNames[i];
			}
		}
		String mailRemark = "\n\nVeuille r�pondre � l'une des adresses email suivantes:\n" + mailReturnInfo
				+ "ou tout simplement nous contacter d�s que possible par un autre moyen.\n"
				+ "N'h�site pas � nous contacter, si tu as besoin d'informations suppl�mentaires.\n\n"
				+ "Si tu nous as d�ja fait parvenir tes adresses ces derni�res semaines, tu peux ignorer cet email ou simplement nous le rappeler.\n";
		String signature = "\n\nMerci d'avance pour ta coop�ration et tes efforts.\n\n" + returnNames;
		mailContent += mailRemark;
		mailContent += signature;
		System.out.println("Sending mail to " + mailReceiver + " ...");
		sendMail(mailSender, mailReceiver, mailSubject, mailContent);
		return true;
	}

	/**
	 * Send a mail to a publisher, requesting the list of interested addresses
	 * of his territory.
	 *
	 * @param p
	 *            is a publisher instance meeting certain criteria.
	 * @return true or false.
	 * @throws IOException
	 */
	public static boolean sendTerritoryAddressesMailToPublisher(Publisher p) throws IOException {
		org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
		PublisherHome dao = new PublisherHome();
		return sendTerritoryAddressesMailToDatabasePublisher((dao.findByExample(session, p)).get(0));

	}

	/**
	 * Sending an email to a given address.
	 *
	 * TODO: Find a way to send emails without deactivating the virus scaner!!
	 *
	 * @param mailSender
	 * @param mailReceiver
	 * @param mailSubject
	 * @param mailContent
	 * @throws IOException
	 *
	 */
	public static void sendMail(String mailSender, String mailReceiver, String mailSubject, String mailContent)
			throws IOException {

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

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		session.setDebug(true);

		try {

			System.out.println("Sending email to " + mailReceiver);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailReceiver));
			message.setSubject(mailSubject);
			message.setText(mailContent);

			// FIXME: When the virus scanner is running, a certificate
			// validation is thrown during the mail transfer. As workaround, the
			// virus scanner must be shut down before sending mails
			// successfully!!!
			Transport.send(message);

			System.out.println("Done - Email sending.");

			// GraphicsUtils.openInformationDialog("MinistrySoftApp",
			// "Email envoy� � " + mailReceiver + "!",
			// null);

		} catch (MessagingException e) {
			GraphicsUtils.openErrorDialog("MinistrySoftApp",
					"Echec d'envoi d'Email � " + mailReceiver + "!" + "\n" + e.getMessage(), null);
			throw new RuntimeException(e);
		}
	}
}
