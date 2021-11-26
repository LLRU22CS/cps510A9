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
import java.text.DateFormat;  
import java.text.SimpleDateFormat; 
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
        Button backButton1 = new Button("Back to Profile");
        backButton1.setOnAction(e -> this.switchScene("ProfileScreen.java"));
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
            addPaymentMethodForm();
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
    
    private void addPaymentMethodForm() {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        TextField cardNumField = new TextField("Card Number");
        TextField cardTypeField = new TextField("Card Type");
        TextField cardCVVField = new TextField("Card CVV");
        DatePicker cardExpField = new DatePicker();
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        TextField billAdd1Field = new TextField("Address 1");
        TextField billAdd2Field = new TextField("Address 2");
        TextField billAddCityField = new TextField("City");
        TextField billAddStateField = new TextField("State");
        TextField billAddPCField = new TextField("Postal Code");
        TextField billAddCountryField = new TextField("Country");
        
        Button addPaymentButton = new Button("Add Payment Method");
        addPaymentButton.setOnAction(e -> {
            
            double cardNum = Double.parseDouble(cardNumField.getText());
            String cardType = cardTypeField.getText();
            int cardCVV = Integer.parseInt(cardCVVField.getText());
            Date cardExp = Date.valueOf(cardExpField.getValue());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String billAdd1 = billAdd1Field.getText();
            String billAdd2 = billAdd2Field.getText();
            String city = billAddCityField.getText();
            String state = billAddStateField.getText();
            String postalCode = billAddPCField.getText();
            String country = billAddCountryField.getText();
            
            addQuery(cardNum, cardType, cardCVV, cardExp, firstName, lastName, billAdd1, billAdd2, city, state, postalCode, country);
            newStage.close();
        });
        
        comp.getChildren().addAll(cardNumField, cardTypeField, cardCVVField, cardExpField, firstNameField, lastNameField, billAdd1Field, billAdd2Field, billAddCityField, billAddStateField, billAddPCField, billAddCountryField, addPaymentButton);

        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();
    }
    
    private void addQuery(double cardNum, String cardType, int cardCVV, Date cardExp, String firstName, String lastName, String billingAdd1, String billingAdd2, String billingAddCity, String billingAddState, String billingAddPC, String billingAddCountry) {
        DateFormat dateFormat = new SimpleDateFormat("mm/yyyy");  
        String strDate = dateFormat.format(cardExp); 
        
        try (Statement stmt = conn1.createStatement()) {
            stmt.executeQuery("INSERT INTO PAYMENT_METHOD (`userID`,`card_number`,`card_type`,`card_CVV`,`card_expiry_date`,`cardholder_first_name`,`cardholder_last_name`,`billing_address_1`,`billing_address_2`,`billing_address_city`,`billing_address_state`,`billing_address_postal_code`,`billing_address_country`) " + 
                    "VALUES (" + Main.userID + "," + cardNum + ",'" + cardType + "'," + cardCVV + ",TO_DATE('" + strDate + "','MM/YYYY'),'" + firstName + "','" + lastName + "','" + billingAdd1 + "','" + billingAdd2 + "','" + billingAddCity + "','" + billingAddState + "','" + billingAddPC + "','" + billingAddCountry + "')");
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