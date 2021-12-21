import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;



class gameobjcts{
    public ImageView Node;
    //private void Collision(){}
}

class islands extends gameobjcts {
    public islands(double x, double y){
        Node = new ImageView("island.png");
        Node.setLayoutX(x);
        Node.setLayoutY(y);
    }
}

class Orc extends gameobjcts{

    public Orc (double x, double y){
        String orcT[] = {"Orc1.png","RedOrc1.png"};
        Random rand = new Random();
        int OT = rand.nextInt(2);
        Node = new ImageView(orcT[OT]);
        Node.setLayoutX(x);
        Node.setLayoutY(y);
    }
}

class Hero extends gameobjcts{
    public Hero(){
        Node = new ImageView("hero.png");
        Node.setLayoutX(60);
        Node.setLayoutY(200);
    }
}

class Game implements Serializable{
    public int[] isl = {0,0};
    public Hero hero;
    public ArrayList<islands> Islands = new ArrayList<islands>();
    public ArrayList<Orc> Orcs = new ArrayList<Orc>();
    public Game(){
        hero = new Hero();
        islands i1 = new islands(40,300);
        Islands.add(i1);
        for (int i = 0; i<3;i++){
        IslandSpawner();
        }
    }

    private void Orc_Spwaner(){

    }

    private void IslandSpawner(){
        double New_Y[] = {-50,0,50};
        Random rand = new Random();
        int y = rand.nextInt(3);
        boolean OV = rand.nextBoolean();
        islands i = new islands((Islands.get(Islands.size()-1).Node.getLayoutX()+ 300),
                (Islands.get(Islands.size()-1).Node.getLayoutY() + New_Y[y]));
        if (i.Node.getLayoutY() > 450){
            i.Node.setLayoutY(450);
        }

        if (i.Node.getLayoutY() < 100){
            i.Node.setLayoutY(100);
        }
        Islands.add(i);
        if (OV){
            Orc orc = new Orc(i.Node.getLayoutX() + 90,i.Node.getLayoutY() - 60);
            Orcs.add(orc);
        }

        //return i;
    }

    public void save(Game G1) throws IOException {
        isl[0] = 200;
        isl[1] = 200;
        String st = "GAME34.ser";
        FileOutputStream fileout = new FileOutputStream(st);
        ObjectOutputStream out = new ObjectOutputStream(fileout);
        out.writeObject(G1.isl);
        out.close();
        fileout.close();
        System.out.println("Saved!");
    }

    public int[] load() throws IOException, ClassNotFoundException {
        int g1[] = null;
        FileInputStream filein = new FileInputStream("GAME34.ser");
        ObjectInputStream in = new ObjectInputStream(filein);
        g1 = (int[]) in.readObject();
        in.close();
        filein.close();
        System.out.println("loaded");
        return g1;
    }
}

public class GameController implements Initializable{
    @FXML
    private Text WILLHERO;
	
	@FXML
    private AnchorPane anchorPane;
    @FXML
    private Button save;

    @FXML
    private Button Start;

    @FXML
    private Button Stop;


    @FXML
    private Button dash;
    
    private boolean hasStarted;
    
    public Game G1 = new Game();

    public TranslateTransition transition = new TranslateTransition();

    Timeline T1 = new Timeline(new KeyFrame(Duration.millis(6), new EventHandler<ActionEvent>() {
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
    	if(hasStarted == false) {
    		hasStarted = true;
	    	TranslateTransition transition = new TranslateTransition();
	    	transition.setNode(WILLHERO);
	    	transition.setDuration(Duration.millis(250));
	    	transition.setByY(-230);
	    	transition.play();
    	}
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

        G1.isl = G1.load();
        G1.Islands.get(0).Node.setLayoutX(G1.isl[0]);
        G1.Islands.get(0).Node.setLayoutY(G1.isl[1]);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchorPane.getChildren().addAll(G1.hero.Node);

        for (int i = 0; i<G1.Islands.size();i++){
            anchorPane.getChildren().addAll(G1.Islands.get(i).Node);
        }

        for (int i = 0; i<G1.Orcs.size();i++){
            anchorPane.getChildren().addAll(G1.Orcs.get(i).Node);
        }
    }
}


//min y = 100
//max y = 450
//shift x = 300
//shift y = 50