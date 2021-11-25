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
    private double points = 0.0;
    
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
        
    }
    
    public Video(
            int videoID,
            String title,
            int releaseYear,
            String director,
            double videoDuration,
            String rating,
            String description,
            double purchasePrice,
            double points) {
        
        this.videoID = videoID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.director = director;
        this.videoDuration = videoDuration;
        this.rating = rating;
        this.description = description;
        this.purchasePrice = purchasePrice;
        this.points = points;
        
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

    public double getPoints() {
        return points;
    }
    
}
