package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class PartyViewController {

	private Party party;
	private Term term;
	private PartyFxModel partyFxModel;
	private ProgramFxModel programFxModel;
	private ObservableList<Term> termsModel;
	private ObservableList<Item> itemsModel;
	private ObservableList<Candidate> candidatesModel;
	private Term termWatched;
	private Stage stage;

	@FXML
	private ListView<Candidate> candidatesListView;

	@FXML
	private TableView<Item> itemsTableView;

	@FXML
	private Label namePartyLabel;

	@FXML
	private TextArea partyInfoTextArea;

	@FXML
	private ComboBox<Term> partyTermComboBox;

	@FXML
	private Label programNameLabel;

	public PartyViewController(Party party) {
		this.party = party;
		partyFxModel = new PartyFxModel(party);
	}

	@FXML
	void initialize() {
		namePartyLabel.setText(party.getName());
		partyInfoTextArea.textProperty().bindBidirectional(partyFxModel.getInfoProperty());

		List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
		termsModel = FXCollections.observableArrayList(terms);
		partyTermComboBox.setItems(termsModel);
		partyTermComboBox.getSelectionModel().selectFirst();

		// TUUUUUUUUUUUUUU pri smere v druho terme sa zobrazuje dajaky drist idk preco
		List<Program> list = DaoFactory.INSTANCE.getProgramDao()
				.getByTermParty(partyTermComboBox.getSelectionModel().getSelectedItem(), party);
		if (list.size() != 0) {
			programNameLabel.setText(list.get(0).getName());// tu get name
		} else {
			programNameLabel.setText("žiaden program");
		}

		if (party != null) {
			List<Candidate> list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party,
					partyTermComboBox.getSelectionModel().getSelectedItem());
			candidatesModel = FXCollections.observableArrayList(list_c);
		} else {
			candidatesModel = partyFxModel.getCandidatesModel();
		}

		candidatesListView.setItems(candidatesModel);
		partyTermComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Term>() {

			@Override
			public void changed(ObservableValue<? extends Term> observable, Term oldValue, Term newValue) {
				if (newValue != null) {
					termWatched = partyTermComboBox.getSelectionModel().getSelectedItem();
					updateCandidatesListView(termWatched);
					updateProgramNameLabel(termWatched);
					updateItemsTableView(termWatched);
				}
			}

		});

		TableColumn<Item, String> categoryColumn = new TableColumn<>("Kategória");
		categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
		itemsTableView.getColumns().add(categoryColumn);

		TableColumn<Item, String> itemColumn = new TableColumn<>("Bod");
		itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("info"));
		itemsTableView.getColumns().add(itemColumn);

		Term term = partyTermComboBox.getSelectionModel().getSelectedItem();
		List<Item> list_i = new ArrayList<>();
		if (party != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(term, party);
		}
		itemsModel = FXCollections.observableArrayList(list_i);
		itemsTableView.setItems(itemsModel);

	}

	private void updateItemsTableView(Term termWatched) {
		List<Item> list_i = new ArrayList<>();
		list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(termWatched, party);
		itemsModel.setAll(list_i);

	}

	private void updateProgramNameLabel(Term termWatched) {
		List<Program> list_p = new ArrayList<>();
		list_p = DaoFactory.INSTANCE.getProgramDao().getByTermParty(termWatched, party);
		if (list_p.size() != 0) {
			programFxModel = new ProgramFxModel(list_p.get(0), party);
		} else {
			programFxModel = new ProgramFxModel(party, termWatched);
		}
		programNameLabel.textProperty().bindBidirectional(programFxModel.getNameProperty());
	}

	private void updateCandidatesListView(Term termWatched) {
		List<Candidate> list_c = new ArrayList<>();
		list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party, termWatched);
		candidatesModel = FXCollections.observableArrayList(list_c);
		candidatesListView.setItems(candidatesModel);
	}

	@FXML
	void candidateInfoButtonClick(ActionEvent event) {

		Candidate candidate = candidatesListView.getSelectionModel().getSelectedItem();
		if (candidate != null) {
			CandidateViewController controller = new CandidateViewController(candidate, party);
			showCandidateView(controller, "Náhľad kandidáta");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiaden kandidát nie je vybraný, vyberte prosím kandidáta");
			alert.show();
			return;
		}

	}

	void showCandidateView(CandidateViewController controller, String name) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("candidateView.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();

			String css = this.getClass().getResource("votuj.css").toExternalForm();
			scene.getStylesheets().add(css);
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);

			stage.setTitle(name);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
