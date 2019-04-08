package com.example.afinal.fingerPrint_Login.oop;

public class MC_Null_TestTimeStamp {

    private TestTimeStamp testTimeStamp;
    private int id;
    private String name;
    private String note;
    private boolean mC_note;

    public MC_Null_TestTimeStamp() {

    }

    public MC_Null_TestTimeStamp(String name, TestTimeStamp testTimeStamp) {
        this.testTimeStamp = testTimeStamp;
        this.name=name;
    }

    public TestTimeStamp getTestTimeStamp() {
        return testTimeStamp;
    }

    public void setTestTimeStamp(TestTimeStamp testTimeStamp) {
        this.testTimeStamp = testTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean ismC_note() {
        return mC_note;
    }

    public void setmC_note(boolean mC_note) {
        this.mC_note = mC_note;
    }
}
