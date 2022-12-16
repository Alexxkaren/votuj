package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Item;
import sk.upjs.ics.votuj.storage.Program;
import sk.upjs.ics.votuj.storage.Term;

public class ProgramEditController {

	private Program program;
	private ProgramFxModel programFxModel;
	//private TermFxModel termFxModel;
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
	
	

	public ProgramEditController() {
		programFxModel = new ProgramFxModel();
	}
	
	public ProgramEditController(Program program) {
		this.program = program;
		programFxModel = new ProgramFxModel(program);
	}
	
	@FXML
	void initialize() {
		programNameTextField.textProperty().bindBidirectional(programFxModel.getNameProperty());
		//activeCheckBox.selectedProperty().bindBidirectional(programFxModel.getIsActiveProperty(), isActiveModel );
		//programItemTableView.textProperty().bindBidirectional(programFxModel.getIt());
		
		//TU KONCIM NEVLADZEM ROZMYSLAT //////////////////////
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
		
		
	}



	@FXML
	void saveProgramButtonClick(ActionEvent event) {

	}

}
