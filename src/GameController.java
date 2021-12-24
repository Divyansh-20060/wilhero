import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
import java.util.concurrent.TimeUnit;

import javafx.animation.ScaleTransition;
import javafx.animation.Interpolator;


class gameobjcts{
    public ImageView Node;
    //private void Collision(){}
}

class islands extends gameobjcts {
    public int coins = 0;
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

class Coin extends gameobjcts{
    public Coin(double x, double y){
        Node = new ImageView("Coin.png");
        Node.setLayoutX(x+90);
        Node.setLayoutY(y);
    }
}

class Game implements Serializable{
    public int[] isl = {0,0};
    public int coinsCollected;
    public Hero hero;
    
    public ArrayList<islands> Islands = new ArrayList<islands>();
    public ArrayList<Orc> Orcs = new ArrayList<Orc>();
    public ArrayList<Coin> Coins = new ArrayList<Coin>();
    public Game(){
        hero = new Hero();
        
        islands i1 = new islands(40,300);
        Islands.add(i1);
        for (int i = 0; i<3;i++){
        IslandSpawner();
        }
    }

    public void IslandSpawner(){
        double New_Y[] = {-50,0,50};
        Random rand = new Random();
        int y = rand.nextInt(3);
        boolean OV = rand.nextBoolean();
        islands i = new islands((Islands.get(Islands.size()-1).Node.getLayoutX()+ 450),
                (Islands.get(Islands.size()-1).Node.getLayoutY() + New_Y[y]));
        if (i.Node.getLayoutY() > 450){
            i.Node.setLayoutY(450);
        }

        if (i.Node.getLayoutY() < 100){
            i.Node.setLayoutY(100);
        }
        Islands.add(i);
        if (OV){
            Orc orc = new Orc(i.Node.getLayoutX() + 90,i.Node.getLayoutY() - 55);
            Orcs.add(orc);
        }

        else{
            int No_C = rand.nextInt(4);
            if(No_C != 0){
                i.coins = No_C;
                double DX = i.Node.getLayoutX() + 30;
                for(int j = 0; j<2;j ++){
                    double DY = i.Node.getLayoutY() - 140;
                    for(int k = 0; k <No_C; k++){
                        
                        Coins.add(new Coin(DX,DY));
                        DY = DY - 40;
                    }
                    DX = DX - 50;
                }
            }
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
    private ImageView coinIcon;
	
	@FXML
    private Label coinCounter;
    
	@FXML
    private Text WILLHERO;
	
	@FXML
    private AnchorPane anchorPane;
    @FXML
    private Button save;

    @FXML
    private Button Start;

    @FXML
    private ImageView BG;

    @FXML
    private Button Stop;


    @FXML
    private Button dash;
    
    private boolean hasStarted;
    
    public Game G1 = new Game();

    public TranslateTransition transition = new TranslateTransition();

    private boolean DT = false;
    
    Timeline Dash_orc = new Timeline(new KeyFrame(Duration.millis(4), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        	
        }
    }
    ));
    
    Timeline T2 = new Timeline(new KeyFrame(Duration.millis(4), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        	for (int j = 0; j < G1.Orcs.size(); j++) {
        		G1.Orcs.get(j).Node.setLayoutY(G1.Orcs.get(j).Node.getLayoutY() + 2);
        	}
        	
            for (int i = 0;i < G1.Islands.size(); i++){
            	for (int j = 0 ;j < G1.Orcs.size(); j++) {
	                if(G1.Orcs.get(j).Node.getBoundsInParent().intersects(G1.Islands.get(i).Node.getBoundsInParent())){
	                T2.stop();
	                jump_orc();
	                }
            	}

            }
        }
    }
    ));
    
    

    Timeline T1 = new Timeline(new KeyFrame(Duration.millis(4), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        	if (jump.getStatus() != Animation.Status.RUNNING && Dash.getStatus() != Animation.Status.RUNNING) {
        		G1.hero.Node.setLayoutY(G1.hero.Node.getLayoutY() + 2);
        		for (int i = 0; i < G1.Coins.size(); i++) {
        			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
        				
        				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
        				G1.Coins.remove(i);
        				G1.coinsCollected++;
        				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+1));
        				System.out.println("coin removed");
        				
        			}
        		}
        	}
            for (int i = 0;i < G1.Islands.size(); i++){
                if(G1.hero.Node.getBoundsInParent().intersects(G1.Islands.get(i).Node.getBoundsInParent())){
                T1.stop();
                Dash.stop();

                
                jump(G1.hero.Node);
                }

            }
        }
    }
    ));
    
    Timeline jump_orc = new Timeline(new KeyFrame(Duration.millis(7), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {
        	for (int i = 0; i < G1.Orcs.size(); i++) {
        		if(T2.getStatus() != Animation.Status.RUNNING){
        		G1.Orcs.get(i).Node.setLayoutY(G1.Orcs.get(i).Node.getLayoutY() - 3);
                }
        	}

        }
    }));
    
    Timeline jump = new Timeline(new KeyFrame(Duration.millis(7), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {
        	if ( Dash.getStatus() != Animation.Status.RUNNING) {
        	G1.hero.Node.setLayoutY(G1.hero.Node.getLayoutY() - 3);
    		for (int i = 0; i < G1.Coins.size(); i++) {
    			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
    				
    				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
    				G1.Coins.remove(i);
    				G1.coinsCollected++;
    				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+1));
    				System.out.println("coin removed");
    				
    			}
    		}
        	}
        	else {
        		jump.stop();
        	}
        }
    }));
    
    Timeline DashBack = new Timeline(new KeyFrame(Duration.millis(2), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {


            //////////buggy code starts

            boolean leftI = G1.Islands.get(0).Node.getBoundsInParent().getMaxX() <= BG.getBoundsInParent().getMinX();
            boolean leftO;
            if(G1.Orcs.size() != 0){
            leftO = G1.Orcs.get(0).Node.getBoundsInParent().getMaxX() <= BG.getBoundsInParent().getMinX();
            }
            else{
                leftO = false;
            }
            if(leftI){
                anchorPane.getChildren().removeAll(G1.Islands.get(0).Node);
                G1.Islands.remove(0);
                G1.IslandSpawner();
                anchorPane.getChildren().addAll(G1.Islands.get(G1.Islands.size() - 1).Node);
                for(int i = 0; i < G1.Orcs.size(); i++){
                    if(!anchorPane.getChildren().contains(G1.Orcs.get(i).Node)){
                        anchorPane.getChildren().addAll(G1.Orcs.get(i).Node);
                    }
                }

                for(int i = 0; i < G1.Coins.size(); i++){
                    if(!anchorPane.getChildren().contains(G1.Coins.get(i).Node)){
                        anchorPane.getChildren().addAll(G1.Coins.get(i).Node);
                    }
                }
            }
            if(leftO){
                System.out.println("ayo");
                anchorPane.getChildren().removeAll(G1.Orcs.get(0).Node);
                G1.Orcs.remove(0);
            }

            /////////clean code starts

        	if (G1.hero.Node.getLayoutX() >= 60 && G1.hero.Node.getLayoutX() <= 250) {

        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() - 0.5 );
        		for (int i = 0; i < G1.Islands.size(); i++) {
        			G1.Islands.get(i).Node.setLayoutX(G1.Islands.get(i).Node.getLayoutX() -0.5 );
        		}
        		for (int i = 0; i < G1.Orcs.size(); i++) {
        			G1.Orcs.get(i).Node.setLayoutX(G1.Orcs.get(i).Node.getLayoutX() -0.5 );

                }

                for (int i = 0; i < G1.Coins.size(); i++) {
                    G1.Coins.get(i).Node.setLayoutX(G1.Coins.get(i).Node.getLayoutX() -0.5 );
                }

        	}
        	else if(G1.hero.Node.getLayoutX() >250) {
        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() - 0.5 );
        		for (int i = 0; i < G1.Islands.size(); i++) {
        			G1.Islands.get(i).Node.setLayoutX(G1.Islands.get(i).Node.getLayoutX() -1.5 );
        		}
        		for (int i = 0; i < G1.Orcs.size(); i++) {
        			G1.Orcs.get(i).Node.setLayoutX(G1.Orcs.get(i).Node.getLayoutX() -1.5 );

                }

                for (int i = 0; i < G1.Coins.size(); i++) {
                    G1.Coins.get(i).Node.setLayoutX(G1.Coins.get(i).Node.getLayoutX() -1.5 );
                }
        	}
        	
        }
    }));
    
    Timeline rest_orc = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        
        }
    }));
    
    Timeline rest = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
        
        @Override
        public void handle(ActionEvent event) {
        		
        	
        }
    }));
    
   
    Timeline Dash = new Timeline(new KeyFrame(Duration.millis(2), new EventHandler<ActionEvent>() {
        
        @Override
        
        public void handle(ActionEvent event) {
        	
        	
        	if (G1.hero.Node.getLayoutX() <= 250 ) {
        		
        		G1.hero.Node.setLayoutX(G1.hero.Node.getLayoutX() + 2.5);
        		for (int i = 0; i < G1.Coins.size(); i++) {
        			if (G1.hero.Node.getBoundsInParent().intersects(G1.Coins.get(i).Node.getBoundsInParent())) {
        				
        				anchorPane.getChildren().removeAll(G1.Coins.get(i).Node);
        				G1.Coins.remove(i);
        				G1.coinsCollected++;
        				coinCounter.setText(Integer.toString(Integer.parseInt(coinCounter.getText())+1));
        				System.out.println("coin removed");
        			}
        		}
                for (int i = 0;i < G1.Islands.size(); i++){
                    if((G1.hero.Node.getBoundsInParent().intersects(G1.Islands.get(i).Node.getBoundsInParent())) && G1.hero.Node.getBoundsInParent().getMaxY() > (G1.Islands.get(i).Node.getBoundsInParent()).getMinY() +2 ){
                    T1.stop();
                    Dash.stop();

                    
                    jump(G1.hero.Node);
                    }
                    

                }
        	}	
        	
        }
    }));


    public GameController() throws IOException {
    }

    public void Dash(){
    	jump.stop();
    	T1.stop();
        Dash.setCycleCount(70);
        DashBack.setCycleCount(Animation.INDEFINITE);
        DashBack.play();
       
        
        Dash.play();
        
        Dash.setOnFinished(e -> {
            boolean left = G1.Islands.get(0).Node.getBoundsInLocal().getMaxX() <= BG.getBoundsInLocal().getMinX();
            if(left){
                anchorPane.getChildren().remove(G1.Islands.get(0).Node);
                G1.Islands.remove(0);
                G1.IslandSpawner();
                anchorPane.getChildren().addAll(G1.Islands.get(G1.Islands.size() -1 ).Node);
            }
//
//            if(!G1.Orcs.get(0).Node.getBoundsInParent().intersects(BG.getBoundsInParent())){
//                anchorPane.getChildren().removeAll(G1.Orcs.get(0).Node);
//                G1.Orcs.remove(0);
//            }

            T1.play();
        }
        );
        

        

    }
    public void jump_orc() {
    	jump_orc.setCycleCount(56);
    	rest_orc.setCycleCount(70);
    	rest_orc.play();
    	rest_orc.setOnFinished(event -> jump_orc.play());
    	jump_orc.setOnFinished(event -> T2.play());
    }
    public void jump(Node pp){
//	ScaleTransition scale = new ScaleTransition();
//    	scale.setNode(pp);
//    	scale.setDuration(Duration.millis(80));
//    	scale.setCycleCount(0);
//    	scale.setInterpolator(Interpolator.LINEAR);
//    	scale.setToY(1);
////    	scale.setAutoReverse(true);
//    	scale.play();
    	
    	jump.setCycleCount(56);
    	rest.setCycleCount(15);
    	rest.play();
//    	Dash.setOnFinished(event -> rest.stop());
    	rest.setOnFinished(event -> jump.play());
    	jump.setOnFinished(e -> T1.play());
//        TranslateTransition transition = new TranslateTransition();
//        transition.setDuration(Duration.seconds(0.5));
//        transition.setAutoReverse(false);
//        transition.setCycleCount(0);
//        //while(System.get)
//        transition.setByY(-180);
//        transition.setNode(pp);
//        transition.play();
//        transition.setOnFinished(e->T1.play());
    	
    }

    public void onStartButtonClick() {
    	if(!hasStarted) {
    		hasStarted = true;
	    	TranslateTransition transition = new TranslateTransition();
	    	transition.setNode(WILLHERO);
	    	transition.setDuration(Duration.millis(250));
	    	transition.setByY(-230);
	    	transition.play();
    	}
    	
        DT = true;
        T2.setCycleCount(Animation.INDEFINITE);
        T2.play();
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

        for (int i = 0; i<G1.Coins.size();i++){
            anchorPane.getChildren().addAll(G1.Coins.get(i).Node);
        }
        
        
    }
}


//min y = 100
//max y = 450
//shift x = 300
//shift y = 50
