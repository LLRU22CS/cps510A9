package cps510Assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

public class Main extends Application implements EventHandler<ActionEvent> {

	Button button_runQuery;
	Button button_swapQuery;
	static Connection conn1 = null;
	static int query_val = 0;	//max size(queries[])
	String[] queries = {"SELECT movies, actor_first_name, actor_last_name FROM (select COUNT(actor_last_name) as movies, actor_first_name, actor_last_name from actors group by actor_first_name, actor_last_name) WHERE movies > 1 GROUP BY actor_first_name, actor_last_name, movies",
			"SELECT v.title, f.file_location FROM VIDEO v, FILE_LOCATIONS f, VIDEO_CATEGORIES c WHERE v.videoID = c.videoID AND c.categoryID = 103 AND f.videoID = v.videoID AND f.file_type = 'TRAILER'",
			"SELECT title, release_year FROM VIDEO WHERE release_year >= 2000 ORDER BY release_year DESC",
			"SELECT title FROM VIDEO"
	};
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        primaryStage.setTitle("Hello World");
        
        button_runQuery = new Button();
        button_runQuery.setText("Click me!");
        button_runQuery.setOnAction(this);
        button_swapQuery = new Button("Swap Query");
        button_swapQuery.setOnAction(e -> { if (query_val == 3) query_val = 0; else query_val += 1; });
//        StackPane layout = new StackPane();
//        layout.getChildren().addAll(button_runQuery, button_swapQuery);
        
        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(10, 20, 10, 20)); 
        vbButtons.getChildren().addAll(button_runQuery, button_swapQuery);
        
        primaryStage.setScene(new Scene(vbButtons /* layout root*/, 400, 300));
        primaryStage.show();
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


	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == button_runQuery) {
			testQuery();
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
}
