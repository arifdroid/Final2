package com.example.afinal.fingerPrint_Login.customclass;

public class UploadToFirebase {

    private String pathname;
    private String imageURL;
    private String imagename;

    public UploadToFirebase() {

    }


    public UploadToFirebase(String pathname, String imageURL, String imagename) {
        this.pathname = pathname;
        this.imageURL = imageURL;
        this.imagename = imagename;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
