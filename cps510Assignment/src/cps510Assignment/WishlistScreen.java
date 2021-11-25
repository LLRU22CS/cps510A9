/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps510Assignment;

import static cps510Assignment.Screen.WIN_WIDTH;
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
/**
 *
 * @author david
 */
public class WishlistScreen extends Screen{
    
    public WishlistScreen(Stage window, HashMap allScenes){
        super(window, allScenes);
    }
    
    @Override
    public Scene getScene() {
       final Label wlabel = new Label("Wishlist");
       wlabel.setFont(new Font("Arial", 20));
       Button backButton1 = new Button("Back to Home");
       backButton1.setOnAction(e -> this.switchScene("HomepageScreen.java"));
       TableView wishlistTable = new TableView();
       
       
       TableColumn MovieNameCol = new TableColumn("Movie");
       TableColumn DirectorCol = new TableColumn("Director");
       TableColumn release_yearCol = new TableColumn("Release Year");
       
       wishlistTable.setPlaceholder(new Label("No rows to display"));
       wishlistTable.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        MovieNameCol.setMaxWidth( 1f * Integer.MAX_VALUE * 40 ); 
        DirectorCol.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );
        release_yearCol.setMaxWidth( 1f * Integer.MAX_VALUE * 30 );

       wishlistTable.getColumns().addAll(MovieNameCol, DirectorCol, release_yearCol);
        final VBox wishbox = new VBox();
        wishbox.setSpacing(5);
        wishbox.setPadding(new Insets(20, 10, 10, 20));
        wishbox.getChildren().addAll(wlabel, wishlistTable, backButton1);
        return new Scene(wishbox, WIN_WIDTH, WIN_HEIGHT);
     }
    
    
}
