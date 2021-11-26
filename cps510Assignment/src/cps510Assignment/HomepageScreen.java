/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps510Assignment;

import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.sql.ResultSet;
import java.util.Properties;

/**
 *
 * @author Nathan
 */
public class HomepageScreen extends Screen {
    
    public HomepageScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        
        /*
	ScrollPane hpScrollPane_Grid;
	HBox hpHBox_ScrollPane_Grid;*/
        GridPane hpGrid = new GridPane();
        hpGrid.setPadding(new Insets(20,20,20,20));
        hpGrid.setVgap(15);
        hpGrid.setHgap(15);
        Button hpButton1 = new Button("Sign Out");
        hpButton1.setOnAction(e -> {
        	Main.userID = -1;
        	this.switchScene("EntryPortalScreen.java");
        });
        
        Button hpButton2 = new Button("Wishlist");
        hpButton2.setOnAction(e -> this.switchScene("WishlistScreen.java"));
        Button hpButton3 = new Button("Shopping Cart");
        hpButton3.setOnAction(e -> this.switchScene("ShoppingCartScreen.java"));
        Button hpButton4 = new Button("Profile");
        hpButton4.setOnAction(e -> this.switchScene("ProfileScreen.java"));
        
        ScrollPane hpScrollPane_Grid = new ScrollPane();
        hpScrollPane_Grid.setPannable(true);
        hpScrollPane_Grid.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        hpScrollPane_Grid.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        hpScrollPane_Grid.fitToHeightProperty().set(true);
        HBox hpHBox_ScrollPane_Grid = new HBox(50);
        int videoID, release_year;
        String title, thumbnailFilepath;
        Label titleLabel, release_yearLabel, videoIDLabel;
        Image thumbnail;
        ImageView imageView;
        try (Statement defaultDisplayStmt = conn1.createStatement()) {
            String defaultDisplayQuery = "SELECT videoID, title, release_year FROM video_r1 ORDER BY videoID";
            ResultSet getTopUIDResult = defaultDisplayStmt.executeQuery(defaultDisplayQuery);
           
            while (getTopUIDResult.next()) {
                VBox currVBox = new VBox();
                currVBox.setStyle("-fx-background-color: #FFFFFF;");
                VBox currSubVBox = new VBox(0);
                currSubVBox.setPadding(new Insets(18, 10, 0, 10));
                videoID = getTopUIDResult.getInt("videoID");
                title = getTopUIDResult.getString("title");
                release_year = getTopUIDResult.getInt("release_year");
                titleLabel = new Label(title);
                release_yearLabel = new Label(String.valueOf(release_year));
                videoIDLabel = new Label(String.valueOf(videoID));
                videoIDLabel.setVisible(false);
                titleLabel.setFont(new Font("Calibri", 20)); 
                release_yearLabel.setFont(new Font("Calibri", 20));
                thumbnailFilepath = "/thumbnails/THUMBNAIL_" + String.valueOf(videoID) + ".jpg";
                thumbnail = new Image(thumbnailFilepath);
                imageView = new ImageView(thumbnail);
                imageView.setPreserveRatio(true); 
                imageView.setFitHeight(410);
                currVBox.setAlignment(Pos.CENTER);
                currSubVBox.setAlignment(Pos.CENTER);
                currSubVBox.getChildren().addAll(titleLabel, release_yearLabel, videoIDLabel);
                currVBox.getChildren().addAll(imageView, currSubVBox);
				currVBox.setPickOnBounds(true);	
				currSubVBox.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
					currSubVBox.setStyle("-fx-background-color: #464d7740;");
				});
				currSubVBox.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
					currSubVBox.setStyle("-fx-background-color: #FFFFFF;");
				});
				currSubVBox.setOnMouseClicked(e -> {
		            Data.videoID = Integer.parseInt(((Label)currSubVBox.getChildren().get(2)).getText());
//		            Data.printData();
		            this.switchScene("VideoViewScreen.java");
		        });
                hpHBox_ScrollPane_Grid.getChildren().add(currVBox);
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        
        hpScrollPane_Grid.setContent(hpHBox_ScrollPane_Grid);
        hpGrid.add(hpButton1, 0, 0);
        hpGrid.add(hpButton2, 2, 0);
        hpGrid.add(hpButton3, 3, 0);
        hpGrid.add(hpButton4, 4, 0);
        hpGrid.add(hpScrollPane_Grid, 0, 1, 5, 1);
//        hpGrid.getChildren().addAll(hpLabel1, hpButton1, hpScrollPane_Grid);
        Scene homePage = new Scene(hpGrid, WIN_WIDTH, WIN_HEIGHT);
        hpScrollPane_Grid.setStyle("-fx-focus-color: transparent;");
        
        return homePage;
        
    }
    
}