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
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");
		
		Term term = termFxModel.getTerm();
		if (term.getSince()==null || term.getTo() == null || term.getSince()==0 ||term.getTo()==0) {
			alert.setContentText("Od aj do musia byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}
		System.out.println("povodne: " + term.getSince());		
		System.out.println("povodne: " + term.getTo()); 
		Long foreignId = null;
		List<Term> allTerms = DaoFactory.INSTANCE.getTermDao().getAll();
		for (Term t : allTerms) {
			System.out.println(t.getSince());
			System.out.println(t.getTo());
			
			int x = term.getSince();
			int y = term.getTo();
			int a = (int) t.getSince();
			int b = (int) t.getTo();
			
			if ((t.getSince().equals(term.getSince())) && (t.getTo().equals(term.getTo()))) {
				foreignId = t.getId();
				alert.setContentText(
						"Pokúšate sa pridať také obdobie, ktoré sa už nachádza v databáze s id:" + foreignId);
				alert.show();
				return;
			}
			
			if (((x<a) && (y>a)) || 
					((x<b) && (y>b)) || 
					((x>=a) && (y<=b)) ) {
				foreignId = t.getId();
				alert.setContentText(
						"Pokúšate sa pridať také obdobie, ktoré sa prekrýva s obdobím v databáze: " + t.getSince() + " - " + t.getTo());
				alert.show();
				return;
			}
		}

		try {
			if (term != null) {
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
