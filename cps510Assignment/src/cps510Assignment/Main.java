package cps510Assignment;

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
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
	final static int WIN_WIDTH = 400, WIN_HEIGHT = 350;
	static Connection conn1 = null;
	Stage window;
	Scene entryPortal, newUser, login, adminLogin, homePage, practiceQueries;
	
	/* entryPortal components */
	GridPane entryGrid;
	Label epNewUserLabel, epLoginLabel, epAdminLoginLabel;
	Button epNewUserButton, epLoginButton, epAdminLoginButton;
	
	/* newUser components */
	Label tempLabel1;
	Button tempButton1;
	
	/* adminLogin components */
	Label tempLabel2;
	Button tempButton2;
	
	
	/* Login scene components */
	GridPane loginGrid;
	Label username, password;
	TextField username_text, password_text;
	Button login_button;
	
	/* homepage scene components */
	GridPane homePageGrid;
	Label hpLabel1;
	Button hpButton1;
	
	/* Practice scene components */
	Button button_runQuery, button_swapQuery, button_return_home;
	static int query_val = 0;	//max size(queries[])
	String[] queries = {
			"SELECT movies, actor_first_name, actor_last_name FROM (select COUNT(actor_last_name) as movies, actor_first_name, actor_last_name from actors group by actor_first_name, actor_last_name) WHERE movies > 1 GROUP BY actor_first_name, actor_last_name, movies",
			"SELECT v.title, f.file_location FROM VIDEO v, FILE_LOCATIONS f, VIDEO_CATEGORIES c WHERE v.videoID = c.videoID AND c.categoryID = 103 AND f.videoID = v.videoID AND f.file_type = 'TRAILER'",
			"SELECT title, release_year FROM VIDEO WHERE release_year >= 2000 ORDER BY release_year DESC",
			"SELECT title FROM VIDEO"
	};
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	window = primaryStage;
        window.setTitle("Blockbuster 2");
        tempLabel1 = new Label("Nothing to see here.");
        tempButton1 = new Button("Return");
        tempButton1.setOnAction(e -> window.setScene(entryPortal));
        tempLabel2 = new Label("Nothing to see here.");
        tempButton2 = new Button("Return");
        tempButton2.setOnAction(e -> window.setScene(entryPortal));
        
        // SCENE entryPortal
        entryGrid = new GridPane();
        entryGrid.setPadding(new Insets(20,20,20,20));
        entryGrid.setVgap(15);
        entryGrid.setHgap(15);
        epNewUserLabel = new Label("New Users:");
        epNewUserButton = new Button("Create new account");
        epNewUserButton.setOnAction(e -> window.setScene(newUser));
        epLoginLabel = new Label("Returning Users:");
        epLoginButton = new Button("Log In");
        epLoginButton.setOnAction(e -> window.setScene(login));
        epAdminLoginLabel = new Label("Admin:");
        epAdminLoginButton = new Button("Admin Log In");
        epAdminLoginButton.setOnAction(e -> window.setScene(adminLogin));
        GridPane.setConstraints(epNewUserLabel, 0, 0);
        GridPane.setConstraints(epNewUserButton, 0, 1);
        GridPane.setConstraints(epLoginLabel, 1, 0);
        GridPane.setConstraints(epLoginButton, 1, 1);
        GridPane.setConstraints(epAdminLoginLabel, 2, 0);
        GridPane.setConstraints(epAdminLoginButton, 2, 1);
        entryGrid.getChildren().addAll(epNewUserLabel, epNewUserButton, epLoginLabel, epLoginButton, epAdminLoginLabel, epAdminLoginButton);
        entryPortal = new Scene(entryGrid, WIN_WIDTH, WIN_HEIGHT);
        
        // SCENE newUser TEMP
        VBox newUserLayout = new VBox(10);
        newUserLayout.getChildren().addAll(tempLabel1, tempButton1);
        newUserLayout.setAlignment(Pos.CENTER);
		newUser = new Scene(newUserLayout, WIN_WIDTH, WIN_HEIGHT);
        
        // SCENE adminLogin TEMP
        VBox adminLoginLayout = new VBox(10);
        adminLoginLayout.getChildren().addAll(tempLabel2, tempButton2);
        adminLoginLayout.setAlignment(Pos.CENTER);
		adminLogin = new Scene(adminLoginLayout, WIN_WIDTH, WIN_HEIGHT);
        
        // SCENE login
        loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20,20,20,20));
        loginGrid.setVgap(15);
        loginGrid.setHgap(15);
        username = new Label("Username: ");
        password = new Label("Password: ");
        username_text = new TextField();
        username_text.setPromptText("username");
        password_text = new TextField();
        password_text.setPromptText("password");
        login_button = new Button("Log In");
        login_button.setOnAction(e -> {
        	boolean loginSuccess = loginAttempt(username_text, password_text);
        	if (loginSuccess) {
        		window.setScene(homePage);
        	}
        });
        Button buttonNew = new Button("Bypass");
        buttonNew.setOnAction(e -> window.setScene(homePage));
        GridPane.setConstraints(username, 0, 0);
        GridPane.setConstraints(password, 0, 1);
        GridPane.setConstraints(username_text, 1, 0);
        GridPane.setConstraints(password_text, 1, 1);
        GridPane.setConstraints(login_button, 1, 2);
        GridPane.setConstraints(buttonNew, 2, 2);
        loginGrid.getChildren().addAll(username, password, username_text, password_text, login_button, buttonNew);
        login = new Scene(loginGrid, WIN_WIDTH, WIN_HEIGHT);
        
        // SCENE homePage
        homePageGrid = new GridPane();
        homePageGrid.setPadding(new Insets(20,20,20,20));
        homePageGrid.setVgap(15);
        homePageGrid.setHgap(15);
        hpLabel1 = new Label("Try some queries in another page:");
        hpButton1 = new Button("Click here.");
        hpButton1.setOnAction(e -> window.setScene(practiceQueries));
        GridPane.setConstraints(hpLabel1, 0, 0);
        GridPane.setConstraints(hpButton1, 0, 1);
        homePageGrid.getChildren().addAll(hpLabel1, hpButton1);
        homePage = new Scene(homePageGrid, WIN_WIDTH, WIN_HEIGHT);
        
        
        // SCENE practiceQueries
        button_runQuery = new Button();
        button_runQuery.setText("Click me!");
        button_runQuery.setOnAction(e -> testQuery());
        button_swapQuery = new Button("Swap Query");
        button_swapQuery.setOnAction(e -> { if (query_val == 3) query_val = 0; else query_val += 1; });
        button_return_home = new Button("Return Home");
        button_return_home.setOnAction(e -> window.setScene(homePage));
        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(10, 20, 10, 20)); 
        vbButtons.getChildren().addAll(button_runQuery, button_swapQuery, button_return_home);
        practiceQueries = new Scene(vbButtons, WIN_WIDTH, WIN_HEIGHT);
        
        window.setScene(entryPortal);
        window.show();
    }

	public static void main(String[] args) {
        try {
            String dbURL1 = "jdbc:oracle:thin:lloewen/10120860@oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs
			// String dbURL1 = "jdbc:oracle:thin:username/password@localhost:1521:xe";
			/* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation. 
			xe is the sid, change according to what you setup during installation. */
			conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
            }
            launch(args);
			
        } 
        /*catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } */catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }
     
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

	public String formatQueryResults(ResultSet out) throws SQLException {
		String result = "";
		if (out != null) {
		switch (query_val) {
		case 0:
			while (out.next()) {
				int num = out.getInt("movies");
				String name_f = out.getString("actor_first_name");
				String name_l = out.getString("actor_last_name");
				result += num + " --> " + name_f + " " + name_l + "\n";
			}
			break;
		case 1:
			while (out.next()) {
				String title = out.getString("title");
				String file_location = out.getString("file_location");
				result += title + " --> " + file_location + "\n";
			}
			break;
		case 2:
			while (out.next()) {
				String title = out.getString("title");
				int release_year = out.getInt("release_year");
				result += title + " --> " + release_year + "\n";
			}
			break;
		case 3:
			while (out.next()) {
				String title = out.getString("title");
				result += " ~ " + title + " ~ " + "\n";
			}
			break;
		}
		}
		else {result = "No result";}
		return result;
}
	
	public void testQuery() {		
		try (Statement stmt = conn1.createStatement()) {

		ResultSet rs = stmt.executeQuery(queries[query_val]);
		if (rs == null) {
			System.out.println("receiving");
		}
		System.out.println(formatQueryResults(rs));
		
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
		}
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
    private boolean loginAttempt(TextField un, TextField pw) {
		boolean result = false;
		ResultSet rs = null;
//		PreparedStatement loginStmt = null;
		   try (Statement stmtL = conn1.createStatement()) {
//			  loginStmt = conn1.prepareStatement("SELECT user_password FROM STORE_USER WHERE username = ?");
//			  loginStmt.setString(1, un.getText());
//			  rs = loginStmt.executeQuery();
			   String loginQuery = "SELECT user_password FROM STORE_USER WHERE username = '" + un.getText() + "'";
			   rs = stmtL.executeQuery(loginQuery);
			  
			  if (rs.next()) {
				  String expected_pw = rs.getString("user_password");
				  if (!pw.getText().equals(expected_pw)) {
					  AlertBox.display("Error", "Username/Password incorrect.");
				  }
				  else {
					  result = true;
				  }
			  }
			  else {
				  AlertBox.display("Error", "Username/Password incorrect.");
			  }
			  
		   } catch (SQLException e) {
//			   System.out.println(e.getErrorCode());
			   e.printStackTrace();
		   }
		   finally {
//		      try {
//		         if (loginStmt != null) { loginStmt.close(); }
//		      }
//		      catch (Exception e) {
//				  System.out.println("loginStmt close exception");
//		      }
		   }
		return result;
	}
	
}
