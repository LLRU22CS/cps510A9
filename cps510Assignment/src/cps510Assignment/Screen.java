/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps510Assignment;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Nathan
 */
abstract public class Screen {
    private Stage window;
    private HashMap<String, Screen> allScenes;
    final static Connection conn1 = connectToDatabase();
    final static int WIN_WIDTH = 800, WIN_HEIGHT = 600;
    
    public Screen(Stage window, HashMap allScenes) {
        this.window = window;
        this.allScenes = allScenes;
    }
    
    static Connection connectToDatabase() {
        
        Connection tempConn = null;
        
        try {
            String dbUsername = "n1cheung";
            String dbPassword = "12241603";

            String dbURL1 = "jdbc:oracle:thin:" + dbUsername + "/" + dbPassword + "@oracle.scs.ryerson.ca:1521:orcl";  
            // that is school Oracle database and you can only use it in the labs
            // String dbURL1 = "jdbc:oracle:thin:username/password@localhost:1521:xe";
            /* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation. 
            xe is the sid, change according to what you setup during installation. */

            tempConn = DriverManager.getConnection(dbURL1);
        
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (tempConn != null && !tempConn.isClosed()) 
                    tempConn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            return tempConn;
        }
        
    }
    
    public Stage getWindow() {
        return this.window;
    }
    
    public HashMap getAllScenes() {
        return this.allScenes;
    }
    
    public void switchScene(String sceneName) {
        Screen desiredScreen = allScenes.get(sceneName);
        this.window.setScene(desiredScreen.getScene());
    }
    
    abstract public Scene getScene();
}
