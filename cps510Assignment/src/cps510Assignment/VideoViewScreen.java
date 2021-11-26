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
		Button addToCartPurchaseButton = new Button("Purchase");
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
                duration.setText(String.valueOf(videoResult.getInt("video_duration")) + " minutes");    
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
      	videoViewGrid.add(rating, 2, 0);
      	videoViewGrid.add(duration, 0, 3);
      	if (purchaseable) {
      		videoViewGrid.add(price, 2, 1);
      		videoViewGrid.add(addToCartPurchaseButton, 3, 1);
      	}
      	videoViewGrid.add(description, 0, 5, 4, 3);
      	videoViewGrid.add(addToWishlistButton, 0, 8);
      	
      	int y = 2;
      	try (Statement rentalStmt = conn1.createStatement()) {
            String rentalQuery = "SELECT rental_duration, rental_price FROM rental_duration WHERE videoID = " + String.valueOf(videoID);
            ResultSet rentalResult = rentalStmt.executeQuery(rentalQuery);
            while(rentalResult.next() && y < 5) {
            	Label p = new Label();
            	Button b = new Button();
            	int nights = rentalResult.getInt("rental_duration");
            	double rentPrice = rentalResult.getDouble("rental_price");
            	if (nights < 3) b.setText(String.valueOf(nights*24) + " hours");
            	else b.setText(String.valueOf(nights) + " days");
            	p.setText("Rent: $" + String.valueOf(rentPrice));
            	b.setOnAction(e -> {
        			addToCart(Main.userID, videoID, rentPrice);
        			AlertBox.display("Success", title.getText() + " was added to your cart.");
            	});
            	videoViewGrid.add(p, 2, y);
            	videoViewGrid.add(b, 3, y);
            	y++;
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
      	
      	int maxActors = 0;
      	y = 9;
      	try (Statement actorStmt = conn1.createStatement()) {
            String actorQuery = "SELECT actor_first_name, actor_last_name, character_name FROM actors WHERE videoID = " + String.valueOf(videoID);
            ResultSet actorResult = actorStmt.executeQuery(actorQuery);
            while(actorResult.next() && maxActors < 4) {
            	Text actor = new Text();
            	String fn = actorResult.getString("actor_first_name");
            	String ln = actorResult.getString("actor_last_name");
            	String cn = actorResult.getString("character_name");
            	if (maxActors == 0) actor.setText(" With " + fn + " " + ln + " as " + cn);
            	else actor.setText(" And " + fn + " " + ln + " as " + cn);
            	videoViewGrid.add(actor, 0, y, 4, 1);
            	y++;
            	maxActors++;
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }

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

        leaveReviewButton.setOnAction(e -> {
        	String r = review.getText();
        	String t = reviewTitle.getText();
        	if (r.equals("") || t.equals("")) {
            	AlertBox.display("Unable to Post", "Please type a review and a review title.");        		
        		return;
        	}
        	PreparedStatement addReviewStmt = null;
            try {
            	addReviewStmt = conn1.prepareStatement("INSERT INTO review (userID, videoID, review, rating, rtitle) values (?, ?, ?, ?, ?)");
            	addReviewStmt.setInt(1, Main.userID);
            	addReviewStmt.setInt(2, videoID);
            	addReviewStmt.setString(3, r);
            	addReviewStmt.setDouble(4, round(reviewValue.getValue(), 1));
            	addReviewStmt.setString(5, t);
            	addReviewStmt.executeUpdate();
            } catch (SQLException a) {
            	AlertBox.display("Unable to Post", "You have already added a review for this movie.");
            }
            finally {
               try {
                  if (addReviewStmt != null) { addReviewStmt.close(); }
               }
               catch (Exception a) {
                  // log this error
               }
            }
        	this.switchScene("VideoViewScreen.java");
        });
        
		GridPane reviewsGrid = new GridPane();
		reviewsGrid.setPadding(new Insets(0,20,20,20));
		reviewsGrid.setVgap(3);
		reviewsGrid.setHgap(5);
      	y = 0;
      	try (Statement reviewStmt = conn1.createStatement()) {
            String reviewQuery = "SELECT rtitle, review, rating, userID FROM review WHERE videoID = " + String.valueOf(videoID);
            ResultSet reviewResult = reviewStmt.executeQuery(reviewQuery);
            while(reviewResult.next()) {
            	Label rT = new Label(reviewResult.getString("rtitle"));
            	Text rB = new Text(reviewResult.getString("review"));
            	rB.setWrappingWidth(WIN_WIDTH/2);
            	Label r = new Label(String.valueOf(reviewResult.getDouble("rating")) + " / 5.0");
            	int uID = reviewResult.getInt("userID");
            	String name = "";
            	try (Statement userStmt = conn1.createStatement()) {
            		String userQuery = "SELECT first_name, last_name FROM store_user WHERE userID = " + String.valueOf(uID);
            		ResultSet userResult = userStmt.executeQuery(userQuery);
            		if (userResult.next()) {
            			name = userResult.getString("first_name") + " " + userResult.getString("last_name");
            		}
            	} catch (SQLException e) {
            		System.out.println(e.getErrorCode());
            	}
            	Label n = new Label(name);
            	reviewsGrid.add(n, 0, y, 1, 1);
            	reviewsGrid.add(rT, 1, y, 1, 1);
            	y++;
            	reviewsGrid.add(r, 0, y, 1, 1);
            	reviewsGrid.add(rB, 1, y, 1, 1); 
            	y+=3;        	
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        c1.setPercentWidth(30);
        c2.setPercentWidth(70);
        reviewsGrid.getColumnConstraints().addAll(c1,c2);
        
		vvHBox1.getChildren().addAll(thumbnail, videoViewGrid);
		vvVBox1.getChildren().addAll(vvHBox1, videoReviewGrid, reviewsGrid);
		vvVBox1.setPadding(new Insets(20,20,20,20));
		videoViewScroll.setContent(vvVBox1);
		Scene videoView = new Scene(videoViewScroll, WIN_WIDTH, WIN_HEIGHT);
		return videoView;
	}

	private void addToWishlist(int userID, int videoID) {
		try (Statement wishlistStmt = conn1.createStatement()) {
            String wishlistQuery = "INSERT INTO wishlist VALUES (" + String.valueOf(userID) + "," + String.valueOf(videoID)  + ")";
            wishlistStmt.executeQuery(wishlistQuery);
        } catch (SQLException e) {
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
        	e.printStackTrace();
        }
	}

	private static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
}
