package sk.upjs.ics.votuj;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Term;

public class CandidateEditController {
	
	private Candidate candidate;
	private CandidateFxModel candidateFxModel;
	private ObservableList<Term> termsModel;

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
    
    
    
    public CandidateEditController() {
    	candidateFxModel = new CandidateFxModel();
	}

	public CandidateEditController(Candidate candidate) {
		this.candidate = candidate;
		candidateFxModel = new CandidateFxModel(candidate);
	}

	@FXML
	void initialize() {
    	candidateNameTextField.textProperty().bindBidirectional(candidateFxModel.getNameProperty());
    	candidateNumberTextField.textProperty().bindBidirectional(candidateFxModel.getCandidateNumberProperty());
    	candidateSurnameTextField.textProperty().bindBidirectional(candidateFxModel.getSurnameProperty());
    	candidateInfoTextArea.textProperty().bindBidirectional(candidateFxModel.getInfoProperty());
        
    	List<Term> terms = DaoFactory.INSTANCE.getTermDao().getAll();
    	termsModel = FXCollections.observableArrayList(terms);
    	candidateTermComboBox.setItems(termsModel);
    	
    	candidateTermComboBox.getSelectionModel().selectFirst();
		
    }

    @FXML
    void saveCandidateButtonClick(ActionEvent event) {
    	//TODO

    }

}
