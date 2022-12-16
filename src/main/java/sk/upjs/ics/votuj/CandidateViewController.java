package sk.upjs.ics.votuj;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sk.upjs.ics.votuj.storage.Candidate;
import sk.upjs.ics.votuj.storage.Party;

public class CandidateViewController {
	
	CandidateFxModel candidateFxModel;
    @FXML
    private TextArea candidateInfoTextArea;

    @FXML
    private Label candidateNameLabel;

    @FXML
    private Label candidateNumberLabel;

    @FXML
    private Label candidateSurnameLabel;

	
	public CandidateViewController( Candidate candidate, Party party) {
		candidateFxModel = new CandidateFxModel(candidate, party);
	}
	
	@FXML
	void initialize() {
		candidateNameLabel.setText(candidateFxModel.getName());
		candidateInfoTextArea.textProperty().bindBidirectional(candidateFxModel.getInfoProperty());
		candidateSurnameLabel.setText(candidateFxModel.getSurname());
		candidateNumberLabel.setText(candidateFxModel.getCandidateNumber());
	}
}
