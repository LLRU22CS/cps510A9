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

/**
 *
 * @author Nathan
 */
public class PracticeQueriesScreen extends Screen {
    
    static int query_val = 0;
    
    public PracticeQueriesScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        
        // SCENE practiceQueries
        Button button_runQuery = new Button();
        button_runQuery.setText("Click me!");
        button_runQuery.setOnAction(e -> testQuery());
        Button button_swapQuery = new Button("Swap Query");
        button_swapQuery.setOnAction(e -> { if (query_val == 3) query_val = 0; else query_val += 1; });
        Button button_return_home = new Button("Return Home");
        button_return_home.setOnAction(e -> this.switchScene("HomepageScreen.java"));
        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(10, 20, 10, 20)); 
        vbButtons.getChildren().addAll(button_runQuery, button_swapQuery, button_return_home);
        return new Scene(vbButtons, WIN_WIDTH, WIN_HEIGHT);
        
    }
    
    public void testQuery() {	
        
        String[] queries = {
            "SELECT movies, actor_first_name, actor_last_name FROM (select COUNT(actor_last_name) as movies, actor_first_name, actor_last_name from actors group by actor_first_name, actor_last_name) WHERE movies > 1 GROUP BY actor_first_name, actor_last_name, movies",
            "SELECT v.title, f.file_location FROM VIDEO v, FILE_LOCATIONS f, VIDEO_CATEGORIES c WHERE v.videoID = c.videoID AND c.categoryID = 103 AND f.videoID = v.videoID AND f.file_type = 'TRAILER'",
            "SELECT title, release_year FROM VIDEO WHERE release_year >= 2000 ORDER BY release_year DESC",
            "SELECT title FROM VIDEO"
	};
        
        
        try {
                    
            Statement stmt = conn1.createStatement();
            ResultSet rs = stmt.executeQuery(queries[query_val]);
            if (rs == null) {
                    System.out.println("receiving");
            }
            System.out.println(formatQueryResults(rs));

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }
    
    public String formatQueryResults(ResultSet out) throws SQLException {
        String result = "";
        if (out != null) {
            
        switch (query_val) {
            case 0:
                while (out.next()) {
                    int num = out.getInt("movies");
                    String name_f = out.getString("actor_first_name");
                    String name_l = out.getString("actor_last_name");
                    result += num + " --> " + name_f + " " + name_l + "\n";
                }
                break;
            case 1:
                while (out.next()) {
                    String title = out.getString("title");
                    String file_location = out.getString("file_location");
                    result += title + " --> " + file_location + "\n";
                }
                break;
            case 2:
                while (out.next()) {
                    String title = out.getString("title");
                    int release_year = out.getInt("release_year");
                    result += title + " --> " + release_year + "\n";
                }
                break;
            case 3:
                while (out.next()) {
                    String title = out.getString("title");
                    result += " ~ " + title + " ~ " + "\n";
                }
                break;
            }
        }
        else {result = "No result";}
        return result;
    }
    
}
