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
public class LoginScreen extends Screen {    
    
    public LoginScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        
        // SCENE login
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20,20,20,20));
        loginGrid.setVgap(15);
        loginGrid.setHgap(15);
        Label username = new Label("Username: ");
        Label password = new Label("Password: ");
        TextField username_text = new TextField();
        username_text.setPromptText("username");
        PasswordField password_text = new PasswordField();
        password_text.setPromptText("password");
        Button login_button = new Button("Log In");
        login_button.setOnAction(e -> {
        	boolean loginSuccess = loginAttempt(username_text, password_text);
        	if (loginSuccess) {
                    this.switchScene("HomepageScreen.java");
        	}
        });
        Button return_to_ep_button = new Button("Back");
        return_to_ep_button.setOnAction(e -> this.switchScene("EntryPortalScreen.java"));
        Button buttonNew = new Button("Bypass");
        buttonNew.setOnAction(e -> this.switchScene("HomepageScreen.java"));
        GridPane.setConstraints(username, 0, 0);
        GridPane.setConstraints(password, 0, 1);
        GridPane.setConstraints(username_text, 1, 0);
        GridPane.setConstraints(password_text, 1, 1);
        GridPane.setConstraints(login_button, 1, 2);
        GridPane.setConstraints(buttonNew, 3, 2);
        GridPane.setConstraints(return_to_ep_button, 2, 2);
        loginGrid.getChildren().addAll(username, password, username_text, password_text, login_button, return_to_ep_button, buttonNew);
        return new Scene(loginGrid, WIN_WIDTH, WIN_HEIGHT);
        
    }
    
    private boolean loginAttempt(TextField un, TextField pw) {
        boolean result = false;
        ResultSet rs = null;
        try (Statement stmtL = conn1.createStatement()) {
//            loginStmt = conn1.prepareStatement("SELECT user_password FROM STORE_USER WHERE username = ?");
//            loginStmt.setString(1, un.getText());
//            rs = loginStmt.executeQuery();
            String loginQuery = "SELECT user_password FROM STORE_USER WHERE username = '" + un.getText() + "'";
            rs = stmtL.executeQuery(loginQuery);

            if (rs.next()) {
                String expected_pw = rs.getString("user_password");
                if (!pw.getText().equals(expected_pw)) {
                    cps510Assignment.AlertBox.display("Error", "Username/Password incorrect.");
                } else {
                    result = true;
                }
            } else {
                cps510Assignment.AlertBox.display("Error", "Username/Password incorrect.");
            }

        } catch (SQLException e) {
//            System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
        finally {
//            try {
//               if (loginStmt != null) { loginStmt.close(); }
//            }
//            catch (Exception e) {
//                        System.out.println("loginStmt close exception");
//            }
        }
        return result;
    }
    
}
