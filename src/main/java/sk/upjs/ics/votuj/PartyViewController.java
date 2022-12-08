package sk.upjs.ics.votuj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import sk.upjs.ics.votuj.storage.Party;

public class PartyViewController {

	private Party party;
	private PartyFxModel partyFxModel;

	@FXML
	private ListView<?> candidatesListView;

	@FXML
	private TableView<?> itemsTableView;

	@FXML
	private Label namePartyLabel;

	@FXML
	private ListView<?> partyInfoListView;

	@FXML
	private Label programYearLabel;

	public PartyViewController(Party party) {
		this.party = party;
		partyFxModel = new PartyFxModel(party);
	}

	@FXML
	void candidateInfoButtonClick(ActionEvent event) {

	}

}
