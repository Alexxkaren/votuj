package sk.upjs.ics.votuj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ProgramEditController {

	@FXML
	private CheckBox programActiveCheckBox;

	@FXML
	private ListView<?> programItemListView;

	@FXML
	private TextField programNameTextField;

	@FXML
	private TextField programTermTextField;

	@FXML
	void saveProgramButtonClick(ActionEvent event) {

	}

}
