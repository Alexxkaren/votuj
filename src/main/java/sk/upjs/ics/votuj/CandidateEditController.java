package sk.upjs.ics.votuj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sk.upjs.ics.votuj.storage.Term;

public class CandidateEditController {

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
    void saveCandidateButtonClick(ActionEvent event) {

    }

}
