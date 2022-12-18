package sk.upjs.ics.votuj;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.mysql.cj.util.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.Term;

public class CandidateEditController {

	private Party party;
	private Term term;
	private Candidate candidate;
	private Candidate savedCandidate;
	private CandidateFxModel candidateFxModel;
	private ObservableList<Term> termsModel;
	private ObservableList<Candidate> candidatesModel;
	private DialogPane dialog;
	String css = this.getClass().getResource("votuj.css").toExternalForm();

	@FXML
	private TextArea candidateInfoTextArea;

	@FXML
	private TextField candidateNameTextField;

	@FXML
	private TextField candidateNumberTextField;

	@FXML
	private TextField candidateSurnameTextField;

	@FXML
	private ComboBox<Term> candidateTermComboBox;

	public CandidateEditController(Party party, Term term) {
		this.term = term;
		this.party = party;
		candidateFxModel = new CandidateFxModel(party, term);
	}

	public CandidateEditController(Candidate candidate, Party party, Term term) {
		this.term = term;
		this.party = party;
		this.candidate = candidate;
		candidateFxModel = new CandidateFxModel(candidate, party);
	}

	@FXML
	void initialize() {
		candidateNameTextField.textProperty().bindBidirectional(candidateFxModel.getNameProperty());
		candidateNumberTextField.textProperty().bindBidirectional(candidateFxModel.getCandidateNumberProperty());
		candidateSurnameTextField.textProperty().bindBidirectional(candidateFxModel.getSurnameProperty());
		candidateInfoTextArea.textProperty().bindBidirectional(candidateFxModel.getInfoProperty());
		
		if (term != null) {
			List<Term> terms = new ArrayList<>();
			terms.add(term);
			termsModel = FXCollections.observableArrayList(terms);
		} else {
			List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
			termsModel = FXCollections.observableArrayList(terms);
		}
		candidateTermComboBox.setItems(termsModel);
		candidateTermComboBox.getSelectionModel().selectFirst();

	}

	@FXML
	void saveCandidateButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");
		
		Candidate candidate = candidateFxModel.getCandidate();
		List<Term> termss = candidateFxModel.getTerms();
		
		if (candidate.getName() == null) {
			alert.setContentText("Meno musí byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}
		if (candidate.getSurname() == null) {
			alert.setContentText("Priezvisko musí byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}
		if (candidate.getCandidateNumber() == null || !StringUtils.isStrictlyNumeric(candidate.getCandidateNumber())) {
			alert.setContentText("Číslo kandidáta musí byť vyplnené a musí byť číselnou hodnotou, prosím doplňte.");
			alert.show();
			return;
		}
		if(candidate.getInfo()==null) {
			alert.setContentText("Info musí byť vyplnené, prosím doplňte.");
			alert.show();
			return;
		}
		if(candidate.getParty()==null) {
			alert.setContentText("Politická strana musí byť vyplnená, prosím doplňte.");
			alert.show();
			return;
		}

		try {
			if (candidate != null) {
				//savedCandidate = DaoFactory.INSTANCE.getCandidateDao().save(candidate);
				savedCandidate = DaoFactory.INSTANCE.getCandidateDao().save(candidate, term);
				
				//mam term a mam kadidata - potrebujem ho ulozit do term_has_candidate
				System.out.println("sa uložil!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println("jeho meno: " + savedCandidate.getName());
			
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		candidateNameTextField.getScene().getWindow().hide();

	}

	public Candidate getSavedCandidate() {
		return this.savedCandidate;
	}

}
