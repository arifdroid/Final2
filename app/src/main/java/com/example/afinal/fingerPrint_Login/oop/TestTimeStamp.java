package com.example.afinal.fingerPrint_Login.oop;

public class TestTimeStamp {

    private String mon_morning;
    private String tue_morning;
    private String wed_morning;
    private String thu_morning;
    private String fri_morning;

    private int referenceiD;

    private String name;

    public TestTimeStamp(int referenceiD) {
        this.referenceiD = referenceiD;
    }

    public TestTimeStamp(int referenceiD, String mon_morning, String tue_morning, String wed_morning, String thu_morning, String fri_morning, String name) {

        this.name = name;
        this.mon_morning = mon_morning;
        this.tue_morning = tue_morning;
        this.wed_morning = wed_morning;
        this.thu_morning = thu_morning;
        this.fri_morning = fri_morning;
        this.referenceiD = referenceiD;

    }

    public int getReferenceiD() {
        return referenceiD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMon_morning() {
        return mon_morning;
    }

    public void setMon_morning(String mon_morning) {
        this.mon_morning = mon_morning;
    }

    public String getTue_morning() {
        return tue_morning;
    }

    public void setTue_morning(String tue_morning) {
        this.tue_morning = tue_morning;
    }

    public String getWed_morning() {
        return wed_morning;
    }

    public void setWed_morning(String wed_morning) {
        this.wed_morning = wed_morning;
    }

    public String getThu_morning() {
        return thu_morning;
    }

    public void setThu_morning(String thu_morning) {
        this.thu_morning = thu_morning;
    }

    public String getFri_morning() {
        return fri_morning;
    }

    public void setFri_morning(String fri_morning) {
        this.fri_morning = fri_morning;
    }
}
