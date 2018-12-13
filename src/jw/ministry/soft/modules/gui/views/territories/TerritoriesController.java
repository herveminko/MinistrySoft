package jw.ministry.soft.modules.gui.views.territories;

import java.awt.Dialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBException;

//import org.controlsfx.dialog.Dialogs;
import org.hibernate.Session;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fxmlviews.RootController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jw.ministry.soft.application.Main;
import jw.ministry.soft.components.AutoCompleteComboBoxListener;
import jw.ministry.soft.modules.data.dao.CongregationHome;
import jw.ministry.soft.modules.data.dao.PublisherHome;
import jw.ministry.soft.modules.data.dao.TerritoriesassignmentsHome;
import jw.ministry.soft.modules.data.dao.TerritoryHome;
import jw.ministry.soft.modules.data.dao.TerritoryhistoryHome;
import jw.ministry.soft.modules.data.dto.Congregation;
import jw.ministry.soft.modules.data.dto.Publisher;
import jw.ministry.soft.modules.data.dto.Territoriesassignments;
import jw.ministry.soft.modules.data.dto.Territory;
import jw.ministry.soft.modules.data.dto.Territoryhistory;
import jw.ministry.soft.modules.data.exchange.imports.InterestedListParser;
import jw.ministry.soft.modules.data.exchange.imports.TerritoryHistoryImporter;
import jw.ministry.soft.modules.data.reports.DetailledGroupTerritoryReportData;
import jw.ministry.soft.modules.data.reports.TerritoryReportData;
import jw.ministry.soft.modules.data.reports.TypedGroupTerritoryReportData;
import jw.ministry.soft.modules.gui.views.publishers.model.PublisherModel;
import jw.ministry.soft.modules.gui.views.publishers.model.PublisherNameComparator;
import jw.ministry.soft.modules.gui.views.territories.model.TerritoryHistoryModel;
import jw.ministry.soft.modules.gui.views.territories.model.TerritoryModel;
import jw.ministry.soft.modules.utils.DateUtils;
import jw.ministry.soft.modules.utils.GraphicsUtils;
import jw.ministry.soft.modules.utils.HibernateUtil;
import jw.ministry.soft.modules.utils.MailUtils;
import jw.ministry.soft.modules.utils.territories.HeaderFooterPageEvent;
import jw.ministry.soft.modules.utils.territories.HeaderFooterPageEvent2;
import jxl.write.WriteException;

public class TerritoriesController {

	RootController rootParentController;

	private final String BLACK_LISTED_TERRITORY_STATUS_STRING = "Archivé";

	private Map<String, TerritoryReportData> totalReportsData;
	private List<DetailledGroupTerritoryReportData> detailledReportsData;

	@FXML
	private WebView mapViewer;
	private WebEngine webEngine;
	@FXML
	private WebView territoriesReportHtmlViewer;
	private WebEngine territoriesReportWebEngine;
	@FXML
	private TabPane territoriesPane;
	@FXML
	private Button assignButton;
	@FXML
	private Tab modificationTab;
	@FXML
	private Tab historyTab;
	@FXML
	private Tab territoryMap;
	@FXML
	private Tab mailingTab;

	/* Insertion pane */

	@FXML
	private Button carte;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField codeTextField;
	@FXML
	private TextField regionTextField;
	@FXML
	private ComboBox<String> typeComboBox;
	@FXML
	private ComboBox<String> groupComboBox;
	@FXML
	private ComboBox<String> congregationItemComboBox;
	@FXML
	private ComboBox<String> territoryWorkLevelItemComboBox;
	@FXML
	private DatePicker datePickerTerritoryReception;
	@FXML
	private DatePicker datePickerTerritoryReturn;
	@FXML
	private DatePicker datePickerTerritoryWork;
	@FXML
	private DatePicker datePickerReportReference;

	ObservableList<String> typeOptions = FXCollections.observableArrayList("Normal", "Foyer étudiants",
			"Foyer réfugiés", "Foyer réfugiés - Banlieue");
	ObservableList<String> groupOptions = FXCollections.observableArrayList("Bielefeld", "Paderborn", "Osnabrück", "");

	/* Table pane */

	@FXML
	private AnchorPane territoriesTablePane;
	@FXML
	private TableView<TerritoryModel> territoriesTable;
	@FXML
	private TableColumn<TerritoryModel, String> numberColumn;
	@FXML
	private TableColumn<TerritoryModel, String> territoryNameColumn;
	@FXML
	private TableColumn<TerritoryModel, String> territoryCodeColumn;
	@FXML
	private TableColumn<TerritoryModel, String> territoryStatusColumn;
	@FXML
	private TableColumn<TerritoryModel, String> territoryTypeColumn;
	@FXML
	private TableColumn<TerritoryModel, String> territoryRegionColumn;
	@FXML
	private TableColumn<TerritoryModel, String> publisherColumn;
	@FXML
	private TableColumn<TerritoryModel, String> territoryGroupColumn;

	@FXML
	private TableView<TerritoryHistoryModel> territoryHistoryTable;
	@FXML
	private TableColumn<TerritoryHistoryModel, String> territoryHistoryDate;
	@FXML
	private TableColumn<TerritoryHistoryModel, String> territoryHistoryAction;
	@FXML
	private TableColumn<TerritoryHistoryModel, String> territoryHistoryPublisherName;

	@FXML
	private TableView<PublisherModel> MailReceiversTable;
	@FXML
	private TableColumn<PublisherModel, String> publisherReceiverFirstNameColumn;
	@FXML
	private TableColumn<PublisherModel, String> publisherReceiverLastNameColumn;
	@FXML
	private TableColumn<PublisherModel, String> emailReceiverColumn;
	@FXML
	private TableColumn<PublisherModel, CheckBox> checkItemsColumns;

	@FXML
	private TextField filterField;
	@FXML
	private TextField publisherSearch;

	/**
	 * The data as an observable list of territories.
	 */
	private ObservableList<TerritoryModel> territoriesData = FXCollections.observableArrayList();

	private FilteredList<TerritoryModel> filteredTerritoryModelList;

	/**
	 * The data as an observable list of territory histories.
	 */
	private ObservableList<TerritoryHistoryModel> territoryHistoriesData = FXCollections.observableArrayList();

	private ObservableList<PublisherModel> publisherReceiver = FXCollections.observableArrayList();

	private ObservableList<Publisher> MailListOfRecipients = FXCollections.observableArrayList();

	private ObservableList<String> congregationName = FXCollections.observableArrayList();

	/* Edition pane */

	@FXML
	private TextField updateNameTextField;
	@FXML
	private TextField updateCodeTextField;
	@FXML
	private TextField updateRegionTextField;
	@FXML
	private ComboBox<String> updateTypeComboBox;
	@FXML
	private ComboBox<String> updatePublisherComboBox;
	@FXML
	private ComboBox<String> updateGroupComboBox;
	@FXML
	private Button saveButton;
	@FXML
	private ImageView viewer = new ImageView();

	private final ToggleGroup tg = new ToggleGroup();
	@FXML
	private RadioButton adressList;
	@FXML
	private RadioButton statisticsWork;
	@FXML
	private RadioButton workStatus;

	@FXML
	private Label territoriesTableTitle;

	@FXML
	private ComboBox<String> filterTerritoriesGroupsComboBox;

	@FXML
	private CheckBox filterArchivedTerritoriesCheckbox;

	@FXML
	private Accordion territoriesAccordionPane;
	@FXML
	private TitledPane territoryInsertionTitledPane;
	@FXML
	private TitledPane territoryListTitledPane;

	/* REPORTS */

	@FXML
	private StackPane territoriesReportsPane = new StackPane();
	@FXML
	private BorderPane singleGroupsReportsBorderPane;
	@FXML
	private BorderPane numbersReportsBorderPane;
	@FXML
	private BorderPane totalReportsBorderPane;
	@FXML
	RadioButton buttonReportTotal;
	@FXML
	RadioButton buttonReportDetails;
	@FXML
	RadioButton buttonReportNumbers;
	@FXML
	BarChart detailledReportsBarChart;
	@FXML
	private PieChart totalReportsPieChart;

	@FXML
	ObservableList<PieChart.Data> pieChartDataItems;

	@FXML
	public void sendMailToPublishers() {
		try {
			MailUtils.testMail();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void saveTerritoryHistoryRecord() {
		List<Publisher> list = getPublishersList();
		int selectedIndex = updatePublisherComboBox.getSelectionModel().getSelectedIndex();

		Publisher pub = list.get(selectedIndex);

		Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
		TerritoryHome territoryDao = new TerritoryHome();
		PublisherHome publisherDao = new PublisherHome();
		TerritoryhistoryHome territoryHistoryDao = new TerritoryhistoryHome();

		Session session = HibernateUtil.getSessionFactory().openSession();

		Publisher dbPublisher = (publisherDao.findByExample(session, pub)).get(0);
		Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);

		Territoryhistory newHistory = new Territoryhistory();
		newHistory.setPublisher(dbPublisher);
		newHistory.setTerritory(dbTerritory);
		newHistory.setActionDescrption(territoryWorkLevelItemComboBox.getValue());
		Date historyDate = DateUtils.asDate(datePickerTerritoryWork.getValue());
		newHistory.setActionDate(historyDate);

		territoryHistoryDao.persist(session, newHistory);

		GraphicsUtils.openInformationDialog("Modification  d'historique",
				"L'historique du territoire a été modifiée avec succès.", null);

	}

	private void updateTerritoryMap(String territoryWebUrl) {

		if (territoryWebUrl == null) {
			return;
		}
		// Create a trust manager that does not validate certificate chains
		X509TrustManager[] trustAllCerts = new X509TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		}
		// Now you can access an https URL without having the certificate in the
		// truststore
		try {
			URL url = new URL(territoryWebUrl);
		} catch (MalformedURLException e) {
		}

		webEngine = mapViewer.getEngine();
		webEngine.setJavaScriptEnabled(true);
		// final URL urlOSMMaps = getClass().getResource(territoryWebUrl);
		// webEngine.load(urlOSMMaps.toExternalForm());
		System.out.println("Loading web url ...  " + territoryWebUrl);
		webEngine.load(territoryWebUrl);
	}

	@FXML
	public void initialize() {

		territoriesReportsPane.getChildren().clear();
		territoriesReportsPane.getChildren().add(totalReportsBorderPane);

		ObservableList<String> territoryWorkLevelOptions = FXCollections.observableArrayList("Travaillé",
				"Travaillé complètement");
		territoryWorkLevelItemComboBox.setItems(territoryWorkLevelOptions);

		ObservableList<String> publishersGroupsOptions = FXCollections.observableArrayList("Bielefeld", "Paderborn");
		filterTerritoriesGroupsComboBox.setItems(publishersGroupsOptions);

		territoriesAccordionPane.setExpandedPane(territoryListTitledPane);

		webEngine = mapViewer.getEngine();
		final URL urlOSMMaps = getClass().getResource("map/openstreetmap.html");
		webEngine.load(urlOSMMaps.toExternalForm());

		// Adding Radio buttons to the ToggleGroup: Only one button can be
		// selected!
		adressList.setToggleGroup(tg);
		statisticsWork.setToggleGroup(tg);
		workStatus.setToggleGroup(tg);
		adressList.setSelected(true);
		// Listener
		tg.selectedToggleProperty().addListener((value, oldValue, newValue) -> {
			//
		});

		// Add a toolTip
		Tooltip tooltip = new Tooltip("Cliquez-ici pour selectionner " + "une carte de térritoire");
		carte.setTooltip(tooltip);

		datePickerTerritoryReception.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});

		datePickerTerritoryReturn.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});

		datePickerTerritoryWork.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});

		datePickerReportReference.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtils.getUiDateFormat());

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});

		datePickerReportReference.setValue(DateUtils.asLocalDate(new Date()));

		if (getRootParentController() != null) {
			updatePublishersList();
			if (this.getRootParentController().getFxStage() != null) {
				this.getRootParentController().getFxStage().getScene().getStylesheets()
						.add(getClass().getResource("territories.css").toExternalForm());
			}
		}

		saveButton.setDisable(true);
		assignButton.setDisable(true);

		typeComboBox.setItems(typeOptions);
		groupComboBox.setItems(groupOptions);
		updateTypeComboBox.setItems(typeOptions);
		updateGroupComboBox.setItems(groupOptions);

		// Initialize the territories table with the five columns.
		territoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryName());
		territoryCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryCode());
		territoryStatusColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryStatusProperty());
		territoryStatusColumn
				.setCellFactory(new Callback<TableColumn<TerritoryModel, String>, TableCell<TerritoryModel, String>>() {
					@Override
					public TableCell<TerritoryModel, String> call(
							TableColumn<TerritoryModel, String> soCalledFriendBooleanTableColumn) {
						return new TableCell<TerritoryModel, String>() {
							@Override
							public void updateItem(final String item, final boolean empty) {
								super.updateItem(item, empty);
								// update the item and set a custom style if
								// necessary
								setText(item);
								this.getTableRow().getStyleClass().remove("assignedTerritoryRow");
								this.getTableRow().getStyleClass().remove("unassignedTerritoryRow");
								this.getTableRow().getStyleClass().remove("archivedTerritoryRow");

								if (item != null) {
									if (item.equalsIgnoreCase(BLACK_LISTED_TERRITORY_STATUS_STRING)) {
										// this.getTableRow().setStyle(
										// "-fx-background-color: orange");
										this.getTableRow().getStyleClass().add("archivedTerritoryRow");
										// this.getStyleClass().add("archivedTerritoryRow");
									} else if (item.trim().isEmpty()) {
										this.getTableRow().getStyleClass().add("unassignedTerritoryRow");
										// this.getTableRow().setStyle(
										// "-fx-background-color: palegreen");
									} else {
										this.getTableRow().getStyleClass().add("assignedTerritoryRow");
										// this.getTableRow().setStyle(
										// "");
									}

								} else {
									this.getTableRow().getStyleClass().add("assignedTerritoryRow");
									// this.getTableRow().setStyle(
									// "");
								}
							}
						};
					}
				});
		territoryTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryType());
		territoryRegionColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryRegion());
		publisherColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryOwnerName());
		territoryGroupColumn.setCellValueFactory(cellData -> cellData.getValue().getTerritoryGroup());

		ObservableList<Long> checkPubisher = FXCollections.observableArrayList(new Long(1));
		ObservableList<PublisherModel> publisher = FXCollections.observableArrayList();

		checkItemsColumns.setCellValueFactory(
				new Callback<CellDataFeatures<PublisherModel, CheckBox>, ObservableValue<CheckBox>>() {
					@Override
					public ObservableValue<CheckBox> call(CellDataFeatures<PublisherModel, CheckBox> arg) {
						Publisher pm = arg.getValue().getPublisherRecord();
						CheckBox chb = new CheckBox();
						for (Long value : checkPubisher) {
							if (value.intValue() == pm.getPublisherId())
								chb.selectedProperty().setValue(Boolean.TRUE);
						}

						return new SimpleObjectProperty<CheckBox>(chb);
					}
				});

		publisherReceiverFirstNameColumn
				.setCellValueFactory(cellData -> cellData.getValue().getPublisherFirstNameProperty());

		publisherReceiverLastNameColumn
				.setCellValueFactory(cellData -> cellData.getValue().getPublisherLastNameProperty());

		emailReceiverColumn.setCellValueFactory(cellData -> cellData.getValue().getPublisherEmailProperty());

		// Initialize the territory histories table with the three columns.
		territoryHistoryDate.setCellValueFactory(cellData -> cellData.getValue().getHistoryActionDateProperty());
		territoryHistoryAction.setCellValueFactory(cellData -> cellData.getValue().getHistoryActionProperty());
		territoryHistoryPublisherName
				.setCellValueFactory(cellData -> cellData.getValue().getHistoryPublisherNameProperty());
		// Listen for selection changes and show the person details when
		// changed.
		territoriesTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showCurrentTerritoryHistory());

		getAllPublisherReceiverInCongregation();

		getAllTerritoriesFromDatabase();

		// Setting territory's table context menus
		territoriesTable.setRowFactory(new Callback<TableView<TerritoryModel>, TableRow<TerritoryModel>>() {
			@Override
			public TableRow<TerritoryModel> call(TableView<TerritoryModel> tableView) {
				final TableRow<TerritoryModel> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				MenuItem editItem = new MenuItem("Modifier...");
				editItem.setOnAction((event) -> {

					territoriesPane.getSelectionModel().select(modificationTab);
					// Edit item was clicked, do something...
					String s = "Editing territory: name = " + row.getItem().getTerritoryName().getValue();

					System.out.println(s);
					disableTerritoriesTable();
					fillUpdateFields(row.getItem());

				});
				MenuItem removeItem = new MenuItem("Delete");
				removeItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						territoriesTable.getItems().remove(row.getItem());
					}
				});
				MenuItem historyItem = new MenuItem("Afficher historique");
				historyItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						showCurrentTerritoryHistory();

						territoriesPane.getSelectionModel().select(historyTab);
						// evaluationTab

					}
				});

				MenuItem setMap = new MenuItem("Afficher carte");
				setMap.setOnAction((Event) -> {
					// territoryMap tab
					territoriesPane.getSelectionModel().select(territoryMap);
					// String ort =
					// row.getItem().getTerritoryName().getValue();
					// String region =
					// row.getItem().getTerritoryRegion().getValue();

					// Displaying the map

				});

				MenuItem sendMail = new MenuItem("Envoyer courriel - Adresses...");
				sendMail.setOnAction((Event) -> {

					TerritoryModel mod = row.getItem();
					Publisher pub = mod.getTerritoryOwner();
					try {
						boolean isMailSent = MailUtils.sendTerritoryAddressesMailToPublisher(pub);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});

				MenuItem sendMail2 = new MenuItem("Envoyer courriel - Status...");
				sendMail2.setOnAction((Event) -> {

					TerritoryModel mod = row.getItem();
					Publisher pub = mod.getTerritoryOwner();
					try {
						boolean isMailSent = MailUtils.sendTerritoryWorkStatusRemainderMailToDatabasePublisher(pub);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				});

				MenuItem deactivateItem = new MenuItem("Archiver");
				deactivateItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						deactivateTerritory();

					}
				});

				MenuItem activateItem = new MenuItem("Réactiver");
				activateItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						reactivateTerritory();

					}
				});

				rowMenu.getItems().addAll(editItem, historyItem, setMap, sendMail, sendMail2, deactivateItem,
						activateItem);

				// only display context menu for non-null items:
				row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu)
						.otherwise((ContextMenu) null));
				return row;
			}

		});

		recalculateReportsData();

		territoriesReportWebEngine = territoriesReportHtmlViewer.getEngine();
		territoriesReportWebEngine.loadContent("<html>" + "<head><title>html reports tables</title></head>"

				+ "<style type='text/css'>" + "<!--" + " #rand09 {" + "  border: 1px solid #003399;"
				+ "  background-color: #E8EDFF;" + "}"

				+ " #rand09 caption {" + "  color: #003399;" + "  margin-bottom: 5px;" + "}"

				+ " #rand09 th {" + "  background-color: #B9C9FE;" + "  color:#FFFFFF;" + "  font-weight: bold;" + "}"

				+ " #rand09 td, #rand09 th {" + "  border: 1px solid #FFFFFF;"
				+ "  font-family: Verdana, Arial, sans-serif;" + "  font-size:11px;" + "}"

				+ "#rand09 td {" + "  color: #003399;" + "  font-family: Verdana, Arial, sans-serif;"
				+ "  font-size:11px;" + "  font-weight:normal;" + "}"

				+ "table { " + "border: 1px solid #4F4F4F; " + "border-collapse: separate; " + "}"

				+ "-->"

				+ "</style>"

				+ "<body>" + getDetailledHtmlTerritoriesStatus() + "</body>" + "</html>");

	}

	private String getDetailledHtmlTerritoriesStatus() {
		String htmlString = "<table id='rand09' cellspacing='0' cellpadding='4' >"
				+ "<caption>Répartition des territoires</caption>" + "<tr>" + "<th/>" + "<th>Étudiants</th>" + "<th/>"
				+ "<th>Réfugiés</th>" + "<th/>" + "<th>Recherche</th>" + "<th/>" + "<th>Total:</th>" + "<th/>"
				+ "</tr>";

		int totalCount = 0;
		int totalNotAssignedCount = 0;

		for (DetailledGroupTerritoryReportData groupReport : detailledReportsData) {

			int refugeesTotal = groupReport.getRefugeesTerritoriesReports().getAllWorkedTerritories().size()
					+ groupReport.getRefugeesTerritoriesReports().getNotAssigned().size();
			int refugeesNotAssigned = groupReport.getRefugeesTerritoriesAssignedToGroup();

			int studentsTotal = groupReport.getStudentsTerritoriesReports().getAllWorkedTerritories().size()
					+ groupReport.getStudentsTerritoriesReports().getNotAssigned().size();
			int studentsNotAssigned = groupReport.getStudentTerritoriesAssignedToGroup();

			int searchTotal = groupReport.getResearchTerritoriesReports().getAllWorkedTerritories().size()
					+ groupReport.getResearchTerritoriesReports().getNotAssigned().size();
			int searchTotalNotAssigned = groupReport.getSearchTerritoriesAssignedToGroup();

			int totalTerritories = refugeesTotal + studentsTotal + searchTotal;
			totalCount += totalTerritories;
			int totalNotAssignedTerritories = refugeesNotAssigned + studentsNotAssigned + searchTotalNotAssigned;
			totalNotAssignedCount += (totalTerritories - totalNotAssignedTerritories);
			htmlString += "<tr>" + "<td>" + groupReport.getGroupName() + "</td>" + "<td>" + studentsTotal + "</td>"
					+ "<td style='color:red' >" + (studentsTotal - studentsNotAssigned) + "</td>" + "<td>"
					+ refugeesTotal + "</td>" + "<td style='color:red' >" + (refugeesTotal - refugeesNotAssigned)
					+ "</td>" + "<td>" + searchTotal + "</td>" + "<td style='color:red' >"
					+ (searchTotal - searchTotalNotAssigned) + "</td>" + "<td>" + totalTerritories + "</td>"
					+ "<td style='color:red'>" + (totalTerritories - totalNotAssignedTerritories) + "</td>" + "</tr>";
		}
		htmlString += "<tr>" + "<td colspan='7'>Bilan assignations</td>" + "<td>" + totalCount + "</td>"
				+ "<td style='color:red' >" + totalNotAssignedCount + "</td>" + "</tr>";

		htmlString += "<tfoot><tr><td style='color:red' colspan='9'>* Les chiffres en rouge représentent les nombres de territoires respectifs ayant été sortis.</td></tr></tfoot>";
		htmlString += "</table>";

		return htmlString;

	}

	public void resetReportsData() {
		List<String> groups = Arrays.asList("Bielefeld", "Paderborn");
		totalReportsData = new HashMap<String, TerritoryReportData>();
		for (String groupName : groups) {
			totalReportsData.put(groupName, new TerritoryReportData());
		}

		detailledReportsData = new ArrayList<DetailledGroupTerritoryReportData>();
	}

	@FXML
	private void sendMailForTerroriesAddresses() {
		List<Publisher> listOfPublisherWithoutMailAddresses = new ArrayList<Publisher>();
		Map<Integer, Publisher> listOfContactedPublisher = new HashMap<Integer, Publisher>();

		Session session = HibernateUtil.getSessionFactory().openSession();

		TerritoryHome territoriesDao = new TerritoryHome();
		List<Territory> territories = territoriesDao.getAllTerritories(session);
		for (Territory t : territories) {
			if (!t.getTerritoriesassignmentses().isEmpty()) {
				Territoriesassignments assignMent = (Territoriesassignments) (t.getTerritoriesassignmentses()
						.toArray()[0]);
				Publisher responsibePublisher = assignMent.getPublisher();
				if (assignMent.getReturnDate() == null) {
					boolean isMailSent = false;
					if (listOfContactedPublisher.get(responsibePublisher.getPublisherId()) == null) {
						try {
							isMailSent = MailUtils.sendTerritoryAddressesMailToDatabasePublisher(responsibePublisher);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							isMailSent = false;
						}

					} else {
						isMailSent = true;
					}

					if (!isMailSent) {
						listOfPublisherWithoutMailAddresses.add(responsibePublisher);
					} else {

						listOfContactedPublisher.put(responsibePublisher.getPublisherId(), responsibePublisher);
					}
				}
			}
		}

		ResourceBundle bundle = null;
		try {
			bundle = Main.getMainBundle();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String mailSender = bundle.getString("mail_sender");
		String mailSubject = "Liste d'adresses des territoires - Rappel";

		if (!listOfPublisherWithoutMailAddresses.isEmpty()) {

			String mailContent = "Les proclamateurs suivants ont des territoires, mais ne possèdent pas d'adresse email (ou l'envoie ne fonctionna pas):\n\n";
			for (Publisher p : listOfPublisherWithoutMailAddresses) {
				String territoriesInfo = "";

				for (Territoriesassignments ass : p.getTerritoriesassignmentses()) {
					Territory ter = ass.getTerritory();
					territoriesInfo += ter.getCode() + " - " + ter.getName() + "\n";
				}
				mailContent += p.getFirstName() + " " + p.getLastName() + "\n";
				mailContent += territoriesInfo + "\n";

			}

			mailContent += "Veuille les contacter directement le plus tôt possible!!\n\n";

			try {
				String mailReceivers = bundle.getString("mail_response_adresses");
				String[] receiversArray = mailReceivers.split(";");
				for (int i = 0; i < receiversArray.length; i++) {
					String mailReceiver = receiversArray[i];
					MailUtils.sendMail(mailSender, mailReceiver, mailSubject, mailContent);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!listOfContactedPublisher.isEmpty()) {
			mailSubject = "Liste d'adresses des territoires - Bilan";
			String mailContent = "Les proclamateurs suivants ont été contactés pour obtenir les listes d'adresses de leurs territoires:\n\n";

			for (Integer pubId : listOfContactedPublisher.keySet()) {

				Publisher pub = listOfContactedPublisher.get(pubId);
				mailContent += pub.getFirstName() + " " + pub.getLastName() + "\n";
			}

			try {
				String mailReceivers = bundle.getString("mail_response_adresses");
				String[] receiversArray = mailReceivers.split(";");
				for (int i = 0; i < receiversArray.length; i++) {
					String mailReceiver = receiversArray[i];
					MailUtils.sendMail(mailSender, mailReceiver, mailSubject, mailContent);
				}

				GraphicsUtils.openInformationDialog("Email adresses", "Emails envoyé avec succès!", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		session.close();
	}

	@FXML
	private void sendMailForTerroriesReturnRequest() {
		List<Publisher> listOfPublisherWithoutMailAddresses = new ArrayList<Publisher>();
		Map<Integer, Publisher> listOfContactedPublisher = new HashMap<Integer, Publisher>();

		Session session = HibernateUtil.getSessionFactory().openSession();

		TerritoryHome territoriesDao = new TerritoryHome();
		List<Territory> territories = territoriesDao.getAllTerritories(session);
		for (Territory t : territories) {
			String status = "";
			if (t.getStatus() != null) {
				status = t.getStatus();
			}
			if (!t.getTerritoriesassignmentses().isEmpty() && !status.contains(BLACK_LISTED_TERRITORY_STATUS_STRING)) {
				for (Territoriesassignments assignMent : t.getTerritoriesassignmentses()) {
					Publisher responsibePublisher = assignMent.getPublisher();
					if (assignMent.getReturnDate() == null) {
						boolean isMailSent = false;
						if (listOfContactedPublisher.get(responsibePublisher.getPublisherId()) == null) {
							try {
								ResourceBundle bundle;
								bundle = Main.getMainBundle();
								isMailSent = MailUtils
										.sendTerroriesReturnRequestMailToDatabasePublisher(responsibePublisher);

							} catch (IOException e) {
								e.printStackTrace();
								isMailSent = false;
							}

						} else {
							isMailSent = true;
						}

						if (!isMailSent) {
							listOfPublisherWithoutMailAddresses.add(responsibePublisher);
						} else {

							listOfContactedPublisher.put(responsibePublisher.getPublisherId(), responsibePublisher);
						}
					}
				}
			}
		}

	}

	@FXML
	private void sendRemainderMailForTerroriesCoverage() {
		sendCoverageMail(true);
	}

	@FXML
	private void sendMailForTerroriesCoverage() {
		sendCoverageMail(false);
	}

	private void sendCoverageMail(boolean mailRemainder) {
		List<Publisher> listOfPublisherWithoutMailAddresses = new ArrayList<Publisher>();
		Map<Integer, Publisher> listOfContactedPublisher = new HashMap<Integer, Publisher>();

		Session session = HibernateUtil.getSessionFactory().openSession();

		TerritoryHome territoriesDao = new TerritoryHome();
		List<Territory> territories = territoriesDao.getAllTerritories(session);
		for (Territory t : territories) {
			String status = "";
			if (t.getStatus() != null) {
				status = t.getStatus();
			}
			if (!t.getTerritoriesassignmentses().isEmpty() && !status.contains(BLACK_LISTED_TERRITORY_STATUS_STRING)) {
				for (Territoriesassignments assignMent : t.getTerritoriesassignmentses()) {
					Publisher responsibePublisher = assignMent.getPublisher();
					if (assignMent.getReturnDate() == null) {
						boolean isMailSent = false;
						if (listOfContactedPublisher.get(responsibePublisher.getPublisherId()) == null) {
							try {
								ResourceBundle bundle;
								bundle = Main.getMainBundle();
								if (!mailRemainder) {
									isMailSent = MailUtils
											.sendTerritoryWorkStatusMailToDatabasePublisher(responsibePublisher);
								} else {
									isMailSent = MailUtils.sendTerritoryWorkStatusRemainderMailToDatabasePublisher(
											responsibePublisher);
								}

							} catch (IOException e) {
								e.printStackTrace();
								isMailSent = false;
							}

						} else {
							isMailSent = true;
						}

						if (!isMailSent) {
							listOfPublisherWithoutMailAddresses.add(responsibePublisher);
						} else {

							listOfContactedPublisher.put(responsibePublisher.getPublisherId(), responsibePublisher);
						}
					}
				}
			}
		}

		ResourceBundle bundle = null;
		try {
			bundle = Main.getMainBundle();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String mailSender = bundle.getString("mail_sender");
		String mailSubject = "Travail des territoires - Rappel";

		if (!listOfPublisherWithoutMailAddresses.isEmpty()) {

			String mailContent = "Les proclamateurs suivants ont des territoires, mais ne possèdent pas d'adresse email (ou l'envoie ne fonctionna pas):\n\n";
			for (Publisher p : listOfPublisherWithoutMailAddresses) {
				String territoriesInfo = "";

				for (Territoriesassignments ass : p.getTerritoriesassignmentses()) {
					Territory ter = ass.getTerritory();
					if (ass.getReturnDate() == null) {
						territoriesInfo += ter.getCode() + " - " + ter.getName() + "\n";
					}
				}
				mailContent += p.getFirstName() + " " + p.getLastName() + "\n";
				mailContent += territoriesInfo + "\n";

			}

			mailContent += "Veuille les contacter directement le plus tôt possible!!\n\n";

			try {
				String mailReceivers = bundle.getString("mail_response_adresses");
				String[] receiversArray = mailReceivers.split(";");
				for (int i = 0; i < receiversArray.length; i++) {
					String mailReceiver = receiversArray[i];
					MailUtils.sendMail(mailSender, mailReceiver, mailSubject, mailContent);
				}
				GraphicsUtils.openInformationDialog("Email couverture", "Emails envoyé avec succès!", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!listOfContactedPublisher.isEmpty()) {
			mailSubject = "Travail des territoires - Bilan";
			String mailContent = "Les proclamateurs suivants ont été contactés pour obtenir les dates de travail de leurs territoires:\n\n";

			for (Integer pubId : listOfContactedPublisher.keySet()) {
				Publisher pub = listOfContactedPublisher.get(pubId);
				mailContent += pub.getFirstName() + " " + pub.getLastName() + "\n";
			}

			try {
				String mailReceivers = bundle.getString("mail_response_adresses");
				String[] receiversArray = mailReceivers.split(";");
				for (int i = 0; i < receiversArray.length; i++) {
					String mailReceiver = receiversArray[i];
					MailUtils.sendMail(mailSender, mailReceiver, mailSubject, mailContent);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		session.close();

	}

	/**
	 * Create a pdf file with a given mail content.
	 *
	 * @param filename
	 * @param body
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void createPdf(String filename, String body) throws DocumentException, IOException {

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("mails/" + filename + ".pdf"));
		document.open();
		document.add(new Paragraph(body));
		document.close();
	}

	/**
	 * Open files system, to select card of types jpg, png
	 *
	 * @author hervengassop
	 */
	@FXML
	private void selectCard() {
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Selectionner une carte de térritoire");
		// filechooser.setInitialDirectory(value);
		ExtensionFilter png = new ExtensionFilter("PNG files", "*.png");
		ExtensionFilter jpg = new ExtensionFilter("JPG files", "*.jpg");
		filechooser.getExtensionFilters().addAll(png, jpg);
		File selectedFile = filechooser.showOpenDialog(null);
		// Set button's name ...
		carte.setText(selectedFile.getName());
		// Enable the pane for the view
		territoryViewer(selectedFile);
	}

	// Open files system, to select territory histories files.
	@FXML
	private void selectTerritoryHistories() {
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Selectionner des historiques de territoires à importer");
		// filechooser.setInitialDirectory(value);
		ExtensionFilter xml = new ExtensionFilter("XML files", "*.xml");
		filechooser.getExtensionFilters().addAll(xml);
		filechooser.setInitialDirectory(new File("."));
		List<File> selectedFiles = filechooser.showOpenMultipleDialog(null);

		String fileNames = "";

		if (selectedFiles != null) {
			for (File selectedFile : selectedFiles) {

				fileNames += selectedFile.getName() + "\n";
				try {
					TerritoryHistoryImporter.importTerritoryHistories(selectedFile.getAbsolutePath());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			GraphicsUtils.openInformationDialog("Importation d'historiques de Territoires",
					"Importation d'historiques de Territoire(s) réussies!\nLes fichiers suivant fûrent importés:\n\n"
							+ fileNames,
					null);
		}

	}

	/**
	 *
	 * @param file File to show in ImageView Viewer of a file
	 * @author hervengassop
	 */
	public void territoryViewer(File file) {
		String str = file.toURI().toString();
		territoriesPane.getSelectionModel().select(territoryMap);
		viewer.setImage(new Image(str));
	}

	@FXML
	private void changeTerritoryAssignmentDate() {

		List<Publisher> list = getPublishersList();
		int selectedIndex = updatePublisherComboBox.getSelectionModel().getSelectedIndex();

		Publisher pub = list.get(selectedIndex);

		Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
		TerritoryHome territoryDao = new TerritoryHome();
		TerritoriesassignmentsHome territoryAssignmentDao = new TerritoriesassignmentsHome();

		Session session = HibernateUtil.getSessionFactory().openSession();

		Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);

		boolean execution = false;
		if (!dbTerritory.getTerritoriesassignmentses().isEmpty()) {
			for (Territoriesassignments assign : dbTerritory.getTerritoriesassignmentses()) {
				if (assign.getReturnDate() == null) {

					Date setDate = DateUtils.asDate(datePickerTerritoryReception.getValue());

					assign.setAssignmentDate(setDate);
					territoryAssignmentDao.persist(session, assign);

					GraphicsUtils.openInformationDialog("Modification de Territoire",
							"Date d'assignation au proclamateur " + getPublisherExternalname(pub) + " modifiée", null);

					execution = true;
				}
			}
		}

		if (!execution) {

			GraphicsUtils.openErrorDialog("Modification de Territoire",
					"Impossible de modifier la date d'assignation de ce territroire !", null);

		}
		session.close();

		// getAllTerritoriesFromDatabase();

	}

	@FXML
	private void changeTerritoryReturnDate() {

		List<Publisher> list = getPublishersList();
		int selectedIndex = updatePublisherComboBox.getSelectionModel().getSelectedIndex();

		Publisher pub = list.get(selectedIndex);

		Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
		TerritoryHome territoryDao = new TerritoryHome();
		TerritoriesassignmentsHome territoryAssignmentDao = new TerritoriesassignmentsHome();

		Session session = HibernateUtil.getSessionFactory().openSession();

		Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);

		boolean execution = false;
		if (!dbTerritory.getTerritoriesassignmentses().isEmpty()) {
			for (Territoriesassignments assign : dbTerritory.getTerritoriesassignmentses()) {
				if (assign.getReturnDate() == null) {

					Date returnDate = DateUtils.asDate(datePickerTerritoryReturn.getValue());

					assign.setReturnDate(returnDate);
					territoryAssignmentDao.persist(session, assign);

					GraphicsUtils.openInformationDialog("Modification de Territoire",
							"Date de retour du territoire " + ter.getName() + " modifiée", null);

					execution = true;
				}
			}
		}

		if (!execution) {

			GraphicsUtils.openErrorDialog("Modification de Territoire",
					"Impossible de modifier la date de retour de ce territroire !", null);
		}
		session.close();

		// getAllTerritoriesFromDatabase();

	}

	@FXML
	private void assignTerritoryToPublisher() {

		List<Publisher> list = getPublishersList();
		int selectedIndex = updatePublisherComboBox.getSelectionModel().getSelectedIndex();

		Publisher pub = list.get(selectedIndex);

		Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
		TerritoryHome territoryDao = new TerritoryHome();
		PublisherHome publisherDao = new PublisherHome();
		TerritoriesassignmentsHome territoryAssignmentDao = new TerritoriesassignmentsHome();

		Session session = HibernateUtil.getSessionFactory().openSession();

		Publisher dbPublisher = (publisherDao.findByExample(session, pub)).get(0);
		Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);
		Territoriesassignments newAssignment = new Territoriesassignments(dbPublisher, dbTerritory, new Date());

		if (!dbTerritory.getTerritoriesassignmentses().isEmpty()) {
			for (Territoriesassignments assign : dbTerritory.getTerritoriesassignmentses()) {
				assign.setReturnDate(new Date());
				// territoryAssignmentDao.delete(session, assign);
			}
		}

		territoryAssignmentDao.persist(session, newAssignment);

		// Set<Territoriesassignments> assignments = new
		// HashSet<Territoriesassignments>();
		// assignments.add(newAssignment);

		// dbTerritory.getTerritoriesassignmentses().clear();
		dbTerritory.getTerritoriesassignmentses().add(newAssignment);
		territoryDao.persist(session, dbTerritory);

		session.close();

		GraphicsUtils.openInformationDialog("Modification de Territoire",
				"Assignation au proclamateur " + getPublisherExternalname(pub), null);

		enableTerritoriesTable();

	}

	@FXML
	public void abortTerritoryEdition() {
		enableTerritoriesTable();
	}

	public void disableTerritoriesTable() {
		territoriesTablePane.setDisable(true);
		saveButton.setDisable(false);
		assignButton.setDisable(false);
	}

	public void enableTerritoriesTable() {
		territoriesTablePane.setDisable(false);
		resetUpdateFields();
		getAllTerritoriesFromDatabase();
		territoriesPane.getSelectionModel().select(historyTab);

	}

	/**
	 * Insert a territory from the UI inputs.
	 */
	@FXML
	public void insertTerritory() {

		TerritoryHome territoryDao = new TerritoryHome();

		Territory ter = new Territory();
		ter.setName(nameTextField.getText());
		ter.setCode(codeTextField.getText());
		ter.setCity(regionTextField.getText());
		ter.setTerritoryType(typeComboBox.getValue());
		ter.setGroupName(groupComboBox.getValue());
		Session session = HibernateUtil.getSessionFactory().openSession();
		territoryDao.persist(session, ter);

		session.close();

		GraphicsUtils.openInformationDialog("Insertion de Territoire", "Territoire correctement inséré!", null);

		getAllTerritoriesFromDatabase();
	}

	@FXML
	public void updateTerritoryData() {
		Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
		TerritoryHome territoryDao = new TerritoryHome();

		Session session = HibernateUtil.getSessionFactory().openSession();

		Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);

		dbTerritory.setName(updateNameTextField.getText());
		dbTerritory.setCode(updateCodeTextField.getText());
		dbTerritory.setCity(updateRegionTextField.getText());
		dbTerritory.setGroupName(updateGroupComboBox.getValue());
		dbTerritory.setTerritoryType(updateTypeComboBox.getValue());

		territoryDao.persist(session, dbTerritory);

		session.close();

		GraphicsUtils.openInformationDialog("Modification de Territoire", "Territoire correctement modifié!", null);
	}

	@FXML
	public void extractSingleTerritoryContactsData() throws WriteException, IOException {

		TerritoryModel ter = territoriesTable.getSelectionModel().getSelectedItem();

		if (ter != null) {
			String selectedTerritoryCode = ter.getTerritoryCode().getValueSafe();

			String fileName = "interested-" + selectedTerritoryCode + ".xlsx";
			int territoryFilter = Integer.valueOf(selectedTerritoryCode);

			InterestedListParser.extractSpecificTerritoriesInterested(fileName, territoryFilter, null);

			GraphicsUtils.openInformationDialog("Exportation des adresses", "Adresses du Territoire  "
					+ selectedTerritoryCode + " - " + ter.getTerritoryName().getValueSafe() + " exportée!", null);

		} else {

			GraphicsUtils.openWarningDialog("Exportation des adresses", "Aucun territoire n'est selectionné!!", null);
		}

	}

	@FXML
	public void extractAllTerritoryContactsData() throws WriteException, IOException {

		String fileName = null;
		InterestedListParser.extractSpecificTerritoriesInterested(fileName, null,
				Arrays.asList("Paderborn", "Bielefeld"));

		GraphicsUtils.openInformationDialog("Exportation des adresses",
				"Adresses de tous les territoires " + " exportées!", null);

	}

	public void updatePublishersList() {

		ObservableList<String> publishersOptions = FXCollections.observableArrayList();
		List<Publisher> l = getPublishersList();
		for (Publisher p : l) {
			publishersOptions.add(getPublisherExternalname(p));
		}
		updatePublisherComboBox.setItems(publishersOptions);
		AutoCompleteComboBoxListener comboListener = new AutoCompleteComboBoxListener<>(updatePublisherComboBox);
	}

	/**
	 * Reset the all fields.
	 */
	@FXML
	public void reset() {

		nameTextField.setText("");
		codeTextField.setText("");
		regionTextField.setText("");
		typeComboBox.setValue("");
		groupComboBox.setValue("");

	}

	/**
	 * Reset the all update fields.
	 */
	@FXML
	public void resetUpdateFields() {

		updateNameTextField.setText("");
		updateCodeTextField.setText("");
		updateRegionTextField.setText("");
		updateTypeComboBox.setValue("");
		updateGroupComboBox.setValue("");
		updatePublisherComboBox.setValue("");

		saveButton.setDisable(true);
		assignButton.setDisable(true);

		datePickerTerritoryReception.setValue(null);
		datePickerTerritoryReturn.setValue(null);
		datePickerTerritoryWork.setValue(null);

	}

	/**
	 * Reset the all fields.
	 */
	@FXML
	public void fillUpdateFields(TerritoryModel model) {

		updateNameTextField.setText(model.getTerritoryName().getValue());
		updateCodeTextField.setText(model.getTerritoryCode().getValue());
		updateRegionTextField.setText(model.getTerritoryRegion().getValue());
		if (model.getTerritoryOwnerName() != null) {
			updatePublisherComboBox.setValue(model.getTerritoryOwnerName().getValue());
		}
		updateTypeComboBox.setValue(model.getTerritoryType().getValue());
		updateGroupComboBox.setValue(model.getTerritoryGroup().getValue());

		SimpleDateFormat f = new SimpleDateFormat(DateUtils.getUiDateFormat());
		try {
			if (model.getTerritoryAssignDateProperty() != null) {
				datePickerTerritoryReception
						.setValue(DateUtils.asLocalDate(f.parse(model.getTerritoryAssignDateProperty().getValue())));
			}
			if (model.getTerritoryLastWorkProperty() != null
					&& !model.getTerritoryLastWorkProperty().getValue().isEmpty()) {
				datePickerTerritoryWork
						.setValue(DateUtils.asLocalDate(f.parse(model.getTerritoryLastWorkProperty().getValue())));
			}
			if (model.getTerritoryReturnDateProperty() != null
					&& !model.getTerritoryReturnDateProperty().getValue().isEmpty()) {
				datePickerTerritoryReturn
						.setValue(DateUtils.asLocalDate(f.parse(model.getTerritoryReturnDateProperty().getValue())));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Insert a list of terrritories.
	 *
	 * @param listToInsert
	 */
	public void insertTerritories(List<Territory> listToInsert) {

		TerritoryHome territoryDao = new TerritoryHome();

		Session session = HibernateUtil.getSessionFactory().openSession();
		territoryDao.persist(session, listToInsert);
		session.close();

		GraphicsUtils.openInformationDialog("Insertion de Territoires", "Liste de territoires correctement insérée!",
				null);

		getAllTerritoriesFromDatabase();

	}

	@FXML
	public void showCurrentTerritoryHistory() {

		// Territory ter =
		// territoriesTable.getSelectionModel().getSelectedItem()
		// .getTerritory();
		// TerritoryHome territoryDao = new TerritoryHome();
		// Session session = HibernateUtil.getSessionFactory().openSession();
		//
		// Territory dbTerritory = (territoryDao.findByExample(session, ter))
		// .get(0);
		if (territoriesTable.getSelectionModel().getSelectedItem() != null) {

			List<TerritoryHistoryModel> historiesModelList = territoriesTable.getSelectionModel().getSelectedItem()
					.getHistoryData();
			Collections.sort(historiesModelList);

			territoryHistoriesData.clear();
			territoryHistoriesData.addAll(historiesModelList);

			// Add observable list data to the table
			territoryHistoryTable.setItems(territoryHistoriesData);

			TerritoryModel model = territoriesTable.getSelectionModel().getSelectedItem();
			// if (400 <
			// Integer.valueOf(model.getTerritoryCode().getValueSafe())) {
			// updateTerritoryMap("https://www.territoryassistant.com//QRTerritory/Find?QRGuid=B3586B5F-120F-4F73-A169-92E8E2CA9B8B");
			updateTerritoryMap(model.getTerritoryBounds().getValue());
			// } else {
			// //updateTerritoryMap("https://www.territoryassistant.com//QRTerritory/Find?QRGuid=E8DBAED0-0E5A-4EA7-80E3-D172457F0F8C");
			// }

		}

	}

	@FXML
	public void deactivateTerritory() {

		if (territoriesTable.getSelectionModel().getSelectedItem() != null) {
			Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
			TerritoryHome territoryDao = new TerritoryHome();
			Session session = HibernateUtil.getSessionFactory().openSession();

			Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);
			dbTerritory.setStatus(BLACK_LISTED_TERRITORY_STATUS_STRING);

			territoryDao.persist(session, dbTerritory);

			session.close();

			getAllTerritoriesFromDatabase();
		}
	}

	@FXML
	public void reactivateTerritory() {

		if (territoriesTable.getSelectionModel().getSelectedItem() != null) {
			Territory ter = territoriesTable.getSelectionModel().getSelectedItem().getTerritory();
			TerritoryHome territoryDao = new TerritoryHome();
			Session session = HibernateUtil.getSessionFactory().openSession();

			Territory dbTerritory = (territoryDao.findByExample(session, ter)).get(0);
			dbTerritory.setStatus("");

			territoryDao.persist(session, dbTerritory);

			session.close();

			getAllTerritoriesFromDatabase();
		}
	}

	/**
	 * Retrieve all territories from the database and build the UI model.
	 */
	@FXML
	public void getAllTerritoriesFromDatabase() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		TerritoryHome dao = new TerritoryHome();
		List<Territory> dbTerritories = dao.getAllTerritories(session);

		List<TerritoryModel> territoriesModelList = new ArrayList<TerritoryModel>();
		for (Territory t : dbTerritories) {
			territoriesModelList.add(new TerritoryModel(t));
		}

		session.close();
		territoriesData.clear();
		territoriesData.addAll(territoriesModelList);

		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<TerritoryModel> filteredData = new FilteredList<>(territoriesData, p -> true);
		setFilteredTerritoryModelList(filteredData);

		filterArchivedTerritoriesCheckbox.selectedProperty().addListener((observable, oldBoolean, newBoolean) -> {
			filteredTerritoryModelList.setPredicate(territoryModelInstance -> {
				return determineTerritoryVisibility(territoryModelInstance, newBoolean, null, null);
			});

			int count = territoriesTable.getItems().size();
			if (count > 0) {
				territoriesTableTitle.setText("Nombre d'éléments trouvés: " + count);
			} else {
				territoriesTableTitle.setText("Aucun élément trouvé");
			}
		});

		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredTerritoryModelList.setPredicate(territoryModelInstance -> {
				return determineTerritoryVisibility(territoryModelInstance, null, null, newValue.trim());
			});

			int count = territoriesTable.getItems().size();
			if (count > 0) {
				territoriesTableTitle.setText("Nombre d'éléments trouvés: " + count);
			} else {
				territoriesTableTitle.setText("Aucun élément trouvé");
			}

		});

		filterTerritoriesGroupsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			filteredTerritoryModelList.setPredicate(territoryModelInstance -> {
				return determineTerritoryVisibility(territoryModelInstance, null, newValue.trim(), null);
			});

			int count = territoriesTable.getItems().size();
			if (count > 0) {
				territoriesTableTitle.setText("Nombre d'éléments trouvés: " + count);
			} else {
				territoriesTableTitle.setText("Aucun élément trouvé");
			}

		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<TerritoryModel> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(territoriesTable.comparatorProperty());

		// Add observable list data to the table
		territoriesTable.setItems(sortedData);

		if (!sortedData.isEmpty()) {
			territoriesTableTitle.setText("Nombre d'éléments trouvés: " + sortedData.size());
		} else {
			territoriesTableTitle.setText("Aucun élément trouvé");
		}

		// Force the filter to be applied --> Workaround
		String tmp = filterField.getText();
		filterField.setText(tmp + " ");
		filterField.setText(tmp);

	}

	private boolean determineTerritoryVisibility(TerritoryModel territoryModelInstance, Boolean archivedBoxChecked,
			String territoryGroup, String freeText) {
		boolean archivedChecked;
		String group = null;
		String searchText = null;

		if (archivedBoxChecked != null) {
			archivedChecked = archivedBoxChecked.booleanValue();
		} else {
			archivedChecked = filterArchivedTerritoriesCheckbox.isSelected();
		}

		if (territoryGroup != null && !territoryGroup.isEmpty()) {
			group = territoryGroup;
		} else {
			group = filterTerritoriesGroupsComboBox.getValue();
		}

		if (freeText != null && !freeText.isEmpty()) {
			searchText = freeText;
		} else {
			searchText = filterField.getText();
		}

		// Check the territory against the archived property
		boolean archivedVisibilityResult = true;
		if (archivedChecked) {
			if (territoryModelInstance.getTerritoryStatusProperty().toString()
					.contains(BLACK_LISTED_TERRITORY_STATUS_STRING)) {
				archivedVisibilityResult = false;
			} else {
				archivedVisibilityResult = true;
			}
		} else {
			archivedVisibilityResult = true;
		}

		// Check the territory against the group combo box value
		boolean groupBooleanResult = publishersGroupsFiltering(territoryModelInstance, group);

		// Check the territory against the free search text input value
		boolean freeSearchBooleanResult = freeTextFiltering(territoryModelInstance, searchText);

		// Combine all visibility conditions
		return freeSearchBooleanResult && archivedVisibilityResult && groupBooleanResult;

	}

	private boolean publishersGroupsFiltering(TerritoryModel territoryModelInstance, String filterGroup) {

		// If filter text is empty, display all persons.
		if (filterGroup == null || filterGroup.isEmpty()) {
			return true;
		}

		// Compare first name and last name of every person
		// with filter text.
		String lowerCaseFilter = filterGroup.toLowerCase();

		if (territoryModelInstance.getTerritoryGroup().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true; // Filter matches territory group.
		}

		return false; // Does not match.
	}

	private boolean freeTextFiltering(TerritoryModel territoryModelInstance, String filterText) {

		// If filter text is empty, display all persons.
		if (filterText == null || filterText.isEmpty()) {
			return true;
		}

		// Compare first name and last name of every person
		// with filter text.
		String lowerCaseFilter = filterText.toLowerCase();

		if (territoryModelInstance.getTerritoryName().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true; // Filter matches territory name.
		} else if (territoryModelInstance.getTerritoryCode().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true; // Filter matches territory code.
		} else if (territoryModelInstance.getTerritoryOwnerName() != null
				&& territoryModelInstance.getTerritoryOwnerName().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true; // Filter matches territory owner
							// name or firstname.
		} else if (territoryModelInstance.getTerritoryRegion().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true; // Filter matches territory region.
		} else if (territoryModelInstance.getTerritoryType().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true; // Filter matches territory type.
		} else if (territoryModelInstance.getTerritoryStatusProperty().getValue().toLowerCase()
				.contains(lowerCaseFilter)) {
			return true;
		} else if (territoryModelInstance.getTerritoryGroup().getValue().toLowerCase().contains(lowerCaseFilter)) {
			return true;
		}
		return false; // Does not match.
	}

	/**
	 * This method retrieves all publishers a selected congregation whom we want to
	 * send an email
	 *
	 * @author hervengassop
	 */
	@FXML
	public void getAllPublisherReceiverInCongregation() {
		Session sess = HibernateUtil.getSessionFactory().openSession();
		CongregationHome dao = new CongregationHome();
		// First, collecting all congregations existing in the database
		List<Congregation> dbCongregation = dao.getAllCongregations(sess);
		if (dbCongregation != null) {
			for (Congregation c : dbCongregation) {
				// Collecting the Congregations'name in a observable list
				congregationName.add(c.getCongregationName());
			}
		}
		// Setting the items of the ComboBox with the collected
		// congregations'name
		congregationItemComboBox.setItems(congregationName);
		int selectedIndex = congregationItemComboBox.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0)
			selectedIndex = 0;
		// Setting the value of the ComboBox to the selected item
		congregationItemComboBox.setValue(congregationName.get(selectedIndex));

		Congregation selectedCongregation = dbCongregation.get(selectedIndex);

		if (selectedCongregation != null) {
			// Get all publishers of the selected congregation
			getDataFromSelectedCongregation(sess, selectedCongregation);
		}

		sess.close();
	}

	/**
	 * Display the wanted informations
	 *
	 * @author hervengassop
	 */
	public void getDataFromSelectedCongregation(Session sess, Congregation cong) {
		Set<Publisher> dbPublishers = cong.getPublishers();
		List<PublisherModel> publishersList = new ArrayList<>();
		for (Publisher p : dbPublishers) {
			publishersList.add(new PublisherModel(p));
		}
		publisherReceiver.clear();
		publisherReceiver.addAll(publishersList);

		// 1. Wrap the ObservableList in a FilteredList (initially display all
		// data).
		FilteredList<PublisherModel> filteredPublisher = new FilteredList<>(publisherReceiver, p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		publisherSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredPublisher.setPredicate(publisherModelInstance -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person
				// with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (publisherModelInstance.getPublisherFirstNameProperty().getValue().toLowerCase()
						.contains(lowerCaseFilter)) {
					return true; // Filter matches publisher first
									// name.
				} else if (publisherModelInstance.getPublisherLastNameProperty().getValue().toLowerCase()
						.contains(lowerCaseFilter)) {
					return true; // Filter matches publisher last
									// name
				} else if (publisherModelInstance.getPublisherEmailProperty().getValue().toLowerCase()
						.contains(lowerCaseFilter)) {
					return true; // Filter matches publisher email
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<PublisherModel> sortedData = new SortedList<>(filteredPublisher);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(MailReceiversTable.comparatorProperty());

		// Add observable list data to the table
		MailReceiversTable.setItems(sortedData);

	}

	@FXML
	public void showDetailledTerritoriesStatistics() {
		territoriesReportsPane.getChildren().clear();
		territoriesReportsPane.getChildren().add(singleGroupsReportsBorderPane);
	}

	@FXML
	public void showAllTerritoriesStatistics() {
		territoriesReportsPane.getChildren().clear();
		territoriesReportsPane.getChildren().add(totalReportsBorderPane);

	}

	@FXML
	public void showTerritoriesDetailledStatisticsTables() {
		territoriesReportsPane.getChildren().clear();
		territoriesReportsPane.getChildren().add(numbersReportsBorderPane);
	}

	public void restoreStatisticsView() {
		try {
			if (buttonReportTotal.isSelected()) {
				showAllTerritoriesStatistics();
			} else if (buttonReportDetails.isSelected()) {
				showDetailledTerritoriesStatistics();
			} else if (buttonReportNumbers.isSelected()) {
				showTerritoriesDetailledStatisticsTables();
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAllTerritoriesStatistics();
		}
	}

	@FXML
	public void recalculateReportsData() {
		resetReportsData();

		buildReportsStatistics();

		displayReportsGraphics();

	}

	private void displayReportsGraphics() {
		int lastSixMonths = 0;
		int lastTwelveMonths = 0;
		int others = 0;
		int notAssigned = 0;
		int neverWorked = 0;

		for (String groupName : totalReportsData.keySet()) {
			// ...
			TerritoryReportData data = totalReportsData.get(groupName);
			others += data.getLongerThanAYearWork().size();
			lastSixMonths += data.getLastSixMonthsWork().size();
			lastTwelveMonths += data.getLastTwelveMonthsWork().size();
			notAssigned += data.getNotAssigned().size();
			neverWorked += data.getNeverWorked().size();
		}
		pieChartDataItems = FXCollections
				.observableArrayList(new PieChart.Data("Travaillés il y a plus d'un an:" + " " + others, others),
						new PieChart.Data("Travaillés les 6 derniers mois:" + " " + lastSixMonths, lastSixMonths),
						new PieChart.Data("Travaillés les 12 derniers mois:" + " " + lastTwelveMonths,
								lastTwelveMonths),
						new PieChart.Data("Non attribués:" + " " + notAssigned, notAssigned),
						new PieChart.Data("Jamais travaillés:" + " " + neverWorked, neverWorked));

		totalReportsPieChart.setData(pieChartDataItems);

		// Detailled reports
		detailledReportsBarChart.getData().clear();

		for (DetailledGroupTerritoryReportData groupDetails : detailledReportsData) {
			XYChart.Series series = new XYChart.Series();
			series.setName(groupDetails.getGroupName());
			XYChart.Data<String, Integer> data1 = new XYChart.Data("Travaillés les 6 derniers mois",
					groupDetails.getLastSixMonthWorkedTerritoriesCount());
			XYChart.Data<String, Integer> data2 = new XYChart.Data("Travailés la dernière année",
					groupDetails.getLastTwelveMonthWorkedTerritoriesCount());
			XYChart.Data<String, Integer> data3 = new XYChart.Data("Travaillés il y a plus d'un an",
					groupDetails.getLongerThanAYearWorkedTerritoriesCount());
			XYChart.Data<String, Integer> data4 = new XYChart.Data("Non attribués",
					groupDetails.getNotAssignedTerritoriesCount());
			XYChart.Data<String, Integer> data5 = new XYChart.Data("Jamais travaillés",
					groupDetails.getNeverWorkedTerritoriesCount());

			List<XYChart.Data> dataList = Arrays.asList(data1, data2, data3, data4, data5);
			for (XYChart.Data currentData : dataList) {
				currentData.nodeProperty().addListener(new ChangeListener<Node>() {
					@Override
					public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
						if (node != null) {
							// setNodeStyle(currentData);
							displayLabelForData(currentData);
						}
					}
				});
				series.getData().add(currentData);
			}

			detailledReportsBarChart.getData().add(series);

			detailledReportsBarChart.getYAxis().setMaxHeight(100);

		}
	}

	@FXML
	private void saveAllReportsImages() throws DocumentException, MalformedURLException, IOException {

		showDetailledTerritoriesStatistics();
		GraphicsUtils.exportFxChartAsImage(detailledReportsBarChart, "detailledChart", "png");

		showAllTerritoriesStatistics();
		GraphicsUtils.exportFxChartAsImage(totalReportsPieChart, "totalChart", "png");

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("sampleReport.pdf"));
		HeaderFooterPageEvent2 event = new HeaderFooterPageEvent2();
		writer.setPageEvent(event);

		document.open();
		document.setPageSize(PageSize.A4);
		com.itextpdf.text.Image img1 = com.itextpdf.text.Image.getInstance("detailledChart.png");
		// img1.scaleAbsolute(50f, 50f);
		img1.scaleToFit(400f, 400f);
		document.add(img1);
		com.itextpdf.text.Image img2 = com.itextpdf.text.Image.getInstance("totalChart.png");
		// img2.scaleToFit(100f, 100f);
		img2.scaleToFit(400f, 400f);
		document.add(img2);

		Paragraph paragraph = new Paragraph();
		paragraph.add(" ");
		document.add(paragraph);
		document.add(paragraph);

		File captureFile = new File("cap.png");
		WritableImage image = territoriesReportHtmlViewer.snapshot(null, null);
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
		final ImageView imageView = new ImageView();
		try {
			ImageIO.write(bufferedImage, "png", captureFile);
			imageView.setImage(new Image(captureFile.toURI().toURL().toExternalForm()));
			System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}

		com.itextpdf.text.Image img3 = com.itextpdf.text.Image.getInstance("cap.png");
		// img1.scaleAbsolute(50f, 50f);
		img3.scaleToFit(400f, 400f);
		document.add(img3);

		document.newPage();
		// text.setFont((BaseFont) new Font(FontFamily.HELVETICA, 10,
		// Font.BOLDITALIC));

		Paragraph paragraph1 = new Paragraph("Liste de territoires jamais travaillés",
				new Font(FontFamily.HELVETICA, 10, Font.BOLDITALIC));

		document.add(paragraph1);
		com.itextpdf.text.List list = new com.itextpdf.text.List(true, false, 10);
		for (String group : getTotalReportsData().keySet()) {
			TerritoryReportData groupReport = getTotalReportsData().get(group);
			for (TerritoryModel territory : groupReport.getNeverWorked()) {
				list.add(new ListItem("         " + territory.getTerritoryCode().getValue() + "  #  "
						+ territory.getTerritoryName().getValue() + "  (Groupe: " + group + ")"));
			}
		}
		document.add(list);
		document.close();

		restoreStatisticsView();
	}

	public void buildReportsStatistics() {

		// Build overall reports
		for (TerritoryModel ter : territoriesData) {
			String group = ter.getTerritory().getGroupName();
			List<TerritoryHistoryModel> histories = ter.getHistoryData();
			Collections.sort(histories);
			TerritoryReportData data = totalReportsData.get(group);
			if (data != null && !ter.getTerritoryStatusProperty().toString().contains("Archivé")) {
				if (!ter.getTerritoryStatusProperty().toString().contains("Travaillé")) {
					data.getNotAssigned().add(ter);
					// System.out.println("Territory not assigned: " +
					// ter.getTerritory().getCode());
				}
				if (histories.isEmpty()) {
					// data.getLongerThanAYearWork().add(ter);
					data.getNeverWorked().add(ter);
				} else {
					Date lastWorkDate = histories.get(0).getHistoryActionDate();
					Date referenceDate = new Date();
					if (datePickerReportReference.getValue() != null) {
						referenceDate = DateUtils.asDate(datePickerReportReference.getValue());
					}

					int diff = DateUtils.monthDifference(lastWorkDate, referenceDate);
					if (diff > 12) {
						data.getLongerThanAYearWork().add(ter);
						// System.out.println("Territory worked since more than
						// 1 year: " + ter.getTerritory().getCode());
					} else if (diff >= 0 && diff <= 6) {
						data.getLastSixMonthsWork().add(ter);
						// System.out.println("Territory worked in the last 6
						// months: " + ter.getTerritory().getCode());
						// data.getLastTwelveMonthsWork().add(ter);
						// System.out.println("Territory worked in the last 12
						// months: " + ter.getTerritory().getCode());
					} else if (diff > 6 && diff <= 12) {
						data.getLastTwelveMonthsWork().add(ter);
						// System.out.println("Territory worked in the last 12
						// months: " + ter.getTerritory().getCode());
					}
				}
			} else {
				if (ter.getTerritoryStatusProperty().toString().contains("Archivé")) {
					System.out.println("Territory archived: " + ter.getTerritory().getCode());
				} else if (data == null) {
					System.out.println("Territory without group: " + ter.getTerritory().getCode());
				}
			}
		}

		// build detailled reports
		for (String groupName : totalReportsData.keySet()) {
			DetailledGroupTerritoryReportData detailledGroupReports = new DetailledGroupTerritoryReportData();
			TypedGroupTerritoryReportData studentsTerritories = new TypedGroupTerritoryReportData("étudiants",
					totalReportsData.get(groupName));
			TypedGroupTerritoryReportData refugeesTerritories = new TypedGroupTerritoryReportData("réfugiés",
					totalReportsData.get(groupName));
			TypedGroupTerritoryReportData researchTerritories = new TypedGroupTerritoryReportData("Normal",
					totalReportsData.get(groupName));

			detailledGroupReports.setGroupName(groupName);
			detailledGroupReports.setStudentsTerritoriesReports(studentsTerritories);
			detailledGroupReports.setRefugeesTerritoriesReports(refugeesTerritories);
			detailledGroupReports.setResearchTerritoriesReports(researchTerritories);

			detailledReportsData.add(detailledGroupReports);
		}

	}

	@FXML
	public void createTerritoryAssignmentsReportOld() throws DocumentException, FileNotFoundException {
		int count = 0;
		int currentX = 0;
		int currentY;

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testPdfNew.pdf"));
		document.open();
		document.setPageSize(PageSize.A4);

		// Page Headers
		PdfPTable pageHeader = new PdfPTable(3);
		pageHeader.setWidthPercentage(100);
		Font pageHeaderSmallFont = new Font(FontFamily.HELVETICA, 6);
		Font pageHeaderFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(
				"Date: " + new SimpleDateFormat(DateUtils.getUiDateFormat()).format(new Date()), pageHeaderSmallFont));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		pageHeader.addCell(cell);

		cell = new PdfPCell(new Phrase("Gebietszuteilungskarte", pageHeaderFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		pageHeader.addCell(cell);

		cell = new PdfPCell(new Phrase("Page: " + (count + 1), pageHeaderSmallFont));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pageHeader.addCell(cell);

		document.add(pageHeader);

		PdfPTable pageHeader2 = new PdfPTable(6);
		pageHeader2.setWidthPercentage(100);
		for (int i = 0; i < 6; i++) {
			PdfPCell cell2 = new PdfPCell(new Phrase("N°:", pageHeaderSmallFont));
			cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell2.setBorder(Rectangle.NO_BORDER);
			pageHeader2.addCell(cell2);
		}
		pageHeader2.setSpacingAfter(5);
		document.add(pageHeader2);

		PdfPTable tableContents = new PdfPTable(6);
		tableContents.setWidthPercentage(100);

		// Build overall reports
		for (TerritoryModel ter : territoriesData) {

			List<TerritoryHistoryModel> histories = ter.getHistoryData();
			Collections.sort(histories);
			if (!ter.getTerritoryStatusProperty().toString().contains("Archivé") && count < 6) {
				PdfPTable outerTable = new PdfPTable(1);
				float outerLength = 100 / 6;
				outerTable.setWidthPercentage(outerLength);
				if (histories.isEmpty()) {

				} else {
					int histCount = histories.size();
					int maxCount = 15;

					int addRows = maxCount - count;

					for (TerritoryHistoryModel hist : histories) {
						// add territory cover dates
						PdfPTable innerTable = new PdfPTable(4);
						PdfPCell cell3 = new PdfPCell(
								new Phrase(ter.getTerritoryCode().getValueSafe(), pageHeaderSmallFont));
						cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
						innerTable.addCell(cell3);
						String owner = "";
						if (ter.getTerritoryOwnerName() != null) {
							owner = ter.getTerritoryOwnerName().getValueSafe();
						}
						cell3 = new PdfPCell(new Phrase(owner, pageHeaderSmallFont));
						cell3.setColspan(3);
						innerTable.addCell(cell3);

						String assignmentDate = "";
						if (ter.getTerritoryAssignDateProperty() != null) {
							assignmentDate = ter.getTerritoryAssignDateProperty().getValueSafe();
						}
						cell3 = new PdfPCell(new Phrase(assignmentDate, pageHeaderSmallFont));
						cell3.setColspan(2);
						cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
						innerTable.addCell(cell3);
						cell3 = new PdfPCell(
								new Phrase(hist.getHistoryActionDateProperty().getValueSafe(), pageHeaderSmallFont));
						cell3.setColspan(2);
						cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
						innerTable.addCell(cell3);
						// cell3.setBorder(Rectangle.NO_BORDER);
						PdfPCell innerCell = new PdfPCell(innerTable);
						innerCell.setBorder(Rectangle.NO_BORDER);
						outerTable.addCell(innerCell);

					}

					// Create empty rows to keep the same columns height by
					// filling spaces
					if (addRows > 0) {
						for (int i = 0; i < addRows; i++) {
							PdfPTable innerTable = new PdfPTable(1);
							PdfPCell cell3 = new PdfPCell(new Phrase("", pageHeaderSmallFont));
							cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell3.setBorder(Rectangle.NO_BORDER);
							innerTable.addCell(cell3);
							cell3 = new PdfPCell(new Phrase("", pageHeaderSmallFont));
							cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell3.setBorder(Rectangle.NO_BORDER);
							innerTable.addCell(cell3);

							PdfPCell innerCell = new PdfPCell(innerTable);
							innerCell.setBorder(Rectangle.NO_BORDER);
							outerTable.addCell(innerCell);
						}
					}
				}
				count++;
				PdfPCell cell4 = new PdfPCell(outerTable);
				cell4.setBorder(Rectangle.NO_BORDER);
				tableContents.addCell(cell4);
			}
		}

		document.add(tableContents);
		document.close();

	}

	@FXML
	public void createTerritoryAssignmentsReport() throws DocumentException, FileNotFoundException {
		int pageCount = 0;

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("bilanTerritoires.pdf"));
		HeaderFooterPageEvent event = new HeaderFooterPageEvent();
		writer.setPageEvent(event);
		document.open();
		document.setPageSize(PageSize.A4);

		List<TerritoryModel> territoriesForPagePrint = new ArrayList<TerritoryModel>();

		// Build overall reports
		for (TerritoryModel ter : territoriesData) {
			if (!ter.getTerritoryStatusProperty().toString().contains(BLACK_LISTED_TERRITORY_STATUS_STRING)) {
				if (territoriesForPagePrint.size() < 6) {
					territoriesForPagePrint.add(ter);
				} else {
					addReportPage(document, territoriesForPagePrint, pageCount + 1);
					document.newPage();
					pageCount++;
					territoriesForPagePrint.clear();
				}
			}
		}

		document.close();

	}

	/*
	 *
	 */
	private void addReportPage(Document pdfDocument, List<TerritoryModel> territories, int pageCount)
			throws DocumentException {

		// Page Headers
		PdfPTable pageHeader = new PdfPTable(3);
		pageHeader.setWidthPercentage(100);
		Font pageHeaderSmallFont = new Font(FontFamily.HELVETICA, 6);
		Font pageHeaderSmallFontRed = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.RED);
		Font pageHeaderFont = new Font(FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE);
		PdfPCell cell = new PdfPCell(new Phrase(
				"Date: " + new SimpleDateFormat(DateUtils.getUiDateFormat()).format(new Date()), pageHeaderSmallFont));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		pageHeader.addCell(cell);

		// cell = new PdfPCell(new Phrase("Gebietszuteilungskarte",
		// pageHeaderFont));
		cell = new PdfPCell(new Phrase("Relevé des attributions de territoire", pageHeaderFont));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		pageHeader.addCell(cell);

		cell = new PdfPCell(new Phrase("Page: " + pageCount, pageHeaderSmallFont));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pageHeader.addCell(cell);

		pdfDocument.add(pageHeader);

		PdfPTable pageHeader2 = new PdfPTable(6);
		pageHeader2.setWidthPercentage(100);
		for (int i = 0; i < 6; i++) {
			PdfPCell cell2 = new PdfPCell(new Phrase("N°:", pageHeaderSmallFont));
			cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell2.setBorder(Rectangle.NO_BORDER);
			pageHeader2.addCell(cell2);
		}
		pageHeader2.setSpacingAfter(5);
		pdfDocument.add(pageHeader2);

		PdfPTable tableContents = new PdfPTable(6);
		tableContents.setWidthPercentage(100);

		int count = 0;
		int maxCount = 15;

		// Build overall reports
		for (TerritoryModel ter : territories) {

			List<TerritoryHistoryModel> histories = ter.getHistoryData();
			Collections.sort(histories, TerritoryHistoryModel.ascendingComparator);
			int addRows = 0;

			if (!ter.getTerritoryStatusProperty().toString().contains(BLACK_LISTED_TERRITORY_STATUS_STRING)) {
				PdfPTable outerTable = new PdfPTable(1);
				float outerLength = 100 / 6;
				outerTable.setWidthPercentage(outerLength);
				int histCount = histories.size();

				if (histories.isEmpty()) {

					histCount = 1;
					addRows = Math.max(0, (maxCount - histCount));

					// For empty histories add only one visible empty record
					PdfPTable innerTable = new PdfPTable(4);

					// Territory code
					PdfPCell cell3 = new PdfPCell(
							new Phrase(ter.getTerritoryCode().getValueSafe(), pageHeaderSmallFont));
					cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
					innerTable.addCell(cell3);
					// Territory owner
					String owner = "";
					if (ter.getTerritoryOwnerName() != null) {
						owner = ter.getTerritoryOwnerName().getValueSafe();
					}
					cell3 = new PdfPCell(new Phrase(owner, pageHeaderSmallFont));
					cell3.setColspan(3);
					cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
					innerTable.addCell(cell3);

					// Empty history
					cell3 = new PdfPCell(new Phrase(" ", pageHeaderSmallFont));
					cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell3.setColspan(2);
					innerTable.addCell(cell3);

					cell3 = new PdfPCell(new Phrase(" ", pageHeaderSmallFont));
					cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell3.setColspan(2);
					innerTable.addCell(cell3);

					PdfPCell innerCell = new PdfPCell(innerTable);
					innerCell.setBorder(Rectangle.NO_BORDER);
					outerTable.addCell(innerCell);

				} else {

					addRows = Math.max(0, (maxCount - histCount));

					int index = 0;
					// add territory cover dates
					for (TerritoryHistoryModel hist : histories) {

						if (count < maxCount) {
							// Territory code
							PdfPTable innerTable = new PdfPTable(4);
							PdfPCell cell3 = new PdfPCell(
									new Phrase(ter.getTerritoryCode().getValueSafe(), pageHeaderSmallFont));
							cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
							innerTable.addCell(cell3);

							// Territory owner
							String owner = "";
							if (ter.getTerritoryOwnerName() != null) {
								owner = ter.getTerritoryOwnerName().getValueSafe();
							}
							cell3 = new PdfPCell(new Phrase(owner, pageHeaderSmallFont));
							cell3.setColspan(3);
							cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
							innerTable.addCell(cell3);

							// Territory assignment date
							String assignmentDate = "";
							if (ter.getTerritoryAssignDateProperty() != null) {
								assignmentDate = ter.getTerritoryAssignDateProperty().getValueSafe();
							}

							Font historyFont = pageHeaderSmallFont;
							if (index == (histCount - 1)) {
								// Red font for the last coverage date
								historyFont = pageHeaderSmallFontRed;
							}
							cell3 = new PdfPCell(new Phrase(assignmentDate, pageHeaderSmallFont));
							cell3.setColspan(2);
							cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
							innerTable.addCell(cell3);

							// Territory coverage date
							cell3 = new PdfPCell(
									new Phrase(hist.getHistoryActionDateProperty().getValueSafe(), historyFont));
							cell3.setColspan(2);
							cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
							innerTable.addCell(cell3);
							// cell3.setBorder(Rectangle.NO_BORDER);
							PdfPCell innerCell = new PdfPCell(innerTable);
							innerCell.setBorder(Rectangle.NO_BORDER);
							outerTable.addCell(innerCell);
						}
						index++;
					}

				}
				// Create empty rows to keep the same columns height by filling
				// spaces
				if (addRows > 0) {
					for (int i = 0; i < addRows; i++) {
						PdfPTable innerTable = new PdfPTable(1);
						PdfPCell cell3 = new PdfPCell(new Phrase("", pageHeaderSmallFont));
						cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell3.setBorder(Rectangle.NO_BORDER);
						innerTable.addCell(cell3);
						cell3 = new PdfPCell(new Phrase("", pageHeaderSmallFont));
						cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell3.setBorder(Rectangle.NO_BORDER);
						innerTable.addCell(cell3);

						PdfPCell innerCell = new PdfPCell(innerTable);
						innerCell.setBorder(Rectangle.NO_BORDER);
						outerTable.addCell(innerCell);
					}
				}

				count++;
				PdfPCell cell4 = new PdfPCell(outerTable);
				cell4.setBorder(Rectangle.NO_BORDER);
				tableContents.addCell(cell4);
			}
		}

		pdfDocument.add(tableContents);
	}

	private void createTestTable() throws FileNotFoundException, DocumentException {
		Rectangle small = new Rectangle(290, 100);
		Font smallfont = new Font(FontFamily.HELVETICA, 10);
		Document document = new Document(small, 5, 5, 5, 5);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("testPdf.pdf"));
		document.open();
		PdfPTable table = new PdfPTable(2);
		table.setTotalWidth(new float[] { 160, 120 });
		table.setLockedWidth(true);
		PdfContentByte cb = writer.getDirectContent();
		// first row
		PdfPCell cell = new PdfPCell(new Phrase("Some text here"));
		cell.setFixedHeight(30);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		table.addCell(cell);
		// second row
		cell = new PdfPCell(new Phrase("Some more text", smallfont));
		cell.setFixedHeight(30);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		Barcode128 code128 = new Barcode128();
		code128.setCode("14785236987541");
		code128.setCodeType(Barcode128.CODE128);
		com.itextpdf.text.Image code128Image = code128.createImageWithBarcode(cb, null, null);
		cell = new PdfPCell(code128Image, true);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setFixedHeight(30);
		table.addCell(cell);
		// third row
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("and something else here", smallfont));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);

		// document.newPage();
		document.add(table);
		document.close();
	}

	/**
	 * Change color of bar if value of i is <5 then red, if >5 then green if i>8
	 * then blue
	 */
	private void setNodeStyle(XYChart.Data<String, Number> data) {
		Node node = data.getNode();
		// if (data.getYValue().intValue() > 8) {
		// node.setStyle("-fx-bar-fill: -fx-exceeded;");
		// } else if (data.getYValue().intValue() > 5) {
		// node.setStyle("-fx-bar-fill: -fx-achieved;");
		// } else {
		// node.setStyle("-fx-bar-fill: -fx-not-achieved;");
		// }
		// node.setStyle("-fx-bar-fill: -fx-not-achieved;");
	}

	private void displayLabelForData(XYChart.Data<String, Number> data) {
		final Node node = data.getNode();
		final Text dataText = new Text(data.getYValue() + "");
		node.parentProperty().addListener(new ChangeListener<Parent>() {
			@Override
			public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
				Group parentGroup = (Group) parent;
				if (parent != null && parentGroup.getChildren() != null) {
					parentGroup.getChildren().add(dataText);
				}
			}
		});

		node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
				dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
				dataText.setLayoutY(Math.round(bounds.getMinY() - dataText.prefHeight(-1) * 0.5));
			}
		});
	}

	@FXML
	public void hideArchivedTerritories() {

	}

	@FXML
	public void filterTerritoriesListByGroupName() {

	}

	/**
	 * @return the reportsData
	 */
	public Map<String, TerritoryReportData> getTotalReportsData() {
		return totalReportsData;
	}

	/**
	 * Get the current used congregation.
	 *
	 * @return
	 */
	public Congregation getCurrentCongregation() {
		return getRootParentController().getPublishersTabPageController().getSelectedCongregation();
	}

	public List<Publisher> getPublishersList() {
		List<Publisher> resulList;
		PublisherNameComparator comp = new PublisherNameComparator();
		if (getCurrentCongregation() != null) {
			Set<Publisher> dbPublishers = getCurrentCongregation().getPublishers(); // dao.getAllPublishersInCongregation(selectedCongregation.session);
			resulList = new ArrayList<Publisher>(dbPublishers);
		} else {
			resulList = new ArrayList<Publisher>();
		}

		Collections.sort(resulList, comp);

		return resulList;
	}

	public String getPublisherExternalname(Publisher publisherInstance) {
		return publisherInstance.getLastName() + " " + publisherInstance.getFirstName();
	}

	/*
	 * public ImageView getRout(String ort, String region) throws
	 * MalformedURLException, IOException { final String URL =
	 * "http://maps.google.com/maps/api/staticmaps?center="+ort+ ","+region+
	 * "&zoom = 8&size=512*512&maptype=roadmap"; URLConnection conn = new
	 * URL(URL).openConnection(); InputStream is = conn.getInputStream(); byte
	 * bytes[] = new byte[conn.getContentLength()]; is.read(); is.close();
	 *
	 * return null; }
	 */
	/**
	 * @return the rootParentController
	 */
	public RootController getRootParentController() {
		return rootParentController;
	}

	/**
	 * @param rootParentController the rootParentController to set
	 */
	public void setRootParentController(RootController rootParentController) {
		this.rootParentController = rootParentController;
	}

	/**
	 * @return the filteredTerritoryModelList
	 */
	public FilteredList<TerritoryModel> getFilteredTerritoryModelList() {
		return filteredTerritoryModelList;
	}

	/**
	 * @param filteredTerritoryModelList the filteredTerritoryModelList to set
	 */
	public void setFilteredTerritoryModelList(FilteredList<TerritoryModel> filteredTerritoryModelList) {
		this.filteredTerritoryModelList = filteredTerritoryModelList;
	}

}
