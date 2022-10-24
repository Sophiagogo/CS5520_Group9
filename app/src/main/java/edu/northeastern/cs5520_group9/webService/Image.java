package edu.northeastern.cs5520_group9.webService;

public class Image {
    private String id;
    private String url;
    private int width;
    private int height;

    public Image(String id, String url, int width, int height) {
        this.id = id;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
