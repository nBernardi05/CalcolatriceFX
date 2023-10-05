package com.bernardispa.calcolatricefx;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Espressione;
import model.EspressioneNonValidaException;

public class PrimaryController {
    @FXML
    private TextField display;
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    boolean msg = false;
    @FXML
    void addNumber(ActionEvent event) {
        if(msg == true){
            display.setText("");
        }
        msg = false;
        String s1 = ((Button)event.getSource()).getText();
        String s2 = display.getText();
        if(s1.equals("=")){
            s2 += s1;
            msg = true;
            try{
                Espressione espr = new Espressione(s2);
                display.setText(espr.toString());
            }catch(EspressioneNonValidaException e){
                display.setText(e.getMessage());
            }catch(RuntimeException re){
                display.setText("Error");
            }
            return;
        }else if(s1.equals("DEL")){
            if(s2.length() == 0)
                return;
            s2 = s2.substring(0, s2.length()-1);
            display.setText(s2);
            return;
        }
        display.setText(s2+s1);
    }
    
    @FXML
    void cancella(ActionEvent event) {
        display.setText("");
    }
    
    
    
}
