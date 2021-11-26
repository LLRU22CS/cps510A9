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
 /*
 * @author david
 */
public class PaymentMethodScreen extends Screen{
    
    public PaymentMethodScreen(Stage window, HashMap allScenes){
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
        final Label sclabel = new Label("Payment Methods");
        sclabel.setFont(new Font("Arial", 20));
        Button backButton1 = new Button("Back to Home");
        backButton1.setOnAction(e -> this.switchScene("HomepageScreen.java"));
        Button addrowButton = new Button("Add Payment Method");
        Button deleterowButton = new Button("Remove Selected Payment Method");
        TableView scTable = new TableView();


        TableColumn cardTypeCol = new TableColumn("Card Type");
        TableColumn cardNumCol = new TableColumn("Card Number");
        TableColumn cardExpCol = new TableColumn("Card Exp Date");
        TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        
        cardTypeCol.setCellValueFactory(new PropertyValueFactory<>("cardType"));
        cardNumCol.setCellValueFactory(new PropertyValueFactory<>("cardNum"));
        cardExpCol.setCellValueFactory(new PropertyValueFactory<>("cardExp"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        
        try (Statement stmt = conn1.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM PAYMENT_METHOD WHERE userID = '" + Main.userID + "'");
            
            while (rs.next()) {
                int paymentMethodID = rs.getInt("paymentID");
                String cardType = rs.getString("card_type");
                String cardNum = String.format("%16.0f", rs.getDouble("card_number"));
                Date cardExp = rs.getDate("card_expiry_date");
                String firstName = rs.getString("cardholder_first_name");
                String lastName = rs.getString("cardholder_last_name");
                
                PaymentMethod p = new PaymentMethod(paymentMethodID, cardNum, cardType, cardExp.toString(), firstName, lastName);
                paymentMethods.add(p);
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        addrowButton.setOnAction(e -> {
            
        });
        
        deleterowButton.setOnAction(e -> {
            int selectedIndex = scTable.getSelectionModel().getSelectedIndex();
            scTable.getItems().removeAll(scTable.getSelectionModel().getSelectedItem());
            int selectedPaymentID = paymentMethods.get(selectedIndex).getPaymentMethodID();
            deleteQuery(selectedPaymentID);
        });
        
        scTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        cardTypeCol.setMaxWidth( 1f * Integer.MAX_VALUE * 30 ); 
        cardNumCol.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        cardExpCol.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        firstNameCol.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );
        lastNameCol.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );

        scTable.getColumns().addAll(cardTypeCol, cardNumCol, cardExpCol, firstNameCol, lastNameCol);
        
        if (paymentMethods.isEmpty()) {
            scTable.setPlaceholder(new Label("No rows to display"));
        } else {
            for (PaymentMethod p : paymentMethods) {
                scTable.getItems().add(p);
            }
        }
        
        final VBox scbox = new VBox();
        scbox.setSpacing(5);
        scbox.setPadding(new Insets(20, 10, 10, 20));
        scbox.getChildren().addAll(sclabel, scTable, addrowButton, deleterowButton, backButton1);
        return new Scene(scbox, WIN_WIDTH, WIN_HEIGHT);
    }
    
    private void addQuery() {
        try (Statement stmt = conn1.createStatement()) {
            stmt.executeQuery("DELETE FROM SHOPPING_CART WHERE userID = '" + Main.userID + "' AND videoID = '" + videoid + "'");
        } catch (SQLException e){
            System.out.println(e);
        }
    }
    
    private void deleteQuery(int selectedPaymentID){
        try(Statement stmt = conn1.createStatement()){
            stmt.executeQuery("DELETE FROM PAYMENT_METHOD WHERE userID = '" + Main.userID + "' AND ID yment= '" + selectedPaymentID + "'");
        }catch (SQLException e){
            System.out.println(e);
            
        }
    }
}