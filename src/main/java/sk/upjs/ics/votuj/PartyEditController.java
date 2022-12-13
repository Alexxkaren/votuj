package sk.upjs.ics.votuj;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;

public class PartyEditController {

	private Party party;
	private PartyFxModel partyFxModel;
	//private CandidateFxModel candidateFxModel;

	@FXML
	private ListView<Candidate> candidatesListView;
	
	@FXML
	private ListView<Program> programsListView;
	
	private ObservableList<Candidate> candidatesModel;
	private ObservableList<Program> programsModel;

	@FXML
	private TextField partyInfoTextField;

	@FXML
	private TextField partyNameTextField;

	

	public PartyEditController() {
		partyFxModel = new PartyFxModel();
	}

	public PartyEditController(Party party) {
		this.party = party;
		partyFxModel = new PartyFxModel(party);
	}

	
	@FXML
    void initialize() {
		programsModel = partyFxModel.getProgramsModel();
		candidatesModel = partyFxModel.getCandidatesModel();
		
		partyInfoTextField.textProperty().bindBidirectional(partyFxModel.getInfoProperty());
		partyNameTextField.textProperty().bindBidirectional(partyFxModel.getNameProperty());
		
		programsListView.setItems(programsModel);
		candidatesListView.setItems(candidatesModel);
		
		programsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Program> () {
			
			@Override
			public void changed(ObservableValue<? extends Program> observable, Program oldValue, Program newValue) {
							if (newValue!=null) {
								updateCandidatesListView();// ak sa zmeni kombobox tak zavolam metodu update table 
							}
			}
		});
		
	};
	
	void updateCandidatesListView() {
		Program program = programsListView.getSelectionModel().getSelectedItem();
		System.out.println(program);
		System.out.println(program.getTerm());
		List<Candidate> list_c = DaoFactory.INSTANCE.getCandidateDao().getByTermParty(party, program.getTerm());
		candidatesModel = FXCollections.observableArrayList(list_c);
		candidatesListView.setItems(candidatesModel);
	}

	

	@FXML
	void addCandidateButtonClick(ActionEvent event) {

	}

	@FXML
	void addProgramButtonClick(ActionEvent event) {

	}

	@FXML
	void deleteCandidateButtonClick(ActionEvent event) {

	}

	@FXML
	void deleteProgramButtonClick(ActionEvent event) {

	}

	@FXML
	void editCandidateButtonClick(ActionEvent event) {

	}

	@FXML
	void editProgramButtonClick(ActionEvent event) {

	}

	@FXML
	void savePartyButtonClick(ActionEvent event) {

	}

}
