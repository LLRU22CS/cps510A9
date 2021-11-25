/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps510Assignment;

/**
 *
 * @author Nathan
 */
public class Video {
    
    private int videoID;
    private String title;
    private int releaseYear;
    private String director;
    private double videoDuration;
    private String rating;
    private String description;
    private double purchasePrice;
    private int points;
    
    public Video(
            int videoID,
            String title,
            int releaseYear,
            String director,
            double videoDuration,
            String rating,
            String description,
            double purchasePrice) {
        
        this.videoID = videoID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.director = director;
        this.videoDuration = videoDuration;
        this.rating = rating;
        this.description = description;
        this.purchasePrice = purchasePrice;
        if (this.purchasePrice > 12) this.points = 30;
        else if (this.purchasePrice > 9) this.points = 20;
        else if (this.purchasePrice > 6) this.points = 10;
        else if (this.purchasePrice > 3) this.points = 6;
        else this.points = 3;
    }
    

    public int getVideoID() {
        return videoID;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public double getVideoDuration() {
        return videoDuration;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public int getPoints() {
        return points;
    }
    
}
