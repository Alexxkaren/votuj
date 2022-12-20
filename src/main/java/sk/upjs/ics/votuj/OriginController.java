package sk.upjs.ics.votuj;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.util.StringUtils;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Region;
import sk.upjs.ics.votuj.storage.Term;
import sk.upjs.ics.votuj.storage.Vote;

public class OriginController {

	public static final Logger logger = LoggerFactory.getLogger(OriginController.class);
	private Vote vote;
	private VoteFxModel voteFxModel;
	private Region chosenRegion;
	private ObservableList<Region> regionsModel;
	Locale l = new Locale("UK");
	private Boolean maleB;

	private Stage stage;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private TextField ageTextField;

	@FXML
	private ToggleGroup group;

	@FXML
	private RadioButton femaleRadioButton;

	@FXML
	private RadioButton maleRadioButton;

	@FXML
	private ComboBox<Region> regionComboBox;

	public OriginController() {
		voteFxModel = new VoteFxModel();
	}

	@FXML
	void initialize() {
		logger.debug("initialize running");
		ageTextField.textProperty().bindBidirectional(voteFxModel.getAgeProperty());
		maleB = null;

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

		femaleRadioButton.setToggleGroup(group);
		maleRadioButton.setToggleGroup(group);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				RadioButton rb = (RadioButton) group.getSelectedToggle();
				if (rb != null) {
					maleB = rb.getText().equals("muž");
				}
			}
		});

	}

	@FXML
	void adminButtonClick(ActionEvent event) {
		AdminLoginController controller = new AdminLoginController();
		showNextWindow(controller, "Login", "adminLogin.fxml");
	}

	@FXML
	void goToCompareButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Vote voteLocal = voteFxModel.getVote();
		voteLocal.setRegion(chosenRegion);
		voteLocal.setMale(maleB);

		if (voteLocal.getRegion() == null) {
			alert.setContentText("Región musí byť vybraný");
			alert.show();
			return;
		}
		if (voteLocal.getAge() == null || !StringUtils.isStrictlyNumeric(voteLocal.getAge())) {
			alert.setContentText("Vek musí byť vyplnený a musí byť celočíselnou hodnotou");
			alert.show();
			return;
		}
		if (Integer.parseInt(voteLocal.getAge()) < 18) {
			alert.setContentText("Na vypĺňanie prieskumu musíte mať viac ako 18 rokov");
			alert.show();
			return;
		}
		if (voteLocal.getMale() == null) {
			alert.setContentText("Pohlavie musí byť vyplnené");
			alert.show();
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		voteLocal.setDate(now);

		ChoiceController controller = new ChoiceController(voteLocal);
		showNextWindow(controller, "Výber kategórí a strán", "choice.fxml");
	}

	@FXML
	void goToVoteButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Vote voteLocal = voteFxModel.getVote();
		voteLocal.setRegion(chosenRegion);
		voteLocal.setMale(maleB);

		if (voteLocal.getRegion() == null) {
			alert.setContentText("Región musí byť vybraný");
			alert.show();
			return;
		}
		if (voteLocal.getAge() == null || !StringUtils.isStrictlyNumeric(voteLocal.getAge())) {
			alert.setContentText("Vek musí byť vyplnený a musí byť celočíselnou hodnotou");
			alert.show();
			return;
		}
		if (Integer.parseInt(voteLocal.getAge()) < 18) {
			alert.setContentText("Na vypĺňanie prieskumu musíte mať viac ako 18 rokov");
			alert.show();
			return;
		}
		if (voteLocal.getMale() == null) {
			alert.setContentText("Pohlavie musí byť vyplnené");
			alert.show();
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		voteLocal.setDate(now);

		voteLocal.setMale(maleB);

		VotingController controller = new VotingController(voteLocal);
		showNextWindow(controller, "Hlasovanie", "voting.fxml");
	}

	void showNextWindow(Object controller, String sceneName, String resource) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			stage = new Stage();

			scene.getStylesheets().add(css);
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);

			stage.setTitle(sceneName);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);

			if (controller.getClass() == AdminLoginController.class) {
				stage.show();
				ageTextField.getScene().getWindow().hide();
			} else {
				stage.showAndWait();
				regionComboBox.getSelectionModel().clearSelection();
				ageTextField.clear();
				maleRadioButton.setSelected(false);
				femaleRadioButton.setSelected(false);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
