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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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

		List<Item> list_i = new ArrayList<>();
		if (program != null) {
			list_i = DaoFactory.INSTANCE.getItemDao().getByProgram(program);
			
		}
		itemsModel = FXCollections.observableArrayList(list_i);
		programItemTableView.setItems(itemsModel);

		if (isActiveModel.get()) {
			activeCheckBox.setSelected(true);
		} else {
			activeCheckBox.setSelected(false);
		}

		allPrograms = DaoFactory.INSTANCE.getProgramDao().getByParty(party);
		for (Program p : allPrograms) {
			if (program == null && p.isActive()) {
				activeCheckBox.setDisable(true);
			
			} else if (program != null && !p.equals(program) && p.isActive()) {
				activeCheckBox.setDisable(true);
			
			}

		}

	}

	@FXML
	void changeOfActive(ActionEvent event) {
		
	}

	@FXML
	void saveProgramButtonClick(ActionEvent event) {

	}

}
