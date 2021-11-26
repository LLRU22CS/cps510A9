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
        
        
        final VBox scbox = new VBox();
        scbox.setSpacing(5);
        scbox.setPadding(new Insets(20, 10, 10, 20));
        scbox.getChildren().addAll(sclabel, backButton1, paymentMethodsButton, emailText, usernameText, firstNameText, lastNameText, phoneNumText, dobText);
        return new Scene(scbox, WIN_WIDTH, WIN_HEIGHT);
     }
}