package cps510Assignment;

import static cps510Assignment.Screen.WIN_WIDTH;
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
 /*
 * @author david
 */
public class ShoppingCartScreen extends Screen{
    
    public ShoppingCartScreen(Stage window, HashMap allScenes){
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        final Label sclabel = new Label("Shopping Cart");
        sclabel.setFont(new Font("Arial", 20));
        Button backButton1 = new Button("Back to Home");
        backButton1.setOnAction(e -> this.switchScene("HomepageScreen.java"));
        TableView scTable = new TableView();


        TableColumn MovieNameCol = new TableColumn("Movie");
        TableColumn DirectorCol = new TableColumn("Director");
        TableColumn release_yearCol = new TableColumn("Release Year");
        TableColumn priceCol = new TableColumn("Price");
        TableColumn pointsCol = new TableColumn("Points");
        
        MovieNameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        DirectorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        release_yearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        
        ArrayList<Video> videos = new ArrayList<>();
        
        // try to grab the wishlist items here
        try (Statement stmt = conn1.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT videoID, points FROM SHOPPING_CART WHERE userID = '" + Main.userID + "'");
            HashMap<Integer, Double> videoPoints = new HashMap<>();
            
            while (rs.next()) {
                int videoID = rs.getInt("videoID");
                double points = rs.getDouble("points");
                videoPoints.put(videoID, points);
            }
            
            for (int videoID : videoPoints.keySet()) {
                double points = videoPoints.get(videoID);
                
                rs = stmt.executeQuery("SELECT * FROM VIDEO_R1 v1, VIDEO_R2 v2 WHERE v1.videoID = '" + videoID + "' AND v1.videoID = v2.videoID");
                
                while (rs.next()) {
                    String title = rs.getString("title");
                    int releaseYear = rs.getInt("release_year");
                    String director = rs.getString("director");
                    double videoDuration = rs.getDouble("video_duration");
                    String rating = rs.getString("rating");
                    String description = rs.getString("video_description");
                    double purchasePrice = rs.getDouble("purchase_price");

                    Video v = new Video(videoID, title, releaseYear, director, videoDuration, rating, description, purchasePrice, points);
                    videos.add(v);
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        
        scTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        MovieNameCol.setMaxWidth( 1f * Integer.MAX_VALUE * 30 ); 
        DirectorCol.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        release_yearCol.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        priceCol.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );
        pointsCol.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );

        scTable.getColumns().addAll(MovieNameCol, DirectorCol, release_yearCol, priceCol, pointsCol);
        
        if (videos.isEmpty()) {
            scTable.setPlaceholder(new Label("No rows to display"));
        } else {
            for (Video v : videos) {
                scTable.getItems().add(v);
            }
        }
        
        final VBox scbox = new VBox();
        scbox.setSpacing(5);
        scbox.setPadding(new Insets(20, 10, 10, 20));
        scbox.getChildren().addAll(sclabel, scTable, backButton1);
        return new Scene(scbox, WIN_WIDTH, WIN_HEIGHT);
     }
    
    
}