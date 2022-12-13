package sk.upjs.ics.votuj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Program;

public class PartyEditController {

	private Party party;
	private PartyFxModel partyFxModel;
	//private CandidateFxModel candidateFxModel;

	@FXML
	private ListView<Candidate> candidatesListView;

	@FXML
	private TextField partyInfoTextField;

	@FXML
	private TextField partyNameTextField;

	@FXML
	private ListView<Program> programsListView;

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
		candidatesListView.setItems(partyFxModel.getCandidatesModel());
		//kandidati sa nezobrazuju lebo problem v PArty FX model
		programsListView.setItems(partyFxModel.getProgramsModel());
		
		Program program = programsListView.getSelectionModel().getSelectedItem();
		
		if (program!=null) {
			System.out.println("TU SOM!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			partyFxModel = new PartyFxModel(party,program);
			partyInfoTextField.textProperty().bindBidirectional(partyFxModel.getInfoProperty());
			partyNameTextField.textProperty().bindBidirectional(partyFxModel.getNameProperty());
			candidatesListView.setItems(partyFxModel.getCandidatesModel());
			//kandidati sa nezobrazuju lebo problem v PArty FX model
			programsListView.setItems(partyFxModel.getProgramsModel());
		}
	};

	

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
