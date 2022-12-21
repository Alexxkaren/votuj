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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Region;
import sk.upjs.ics.votuj.storage.Vote;

public class VotingController {
	
	private Vote vote;
	private Party chosenParty;
	private ObservableList<Party> partiesModel;
	
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	

	@FXML
	private ComboBox<Party> choosePartyComboBox;
	

	public VotingController( Vote vote) {
		this.vote = vote;
	}

	@FXML
	void initialize() {
		List<Party> parties = DaoFactory.INSTANCE.getPartyDao().getAll();
		partiesModel = FXCollections.observableArrayList(parties);
		choosePartyComboBox.setItems(partiesModel);
		chosenParty = choosePartyComboBox.getSelectionModel().getSelectedItem();
		
		choosePartyComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Party>() {
			@Override
			public void changed(ObservableValue<? extends Party> observable, Party oldValue, Party newValue) {
				chosenParty = choosePartyComboBox.getSelectionModel().getSelectedItem();
			}
		});
		
	}

	@FXML
	void submitVoteButtonClick(ActionEvent event) {
		Vote voteToBeSubmited = new Vote(this.vote, chosenParty);
		
		if(chosenParty==null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Žiadna volebná strana nebola vybraná, vyberte stranu");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
		
		Vote saved = DaoFactory.INSTANCE.getVoteDao().save(voteToBeSubmited);
		
		GraphsController controller = new GraphsController();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graphs.fxml"));
			fxmlLoader.setController(controller);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();

			scene.getStylesheets().add(css);
			Image icon = new Image("single_logo.png");
			stage.getIcons().add(icon);

			stage.setTitle("Štatistika");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
			choosePartyComboBox.getScene().getWindow().hide();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	
	

}