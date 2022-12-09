package sk.upjs.ics.votuj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Party;

public class PartyEditController {

	private Party party;
	private PartyFxModel partyFxModel;

	@FXML
	private ListView<?> candidatesListView;

	@FXML
	private TextField partyInfoTextField;

	@FXML
	private TextField partyNameTextField;

	@FXML
	private ListView<?> programsListView;

	public PartyEditController() {
		//partyFxModel = new PartyFxModel(party);
	}

	public PartyEditController(Party party) {
		this.party = party;
		partyFxModel = new PartyFxModel(party);
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
