package sk.upjs.ics.votuj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import sk.upjs.ics.votuj.storage.Category;
import sk.upjs.ics.votuj.storage.CategoryDao;
import sk.upjs.ics.votuj.storage.DaoFactory;
import sk.upjs.ics.votuj.storage.Party;
import sk.upjs.ics.votuj.storage.PartyDao;
import sk.upjs.ics.votuj.storage.Vote;

public class ChoiceController {
	
	private Vote vote;
	private CategoryDao categoryDao; 
	private PartyDao partyDao; 
	private List<Category> chosenCategories;
	private List<Party> chosenParties;
	private List<Category> allCategories;
	private List<Party> allParties;
	
	@FXML
	private FlowPane categoryPane;

	@FXML
	private FlowPane partiesPane;
	
	

	public ChoiceController(Vote vote) {
		this.vote = vote;
		categoryDao = DaoFactory.INSTANCE.getCategoryDao();
		partyDao = DaoFactory.INSTANCE.getPartyDao();
	}
	
	@FXML
	void initialize() {
		allCategories = categoryDao.getAll();
		allParties = partyDao.getAll();
		
		Map<Category, Boolean> categoryMap = new HashMap<>();
		for (Category c: allCategories) {
			categoryMap.put(c, false);
		}
		/*
		for (Category c : allCategories) {
    		CheckBox checkBox = new CheckBox(c.getName()); //to tring da rovno id meno aj priezvisko
    		checkBox.selectedProperty().bindBidirectional(chosenCategories);
    		studentsFlowPane.getChildren().add(checkBox);
    	}*/
		
	}


    @FXML
    void allCategoriesButtonClick(ActionEvent event) {

    }

    @FXML
    void allPartiesButtonClick(ActionEvent event) {

    }

	@FXML
	void filtrationButtonClick(ActionEvent event) {

	}

}
