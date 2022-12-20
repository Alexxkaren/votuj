package sk.upjs.ics.votuj;

import java.io.IOException;
import java.util.NoSuchElementException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.Admin;
import sk.upjs.ics.votuj.storage.AdminDao;
import sk.upjs.ics.votuj.storage.DaoFactory;

public class AdminLoginController {
	
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

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
		Admin admin;
		try {
			admin = adminDao.getByName(loginName);
		} catch (NoSuchElementException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Zadané meno je nesprávne. Skontrolujte svoje prihlasovacie meno a heslo");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
			
		
		}
		Password p = new Password();
		if (p.isCorrect(admin.getPassword(),loginPassword)) {
			PartyOriginController controller = new PartyOriginController();
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("partyOrigin.fxml"));
				fxmlLoader.setController(controller);
				Parent parent = fxmlLoader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				
				scene.getStylesheets().add(css); 
				Image icon = new Image("single_logo.png");
				stage.getIcons().add(icon);
				
				stage.setTitle("Politické strany");
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.show();
				loginNameTextField.getScene().getWindow().hide();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Zadané heslo je nesprávne. Skontrolujte svoje prihlasovacie meno a heslo");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
	}

}
