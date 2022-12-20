package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class ProgramEditController {

	private Party party;
	private Program program;
	private Term termPovod;
	private Term termWatched;
	private Program savedProgram;
	private ProgramFxModel programFxModel;
	private List<Program> allPrograms;
	private ObservableList<Item> itemsModel;
	private ObservableList<Term> termsModel;
	private BooleanProperty isActiveModel;
	private Boolean toBeSaved;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private CheckBox activeCheckBox;

	@FXML
	private TableView<Item> programItemTableView;

	@FXML
	private TextField programNameTextField;

	@FXML
	private ComboBox<Term> programTermComboBox;

	public ProgramEditController(Party party, Term term) {
		this.party = party;
		programFxModel = new ProgramFxModel(party, term);
	}

	public ProgramEditController(Party party) {
		this.party = party;
		programFxModel = new ProgramFxModel(party);
	}

	public ProgramEditController(Program program, Party party) {
		this.party = party;
		this.program = program;
		this.termPovod = program.getTerm();
		programFxModel = new ProgramFxModel(program, party);
	}

	@FXML
	void initialize() {
		toBeSaved = null;
		allPrograms = new ArrayList<>();
		programNameTextField.textProperty().bindBidirectional(programFxModel.getNameProperty());
		isActiveModel = programFxModel.getIsActiveProperty();

		List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
		termsModel = FXCollections.observableArrayList(terms);
		programTermComboBox.setItems(termsModel);

		List<Item> list_i = new ArrayList<>();
		if (program != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByProgram(program);
		}

		itemsModel = FXCollections.observableArrayList(list_i);
		programItemTableView.setItems(itemsModel);

		programTermComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Term>() {
			@Override
			public void changed(ObservableValue<? extends Term> observable, Term oldValue, Term newValue) {
				if (newValue != null) {
					termWatched = programTermComboBox.getSelectionModel().getSelectedItem();
					updateItemsTableView(termWatched);
					updateActive(termWatched);
				}
			}
		});

		if (termPovod != null) {
			programTermComboBox.getSelectionModel().select(termPovod);
		}

		if (isActiveModel.get()) {
			activeCheckBox.setSelected(true);
		} else {
			activeCheckBox.setSelected(false);
		}

		if (party != null) {
			allPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
			for (Program p : allPrograms) {
				if (program == null && p.isActive()) {
					activeCheckBox.setDisable(true);
				} else if (program != null && !p.equals(program) && p.isActive()) {
					activeCheckBox.setDisable(true);
				}
			}
		}

		// PROBLEM S VYBERANIM TEMRU IDEM SPAT POROVNAJ S CANDIDATE
		if (program != null) {
			TableColumn<Item, String> categoryColumn = new TableColumn<>("Kategória");
			categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
			programItemTableView.getColumns().add(categoryColumn);

			TableColumn<Item, String> itemColumn = new TableColumn<>("Bod");
			itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("info"));
			programItemTableView.getColumns().add(itemColumn);

			Term termCh = termWatched;
			// Term termCh = programTermComboBox.getSelectionModel().getSelectedItem();
			List<Item> list_ii = new ArrayList<>();
			if (party != null) {
				list_ii = DaoFactory.INSTANCE.getItemDao().getByTermParty(termCh, party);
			}
			itemsModel = FXCollections.observableArrayList(list_ii);
			programItemTableView.setItems(itemsModel);
		}

	}

	private void updateItemsTableView(Term termWatched) {
		List<Item> list_i = new ArrayList<>();
		if (program != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByProgram(program);
		}
		// list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(termWatched, party);
		itemsModel.setAll(list_i);
		programItemTableView.setItems(itemsModel);
	}

	private void updateActive(Term termWatched) {
		if (party != null) {
			allPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
			for (Program p : allPrograms) {
				if (program == null && p.isActive()) {
					activeCheckBox.setDisable(true);
				} else if (program != null && !p.equals(program) && p.isActive()) {
					activeCheckBox.setDisable(true);
				}
			}
		}

	}

	public Program getSavedProgram() {
		return this.savedProgram;
	}

	@FXML
	void changeOfActive(ActionEvent event) {
		if (program != null) {
			program.setIsActive(activeCheckBox.isSelected());
		} 
		if (savedProgram!=null){
			savedProgram.setIsActive(activeCheckBox.isSelected());
		} else {
			toBeSaved = activeCheckBox.isSelected();
		}
	}

	@FXML
	void saveProgramButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Program program = programFxModel.getProgram();
		program.setTerm(termWatched);

		System.out.println("UKLADANY PROGRAM: " + program.toString());
		program.setParty(party);
		System.out.println("PO PRIDANI PARTY: " + program.toString());
		if (activeCheckBox.isSelected())
			program.setIsActive(true);
		if (!activeCheckBox.isSelected())
			program.setIsActive(false);

		if (program.getName() == null || program.getName().equals("")) {
			alert.setContentText("Názov musí byť vyplený, prosím doplňte.");
			alert.show();
			return;
		}
		if (program.getParty() == null) {
			alert.setContentText("Politická strana musí byť vyplená, prosím doplňte.");
			alert.show();
			return;
		}
		if (program.getTerm() == null) {
			alert.setContentText("Volebné obdobie musí byť vybrané, prosím vyberte.");
			alert.show();
			return;
		}
		boolean alreadyHas = false;
		List<Program> totalyAllPrograms = new ArrayList<>();
		totalyAllPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
		for (Program p : totalyAllPrograms) {
			if (program != null && program.getTerm().equals(p.getTerm()) && !program.equals(p)) {
				alreadyHas = true;
				alert.setContentText("V danom volebnom obodbí už existuje volebný program, vyberte iné obdobie");
				alert.show();
				return;

			}
		}
		
		if (toBeSaved!=null) {
			program.setIsActive(toBeSaved);
		}
		
		try {
			if (program != null) {
				savedProgram = DaoFactory.INSTANCE.getProgramDao().save(program);
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		programNameTextField.getScene().getWindow().hide();

	}

}
