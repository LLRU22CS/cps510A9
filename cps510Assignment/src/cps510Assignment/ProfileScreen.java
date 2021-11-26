package cps510Assignment;

import java.util.HashMap;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.text.Text;
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
public class ProfileScreen extends Screen{
    
    public ProfileScreen(Stage window, HashMap allScenes){
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        final Label sclabel = new Label("My Profile");
        sclabel.setFont(new Font("Arial", 20));
        Button backButton1 = new Button("Back to Home");
        backButton1.setOnAction(e -> this.switchScene("HomepageScreen.java"));
        Button paymentMethodsButton = new Button("My Payment Methods");
        paymentMethodsButton.setOnAction(e -> this.switchScene("PaymentMethodScreen.java"));
        
        Text emailText = new Text("");
        Text usernameText = new Text("");
        Text firstNameText = new Text("");
        Text lastNameText = new Text("");
        Text phoneNumText = new Text("");
        Text dobText = new Text("");
        

        try (Statement stmt = conn1.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM STORE_USER WHERE userID = '" + Main.userID + "'");
            
            while (rs.next()) {
                String email = rs.getString("email");
                String username = rs.getString("username");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                double phoneNum = rs.getDouble("phone_number");
                Date dob = rs.getDate("date_of_birth");
                
                emailText.setText("Email: " + email);
                usernameText.setText("Username: " + username);
                firstNameText.setText("First Name: " + firstName);
                lastNameText.setText("Last Name: " + lastName);
                phoneNumText.setText("Phone Number: " + String.format("%10.0f", phoneNum));
                dobText.setText("Date of Birth: " + dob.toString());
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        Label passLabel = new Label("Update Password: ");
        passLabel.setFont(new Font("Arial", 18));
        TextField passField = new TextField();
        passField.setPromptText("New Password");
        Button passButton = new Button("Update");
        passButton.setOnAction(e -> {
        	if (updatePassword(passField)) AlertBox.display("Success", "Password updated");
        	this.switchScene("ProfileScreen.java");
        });
        final HBox passUpdate = new HBox(5);
        passUpdate.setPadding(new Insets(20, 10, 10, 20));
        passUpdate.getChildren().addAll(passLabel, passField, passButton);
        final VBox scbox = new VBox();
        scbox.setSpacing(5);
        scbox.setPadding(new Insets(20, 10, 10, 20));
        scbox.getChildren().addAll(sclabel, backButton1, paymentMethodsButton, emailText, usernameText, firstNameText, lastNameText, phoneNumText, dobText, passUpdate);
        return new Scene(scbox, WIN_WIDTH, WIN_HEIGHT);
     }

	private boolean updatePassword(TextField pw) {
		String s = pw.getText();
		boolean result = true;
    	if (s.equals("")) {
        	AlertBox.display("Unable to Update", "Password must not be empty.");  
     	   result = false;    
    	}
    	PreparedStatement pwStmt = null;
        try {
        	pwStmt = conn1.prepareStatement("UPDATE store_user SET user_password = (?) WHERE userID = " + Main.userID);
        	pwStmt.setString(1, s);
        	pwStmt.executeUpdate();
        } catch (SQLException a) {
        	a.printStackTrace();
        	AlertBox.display("Unable to Update", "Password not accepted.");
     	   result = false;
        }
        finally {
           try {
              if (pwStmt != null) { pwStmt.close(); }
           }
           catch (Exception a) {
              // log this error
        	   result = false;
           }
        }
        return result;
	}
}