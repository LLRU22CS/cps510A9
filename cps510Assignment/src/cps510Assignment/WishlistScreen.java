/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps510Assignment;

import java.util.HashMap;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/**
 *
 * @author david
 */
public class WishlistScreen extends Screen{
    
    public WishlistScreen(Stage window, HashMap allScenes){
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        final Label wlabel = new Label("Wishlist");
        wlabel.setFont(new Font("Arial", 20));
        Button backButton1 = new Button("Back to Home");
        backButton1.setOnAction(e -> this.switchScene("HomepageScreen.java"));
        Button deleterowButton = new Button("Remove Selected Movie");
        Button addToscButton = new Button("Add Selected Movie to Shopping Cart");
        TableView wishlistTable = new TableView();


        TableColumn MovieNameCol = new TableColumn("Movie");
        TableColumn DirectorCol = new TableColumn("Director");
        TableColumn release_yearCol = new TableColumn("Release Year");
        
        MovieNameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        DirectorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        release_yearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        
        ArrayList<Video> videos = new ArrayList<>();
        
        // try to grab the wishlist items here
        try (Statement stmt = conn1.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT videoID FROM WISHLIST WHERE userID = '" + Main.userID + "'");
            ArrayList<Integer> videoIDs = new ArrayList<>();
            
            while (rs.next()) {
                int videoID = rs.getInt("videoID");
                videoIDs.add(videoID);
            }
            
            for (int videoID : videoIDs) {
                rs = stmt.executeQuery("SELECT * FROM VIDEO_R1 v1, VIDEO_R2 v2 WHERE v1.videoID = '" + videoID + "' AND v1.videoID = v2.videoID");
                
                while (rs.next()) {
                    String title = rs.getString("title");
                    int releaseYear = rs.getInt("release_year");
                    String director = rs.getString("director");
                    double videoDuration = rs.getDouble("video_duration");
                    String rating = rs.getString("rating");
                    String description = rs.getString("video_description");
                    double purchasePrice = rs.getDouble("purchase_price");

                    Video v = new Video(videoID, title, releaseYear, director, videoDuration, rating, description, purchasePrice);
                    videos.add(v);
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }

        wishlistTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        MovieNameCol.setMaxWidth( 1f * Integer.MAX_VALUE * 40 ); 
        DirectorCol.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );
        release_yearCol.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );

        wishlistTable.getColumns().addAll(MovieNameCol, DirectorCol, release_yearCol);
        
        deleterowButton.setOnAction(e -> 
            {
                int selectedIndex = wishlistTable.getSelectionModel().getSelectedIndex();
                wishlistTable.getItems().removeAll(wishlistTable.getSelectionModel().getSelectedItem());
                int selectedVideoID = videos.get(selectedIndex).getVideoID();
                deleteQuery(selectedVideoID);
            });
        
        addToscButton.setOnAction(e -> 
            {
                int selectedIndex = wishlistTable.getSelectionModel().getSelectedIndex();
                wishlistTable.getItems().removeAll(wishlistTable.getSelectionModel().getSelectedItem());
                int selectedVideoID = videos.get(selectedIndex).getVideoID();
                double selectedprice = videos.get(selectedIndex).getPurchasePrice();
                int selectedpoints = videos.get(selectedIndex).getPoints();
                deleteQuery(selectedVideoID);
                addTosc(selectedVideoID, selectedprice, selectedpoints);
            });
        
        if (videos.isEmpty()) {
            wishlistTable.setPlaceholder(new Label("No rows to display"));
        } else {
            for (Video v : videos) {
                wishlistTable.getItems().add(v);
            }
        }
        
        final VBox wishbox = new VBox();
        wishbox.setSpacing(5);
        wishbox.setPadding(new Insets(20, 10, 10, 20));
        wishbox.getChildren().addAll(wlabel, wishlistTable, addToscButton, deleterowButton, backButton1);
        return new Scene(wishbox, WIN_WIDTH, WIN_HEIGHT);
     }
    
    private void deleteQuery(int videoid){
        try(Statement stmt = conn1.createStatement()){
            stmt.executeQuery("DELETE FROM WISHLIST WHERE userID = '" + Main.userID + "' AND videoID = '" + videoid + "'");
        }catch (SQLException e){
            System.out.println(e);
            
        }
    }
    
    private void addTosc(int videoid, double price, int points){
        try(Statement stmt = conn1.createStatement()){
            stmt.executeQuery("INSERT INTO SHOPPING_CART VALUES ('" + Main.userID + "','" + videoid + "'," +price+ ","+points+")");
        }catch (SQLException e){
            System.out.println(e);
            
        }
    }
}