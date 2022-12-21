package sk.upjs.ics.votuj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
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
	private Map<Party, Integer> partyVotesSum;
	private Map<Party, Integer> partyVotesRegionSum;
	private Map<Party, Integer> partyVotesFemaleSum;
	private Map<Party, Integer> partyVotesMaleSum;

	@FXML
	private BarChart<String, Integer> generalPartyVotes;

	@FXML
	private BarChart<String, Integer> regionPartyVotes;

	@FXML
	private StackedBarChart<String, Integer> sexPartiesVotes;

	@FXML
	private NumberAxis votes;

	@FXML
	private CategoryAxis parties;

	@FXML
	private ComboBox<Region> regionComboBox;

	//vzor: https://www.javatpoint.com/javafx-bar-chart
	@FXML
	void initialize() {
		partyVotesSum = new HashMap<>();
		partyVotesRegionSum = new HashMap<>();
		partyVotesFemaleSum = new HashMap<>();
		partyVotesMaleSum = new HashMap<>();

		List<Party> allParties = DaoFactory.INSTANCE.getPartyDao().getAll();
		List<Vote> allVotes = DaoFactory.INSTANCE.getVoteDao().getAll();
		List<Region> allRegions = DaoFactory.INSTANCE.getRegionDao().getAll();

		regionsModel = FXCollections.observableArrayList(allRegions);
		regionComboBox.setItems(regionsModel);
		regionComboBox.getSelectionModel().selectFirst();

		chosenRegion = regionComboBox.getSelectionModel().getSelectedItem();

		for (Party p : allParties) {
			partyVotesSum.put(p, 0);
			partyVotesRegionSum.put(p, 0);
			partyVotesFemaleSum.put(p, 0);
			partyVotesMaleSum.put(p, 0);
		}

		for (Vote v : allVotes) {
			for (Party pp : allParties) {
				if (v.getParty().equals(pp)) {
					partyVotesSum.put(pp, partyVotesSum.get(pp) + 1);
				}
				if (v.getParty().equals(pp) && v.getRegion().equals(chosenRegion)) {
					partyVotesRegionSum.put(pp, partyVotesRegionSum.get(pp) + 1);
				}
				if (v.getParty().equals(pp) && v.getMale()) {
					partyVotesMaleSum.put(pp, partyVotesMaleSum.get(pp) + 1);
				}
				if (v.getParty().equals(pp) && !v.getMale()) {
					partyVotesFemaleSum.put(pp, partyVotesFemaleSum.get(pp) + 1);
				}
			}
		}

		XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
		series1.setName("Hlasy");
		for (Party ppp : partyVotesSum.keySet()) {
			series1.getData().add(new XYChart.Data<String, Integer>(ppp.getName(), partyVotesSum.get(ppp)));
		}

		XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
		series2.setName("Hlasy");
		for (Party ppp : partyVotesRegionSum.keySet()) {
			series2.getData().add(new XYChart.Data<String, Integer>(ppp.getName(), partyVotesRegionSum.get(ppp)));
		}

		XYChart.Series<String, Integer> series3f = new XYChart.Series<>();
		series3f.setName("Hlasy žien");
		for (Party ppp : partyVotesFemaleSum.keySet()) {
			series3f.getData().add(new XYChart.Data<String, Integer>(ppp.getName(), partyVotesFemaleSum.get(ppp)));
		}

		XYChart.Series<String, Integer> series3m = new XYChart.Series<>();
		series3m.setName("Hlasy mužov");
		for (Party ppp : partyVotesMaleSum.keySet()) {
			series3m.getData().add(new XYChart.Data<String, Integer>(ppp.getName(), partyVotesMaleSum.get(ppp)));
		}


		generalPartyVotes.getData().addAll(series1);
		regionPartyVotes.getData().addAll(series2);
		sexPartiesVotes.getData().addAll(series3f, series3m);

		regionComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Region>() {
			@Override
			public void changed(ObservableValue<? extends Region> observable, Region oldValue, Region newValue) {
				chosenRegion = regionComboBox.getSelectionModel().getSelectedItem();
				changeRegionGraph();
			}
		});

	}

	public void changeRegionGraph() {
		regionPartyVotes.getData().clear();
		List<Party> allParties = DaoFactory.INSTANCE.getPartyDao().getAll();
		List<Vote> allVotes = DaoFactory.INSTANCE.getVoteDao().getAll();
		Map<Party, Integer> partyVotesRegionSum = new HashMap<>();

		for (Party p : allParties) {
			partyVotesRegionSum.put(p, 0);
		}

		for (Vote v : allVotes) {
			for (Party pp : allParties) {
				if (v.getParty().equals(pp) && v.getRegion().equals(chosenRegion)) {
					partyVotesRegionSum.put(pp, partyVotesRegionSum.get(pp) + 1);
				}
			}
		}

		XYChart.Series<String, Integer> series = new XYChart.Series<>();
		for (Party ppp : partyVotesRegionSum.keySet()) {
			series.getData().add(new XYChart.Data<String, Integer>(ppp.getName(), partyVotesRegionSum.get(ppp)));
		}
	
		regionPartyVotes.getData().addAll(series);
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
