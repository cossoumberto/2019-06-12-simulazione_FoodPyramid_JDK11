package it.polito.tdp.food;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtCalorie;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<Condiment> boxIngrediente;

    @FXML
    private Button btnDietaEquilibrata;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaDieta(ActionEvent event) {
    	if(boxIngrediente.getValue()!=null) {
    		txtResult.clear();
    		for(Condiment c : model.dieta(boxIngrediente.getValue()))
    			txtResult.appendText(c.toString() + "\n");
    		model.archiDieta();
    	} else 
    		txtResult.setText("Seleziona ingrediente");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Double calorie = null;
    	try {
    		calorie = Double.parseDouble(txtCalorie.getText());
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserisci un numero");
    	}
    	if(calorie!=null) {
    		txtResult.clear();
    		model.creaGrafo(calorie);
    		List<Condiment> list = new ArrayList<>(model.getGrafo().vertexSet());
    		Collections.sort(list);
    		boxIngrediente.getItems().addAll(list);
    		for(Condiment c : list)
    			txtResult.appendText(c.toString() + " " + model.getNumCibiCondiment(c) + "\n");
    	}
    }

    @FXML
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxIngrediente != null : "fx:id=\"boxIngrediente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDietaEquilibrata != null : "fx:id=\"btnDietaEquilibrata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
