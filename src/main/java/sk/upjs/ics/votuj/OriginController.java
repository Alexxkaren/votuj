package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OriginController {

	public static final Logger logger = LoggerFactory.getLogger(OriginController.class);

	@FXML
	private TextField ageTextField;

	@FXML
	private RadioButton femaleRadioButton;

	@FXML
	private RadioButton maleRadioButton;

	@FXML
	private ComboBox<?> regionComboBox;

	@FXML
	void initialize() {
		logger.debug("initialize running");
	}

	@FXML
	void adminButtonClick(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminLogin.fxml"));
			AdminLoginController controller = new AdminLoginController();
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();  
			
			String css = this.getClass().getResource("votuj.css").toExternalForm();
			scene.getStylesheets().add(css); 
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);
			
			stage.setTitle("Login");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			ageTextField.getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void goToCompareButtonClick(ActionEvent event) {

	}

	@FXML
	void goToVoteButtonClick(ActionEvent event) {

	}

}
