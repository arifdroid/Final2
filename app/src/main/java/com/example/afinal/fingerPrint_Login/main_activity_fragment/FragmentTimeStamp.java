package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.oop.EntryMorning;
import com.example.afinal.fingerPrint_Login.oop.MC_Null_TestTimeStamp;
import com.example.afinal.fingerPrint_Login.oop.TestTimeStamp;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class FragmentTimeStamp extends Fragment implements Observer, View.OnClickListener {

    // https://javadoc.jitpack.io/com/github/PhilJay/MPAndroidChart/v3.0.3/javadoc/com/github/mikephil/charting/data/BaseEntry.html#setIcon-android.graphics.drawable.Drawable-

    private LineChart chart;

    public FragmentTimeStamp() {
    }

    //setup collection reference

    private CollectionReference collectionReferenceTest;

    //setup list return from firestore;

    private ArrayList<TestTimeStamp> testTimeStampsList;

    //drawing graph

    private ArrayList<Entry> entries = new ArrayList<>();

    private ArrayList<Entry> entriesV2 = new ArrayList<>();

    ArrayList<Entry> entries3 = new ArrayList<>();


    LineDataSet dataSet;

    LineDataSet dataSet2;

    LineData data;

    /// list of dataset, reset color.

    ArrayList<LineDataSet> dataSetArrayList;

    //wrap firestore for observable

    private TimeStampFireStore_Handler timeStampFireStore_handler;


    //// LAST

    private ArrayList<TestTimeStamp> finalListRemap;

    private ArrayList<ArrayList<Entry>> listof_entryList_Morning;

    //revise
    private ArrayList<EntryMorning> entryMorningArrayList;
    private ArrayList<EntryEvening> entryEveningArrayList;

    private ArrayList<ArrayList<Entry>> listof_entryList_Evening;


    private ArrayList<LineDataSet> dataSetArrayList_Final;

    XAxis xAxis;

    //adding option manipulate viewport

    private FloatingActionButton fButton, fButtonReset;

    //set list, to refer who is MC or null, or not recorded.

    //private ArrayList<MC_Null_Test>

    private ArrayList<MC_Null_TestTimeStamp> arrayListMC_Null;

    //// check  int pointer for late person, from datasetFinal point of view.

    class FinalPointer{

        int pointer_1;
        int pointer_2;

        public FinalPointer(){

        }

        public FinalPointer(int pointer_1,int pointer_2){
            this.pointer_1 = pointer_1;
            this.pointer_2 = pointer_2;
        }

        public int getPointer_1() {
            return pointer_1;
        }

        public void setPointer_1(int pointer_1) {
            this.pointer_1 = pointer_1;
        }

        public int getPointer_2() {
            return pointer_2;
        }

        public void setPointer_2(int pointer_2) {
            this.pointer_2 = pointer_2;
        }
    }

    ArrayList<FinalPointer> finalPointerArrayList;

    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.bottom_nav_timestamp_fragment, container, false);
        //textView = rootView.findViewById(R.id.bottom_nav_fragment_timeStamp_textView);

        fButton = rootView.findViewById(R.id.bottomNav_floatButtonShowLateTodayiD);

        fButtonReset = rootView.findViewById(R.id.bottomNav_floatButtonResetiD);

        dataSetArrayList = new ArrayList<>();
        testTimeStampsList = new ArrayList<>();

        arrayListMC_Null = new ArrayList<>();

        //revise
        entryMorningArrayList = new ArrayList<>();
        entryEveningArrayList = new ArrayList<>();

        dataSetArrayList_Final = new ArrayList<>();
        finalListRemap = new ArrayList<>();

        listof_entryList_Morning = new ArrayList<>();

        listof_entryList_Evening = new ArrayList<>();

        finalPointerArrayList = new ArrayList<>();


        Log.i("checkTimeStamp ", "flow: 1");

        chart = rootView.findViewById(R.id.chartiD);

        collectionReferenceTest = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document("ariff+60190_doc")
                .collection("all_employee_thisAdmin_collection");

        Log.i("checkChart Flow: ", "0");


//
        xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //setting x axis values

        //final String[] months = new String[]{"Mon", "Tue","Wed","Thu","Fri"};

        final String[] months = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri"};

        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value];
            }
        });


        //xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        //  xAxis.setValueFormatter(formatter);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = chart.getAxisLeft();
       // yAxisLeft.setGranularity(1f);
        //yAxisLeft.setInverted(true);
//
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setGranularity(1f);
        //yAxisLeft.setAxisMaximum(21f);
//
//        final String[] hours = new String[]{"","","","","","",   "",   "2100", "2000","1900","1800","1700","1600", "1500","1400","1300","1200", "1100", "1000","0900","0800","0700"};
//                                    //       0, 1, 2, 3, 4, 5,    6,       7,     8      9,    10,     11,    12,    13,    14,    15,     16,     17,    18,    19,   20,  21
//        yAxisLeft.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return hours[(int) value];
//            }
//        });


        final String[] hours2 = new String[]{"2300", "2200", "2100", "2000", "1900", "1800", "1700", "1600", "1500", "1400", "1300", "1200", "1100", "1000", "0900", "0800", "0700", "0600", "0500", "0400", "0300", "0200", "0100", "0000"};
                                     //           0,   1,      2,     3,      4,     5,    6,     7,      8      9,     10,     11,   12,     13,     14,    15,    16,    17,     18,    19,    20,    21,      22,  23
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return hours2[(int) value];
            }
        });


        Log.i("checkChart Flow: ", "1");

        // Setting Data
        entries.add(new Entry(0, 7));
        entries.add(new Entry(1, 8));
        entries.add(new Entry(2, 12));
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 6));


        //dataSet2 = new LineDataSet();
        dataSet = new LineDataSet(entries, "check out V1");

        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));


        dataSet.getEntryForIndex(1).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_outline_black_24dp));
        //dataSet.getEntryForIndex(1).

        //dataSetArrayList.add(dataSet);

        data = new LineData(dataSet);

        chart.setData(data);  //set adapter
//        chart.getLegend().setWordWrapEnabled(true);

        //chart.getLegend();

        chart.getLegend().setEnabled(true);

        chart.animateX(1500);

        Log.i("checkChart Flow: ", "2");


        //   chart.invalidate();

        // part >> we wrap firestore process in a class,

        Log.i("checkChartFlowFinal ", "observer, 1");

        timeStampFireStore_handler = new TimeStampFireStore_Handler(getContext(), dataSet, data, chart);

        timeStampFireStore_handler.addObserver(this);

        Log.i("checkChartFlowFinal ", "observer, 2");

        timeStampFireStore_handler.startFecthData(); //will always be false, unless notified update, and update ui once return true

        Log.i("checkChartFlowFinal ", "observer, 3");


        fButton.setOnClickListener(this);
        fButtonReset.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void update(Observable o, Object arg) {

        Log.i("checkChartFlowFinal ", "observer, 4");

        if (o instanceof TimeStampFireStore_Handler) {

//        TimeStampFireStore_Handler handler = (TimeStampFireStore_Handler) o;

            Log.i("checkChartFlowFinal ", "observer, 5");

            //this is updated dataset
            //dataSet = ((TimeStampFireStore_Handler) o).returnDataSet();

            finalListRemap = ((TimeStampFireStore_Handler) o).reTURNFINAL();


            ////////////>>>>>>>>>>>>>>>> 4 april 10AM

            HashMap<String,Float> remapAfter = new HashMap<>(); //this is final will be reuse.

            int sizeTest = finalListRemap.size();

            for (TestTimeStamp testTimeStamp : finalListRemap) {

                //by default need to set pro_ ** to false?

                testTimeStamp.setProb_mon_morning(false);
                testTimeStamp.setProb_tue_morning(false);
                testTimeStamp.setProb_wed_morning(false);
                testTimeStamp.setProb_thu_morning(false);
                testTimeStamp.setProb_fri_morning(false);


                testTimeStamp.setProb_mon_evening(false);
                testTimeStamp.setProb_tue_evening(false);
                testTimeStamp.setProb_wed_evening(false);
                testTimeStamp.setProb_thu_evening(false);
                testTimeStamp.setProb_fri_evening(false);


                ArrayList<Entry> entryArrayList = new ArrayList<>();

                //we need to handle this separately though, for cases like no entry recorded, like MC. >> add remark in testTimeStamp.

                if (testTimeStamp.getMon_morning().equals("") || testTimeStamp.getMon_morning().isEmpty() || testTimeStamp.getMon_morning() == null|| testTimeStamp.getMon_morning().equals("0")) {

                    //arrayListMC_Null.add(new MC_Null_TestTimeStamp(testTimeStamp.getName(),testTimeStamp)); //we add original value,, need to add pointer? so no need to track loop which day, or entry got problem?
                    // no need for this. just add boolean to original time stamp.
                    testTimeStamp.setProb_mon_morning(true);  // add tag for fast? tag or counter?

                    testTimeStamp.setMon_morning("8");
                }

                if (testTimeStamp.getTue_morning().equals("") || testTimeStamp.getTue_morning().isEmpty() || testTimeStamp.getTue_morning() == null|| testTimeStamp.getTue_morning().equals("0")) {

                    testTimeStamp.setProb_tue_morning(true);
                    testTimeStamp.setTue_morning("8");
                }

                if (testTimeStamp.getWed_morning().equals("") || testTimeStamp.getWed_morning().isEmpty() || testTimeStamp.getWed_morning() == null|| testTimeStamp.getWed_morning().equals("0")) {

                    testTimeStamp.setProb_wed_morning(true);
                    testTimeStamp.setWed_morning("8");
                }

                if (testTimeStamp.getThu_morning().equals("") || testTimeStamp.getThu_morning().isEmpty() || testTimeStamp.getThu_morning() == null|| testTimeStamp.getThu_morning().equals("0")) {
                    testTimeStamp.setProb_thu_morning(true);
                    testTimeStamp.setThu_morning("8");
                }

                if (testTimeStamp.getFri_morning().equals("") || testTimeStamp.getFri_morning().isEmpty() || testTimeStamp.getFri_morning() == null|| testTimeStamp.getFri_morning().equals("0")) {
                    testTimeStamp.setProb_fri_morning(true);
                    testTimeStamp.setFri_morning("8");
                }

                // evening,

                if (testTimeStamp.getMon_evening().equals("") || testTimeStamp.getMon_evening().isEmpty() || testTimeStamp.getMon_evening() == null|| testTimeStamp.getMon_evening().equals("0")) {
                    testTimeStamp.setProb_mon_evening(true);
                    testTimeStamp.setMon_evening("17");
                }
                if (testTimeStamp.getTue_evening().equals("") || testTimeStamp.getTue_evening().isEmpty() || testTimeStamp.getTue_evening() == null|| testTimeStamp.getTue_evening().equals("0")) {
                    testTimeStamp.setProb_tue_evening(true);
                    testTimeStamp.setTue_evening("17");
                }

                if (testTimeStamp.getWed_evening().equals("") || testTimeStamp.getWed_evening().isEmpty() || testTimeStamp.getWed_evening() == null || testTimeStamp.getWed_evening().equals("0")) {
                    testTimeStamp.setProb_wed_evening(true);
                    testTimeStamp.setWed_evening("17");
                }
                if (testTimeStamp.getThu_evening().equals("") || testTimeStamp.getThu_evening().isEmpty() || testTimeStamp.getThu_evening() == null || testTimeStamp.getThu_evening().equals("0")) {

                    testTimeStamp.setProb_thu_evening(true);
                    testTimeStamp.setThu_evening("17");
                }
                if (testTimeStamp.getFri_evening().equals("") || testTimeStamp.getFri_evening().isEmpty() || testTimeStamp.getFri_evening() == null|| testTimeStamp.getFri_evening().equals("0")) {
                    testTimeStamp.setProb_fri_evening(true);
                    testTimeStamp.setFri_evening("17");
                }


                //we could remap timeStampList, single out all morning entry into single morning list.,, remap into hasmap

                boolean check = testTimeStamp.isProb_fri_evening();

                HashMap<String, Float> remap = new HashMap<>();

                Float mon_morning_remap = Float.valueOf(testTimeStamp.getMon_morning());
                remap.put("mon_morning_map", mon_morning_remap);

                Float tue_morning_remap = Float.valueOf(testTimeStamp.getTue_morning());
                remap.put("tue_morning_map", tue_morning_remap);

                Float wed_morning_remap = Float.valueOf(testTimeStamp.getWed_morning());
                remap.put("wed_morning_map", wed_morning_remap);

                Float thu_morning_remap = Float.valueOf(testTimeStamp.getThu_morning());
                remap.put("thu_morning_map", thu_morning_remap);

                Float fri_morning_remap = Float.valueOf(testTimeStamp.getFri_morning());
                remap.put("fri_morning_map", fri_morning_remap);

                // .. 1pm , we could add evening , and set it up direct as well.

                Float mon_evening_remap = Float.valueOf(testTimeStamp.getMon_evening());    // value return will be in 17pm not 5pm.
                remap.put("mon_evening_map", mon_evening_remap);

                Float tue_evening_remap = Float.valueOf(testTimeStamp.getTue_evening());
                remap.put("tue_evening_map", tue_evening_remap);

                Float wed_evening_remap = Float.valueOf(testTimeStamp.getWed_evening());
                remap.put("wed_evening_map", wed_evening_remap);

                Float thu_evening_remap = Float.valueOf(testTimeStamp.getThu_evening());
                remap.put("thu_evening_map", thu_evening_remap);

                Float fri_evening_remap = Float.valueOf(testTimeStamp.getFri_evening());
                remap.put("fri_evening_map", fri_evening_remap);



                // we could remap

                for (Map.Entry<String, Float> entry : remap.entrySet()) {

                        if(entry.getKey().equals("mon_morning_map")) {

                            Float afterAdjusted=0f;

                            if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                                Float beforeAdjusted = 6f+ (1f-((entry.getValue()-6f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 12f-2f); // here we adjusted right?

                                afterAdjusted = entry.getValue();

                            }

                            if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                                Float beforeAdjusted = 7f+ (1f-((entry.getValue()-7f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 10f-2f);
                                afterAdjusted = entry.getValue();
                            }

                            if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                                Float beforeAdjusted = 8f+ (1f-((entry.getValue()-8f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 8f-2f);
                                afterAdjusted = entry.getValue();
                            }

                            if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                                Float beforeAdjusted = 9f+ (1f-((entry.getValue()-9f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 6f-2f);
                                afterAdjusted = entry.getValue();
                            }


                            if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                                Float beforeAdjusted = 10f+ (1f-((entry.getValue()-10f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 4f-2f);
                                afterAdjusted = entry.getValue();

                            }

                            if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                                Float beforeAdjusted = 11f+ (1f-((entry.getValue()-11f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 2f-2f);

                                afterAdjusted = entry.getValue();
                            }

                            if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                                Float beforeAdjusted = 12f+ (1f-((entry.getValue()-12f)/0.6f)); // ... >  6.30 .. +
                                entry.setValue(beforeAdjusted + 0f-2f);
                                afterAdjusted= entry.getValue();
                            }

                            remapAfter.put("mon_morning",afterAdjusted);

                        } //finish monday

                    if(entry.getKey().equals("tue_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            Float beforeAdjusted = 6f+ (1f-((entry.getValue()-6f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 12f-2f); // here we adjusted right?

                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            Float beforeAdjusted = 7f+ (1f-((entry.getValue()-7f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 10f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            Float beforeAdjusted = 8f+ (1f-((entry.getValue()-8f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 8f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            Float beforeAdjusted = 9f+ (1f-((entry.getValue()-9f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 6f-2f);
                            afterAdjusted = entry.getValue();
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            Float beforeAdjusted = 10f+ (1f-((entry.getValue()-10f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 4f-2f);
                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            Float beforeAdjusted = 11f+ (1f-((entry.getValue()-11f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 2f-2f);

                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            Float beforeAdjusted = 12f+ (1f-((entry.getValue()-12f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 0f-2f);
                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("tue_morning",afterAdjusted);

                    } // tue morning

                    if(entry.getKey().equals("wed_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            Float beforeAdjusted = 6f+ (1f-((entry.getValue()-6f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 12f-2f); // here we adjusted right?

                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            Float beforeAdjusted = 7f+ (1f-((entry.getValue()-7f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 10f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            Float beforeAdjusted = 8f+ (1f-((entry.getValue()-8f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 8f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            Float beforeAdjusted = 9f+ (1f-((entry.getValue()-9f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 6f-2f);
                            afterAdjusted = entry.getValue();
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            Float beforeAdjusted = 10f+ (1f-((entry.getValue()-10f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 4f-2f);
                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            Float beforeAdjusted = 11f+ (1f-((entry.getValue()-11f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 2f-2f);

                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            Float beforeAdjusted = 12f+ (1f-((entry.getValue()-12f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 0f-2f);
                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("wed_morning",afterAdjusted);

                    } // wed morning

                    if(entry.getKey().equals("thu_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            Float beforeAdjusted = 6f+ (1f-((entry.getValue()-6f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 12f-2f); // here we adjusted right?

                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            Float beforeAdjusted = 7f+ (1f-((entry.getValue()-7f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 10f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            Float beforeAdjusted = 8f+ (1f-((entry.getValue()-8f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 8f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            Float beforeAdjusted = 9f+ (1f-((entry.getValue()-9f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 6f-2f);
                            afterAdjusted = entry.getValue();
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            Float beforeAdjusted = 10f+ (1f-((entry.getValue()-10f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 4f-2f);
                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            Float beforeAdjusted = 11f+ (1f-((entry.getValue()-11f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 2f-2f);

                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            Float beforeAdjusted = 12f+ (1f-((entry.getValue()-12f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 0f-2f);
                            afterAdjusted= entry.getValue();
                        }
                        remapAfter.put("thu_morning",afterAdjusted);

                    } // thursday morning

                    if(entry.getKey().equals("fri_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            Float beforeAdjusted = 6f+ (1f-((entry.getValue()-6f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 12f-2f); // here we adjusted right?

                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            Float beforeAdjusted = 7f+ (1f-((entry.getValue()-7f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 10f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            Float beforeAdjusted = 8f+ (1f-((entry.getValue()-8f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 8f-2f);
                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            Float beforeAdjusted = 9f+ (1f-((entry.getValue()-9f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 6f-2f);
                            afterAdjusted = entry.getValue();
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            Float beforeAdjusted = 10f+ (1f-((entry.getValue()-10f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 4f-2f);
                            afterAdjusted = entry.getValue();

                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            Float beforeAdjusted = 11f+ (1f-((entry.getValue()-11f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 2f-2f);

                            afterAdjusted = entry.getValue();
                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            Float beforeAdjusted = 12f+ (1f-((entry.getValue()-12f)/0.6f)); // ... >  6.30 .. +
                            entry.setValue(beforeAdjusted + 0f-2f);
                            afterAdjusted= entry.getValue();
                        }
                        remapAfter.put("fri_morning",afterAdjusted);

                    }


                    //////////// evening partt


                    //actually, number return in +12h format, so . 5pm = 17pm.


                    if(entry.getKey().equals("mon_evening_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            Float beforeAdjusted = 16f+ (1f-((entry.getValue()-16f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -10f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            Float beforeAdjusted = 17f+ (1f-((entry.getValue()-17f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -12f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            Float beforeAdjusted = 18f+ (1f-((entry.getValue()-18f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -14f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            Float beforeAdjusted = 19f+ (1f-((entry.getValue()-19f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -16f); //this is the offset,
                            afterAdjusted= entry.getValue();

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            Float beforeAdjusted = 20f+ (1f-((entry.getValue()-20f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -18f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 21f && entry.getValue() < 22f) {
                            Float beforeAdjusted = 21f+ (1f-((entry.getValue()-21f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -20f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("mon_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("tue_evening_map")) {

                        Float afterAdjusted=0f;
                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            Float beforeAdjusted = 16f+ (1f-((entry.getValue()-16f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -10f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            Float beforeAdjusted = 17f+ (1f-((entry.getValue()-17f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -12f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            Float beforeAdjusted = 18f+ (1f-((entry.getValue()-18f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -14f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            Float beforeAdjusted = 19f+ (1f-((entry.getValue()-19f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -16f); //this is the offset,
                            afterAdjusted= entry.getValue();

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            Float beforeAdjusted = 20f+ (1f-((entry.getValue()-20f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -18f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 21f && entry.getValue() < 22f) {
                            Float beforeAdjusted = 21f+ (1f-((entry.getValue()-21f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -20f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }
                        remapAfter.put("tue_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("wed_evening_map")) {

                        Float afterAdjusted=0f;


                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            Float beforeAdjusted = 16f+ (1f-((entry.getValue()-16f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -10f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            Float beforeAdjusted = 17f+ (1f-((entry.getValue()-17f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -12f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            Float beforeAdjusted = 18f+ (1f-((entry.getValue()-18f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -14f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            Float beforeAdjusted = 19f+ (1f-((entry.getValue()-19f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -16f); //this is the offset,
                            afterAdjusted= entry.getValue();

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            Float beforeAdjusted = 20f+ (1f-((entry.getValue()-20f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -18f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 21f && entry.getValue() < 22f) {
                            Float beforeAdjusted = 21f+ (1f-((entry.getValue()-21f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -20f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("wed_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("thu_evening_map")) {

                        Float afterAdjusted=0f;


                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            Float beforeAdjusted = 16f+ (1f-((entry.getValue()-16f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -10f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            Float beforeAdjusted = 17f+ (1f-((entry.getValue()-17f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -12f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            Float beforeAdjusted = 18f+ (1f-((entry.getValue()-18f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -14f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            Float beforeAdjusted = 19f+ (1f-((entry.getValue()-19f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -16f); //this is the offset,
                            afterAdjusted= entry.getValue();

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            Float beforeAdjusted = 20f+ (1f-((entry.getValue()-20f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -18f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 21f && entry.getValue() < 22f) {
                            Float beforeAdjusted = 21f+ (1f-((entry.getValue()-21f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -20f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("thu_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("fri_evening_map")) {

                        Float afterAdjusted=0f;


                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            Float beforeAdjusted = 16f+ (1f-((entry.getValue()-16f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -10f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            Float beforeAdjusted = 17f+ (1f-((entry.getValue()-17f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -12f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            Float beforeAdjusted = 18f+ (1f-((entry.getValue()-18f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -14f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            Float beforeAdjusted = 19f+ (1f-((entry.getValue()-19f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -16f); //this is the offset,
                            afterAdjusted= entry.getValue();

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            Float beforeAdjusted = 20f+ (1f-((entry.getValue()-20f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -18f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        if (entry.getValue() >= 21f && entry.getValue() < 22f) {
                            Float beforeAdjusted = 21f+ (1f-((entry.getValue()-21f)/0.6f)); // ... > this is getting  the minutes valued, scaled.
                            entry.setValue(beforeAdjusted -20f); //this is the offset,
                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("fri_evening",afterAdjusted);

                    }


                } //fnisih hashmap remap loop

                //checking remapAfter size,

                int sizeRemap = remapAfter.size();

                int sizemap = remap.size();

                    entryArrayList.add(new Entry(0, remapAfter.get("mon_morning")));
                    entryArrayList.add(new Entry(1, remapAfter.get("tue_morning")));
                    entryArrayList.add(new Entry(2, remapAfter.get("wed_morning")));
                    entryArrayList.add(new Entry(3, remapAfter.get("thu_morning")));
                    entryArrayList.add(new Entry(4, remapAfter.get("fri_morning")));

                    for (int i = 0; i < entryArrayList.size(); i++) {

                        Float checkConstraintTime = entryArrayList.get(i).getY();

                        if (checkConstraintTime <= 15f) {
                            entryArrayList.get(i).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                        }


                    }

                    entryMorningArrayList.add(new EntryMorning(testTimeStamp.getName(), entryArrayList)); // hence this list, will always correspond to how big our document/employee is
                    //settle morning


                // .>> 10.30AM settle 8 april here,

                ArrayList<Entry> entryArrayList2 = new ArrayList<>();

                entryArrayList2.add(new Entry(0, remapAfter.get("mon_evening"))); //
                entryArrayList2.add(new Entry(1, remapAfter.get("tue_evening")));
                entryArrayList2.add(new Entry(2, remapAfter.get("wed_evening")));
                entryArrayList2.add(new Entry(3, remapAfter.get("thu_evening")));
                entryArrayList2.add(new Entry(4, remapAfter.get("fri_evening")));



                for (int i = 0; i < entryArrayList2.size(); i++) {

                    Float checkConstraintTime = entryArrayList2.get(i).getY();

                    if (checkConstraintTime > 6f) {
                        entryArrayList2.get(i).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                    }

                }

                entryEveningArrayList.add(new EntryEvening(testTimeStamp.getName(), entryArrayList2));
//

                }




                //evening
                ////////////>>>>>>>>>>>>>>>>

                // >> >>>>>>>>

                chart.clearValues();


                for (int listDataSet = 0; listDataSet < entryMorningArrayList.size(); listDataSet++) {


                    dataSetArrayList_Final.add(new LineDataSet(entryMorningArrayList.get(listDataSet).getEntryArrayList(), entryMorningArrayList.get(listDataSet).getName()));


                }

                //

                for (int listDataSet2 = 0; listDataSet2 < entryEveningArrayList.size(); listDataSet2++) {

                    //adding to the same list of data set
                    dataSetArrayList_Final.add(new LineDataSet(entryEveningArrayList.get(listDataSet2).getEntryArrayList(), entryEveningArrayList.get(listDataSet2).getName()));


                }

                dataSetArrayList_Final.get(0).setValueTextSize(0f);

                //set up icon for null value, and mc , check morning first




                for(TestTimeStamp testTimeStamp: finalListRemap) {



                    //for(EntryMorning entryMorning: entryMorningArrayList){
                    // we not sure entry represent which. , so better use index since, 0 always mean monday, and so on
                    for (int k = 0; k < entryMorningArrayList.size(); k++) {

                        if (testTimeStamp.getName() == entryMorningArrayList.get(k).getName()) {

                            if (testTimeStamp.isProb_mon_morning()) {
                                //entryMorning.getEntry().setIcon(); // something wrong ,, we not sure which entry , is

                                entryMorningArrayList.get(k).getEntryArrayList().get(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if (testTimeStamp.isProb_tue_morning()) {
                                entryMorningArrayList.get(k).getEntryArrayList().get(1).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if (testTimeStamp.isProb_wed_morning()) {
                                entryMorningArrayList.get(k).getEntryArrayList().get(2).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if (testTimeStamp.isProb_thu_morning()) {
                                entryMorningArrayList.get(k).getEntryArrayList().get(3).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if (testTimeStamp.isProb_fri_morning()) {
                                entryMorningArrayList.get(k).getEntryArrayList().get(4).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }

                        }

                    }

                }

            for(TestTimeStamp testTimeStamp: finalListRemap) {


                //now set evening MC or null

                    for(int k =0; k< entryEveningArrayList.size();k++){

                        if(testTimeStamp.getName()==entryEveningArrayList.get(k).getName()){

                            if(testTimeStamp.isProb_mon_evening()){
                                //entryMorning.getEntry().setIcon(); // something wrong ,, we not sure which entry , is

                                entryEveningArrayList.get(k).getEntryArrayList().get(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if(testTimeStamp.isProb_tue_evening()){
                                entryEveningArrayList.get(k).getEntryArrayList().get(1).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if(testTimeStamp.isProb_wed_evening()){
                                entryEveningArrayList.get(k).getEntryArrayList().get(2).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if(testTimeStamp.isProb_thu_evening()){
                                entryEveningArrayList.get(k).getEntryArrayList().get(3).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }
                            if(testTimeStamp.isProb_fri_evening()){
                                entryEveningArrayList.get(k).getEntryArrayList().get(4).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));

                            }

                        }

                    }

                }



                //then add to the data.

                //add all
                for (int dataCount = 0; dataCount < (entryEveningArrayList.size() * 2); dataCount++) {

                    data.addDataSet(dataSetArrayList_Final.get(dataCount));
                }

                dataSet.notifyDataSetChanged();

                data.notifyDataChanged();
                chart.notifyDataSetChanged();


                //chart.notifyDataSetChanged(); // THIS ONE FUKIN LINE

                Legend legend = chart.getLegend();

                List<LegendEntry> entrieList = new ArrayList<>();

                RandomColor randomColor = new RandomColor();

                int[] colors = randomColor.randomColor(entryMorningArrayList.size()); //only need color correspond to number of users


                ////// >> color setup below

                //go through colors, if detect red variant generate again., can improve by editing [ randomColor() library ]

                for (int i = 0; i < colors.length; i++) {

                    Log.i("checkColor now: ", " flow : 2" + "  >> i:" + i);

                    //while((colors[i]<-90000 && colors[i]>-180000 )|| colors[i]>-5000 || (colors[i]>-67000 && colors[i]<-40000)||(colors[i]>-1838656 && colors[i]<-1238656) ) {  // non of the while loop gets triggered

                    while ((colors[i] > -67000 && colors[i] < -40000)) {  // non of the while loop gets triggered

                        Log.i("checkColor now: ", " flow : 3" + "  >> i:" + i);

                        colors[i] = randomColor.randomColor(); //making sure color not too bright.

                        Log.i("checkColor now: ", " flow : 3" + " random color generated: " + colors[i] + "  >> i:" + i);


                        if (i < (colors.length - 1)) {

                            Log.i("checkColor now: ", " flow : 5");

                            int check = -(colors[i]) + colors[i + 1];

                            Log.i("checkColor diff: ", "check difference 1 : " + colors[i] + "  >> i:" + i);
                            Log.i("checkColor diff: ", "check difference 2 : " + colors[i + 1] + "  >> i:" + i);
                            Log.i("checkColor diff: ", "check difference color : " + check + "  >> i:" + i);

                            if (colors[i] < -900035) { //for other variant

                                int check2 = -(colors[i]) + colors[i + 1];

                                while (check2 <= 100000 && check2 >= -100000) { // numbers could be
                                    colors[i] = randomColor.randomColor();


                                    check2 = -colors[i] + colors[i + 1]; //everytime we create we check this

                                    Log.i("checkColor now: ", " flow : 6" + "  >> i:" + i);
                                }

                            }


                            while (check <= 10000 && check >= -10000) {
                                colors[i] = randomColor.randomColor();
                                check = -colors[i] + colors[i + 1]; //everytime we create we check this

                                Log.i("checkColor now: ", " flow : 6" + "  >> i:" + i);
                            }

                        }
                        //   } //still check if color[i] within red

                    } // still check if color[i] too bright

                }


                //finish making sure no red, recored

                //adding setup

                for (int i = 0; i < dataSetArrayList_Final.size(); i++) {

                    dataSetArrayList_Final.get(i).setAxisDependency(YAxis.AxisDependency.LEFT);
                }

                for (int j = 0; j < entryMorningArrayList.size(); j++) {

                    LegendEntry legendEntry_Morning = new LegendEntry();

                    legendEntry_Morning.formColor = colors[j];
                    dataSetArrayList_Final.get(j).setColor(colors[j]);
                    dataSetArrayList_Final.get(j).notifyDataSetChanged();
                    legendEntry_Morning.label = entryMorningArrayList.get(j).getName();
                    entrieList.add(legendEntry_Morning);
                }

                int offset = dataSetArrayList_Final.size() - entryMorningArrayList.size();

                for (int j = 0; j < entryEveningArrayList.size(); j++) {

                    LegendEntry legendEntry_Evening = new LegendEntry();

                    // legendEntry_Evening.formColor = colors[j];
                    dataSetArrayList_Final.get(j + offset).setColor(colors[j]);
                    dataSetArrayList_Final.get(j + offset).notifyDataSetChanged();
                    //legendEntry_Morning.label = entryMorningArrayList.get(j).getName();
                    // legendEntry_Evening.label = "NONE";

                    entrieList.add(legendEntry_Evening);

                    //label
                }


                //for some data which exceeds pre-defined constraint time stamp, will be coloured red.

                // dataSetArrayList_Final.get(4).setLabel();

                legend.setCustom(entrieList);


                data.notifyDataChanged();
                chart.notifyDataSetChanged();

                chart.invalidate();

                return;


        }

    }


    private void setupWhosLate(){

        //for(LineDataSet dataSet: dataSetArrayList_Final){


        // check morning first., mean halve the data
        for(int j= 0; j<dataSetArrayList_Final.size()/2;j++){

            for(int i=0; i<5; i++){

                float timestampthis = dataSetArrayList_Final.get(j).getEntryForIndex(i).getY();

                if(timestampthis<=14.99f){

                    finalPointerArrayList.add(new FinalPointer(j,i));

                }

            }

        }

        //then evening logic.

        for(int j= dataSetArrayList_Final.size()/2; j<dataSetArrayList_Final.size();j++){

            for(int i=0; i<5; i++){

                float timestampthis = dataSetArrayList_Final.get(j).getEntryForIndex(i).getY();

                if(timestampthis>=5.99f){ // relative to final data, not chart, no offset.

                    finalPointerArrayList.add(new FinalPointer(j,i));

                }

            }

        }

        int sizepointer=finalPointerArrayList.size();
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.bottomNav_floatButtonResetiD:

                chart.fitScreen();

                break;

            case R.id.bottomNav_floatButtonShowLateTodayiD:

                //logic for who we want to view. , so we need an array record, which entry is late. , correspond to who is late.

                setupWhosLate(); //setup data first.

                //we want to


                countDownTimer = new CountDownTimer() {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {



                    }
                }.start();



//                for(int i =0; i<finalPointerArrayList.size();i++){
//
//                    Handler handler = new Handler();
//
//                    Log.i("checkFinally ", "1 Flow, i: "+ i);
//
//                    final int finalI = i;
//                    handler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            Log.i("checkFinally ", "2 Flow, finalI: "+ finalI);
//                            chart.setVisibleXRange(0f, 2f);
//                            chart.setVisibleYRange(0f, 2f, dataSetArrayList_Final.get(1).getAxisDependency());
//
//                           // chart.centerViewToAnimated(dataSetArrayList_Final.get(finalPointerArrayList.get(finalI).getPointer_1()).getEntryForIndex(2).getX(), , dataSetArrayList_Final.get(4).getAxisDependency(), 2500);
//                            chart.centerViewToAnimated(dataSetArrayList_Final.get(finalPointerArrayList.get(finalI).getPointer_1()).getEntryForIndex(finalPointerArrayList.get(finalI).getPointer_2()).getX(), dataSetArrayList_Final.get(finalPointerArrayList.get(finalI).getPointer_1()).getEntryForIndex(finalPointerArrayList.get(finalI).getPointer_2()).getY(), dataSetArrayList_Final.get(4).getAxisDependency(), 1500);
//
//                        }
//                    },3000);
//
//                    Log.i("checkFinally ", "3 Flow, i: "+ i);
//                //we need to wait before we iterate to next loop.
//
//                }

                /////////////>>>>>>>>>>>>>
//
//
//                chart.setVisibleXRange(0f, 2f);
//                chart.setVisibleYRange(0f, 2f, dataSetArrayList_Final.get(4).getAxisDependency());
//
//                chart.centerViewToAnimated(dataSetArrayList_Final.get(4).getEntryForIndex(2).getX(), 16, dataSetArrayList_Final.get(4).getAxisDependency(), 2500);

                Log.i("checkLate ", " name :" + dataSetArrayList_Final.get(4).getLabel() + " , x: " + dataSetArrayList_Final.get(4).getEntryForIndex(2).getX() + " , y: " + dataSetArrayList_Final.get(4).getEntryForIndex(2).getY());


                break;


        }
    }
}