package sk.upjs.ics.votuj;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Admin;
import sk.upjs.ics.votuj.storage.AdminDao;
import sk.upjs.ics.votuj.storage.DaoFactory;

public class AdminLoginController {

	@FXML
	private TextField loginNameTextField;

	@FXML
	private PasswordField passwordTextField;

	@FXML
	void loginButtonClick(ActionEvent event) {
		String loginName = loginNameTextField.getText().trim();
		String loginPassword = passwordTextField.getText();
		loginNameTextField.clear();
		passwordTextField.clear();

		AdminDao adminDao = DaoFactory.INSTANCE.getAdminDao();
		Admin admin = adminDao.getByName(loginName);

		if (admin.getPassword().equals(loginPassword)) {
			PartyOriginController controller = new PartyOriginController();
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partyOrigin.fxml"));
				fxmlLoader.setController(controller);

				Parent parent = fxmlLoader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setTitle("Editacia politickej strany");
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.show();
				loginNameTextField.getScene().getWindow().hide();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Zadane heslo je nespravne. Skontrolujte svoje prihlasovacie meno a heslo");
			alert.show();
			return;
		}
	}

}
