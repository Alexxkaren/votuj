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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.Category;
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
	private List<Term> listOfSelectedTerms  = new ArrayList<>();;
	private ObservableList<Term> selectedTermsModel  = FXCollections.observableArrayList(new ArrayList<Term>());

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

	@FXML
	private ListView<Term> selectedTermsListView;

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
		selectedTermsListView.setItems(selectedTermsModel);
		

		List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
		termsModel = FXCollections.observableArrayList(terms);
		candidateTermComboBox.setItems(termsModel);
		if (candidate != null) {
			listOfSelectedTerms = DaoFactory.INSTANCE.getTermDao().getByCandidate(candidate);
		} else {
			listOfSelectedTerms.add(this.term);
		}
		selectedTermsModel.setAll(listOfSelectedTerms);
		selectedTermsListView.setItems(selectedTermsModel);
	
	}

	@FXML
	void addTermButtonClick(ActionEvent event) {
		Term term = candidateTermComboBox.getSelectionModel().getSelectedItem();
		if (term != null) {
			if (!listOfSelectedTerms.contains(term)) {
				listOfSelectedTerms.add(term);
				selectedTermsModel.setAll(listOfSelectedTerms);
				selectedTermsListView.setItems(selectedTermsModel);
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("??iadne volebn?? obdobie nie je vybran??, vyberte pros??m obdobie");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}
	}

	@FXML
	void deleteAddedTermButtonClick(ActionEvent event) {
		Term term = selectedTermsListView.getSelectionModel().getSelectedItem();
		if (term != null) {
			listOfSelectedTerms.remove(term);
			selectedTermsModel.setAll(listOfSelectedTerms);
			selectedTermsListView.setItems(selectedTermsModel);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(
					"??iadne volebn?? obdobie nie je vybran??, vyberte pros??m obdobie ktor?? chcete odstr??ni?? z vybran??ch");
			dialog = alert.getDialogPane();
			dialog.getStylesheets().add(css);
			dialog.getStyleClass().add("dialog");
			alert.show();
			return;
		}

	}

	@FXML
	void saveCandidateButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Upozornenie!");
		dialog = alert.getDialogPane();
		dialog.getStylesheets().add(css);
		dialog.getStyleClass().add("dialog");

		Candidate candidate = candidateFxModel.getCandidate();
		List<Term> termss = listOfSelectedTerms;

		if (candidate.getName() == null) {
			alert.setContentText("Meno mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (candidate.getSurname() == null) {
			alert.setContentText("Priezvisko mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (candidate.getCandidateNumber() == null || !StringUtils.isStrictlyNumeric(candidate.getCandidateNumber())) {
			alert.setContentText("????slo kandid??ta mus?? by?? vyplnen?? a mus?? by?? ????selnou hodnotou, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (candidate.getInfo() == null) {
			alert.setContentText("Info mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return;
		}
		if (candidate.getParty() == null) {
			alert.setContentText("Politick?? strana mus?? by?? vyplnen??, pros??m dopl??te.");
			alert.show();
			return; 
		}
		
		if (termss.isEmpty()) {
			alert.setContentText("Kandid??t mus?? ma?? aspo?? 1 volebn?? obdobie");
			alert.show();
			return;
		}

		try { //&& !listOfSelectedTerms.isEmpty()
			if (candidate != null ) {
				candidate.setTerms(termss);
				savedCandidate = DaoFactory.INSTANCE.getCandidateDao().save(candidate, termss);
	
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
