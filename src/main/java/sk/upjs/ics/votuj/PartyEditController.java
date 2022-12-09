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
		//tu sa deje aby boli už veci vypleneé ke dsa robí edit
		partyInfoTextField.textProperty().bindBidirectional(partyFxModel.getInfoProperty());
		partyNameTextField.textProperty().bindBidirectional(partyFxModel.getNameProperty());
		candidatesListView.setItems(partyFxModel.getCandidatesModel()); 
		//kandidati sa nezobrazuju lebo problem v PArty FX model
		programsListView.setItems(partyFxModel.getProgramsModel());
	}
	
	//TODO: dokončiť tentocontroller - otazka:
	// chceme aby sa kandidati zobrazovali vzhladom na program?
	//alebo chceme aby sa zobrazovali vsetci kandidati za vsetky volebne obdobia
	// nebolo by dobre spravit aby sa pri edite dalo vybrat obdobie a nasledne 
	// podla toho sa budu menit aj kandidati aj programy???
	//SERIOUS QUESTIOOOOOOOON!!!!!!!!!!!!!!!!!!!!!!!!!!!

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
