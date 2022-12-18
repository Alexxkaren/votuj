package sk.upjs.ics.votuj;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Term;

public class TermEditController {

	private Term term;
	private Term savedTerm;
	private TermFxModel termFxModel;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

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
	// mpzeme ptmniekedy urobit svoj formatter na cisla<^<^<^<^<^<^<^^<^<^<^<^<

	@FXML
	void initialize() {
		sinceTermTextField.textProperty().bindBidirectional(termFxModel.getSinceProperty(),
				new NumberStringConverter(l));
		toTermTextField.textProperty().bindBidirectional(termFxModel.getToProperty(), new NumberStringConverter(l));

	}

	@FXML
	void saveTermButtonClick(ActionEvent event) {
		Term term = termFxModel.getTerm();
		if (term.getSince() == null || term.getTo() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Upozornenie!");
			alert.setContentText("Od aj do musia byť vyplnené, prosím doplňte.");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
		}
		Long foreignId = null;
		List<Term> allTerms = DaoFactory.INSTANCE.getTermDao().getAll();
		for (Term t : allTerms) {
			if (t.getSince() == term.getSince() && t.getTo() == term.getTo()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Upozornenie!");
				alert.setContentText(
						"Pokúšate sa pridať také obdobie, ktoré sa už nachádza v databáze! s id:" + foreignId);
				dialog = alert.getDialogPane();
				dialog.getStylesheets().add(css);
				dialog.getStyleClass().add("dialog");
				alert.show();
				return;
			}
		}

		try {
			if (term.getId() != null) {
				savedTerm = DaoFactory.INSTANCE.getTermDao().save(term);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		sinceTermTextField.getScene().getWindow().hide();
	}

	public Term getSavedTerm() {
		return this.savedTerm;
	}

}
