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
public class EntryPortalScreen extends Screen {    
    
    public EntryPortalScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        
        // SCENE entryPortal
        GridPane entryGrid = new GridPane();
        entryGrid.setPadding(new Insets(20,20,20,20));
        entryGrid.setVgap(15);
        entryGrid.setHgap(15);
        
        Label epNewUserLabel = new Label("New Users:");
        Button epNewUserButton = new Button("Create new account");
        epNewUserButton.setOnAction(e -> this.switchScene("CreateUserScreen.java"));
        Label epLoginLabel = new Label("Returning Users:");
        Button epLoginButton = new Button("Log In");
        epLoginButton.setOnAction(e -> this.switchScene("LoginScreen.java"));
        Label epAdminLoginLabel = new Label("Admin:");
        Button epAdminLoginButton = new Button("Admin Log In");
        epAdminLoginButton.setOnAction(e -> this.switchScene("AdminLoginScreen.java"));
        
        GridPane.setConstraints(epNewUserLabel, 0, 0);
        GridPane.setConstraints(epNewUserButton, 0, 1);
        GridPane.setConstraints(epLoginLabel, 1, 0);
        GridPane.setConstraints(epLoginButton, 1, 1);
        GridPane.setConstraints(epAdminLoginLabel, 2, 0);
        GridPane.setConstraints(epAdminLoginButton, 2, 1);
        
        entryGrid.getChildren().addAll(epNewUserLabel, epNewUserButton, epLoginLabel, epLoginButton, epAdminLoginLabel, epAdminLoginButton);
        
        return new Scene(entryGrid, WIN_WIDTH, WIN_HEIGHT);
    }
    
}
