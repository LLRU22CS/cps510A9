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
public class UnixScreen extends Screen {
    
    public UnixScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        
        GridPane unixGrid = new GridPane();
        unixGrid.setPadding(new Insets(20,20,20,20));
        unixGrid.setVgap(15);
        unixGrid.setHgap(15);
        
        Button dropTablesButton = new Button("Drop Tables");
        Button createTablesButton = new Button("Create Tables");
        Button populateTablesButton = new Button("Populate Tables");
        Button queryTablesButton = new Button("Query Tables");
        Button exitButton = new Button("Exit");
        
        exitButton.setOnAction(e -> this.switchScene("EntryPortalScreen.java"));
        dropTablesButton.setOnAction(e -> dropTables());
        createTablesButton.setOnAction(e -> System.out.println("create tables"));
        populateTablesButton.setOnAction(e -> System.out.println("populate tables"));
        queryTablesButton.setOnAction(e -> System.out.println("query tables"));
        
        GridPane.setConstraints(dropTablesButton, 0, 0);
        GridPane.setConstraints(createTablesButton, 0, 1);
        GridPane.setConstraints(populateTablesButton, 0, 2);
        GridPane.setConstraints(queryTablesButton, 0, 3);
        GridPane.setConstraints(exitButton, 0, 4);
        unixGrid.getChildren().addAll(dropTablesButton, createTablesButton, populateTablesButton, queryTablesButton, exitButton);
        
        return new Scene(unixGrid, WIN_WIDTH, WIN_HEIGHT);
    }
    
    private void dropTables() {
        String[] queries = {
            "drop TABLE ACTORS",
            "drop TABLE FILE_LOCATIONS",
            "drop TABLE VIDEO_CATEGORIES",
            "drop TABLE RENTAL_DURATION",
            "drop TABLE CATEGORIES",
            "drop TABLE SHOPPING_CART",
            "drop TABLE WISHLIST",
            "drop TABLE REVIEW",
            "drop TABLE USER_LIBRARY_R2",
            "drop TABLE USER_LIBRARY_R1",
            "drop TABLE USER_TRANSACTION",
            "drop TABLE PAYMENT_METHOD",
            "drop TABLE VIDEO",
            "drop TABLE STORE_USER",
            "drop VIEW TOTAL_POINTS",
            "drop VIEW ALL_SUCCESSFUL_TRANSACTIONS",
            "drop VIEW DISCOUNTED_WEEKEND_RENTALS",
            "drop VIEW GREAT_DEALS_TO_OWN",
            "drop VIEW CARD_EXPIRED_TRANSACTIONS"
        };
        
        try (Statement stmt = conn1.createStatement()) {
            
            for (String query : queries) {
                stmt.executeQuery(query);
                System.out.println(query);
            }
            
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println(e.getErrorCode());
        }
    }
    
    private void createTables() {
        String[] queries = {
            
        };
    }
    
    private void populateTables() {
        
    }
    
    private void queryTables() {
        
    }
    
}
