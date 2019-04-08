package com.example.afinal.fingerPrint_Login.oop;

public class TestTimeStamp {

    //recorded data, missing, means, got mc. also need to point out, which data was it?

    private MC_Null_TestTimeStamp testTimeStamp; // a class of this object, but

    //we will set an object of MC_Null_TestTimeStamp, set its value, with current object of testtimestamp.
    //like name, id, and which day

    /// new solution >> 11.23AM 8 April

    private boolean prob_mon_morning;
    private boolean prob_tue_morning;
    private boolean prob_wed_morning;
    private boolean prob_thu_morning;
    private boolean prob_fri_morning;

    private boolean prob_mon_evening;
    private boolean prob_tue_evening;
    private boolean prob_wed_evening;
    private boolean prob_thu_evening;

    public boolean isProb_mon_morning() {
        return prob_mon_morning;
    }

    public void setProb_mon_morning(boolean prob_mon_morning) {
        this.prob_mon_morning = prob_mon_morning;
    }

    public boolean isProb_tue_morning() {
        return prob_tue_morning;
    }

    public void setProb_tue_morning(boolean prob_tue_morning) {
        this.prob_tue_morning = prob_tue_morning;
    }

    public boolean isProb_wed_morning() {
        return prob_wed_morning;
    }

    public void setProb_wed_morning(boolean prob_wed_morning) {
        this.prob_wed_morning = prob_wed_morning;
    }

    public boolean isProb_thu_morning() {
        return prob_thu_morning;
    }

    public void setProb_thu_morning(boolean prob_thu_morning) {
        this.prob_thu_morning = prob_thu_morning;
    }

    public boolean isProb_fri_morning() {
        return prob_fri_morning;
    }

    public void setProb_fri_morning(boolean prob_fri_morning) {
        this.prob_fri_morning = prob_fri_morning;
    }

    public boolean isProb_mon_evening() {
        return prob_mon_evening;
    }

    public void setProb_mon_evening(boolean prob_mon_evening) {
        this.prob_mon_evening = prob_mon_evening;
    }

    public boolean isProb_tue_evening() {
        return prob_tue_evening;
    }

    public void setProb_tue_evening(boolean prob_tue_evening) {
        this.prob_tue_evening = prob_tue_evening;
    }

    public boolean isProb_wed_evening() {
        return prob_wed_evening;
    }

    public void setProb_wed_evening(boolean prob_wed_evening) {
        this.prob_wed_evening = prob_wed_evening;
    }

    public boolean isProb_thu_evening() {
        return prob_thu_evening;
    }

    public void setProb_thu_evening(boolean prob_thu_evening) {
        this.prob_thu_evening = prob_thu_evening;
    }

    public boolean isProb_fri_evening() {
        return prob_fri_evening;
    }

    public void setProb_fri_evening(boolean prob_fri_evening) {
        this.prob_fri_evening = prob_fri_evening;
    }

    private boolean prob_fri_evening;


    ///
    private String mon_morning;
    private String tue_morning;
    private String wed_morning;
    private String thu_morning;
    private String fri_morning;

    //set evening code

    private String mon_evening;
    private String tue_evening;
    private String wed_evening;
    private String thu_evening;
    private String fri_evening;

    public String getMon_evening() {
        return mon_evening;
    }

    public String getTue_evening() {
        return tue_evening;
    }

    public String getWed_evening() {
        return wed_evening;
    }

    public String getThu_evening() {
        return thu_evening;
    }

    public String getFri_evening() {
        return fri_evening;
    }

    public void setMon_evening(String mon_evening) {
        this.mon_evening = mon_evening;
    }

    public void setTue_evening(String tue_evening) {
        this.tue_evening = tue_evening;
    }

    public void setWed_evening(String wed_evening) {
        this.wed_evening = wed_evening;
    }

    public void setThu_evening(String thu_evening) {
        this.thu_evening = thu_evening;
    }

    public void setFri_evening(String fri_evening) {
        this.fri_evening = fri_evening;
    }

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
