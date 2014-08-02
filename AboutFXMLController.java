/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * FXML Controller class
 *
 * @author SMART
 */
public class AboutFXMLController implements Initializable, ControlledScreen {

    @FXML
    public void dshow() {
        try{

        Media ds = new Media("dshow://");
        MediaPlayer  mp = new MediaPlayer(ds);
        MediaView mv = new MediaView();
        mv.setMediaPlayer(mp);
        
        mp.play();
        }catch(Exception e){
        
        e.getMessage();
        System.out.println("Error ocuured");
        }
    }
    ScreenControl myController;

    public void setScreenParent(ScreenControl screenParent) {
        myController = screenParent;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void goToScreen1(ActionEvent event) {
        myController.setScreen(NewApplication.screen1ID);
    }

    @FXML
    private void goToScreen2(ActionEvent e) {
        myController.setScreen(NewApplication.screen2ID);
    }
}
