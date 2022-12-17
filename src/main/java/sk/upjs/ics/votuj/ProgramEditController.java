package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class ProgramEditController {
	
	private Party party;
	private Program program;
	private ProgramFxModel programFxModel;
	private List<Program> allPrograms = new ArrayList<>();
	private ObservableList<Item> itemsModel;
	private ObservableList<Term> termsModel;
	private BooleanProperty isActiveModel;

	@FXML
	private CheckBox activeCheckBox;

	@FXML
	private TableView<Item> programItemTableView;

	@FXML
	private TextField programNameTextField;

	@FXML
	private ComboBox<Term> programTermComboBox;

	public ProgramEditController(Party party) {
		this.party = party;
		programFxModel = new ProgramFxModel(party);
	}

	public ProgramEditController(Program program, Party party) {
		this.party = party;
		this.program = program;
		programFxModel = new ProgramFxModel(program, party);
	}

	@FXML
	void initialize() {
		programNameTextField.textProperty().bindBidirectional(programFxModel.getNameProperty());
		isActiveModel = programFxModel.getIsActiveProperty();

		
		List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
		termsModel = FXCollections.observableArrayList(terms);
		programTermComboBox.setItems(termsModel);
		programTermComboBox.getSelectionModel().selectFirst();

		List<Item> list_p = new ArrayList<>();
		if (program != null) {
			list_p = DaoFactory.INSTANCE.getItemDao().getByProgram(program);
		}
		itemsModel = FXCollections.observableArrayList(list_p);
		programItemTableView.setItems(itemsModel);

		if (isActiveModel.get()) {
			activeCheckBox.setSelected(true);
		} else {
			activeCheckBox.setSelected(false);
		}

		if (party!=null) {
			allPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
			for (Program p : allPrograms) {
				if (program == null && p.isActive()) {
					activeCheckBox.setDisable(true);
				
				} else if (program != null && !p.equals(program) && p.isActive()) {
					activeCheckBox.setDisable(true);
				}
			}
		}
		
		if (program!=null) {
			TableColumn<Item, String> categoryColumn = new TableColumn<>("Kategória");
			categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categories"));
			programItemTableView.getColumns().add(categoryColumn);

			TableColumn<Item, String> itemColumn = new TableColumn<>("Bod");
			itemColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("info"));
			programItemTableView.getColumns().add(itemColumn);

			Term term = programTermComboBox.getSelectionModel().getSelectedItem();
			List<Item> list_i = new ArrayList<>();
			if (party != null) {
				list_i = DaoFactory.INSTANCE.getItemDao().getByTermParty(term, party);
			}
			itemsModel = FXCollections.observableArrayList(list_i);
			programItemTableView.setItems(itemsModel);
		}

	}

	@FXML
	void changeOfActive(ActionEvent event) {
		
	}

	@FXML
	void saveProgramButtonClick(ActionEvent event) {

	}

}
