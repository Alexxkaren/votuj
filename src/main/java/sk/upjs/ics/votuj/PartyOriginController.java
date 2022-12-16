package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.PartyDao;

public class PartyOriginController {

	@FXML
	private ComboBox<Party> partiesComboBox;

	
	private ObservableList<Party> partiesModel;
	private PartyDao partyDao = DaoFactory.INSTANCE.getPartyDao();
	private Stage stage;

	@FXML
	void initialize() {
		List<Party> parties = partyDao.getAll();
		partiesModel = FXCollections.observableArrayList(parties);
		partiesComboBox.setItems(partiesModel);
		partiesComboBox.getSelectionModel().selectFirst();

	}

	@FXML
	void partyDetailButtonClick(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partyView.fxml"));
			Party party = partiesComboBox.getSelectionModel().getSelectedItem();
			PartyViewController controller = new PartyViewController(party);
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setTitle("Podrobnosti o politickej strane");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void addPartyButtonClick(ActionEvent event) {
		PartyEditController controller = new PartyEditController();
		showPartyEdit(controller, "Pridávanie politickej strany");
	}

	@FXML
	void editPartyButtonClick(ActionEvent event) {
		Party selectedParty = partiesComboBox.getSelectionModel().getSelectedItem();
		PartyEditController controller = new PartyEditController(selectedParty);
		showPartyEdit(controller, "Editovanie politickej strany");
  
	}

	void showPartyEdit(PartyEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partyEdit.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();
			stage.setTitle(sceneName);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void deletePartyButtonClick(ActionEvent event) {

	}

}
