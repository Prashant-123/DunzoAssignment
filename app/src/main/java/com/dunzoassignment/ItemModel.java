package com.dunzoassignment;

public class ItemModel {
    public String title;
    public String release_date;
    public String overview;
    public String thumbnail;
    public float rating;

    public ItemModel(String title, String release_date, String overview, String thumbnail, float rating) {
        this.title = title;
        this.release_date = release_date;
        this.overview = overview;
        this.thumbnail = thumbnail;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = "https://image.tmdb.org/t/p/w400" + thumbnail;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ItemModel() {
    }

}
