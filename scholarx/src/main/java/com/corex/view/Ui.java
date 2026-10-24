package com.corex.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Ui extends Application{

public Stage primaryStage;

public void start(Stage myStage) throws Exception {
       
        primaryStage = myStage;
        
        // Text heading = new Text("My firsy Ui");
        // Text tx = new Text();
        // tx.setText("Hello Good Morning");
        
        // Label subHeading = new Label("Welcome to java Fx");
        // Label lb = new Label();
        // lb.setText("Go To Hell");

        // VBox vb1 = new VBox(30, heading, subHeading, tx, lb);
        // VBox vb2 = new VBox(30, heading, subHeading, tx, lb);

        Text tx1 = new Text("First Text");
        Text tx2 = new Text("Second Text");
        Text tx3 = new Text("Third Text");
        Text tx4 = new Text("Fourth Text");
        Text tx5 = new Text("Fifth Text");
        Text tx6 = new Text("Sixth Text");

        // Image img = new Image("assets\\images\\bairan.png");
        // ImageView iv = new ImageView(img);

        VBox vb1 = new VBox(30, tx1,tx2,tx3);
        vb1.setStyle("-fx-background-color: pink; -fx-pref-width: 60    ");
        VBox vb2 = new VBox(30,tx4,tx5,tx6 );
        vb2.setStyle("-fx-background-color: pink;");
        HBox hb = new HBox(60, vb2, vb1);
        hb.setStyle("-fx-background-color: yellow;");


        Scene sc = new Scene(hb, 1000,800);

    
    
        primaryStage.setTitle("First UI");
        // primaryStage.getIcons().add(img);
        primaryStage.setScene(sc);
        primaryStage.show();
    }
    

}