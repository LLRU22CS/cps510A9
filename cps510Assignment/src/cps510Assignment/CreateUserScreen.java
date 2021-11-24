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
public class CreateUserScreen extends Screen {
    
    public CreateUserScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        
        // SCENE newUser
        GridPane newUserLayout = new GridPane();
        newUserLayout.setPadding(new Insets(20,20,20,20));
        newUserLayout.setVgap(15);
        newUserLayout.setHgap(15);
        
        Label firstNameLabel = new Label("First Name: ");
        Label lastNameLabel = new Label("Last Name: ");
        Label DOBLabel = new Label("Date of Birth: ");
        Label emailLabel = new Label("Email: ");
        Label phoneLabel = new Label("Phone Number: ");
        Label usernameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");
        TextField firstNameText = new TextField();
        TextField lastNameText = new TextField();
        DatePicker dateOfBirth = new DatePicker();
        TextField emailText = new TextField();
        emailText.setPromptText("example@domain.com");
        TextField phoneText = new TextField();
        phoneText.setPromptText("1234567890");
        TextField usernameText = new TextField();
        PasswordField passwordText = new PasswordField();
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
        	e.consume();
        	if (validateNewUser(firstNameText, lastNameText, dateOfBirth, emailText, phoneText, usernameText, passwordText)) {
        		this.switchScene("LoginScreen.java");
        		AlertBox.display("Success", "Please log in.");
        	} else {
        		AlertBox.display("Failure", "Please try again.");
        	}
        });
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
        	firstNameText.clear();
        	lastNameText.clear();
        	dateOfBirth.setValue(null);
        	emailText.clear();
        	phoneText.clear();
        	usernameText.clear();
        	passwordText.clear();
        });
        Button newUserBackButton = new Button("Back");
        newUserBackButton.setOnAction(e -> this.switchScene("EntryPortalScreen.java"));   
        GridPane.setConstraints(firstNameLabel, 0, 0);
        GridPane.setConstraints(lastNameLabel, 0, 1);
        GridPane.setConstraints(DOBLabel, 0, 2);
        GridPane.setConstraints(emailLabel, 0, 3);
        GridPane.setConstraints(phoneLabel, 0, 4);
        GridPane.setConstraints(usernameLabel, 0, 5);
        GridPane.setConstraints(passwordLabel, 0, 6);     
        GridPane.setConstraints(firstNameText, 1, 0);
        GridPane.setConstraints(lastNameText, 1, 1);
        GridPane.setConstraints(dateOfBirth, 1, 2);
        GridPane.setConstraints(emailText, 1, 3);
        GridPane.setConstraints(phoneText, 1, 4);
        GridPane.setConstraints(usernameText, 1, 5);
        GridPane.setConstraints(passwordText, 1, 6);    
        GridPane.setConstraints(submitButton, 1, 7);
        GridPane.setConstraints(clearButton, 0, 7);
        GridPane.setConstraints(newUserBackButton, 2, 7);  
        newUserLayout.getChildren().addAll(firstNameLabel, lastNameLabel, DOBLabel, emailLabel, phoneLabel, usernameLabel, passwordLabel, firstNameText, lastNameText, dateOfBirth, emailText, phoneText, usernameText, passwordText, submitButton, clearButton, newUserBackButton);
//        newUserLayout.setAlignment(Pos.CENTER);
	return new Scene(newUserLayout, WIN_WIDTH, WIN_HEIGHT);   
    }
    
    private boolean validateNewUser(TextField firstName, TextField lastName, DatePicker dateOfBirth, TextField email, TextField phone, TextField username, PasswordField password) {
        boolean result = false;
        int prevTopUID = 0, newTopUID;
        try (Statement getTopUIDStmt = conn1.createStatement()) {
            String getTopUIDQuery = "SELECT MAX(userID) AS maxUID FROM store_user";
            ResultSet getTopUIDResult = getTopUIDStmt.executeQuery(getTopUIDQuery);
            if (getTopUIDResult.next()) {
                    prevTopUID = getTopUIDResult.getInt("maxUID");
            }
        } catch (SQLException e) {
                System.out.println(e.getErrorCode());
        } 
        newTopUID = prevTopUID + 1;
        PreparedStatement newUserStmt = null;
        try {
            newUserStmt = conn1.prepareStatement("INSERT INTO store_user (userID, email, username, user_password, first_name, last_name, phone_number, date_of_birth) values (?, ?, ?, ?, ?, ?, ?, ?)");
            newUserStmt.setInt(1, newTopUID);
            newUserStmt.setString(2, email.getText());
            newUserStmt.setString(3, username.getText());
            newUserStmt.setString(4, password.getText());
            newUserStmt.setString(5, firstName.getText());
            newUserStmt.setString(6, lastName.getText());
            newUserStmt.setLong(7, Long.parseLong(phone.getText()));
            java.util.Date date = java.util.Date.from(dateOfBirth.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            newUserStmt.setDate(8, sqlDate);
            newUserStmt.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
           try {
              if (newUserStmt != null) { newUserStmt.close(); }
           }
           catch (Exception e) {
              // log this error
           }
        }
        return result;
    }
    
}
