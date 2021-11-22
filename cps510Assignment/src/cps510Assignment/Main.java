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
	static Connection conn1 = null;
	Stage window;
	Scene entryPortal, newUser, login, adminLogin, homePage, adminHomePage, practiceQueries;
	
	/* entryPortal components */
	GridPane entryGrid;
	Label epNewUserLabel, epLoginLabel, epAdminLoginLabel;
	Button epNewUserButton, epLoginButton, epAdminLoginButton;
	
	/* newUser components */
//	Label tempLabel1;
//	Button tempButton1;
	Label firstNameLabel, lastNameLabel, DOBLabel, emailLabel, phoneLabel, usernameLabel, passwordLabel;
	TextField firstNameText, lastNameText, emailText, phoneText, usernameText;
	DatePicker dateOfBirth;
	PasswordField passwordText;
	Button submitButton, clearButton, newUserBackButton;
	
	/* adminLogin components */
//	Label tempLabel2;
//	Button tempButton2;
	GridPane adminLoginGrid;
	Label adminUsername, adminPassword;
	TextField adminUsernameText;
	PasswordField adminPasswordText;
	Button adminLoginButton, adminReturnToEPButton;
	
	
	/* Login scene components */
	GridPane loginGrid;
	Label username, password;
	TextField username_text;
	PasswordField password_text;
	Button login_button, return_to_ep_button;
	
	/* homepage scene components */
	GridPane hpGrid;
	ScrollPane hpScrollPane_Grid;
	HBox hpHBox_ScrollPane_Grid;
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
        window.getIcons().add(new Image("icon.png"));
//        tempLabel1 = new Label("Nothing to see here.");
//        tempButton1 = new Button("Return");
//        tempButton1.setOnAction(e -> window.setScene(entryPortal));
//        tempLabel2 = new Label("Nothing to see here.");
//        tempButton2 = new Button("Return");
//        tempButton2.setOnAction(e -> window.setScene(entryPortal));
        
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
        
        // SCENE newUser
        GridPane newUserLayout = new GridPane();
        newUserLayout.setPadding(new Insets(20,20,20,20));
        newUserLayout.setVgap(15);
        newUserLayout.setHgap(15);
        firstNameLabel = new Label("First Name: ");
        lastNameLabel = new Label("Last Name: ");
        DOBLabel = new Label("Date of Birth: ");
        emailLabel = new Label("Email: ");
        phoneLabel = new Label("Phone Number: ");
        usernameLabel = new Label("Username: ");
        passwordLabel = new Label("Password: ");
        firstNameText = new TextField();
        lastNameText = new TextField();
        dateOfBirth = new DatePicker();
        emailText = new TextField();
        emailText.setPromptText("example@domain.com");
        phoneText = new TextField();
        phoneText.setPromptText("1234567890");
        usernameText = new TextField();
        passwordText = new PasswordField();
        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
        	e.consume();
        	if (validateNewUser(firstNameText, lastNameText, dateOfBirth, emailText, phoneText, usernameText, passwordText)) {
        		window.setScene(login);
        		AlertBox.display("Success", "Please log in.");
        	} else {
        		AlertBox.display("Failure", "Please try again.");
        	}
        });
        clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
        	firstNameText.clear();
        	lastNameText.clear();
        	dateOfBirth.setValue(null);
        	emailText.clear();
        	phoneText.clear();
        	usernameText.clear();
        	passwordText.clear();
        });
        newUserBackButton = new Button("Back");
        newUserBackButton.setOnAction(e -> window.setScene(entryPortal));   
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
		newUser = new Scene(newUserLayout, WIN_WIDTH, WIN_HEIGHT);
        
        // SCENE adminLogin 
        GridPane adminLoginLayout = new GridPane();
        adminLoginLayout.setPadding(new Insets(20,20,20,20));
        adminLoginLayout.setVgap(15);
        adminLoginLayout.setHgap(15);
        adminUsername = new Label("Username: ");
        adminPassword = new Label("Password: ");
        adminUsernameText = new TextField();
        adminUsernameText.setPromptText("username");
        adminPasswordText = new PasswordField();
        adminPasswordText.setPromptText("password");
        adminLoginButton = new Button("Log In");
        adminLoginButton.setOnAction(e -> {
        	if (adminUsernameText.getText().equals("adminGroup94") && adminPasswordText.getText().equals("p@ssw0rd")) {
//        		window.setScene(adminHomePage);
        		System.out.println("adminLogin success");
        	} else {
        		AlertBox.display("Failure", "Please try again.");
        	}
        });
        adminReturnToEPButton = new Button("Back");
        adminReturnToEPButton.setOnAction(e -> window.setScene(entryPortal));
        GridPane.setConstraints(adminUsername, 0, 0);
        GridPane.setConstraints(adminPassword, 0, 1);
        GridPane.setConstraints(adminUsernameText, 1, 0);
        GridPane.setConstraints(adminPasswordText, 1, 1);
        GridPane.setConstraints(adminLoginButton, 1, 2);
        GridPane.setConstraints(adminReturnToEPButton, 2, 2);
        adminLoginLayout.getChildren().addAll(adminUsername, adminPassword, adminUsernameText, adminPasswordText, adminLoginButton, adminReturnToEPButton);
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
        password_text = new PasswordField();
        password_text.setPromptText("password");
        login_button = new Button("Log In");
        login_button.setOnAction(e -> {
        	boolean loginSuccess = loginAttempt(username_text, password_text);
        	if (loginSuccess) {
        		window.setScene(homePage);
        	}
        });
        return_to_ep_button = new Button("Back");
        return_to_ep_button.setOnAction(e -> window.setScene(entryPortal));
        Button buttonNew = new Button("Bypass");
        buttonNew.setOnAction(e -> window.setScene(homePage));
        GridPane.setConstraints(username, 0, 0);
        GridPane.setConstraints(password, 0, 1);
        GridPane.setConstraints(username_text, 1, 0);
        GridPane.setConstraints(password_text, 1, 1);
        GridPane.setConstraints(login_button, 1, 2);
        GridPane.setConstraints(buttonNew, 3, 2);
        GridPane.setConstraints(return_to_ep_button, 2, 2);
        loginGrid.getChildren().addAll(username, password, username_text, password_text, login_button, return_to_ep_button, buttonNew);
        login = new Scene(loginGrid, WIN_WIDTH, WIN_HEIGHT);
        
        // SCENE homePage
        /*
	ScrollPane hpScrollPane_Grid;
	HBox hpHBox_ScrollPane_Grid;*/
        hpGrid = new GridPane();
        hpGrid.setPadding(new Insets(20,20,20,20));
        hpGrid.setVgap(15);
        hpGrid.setHgap(15);
        hpLabel1 = new Label("Try some queries in another page:");
        hpButton1 = new Button("Click here.");
        hpButton1.setOnAction(e -> window.setScene(practiceQueries));
        hpScrollPane_Grid = new ScrollPane();
        hpScrollPane_Grid.setPannable(true);
        hpScrollPane_Grid.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        hpScrollPane_Grid.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        hpHBox_ScrollPane_Grid = new HBox(50);
        int videoID, release_year;
        String title, thumbnailFilepath;
        Label titleLabel, release_yearLabel;
        Image thumbnail;
        ImageView imageView;
        try (Statement defaultDisplayStmt = conn1.createStatement()) {
			String defaultDisplayQuery = "SELECT videoID, title, release_year FROM video ORDER BY videoID";
			ResultSet getTopUIDResult = defaultDisplayStmt.executeQuery(defaultDisplayQuery);
			while (getTopUIDResult.next()) {
		        VBox currVBox = new VBox(7);
				videoID = getTopUIDResult.getInt("videoID");
				title = getTopUIDResult.getString("title");
				release_year = getTopUIDResult.getInt("release_year");
				titleLabel = new Label(title);
				release_yearLabel = new Label(String.valueOf(release_year));
				thumbnailFilepath = "/thumbnails/THUMBNAIL_" + String.valueOf(videoID) + ".jpg";
				thumbnail = new Image(thumbnailFilepath);
				imageView = new ImageView(thumbnail);
			    imageView.setPreserveRatio(true); 
			    imageView.setFitHeight(455);
				currVBox.setAlignment(Pos.CENTER);
				currVBox.getChildren().addAll(imageView, titleLabel, release_yearLabel);
				hpHBox_ScrollPane_Grid.getChildren().add(currVBox);
			}
			} catch (SQLException e) {
				System.out.println(e.getErrorCode());
			}
        hpScrollPane_Grid.setContent(hpHBox_ScrollPane_Grid);
        hpGrid.add(hpLabel1, 0, 0);
        hpGrid.add(hpButton1, 1, 0);
        hpGrid.add(hpScrollPane_Grid, 0, 1, 2, 1);
//        hpGrid.getChildren().addAll(hpLabel1, hpButton1, hpScrollPane_Grid);
        homePage = new Scene(hpGrid, WIN_WIDTH, WIN_HEIGHT);
        hpScrollPane_Grid.setStyle("-fx-focus-color: transparent;");
        
        
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

	public static void main(String[] args) {
        try {
            String dbUsername = "";
            String dbPassword = "";
            
            String dbURL1 = "jdbc:oracle:thin:" + dbUsername + "/" + dbPassword + "@oracle.scs.ryerson.ca:1521:orcl";  // that is school Oracle database and you can only use it in the labs
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
