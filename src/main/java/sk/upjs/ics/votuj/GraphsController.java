package sk.upjs.ics.votuj;

import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Region;
import sk.upjs.ics.votuj.storage.Vote;

public class GraphsController {
	
	private Stage stage;
	private DialogPane dialog;
	private Region chosenRegion;
	String css = this.getClass().getResource("votuj.css").toExternalForm();
	private ObservableList<Region> regionsModel;


	@FXML
	private BarChart<Party, Vote> krajeStranyHlasy;

	@FXML
	private StackedBarChart<Party, Vote> zenyMuziStranyHlasy;

	@FXML
	private ComboBox<Region> regionComboBox;
	
	@FXML
	void initialize() {
		List<Region> regions = DaoFactory.INSTANCE.getRegionDao().getAll();
		regionsModel = FXCollections.observableArrayList(regions);
		regionComboBox.setItems(regionsModel);
		chosenRegion = regionComboBox.getSelectionModel().getSelectedItem();

		regionComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Region>() {
			@Override
			public void changed(ObservableValue<? extends Region> observable, Region oldValue, Region newValue) {
				chosenRegion = regionComboBox.getSelectionModel().getSelectedItem();
			}
		});
	}

	@FXML
	void closeAll(ActionEvent event) {
		Alert alertW = new Alert(AlertType.WARNING);
		alertW.setTitle("Upozornenie!");
		alertW.setHeaderText("Potvrdenie Ukončenia");
		alertW.setContentText("Celá aplikácia bude ukončená, chcete ukončiť?");
		ButtonType btExit = new ButtonType("Ukončiť");
		ButtonType btCancel = new ButtonType("Zrušiť", ButtonData.CANCEL_CLOSE);

		alertW.getButtonTypes().setAll(btExit, btCancel);

		dialog = alertW.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Optional<ButtonType> result = alertW.showAndWait();
		if (result.get() == btExit) {
			Platform.exit();
		}
		
	}

}
