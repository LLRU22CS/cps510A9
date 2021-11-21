package cps510Assignment;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AlertBox {

	public static void display(String title, String message) {
		Stage window = new Stage();
		window.setTitle(title);
		window.initModality(Modality.APPLICATION_MODAL);
		Label label1 = new Label(message);
		Button button1 = new Button("Okay");
		button1.setOnAction(e -> window.close());
		VBox x = new VBox(10);
		x.getChildren().addAll(label1, button1);
		x.setAlignment(Pos.CENTER);
		Scene scene1 = new Scene(x, 250, 80);
		window.setScene(scene1);
		window.showAndWait();
	}
	
}
