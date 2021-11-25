package cps510Assignment;

import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
import javafx.scene.input.MouseEvent;
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

public class VideoViewScreen extends Screen {

	public VideoViewScreen(Stage window, HashMap allScenes) {
        super(window, allScenes);
	}
	
	static double purchasePrice = 0.00;

	@Override
	public Scene getScene() {
		HBox vvHBox1 = new HBox();
		HBox vvHBox2 = new HBox();
		VBox vvVBox1 = new VBox();
		ScrollPane videoViewScroll = new ScrollPane();
		videoViewScroll.fitToWidthProperty().set(true);
		GridPane videoViewGrid = new GridPane();
		videoViewGrid.setPadding(new Insets(20,20,20,20));
		videoViewGrid.setVgap(15);
		videoViewGrid.setHgap(15);
		GridPane videoReviewGrid = new GridPane();
		videoReviewGrid.setPadding(new Insets(20,20,20,20));
		videoReviewGrid.setVgap(3);
		videoReviewGrid.setHgap(5);
		int videoID = Data.videoID;
		Button addToCartPurchaseButton = new Button("Add to cart");
		Button addToWishlistButton = new Button("Add to wishlist");
		Button leaveReviewButton = new Button("Post");
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> this.switchScene("HomepageScreen.java"));
		Label title = new Label();
		Label year = new Label();
		Label director = new Label();
		Label duration = new Label();
		Label rating = new Label();
		Label price = new Label();
		Text description = new Text();   
		TextField reviewTitle = new TextField();
		reviewTitle.setPromptText("Review Title");
		TextField review = new TextField();
		review.setPromptText("Review body.");
		Slider reviewValue = new Slider(1, 5, 3);
		description.setWrappingWidth(WIN_WIDTH/2);
//        title.setFont(new Font("Calibri", 40)); 
        title.setStyle("-fx-font-weight: bold;-fx-font-family:\"Calibri\";-fx-font-size:30px;");
		String thumbnailFilepath = "/thumbnails/THUMBNAIL_" + String.valueOf(videoID) + ".jpg";
		Image thumbnail_img = new Image(thumbnailFilepath);
		ImageView thumbnail = new ImageView(thumbnail_img);
		thumbnail.setPreserveRatio(true); 
		thumbnail.setFitHeight(380);
		boolean purchaseable = true;
		
        try (Statement videoStmt = conn1.createStatement()) {
            String videoQuery = "SELECT v1.title, v1.release_year, v1.director, v2.video_duration, v2.rating, v2.video_description, v2.purchase_price FROM video_r1 v1, video_r2 v2 WHERE v1.videoID = " + String.valueOf(videoID) + " AND v1.videoID = v2.videoID";
            ResultSet videoResult = videoStmt.executeQuery(videoQuery);
            if(videoResult.next()) {
                title.setText(videoResult.getString("title"));
                year.setText(String.valueOf(videoResult.getInt("release_year")));    
                director.setText(videoResult.getString("director"));    
                duration.setText(String.valueOf(videoResult.getInt("video_duration")));    
                rating.setText(videoResult.getString("rating"));    
                purchasePrice = videoResult.getDouble("purchase_price");
                if (videoResult.wasNull()) {
                    purchaseable = false;
                } else {
                	price.setText("Purchase: $" + String.valueOf(purchasePrice));
                }                
                description.setText(videoResult.getString("video_description"));      
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
//		Label tmp = new Label();
//		tmp.setVisible(false);

		addToWishlistButton.setOnAction(e -> {
			addToWishlist(Main.userID, videoID);
			AlertBox.display("Success", title.getText() + " was added to your wishlist.");
		});
		addToCartPurchaseButton.setOnAction(e -> {
			addToCart(Main.userID, videoID, purchasePrice);
			AlertBox.display("Success", title.getText() + " was added to your cart.");
		});
		videoViewGrid.add(title, 0, 0, 2, 1);
		videoViewGrid.add(backButton, 3, 0);
      	videoViewGrid.add(year, 0, 1);
      	videoViewGrid.add(director, 0, 2);
      	videoViewGrid.add(rating, 1, 1);
      	videoViewGrid.add(duration, 1, 2);
      	if (purchaseable) {
      		videoViewGrid.add(price, 2, 1);
      	}
      	videoViewGrid.add(description, 0, 3, 4, 3);
      	videoViewGrid.add(addToCartPurchaseButton, 0, 6);
      	videoViewGrid.add(addToWishlistButton, 1, 6);

      	videoReviewGrid.add(reviewTitle, 0, 0);   
      	videoReviewGrid.add(review, 0, 1, 2, 1);
      	videoReviewGrid.add(reviewValue, 2, 0);    
      	videoReviewGrid.add(leaveReviewButton, 2, 1);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(30);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(20);
        videoReviewGrid.getColumnConstraints().addAll(c1,c2,c3);
    
		vvHBox1.getChildren().addAll(thumbnail, videoViewGrid);
		vvVBox1.getChildren().addAll(vvHBox1, videoReviewGrid);
		vvVBox1.setPadding(new Insets(20,20,20,20));
		videoViewScroll.setContent(vvVBox1);
		Scene videoView = new Scene(videoViewScroll, WIN_WIDTH, WIN_HEIGHT);
		return videoView;
	}
//
//	private void getUserID() {
//		try (Statement userIDStmt = conn1.createStatement()) {
//            String userIDQuery = "SELECT userID FROM store_user WHERE username = '" + username + "'";
//            ResultSet userIDResult = userIDStmt.executeQuery(userIDQuery);
//            if(userIDResult.next()) {
//                userID = userIDResult.getInt("userID");    
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getErrorCode());
//        }
//	}

	private void addToWishlist(int userID, int videoID) {
		try (Statement wishlistStmt = conn1.createStatement()) {
            String wishlistQuery = "INSERT INTO wishlist VALUES (" + String.valueOf(userID) + "," + String.valueOf(videoID)  + ")";
            wishlistStmt.executeQuery(wishlistQuery);
        } catch (SQLException e) {
//            System.out.println(e.getErrorCode());
        	e.printStackTrace();
        }
	}

	private void addToCart(int userID, int videoID, double price) {
		try (Statement cartStmt = conn1.createStatement()) {
			int points;
			if (price > 12) points = 30;
			else if (price > 9) points = 20;
			else if (price > 6) points = 10;
			else if (price > 3) points = 6;
			else points = 3;
            String cartQuery = "INSERT INTO shopping_cart VALUES (" + String.valueOf(userID) + "," + String.valueOf(videoID) + "," + String.valueOf(price) + "," + String.valueOf(points) + ")";
            cartStmt.executeQuery(cartQuery);
        } catch (SQLException e) {
//            System.out.println(e.getErrorCode());
        	e.printStackTrace();
        }
	}

}
