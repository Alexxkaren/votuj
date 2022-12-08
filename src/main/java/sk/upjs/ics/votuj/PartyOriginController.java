package sk.upjs.ics.votuj;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.PartyDao;

public class PartyOriginController {

	@FXML
	private ComboBox<Party> partiesComboBox;

	@FXML
	private ObservableList<Party> partiesModel;

	private PartyDao partyDao = DaoFactory.INSTANCE.getPartyDao();
      
	@FXML
	void initialize() {
		List<Party> parties =  partyDao.getAll();
		partiesModel = FXCollections.observableArrayList(parties);
		partiesComboBox.setItems(partiesModel);
		partiesComboBox.getSelectionModel().selectFirst();

	}

	@FXML
	void partyDetailButtonClick(ActionEvent event) {

	}

	@FXML
	void addPartyButtonClick(ActionEvent event) {

	}

	@FXML
	void deletePartyButtonClick(ActionEvent event) {

	}

	@FXML
	void editPartyButtonClick(ActionEvent event) {

	}

}
