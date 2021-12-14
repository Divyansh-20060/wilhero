import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController {
    @FXML
    private Button Start;

    @FXML
    private Button Stop;

    @FXML
    private Circle cir;

    @FXML
    private ImageView hero;

    @FXML
    private Button dash;

    private boolean DT= false;
    public int X=14;

    public void Dash(){
//        TranslateTransition transition = new TranslateTransition();
//        transition.setDuration(Duration.seconds(0.5));
//        transition.setAutoReverse(true);
//        transition.setCycleCount(1);
//        transition.setToX(50);
//        X = X + 50;
//        transition.setNode(jojo);
//        transition.play();

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(hero);
        transition.setDuration(Duration.millis(200));
        transition.setCycleCount(2);
        transition.setByX(50);
        transition.setAutoReverse(true);
        transition.play();

    }

    public void anime(Node pp){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setAutoReverse(true);
        transition.setCycleCount(300);
        //while(System.get)
        transition.setToY(-200);
        transition.setNode(pp);
        transition.play();
    }

    public void onStartButtonClick() {
        DT = true;
        anime(hero);
    }

    public void onQuitButtonClick (){
        System.exit(0);
    }

    public void onDashButtonClick(ActionEvent event){
        if(DT){
            Dash();}
    }
}

//text