package sk.upjs.ics.votuj;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class PartyEditController {

	private Party party;
	private PartyFxModel partyFxModel;
	// private CandidateFxModel candidateFxModel;
	private ObservableList<Candidate> candidatesModel;
	private ObservableList<Program> programsModel;
	private ObservableList<Item> itemsModel;
	private ObservableList<Term> termsModel;
	private Term termWatched;
	private Stage stage;

	@FXML
	private ListView<Candidate> candidatesListView;

	@FXML
	private TextField partyInfoTextField;

	@FXML
	private TextField partyNameTextField;

	@FXML
	private TableView<Item> itemsTableView;

	@FXML
	private ListView<Program> programsListView;

	@FXML
	private ComboBox<Term> termsComboBox;

	public PartyEditController() {
		partyFxModel = new PartyFxModel();
	}

	public PartyEditController(Party party) {
		this.party = party;
		partyFxModel = new PartyFxModel(party);
	}

	@FXML
	void initialize() {
		partyInfoTextField.textProperty().bindBidirectional(partyFxModel.getInfoProperty());
		partyNameTextField.textProperty().bindBidirectional(partyFxModel.getNameProperty());

		List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
		termsModel = FXCollections.observableArrayList(terms);
		termsComboBox.setItems(termsModel);

		termsComboBox.getSelectionModel().selectFirst();

		// počiatočne ak nič nie je vybrane --> idk ci to treba este uvidim
		programsModel = partyFxModel.getProgramsModel();
		programsListView.setItems(programsModel);

		if (party != null) {
			List<Candidate> list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party,
					termsComboBox.getSelectionModel().getSelectedItem());
			candidatesModel = FXCollections.observableArrayList(list_c);
		} else {
			candidatesModel = partyFxModel.getCandidatesModel();

		}

		candidatesListView.setItems(candidatesModel);
		termsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Term>() {

			@Override
			public void changed(ObservableValue<? extends Term> observable, Term oldValue, Term newValue) {
				if (newValue != null) {
					termWatched = termsComboBox.getSelectionModel().getSelectedItem();
					updateCandidatesListView(termWatched);
					updateProgramsListview(termWatched);
					updateItemTableView(termWatched);
				}
			}
		});

		TableColumn<Item, String> categoryColumn = new TableColumn<>("Kategória");
		categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
		itemsTableView.getColumns().add(categoryColumn);

		TableColumn<Item, String> itemColumn = new TableColumn<>("Bod");
		itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("info"));
		itemsTableView.getColumns().add(itemColumn);

		Term term = termsComboBox.getSelectionModel().getSelectedItem();
		List<Item> list_i = new ArrayList<>();
		if (party != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(term, party);
		}
		itemsModel = FXCollections.observableArrayList(list_i);
		itemsTableView.setItems(itemsModel);
	};

	void updateCandidatesListView(Term term) {
		List<Candidate> list_c = new ArrayList<>();
		if (party != null) {
			list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party, term);
		}
		candidatesModel = FXCollections.observableArrayList(list_c);
		candidatesListView.setItems(candidatesModel);

	}

	void updateProgramsListview(Term term) {
		List<Program> list_p = new ArrayList<>();
		if (party != null) {
			list_p = DaoFactory.INSTANCE.getProgramDao().getByTermParty(term, party);
		}
		programsModel = FXCollections.observableArrayList(list_p);
		programsListView.setItems(programsModel);

	}

	void updateItemTableView(Term term) {
		List<Item> list_i = new ArrayList<>();
		if (party != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(term, party);
		}
		itemsModel.setAll(list_i);

	}

	@FXML
	void addTermButtonClick(ActionEvent event) {
		TermEditController controller = new TermEditController();
		showTermEdit(controller, "Pridávanie volebného obdobia");
	}

	@FXML
	void editTermButtonClick(ActionEvent event) {
		Term selectedTerm = termsComboBox.getSelectionModel().getSelectedItem();
		TermEditController controller = new TermEditController(selectedTerm);
		showTermEdit(controller, "Editovanie volebného obdobia");
	}

	@FXML
	void deleteTermButtonClick(ActionEvent event) {
		// TODO
	}

	void showTermEdit(TermEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("termEdit.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();
			stage.setTitle(sceneName);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////

	@FXML
	void addCandidateButtonClick(ActionEvent event) {

	}

	@FXML
	void addItemButtonclick(ActionEvent event) {

	}

	@FXML
	void addProgramButtonClick(ActionEvent event) {

	}

	@FXML
	void deleteCandidateButtonClick(ActionEvent event) {

	}

	@FXML
	void deleteItemButtonClick(ActionEvent event) {

	}

	@FXML
	void deleteProgramButtonClick(ActionEvent event) {

	}

	@FXML
	void editCandidateButtonClick(ActionEvent event) {

	}

	@FXML
	void editItemButtonclick(ActionEvent event) {

	}

	@FXML
	void editProgramButtonClick(ActionEvent event) {

	}

	@FXML
	void savePartyButtonClick(ActionEvent event) {

	}

}
