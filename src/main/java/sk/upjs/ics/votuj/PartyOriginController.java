package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.ObjectUndeletableException;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.PartyDao;
import sk.upjs.ics.votuj.storage.Term;

public class PartyOriginController {

	private ObservableList<Party> partiesModel;
	private PartyDao partyDao = DaoFactory.INSTANCE.getPartyDao();
	private Stage stage;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private ComboBox<Party> partiesComboBox;

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
			if (partiesComboBox.getSelectionModel().getSelectedItem() != null) {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partyView.fxml"));
				Party party = partiesComboBox.getSelectionModel().getSelectedItem();
				PartyViewController controller = new PartyViewController(party, false);
				fxmlLoader.setController(controller);
				Parent parent = fxmlLoader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();

				String css = this.getClass().getResource("votuj.css").toExternalForm();
				scene.getStylesheets().add(css);
				Image icon = new Image("single_logo.png");
				stage.getIcons().add(icon);

				stage.setTitle("Náhľad politickej strany");
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Žiadna politická strana nie je vybraná na editovanie");
				dialog = alert.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");
				alert.show();
				return;
			}
		} catch (IOException e) {
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

		if (selectedParty != null) {
			PartyEditController controller = new PartyEditController(selectedParty);
			showPartyEdit(controller, "Editovanie politickej strany");

		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna politická strana nie je vybraná na editovanie");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	void deletePartyButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		boolean successful = false;
		Party party = partiesComboBox.getSelectionModel().getSelectedItem();
		List<Party> parties = new ArrayList<>();

		if (party != null) {
			try {
				Alert alertW = new Alert(AlertType.WARNING);
				alertW.setTitle("Upozornenie!");
				alertW.setHeaderText("Potvrdenie vymazania");
				alertW.setContentText("Volebná strana " + party.getName() + " s id " + party.getId()
						+ " bude vymazaná, chcete skutočne vymazať?");
				ButtonType btDelete = new ButtonType("Vymazať");
				ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

				alertW.getButtonTypes().setAll(btDelete, btCancel);

				dialog = alertW.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");

				Optional<ButtonType> result = alertW.showAndWait();
				if (result.get() == btDelete) {
					successful = DaoFactory.INSTANCE.getPartyDao().delete(party.getId());
					partiesModel.clear();
					parties = DaoFactory.INSTANCE.getPartyDao().getAll();
					partiesModel.addAll(parties);
					partiesComboBox.setItems(partiesModel);
				}
			} catch (ObjectUndeletableException e) {
				alert.setContentText("Snažíte sa vymazať politickú stranu, ktorá je už používaná");
				alert.show();
				return;

			}
		} else {
			alert.setContentText("Žiadna volebná strana nie je vybraná na vymazanie");
			alert.show();
			return;
		}

		if (successful) {
			partiesModel.clear();
			parties = DaoFactory.INSTANCE.getPartyDao().getAll();
			partiesModel.addAll(parties);
			partiesComboBox.setItems(partiesModel);
		}

	}

	void showPartyEdit(PartyEditController controller, String sceneName) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partyEdit.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();

			String css = this.getClass().getResource("votuj.css").toExternalForm();
			scene.getStylesheets().add(css);
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);

			stage.setTitle(sceneName);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			if (controller.getSavedParty() != null) {
				partiesModel.clear();
				List<Party> parties = DaoFactory.INSTANCE.getPartyDao().getAll();
				partiesModel.addAll(parties);
				partiesComboBox.setItems(partiesModel);
				// partiesComboBox.getSelectionModel().select(controller.getSavedParty());

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
