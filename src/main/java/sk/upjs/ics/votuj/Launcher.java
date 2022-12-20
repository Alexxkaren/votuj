package sk.upjs.ics.votuj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Launcher extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("origin.fxml"));
		OriginController controller = new OriginController();
		fxmlLoader.setController(controller);
		Parent parent = fxmlLoader.load();
		Scene scene = new Scene(parent);

		String css = this.getClass().getResource("votuj.css").toExternalForm();
		scene.getStylesheets().add(css);
		Image icon = new Image("single_logo.png");
		stage.getIcons().add(icon);

		stage.setScene(scene);
		stage.setTitle("Votuj!");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}
}
