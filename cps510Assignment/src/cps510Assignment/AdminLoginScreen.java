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
public class AdminLoginScreen extends Screen {
        
    public AdminLoginScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        // SCENE adminLogin 
        GridPane adminLoginLayout = new GridPane();
        adminLoginLayout.setPadding(new Insets(20,20,20,20));
        adminLoginLayout.setVgap(15);
        adminLoginLayout.setHgap(15);
        Label adminUsername = new Label("Username: ");
        Label adminPassword = new Label("Password: ");
        TextField adminUsernameText = new TextField();
        adminUsernameText.setPromptText("username");
        PasswordField adminPasswordText = new PasswordField();
        adminPasswordText.setPromptText("password");
        Button adminLoginButton = new Button("Log In");
        adminLoginButton.setOnAction(e -> {
            if (adminUsernameText.getText().equals("adminGroup94") && adminPasswordText.getText().equals("p@ssw0rd")) {
//                window.setScene(adminHomePage);
                System.out.println("adminLogin success");
            } else {
                cps510Assignment.AlertBox.display("Failure", "Please try again.");
            }
        });
        Button adminReturnToEPButton = new Button("Back");
        adminReturnToEPButton.setOnAction(e -> this.switchScene("EntryPortalScreen.java"));
        GridPane.setConstraints(adminUsername, 0, 0);
        GridPane.setConstraints(adminPassword, 0, 1);
        GridPane.setConstraints(adminUsernameText, 1, 0);
        GridPane.setConstraints(adminPasswordText, 1, 1);
        GridPane.setConstraints(adminLoginButton, 1, 2);
        GridPane.setConstraints(adminReturnToEPButton, 2, 2);
        adminLoginLayout.getChildren().addAll(adminUsername, adminPassword, adminUsernameText, adminPasswordText, adminLoginButton, adminReturnToEPButton);
        
        return new Scene(adminLoginLayout, WIN_WIDTH, WIN_HEIGHT);       
    }
    
}
