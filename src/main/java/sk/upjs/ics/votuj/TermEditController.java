package sk.upjs.ics.votuj;

import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import sk.upjs.ics.votuj.storage.Term;

public class TermEditController {

	private Term term;
	private TermFxModel termFxModel;

	@FXML
	private TextField sinceTermTextField;

	@FXML
	private TextField toTermTextField;

	public TermEditController() {
		termFxModel = new TermFxModel();
	}

	public TermEditController(Term term) {
		this.term = term;
		termFxModel = new TermFxModel(term);
	}
	Locale l = new Locale("UK");
	//mpzeme ptmniekedy urobit svoj formatter na cisla<^<^<^<^<^<^<^^<^<^<^<^<
	
	@FXML
	void initialize() {
		sinceTermTextField.textProperty().bindBidirectional(termFxModel.getSinceProperty(), new NumberStringConverter(l));
		toTermTextField.textProperty().bindBidirectional(termFxModel.getToProperty(),  new NumberStringConverter(l));	

	}

	@FXML
	void saveTermButtonClick(ActionEvent event) {
		//TODO
	}

}
