package cps510Assignment;

import java.util.HashMap;

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

/* PREPARED STATEMENT FORMAT
 * PreparedStatement stmt = null;
        try {
           stmt = conn1.prepareStatement("INSERT INTO person (name, email) values (?, ?)");
           stmt.setString(1, name);
           stmt.setString(2, email);
           stmt.executeUpdate();
        }
        finally {
           try {
              if (stmt != null) { stmt.close(); }
           }
           catch (Exception e) {
              // log this error
           }
        }
 */

public class Main extends Application implements EventHandler<ActionEvent> {

    /* window setup */
    final static int WIN_WIDTH = 800, WIN_HEIGHT = 600;
    private Stage window;
    private HashMap<String, Screen> allScenes = new HashMap<>();
    public static int userID = -1;
    Scene entryPortal, newUser, login, adminLogin, homePage, adminHomePage, practiceQueries;
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	window = primaryStage;
        window.setTitle("Blockbuster 2");
        window.getIcons().add(new Image("icon.png"));

        // initialize child classes here
        AdminLoginScreen adminLoginScreen = new AdminLoginScreen(window, allScenes);
        CreateUserScreen createUserScreen = new CreateUserScreen(window, allScenes);
        EntryPortalScreen entryPortalScreen = new EntryPortalScreen(window, allScenes);
        HomepageScreen homepageScreen = new HomepageScreen(window, allScenes);
        LoginScreen loginScreen = new LoginScreen(window, allScenes);
        PracticeQueriesScreen practiceQueriesScreen = new PracticeQueriesScreen(window, allScenes);
        UnixScreen unixScreen = new UnixScreen(window, allScenes);
        WishlistScreen wishlistScreen = new WishlistScreen(window, allScenes);
        ShoppingCartScreen shoppingcartScreen = new ShoppingCartScreen(window, allScenes);
        VideoViewScreen videoViewScreen = new VideoViewScreen(window, allScenes);
        
        // add all scenes to global HashMap
        allScenes.put("AdminLoginScreen.java", adminLoginScreen);
        allScenes.put("LoginScreen.java", loginScreen);
        allScenes.put("CreateUserScreen.java", createUserScreen);
        allScenes.put("EntryPortalScreen.java", entryPortalScreen);
        allScenes.put("HomepageScreen.java", homepageScreen);
        allScenes.put("PracticeQueriesScreen.java", practiceQueriesScreen);
        allScenes.put("UnixScreen.java", unixScreen);
        allScenes.put("WishlistScreen.java", wishlistScreen);
        allScenes.put("ShoppingCartScreen.java", shoppingcartScreen);
        allScenes.put("VideoViewScreen.java", videoViewScreen);

        // start initial scene
        window.setScene(entryPortalScreen.getScene());
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(ActionEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
}
