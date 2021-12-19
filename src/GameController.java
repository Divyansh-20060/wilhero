import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

class Img extends ImageView implements Serializable{
    //private transient Image image ;
    public Img (String st){
        super(st);
    }
//
//    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
//        s.defaultReadObject();
//        image  = SwingFXUtils.toFXImage(ImageIO.read(s), null);
//    }
//
//    private void writeObject(ObjectOutputStream s) throws IOException {
//        s.defaultWriteObject();
//        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", s);
//    }
}

class gameobjcts implements Serializable {
    public Img Node;
    //private void Collision(){}
}

class islands extends gameobjcts {
    public islands(){
        Node = new Img("island.png");
        Node.setLayoutX(-20);
        Node.setLayoutY(330);
    }
}

class Hero extends gameobjcts{
    public Hero(){
        Node = new Img("hero.png");
        Node.setLayoutX(93);
        Node.setLayoutY(238);
    }
}

class Game implements Serializable{
    public String st = "o";
    public Hero hero;
    public ArrayList<islands> Islands = new ArrayList<islands>();
    public Game(){
        hero = new Hero();
        islands i1 = new islands();
        Islands.add(i1);
    }

    public void save(Game G1) throws IOException {
        FileOutputStream fileout = new FileOutputStream("game1.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileout);
        out.writeObject(G1);
        out.close();
        fileout.close();
        System.out.println("Saved!");
    }

    public Game load() throws IOException, ClassNotFoundException {
        Game g1 = null;
        FileInputStream filein = new FileInputStream("game1.ser");
        ObjectInputStream in = new ObjectInputStream(filein);
        g1 = (Game) in.readObject();
        in.close();
        filein.close();
        System.out.println("loaded");
        return g1;
    }
}

public class GameController implements Initializable{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button save;

    @FXML
    private Button Start;

    @FXML
    private Button Stop;

    @FXML
    private Circle cir;

    //@FXML
    //private ImageView hero;

    @FXML
    private Button dash;

    public Game G1 = new Game();

    //private ArrayList<islands> Islands = new ArrayList<islands>();

    public TranslateTransition transition = new TranslateTransition();

    Timeline T1 = new Timeline(new KeyFrame(Duration.millis(6), new EventHandler<ActionEvent>() { //e->{}
        double DY = 2.0;
        @Override
        public void handle(ActionEvent event) {
            G1.hero.Node.setLayoutY(G1.hero.Node.getLayoutY() + 2);
            if(G1.hero.Node.getBoundsInParent().intersects(G1.Islands.get(0).Node.getBoundsInParent())){
                T1.stop();
                transition.stop();
                jump(G1.hero.Node);

            }
        }
    }
    ));


    private boolean DT= false;
    public int X=14;

    public GameController() throws IOException {
    }

    public void Dash(){
//        TranslateTransition transition = new TranslateTransition();
//        transition.setDuration(Duration.seconds(0.5));
//        transition.setAutoReverse(true);
//        transition.setCycleCount(1);
//        transition.setToX(50);
//        X = X + 50;
//        transition.setNode(jojo);
//        transition.play();

        transition.setNode(G1.hero.Node);
        transition.setDuration(Duration.millis(200));
        transition.setCycleCount(0);
        transition.setByX(50);
        transition.setAutoReverse(false);
        transition.play();

    }

    public void jump(Node pp){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.4));
        transition.setAutoReverse(false);
        transition.setCycleCount(0);
        //while(System.get)
        transition.setByY(-200);
        transition.setNode(pp);
        transition.play();
        transition.setOnFinished(e->T1.play());
    }

    public void onStartButtonClick() {
        DT = true;
        T1.setCycleCount(Animation.INDEFINITE);
        T1.play();
        //anime(hero);
    }

    public void onSaveB(ActionEvent event) throws IOException {
        G1.save(G1);
        System.out.println("SAVED!");
    }

    public void onQuitButtonClick (){
        System.exit(0);
    }

    public void onDashButtonClick(ActionEvent event){
        if(DT){
            Dash();}
    }

    public void onLoadB() throws IOException, ClassNotFoundException {
        //anchorPane.getChildren().removeAll(G1.hero.Node,G1.Islands.get(0).Node);
        //System.out.println(G1.st);
        G1 = G1.load();
        anchorPane.getChildren().addAll(G1.hero.Node,G1.Islands.get(0).Node);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchorPane.getChildren().addAll(G1.hero.Node,G1.Islands.get(0).Node);
    }
}

//text