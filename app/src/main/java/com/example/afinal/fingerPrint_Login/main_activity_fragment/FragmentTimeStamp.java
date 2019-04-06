package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.oop.EntryMorning;
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

        //revise
        entryMorningArrayList = new ArrayList<>();
        entryEveningArrayList = new ArrayList<>();

        dataSetArrayList_Final = new ArrayList<>();
        finalListRemap = new ArrayList<>();

        listof_entryList_Morning = new ArrayList<>();

        listof_entryList_Evening = new ArrayList<>();

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
        yAxisLeft.setGranularity(1f);
        //yAxisLeft.setInverted(true);
//
        yAxisLeft.setAxisMinimum(1f);
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


        final String[] hours2 = new String[]{   "2400", "2300", "2200", "2100", "2000", "1900", "1800", "1700", "1600", "1500", "1400", "1300", "1200", "1100", "1000", "0900", "0800", "0700", "0600", "0500", "0400", "0300", "0200", "0100"};
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

            for (TestTimeStamp testTimeStamp : finalListRemap) {


                ArrayList<Entry> entryArrayList = new ArrayList<>();


                if (testTimeStamp.getMon_morning().equals("") || testTimeStamp.getMon_morning().isEmpty() || testTimeStamp.getMon_morning() == null) {
                    testTimeStamp.setMon_morning("8");
                }

                if (testTimeStamp.getTue_morning().equals("") || testTimeStamp.getTue_morning().isEmpty() || testTimeStamp.getTue_morning() == null) {
                    testTimeStamp.setTue_morning("8");
                }

                if (testTimeStamp.getWed_morning().equals("") || testTimeStamp.getWed_morning().isEmpty() || testTimeStamp.getWed_morning() == null) {
                    testTimeStamp.setWed_morning("8");
                }

                if (testTimeStamp.getThu_morning().equals("") || testTimeStamp.getThu_morning().isEmpty() || testTimeStamp.getThu_morning() == null) {
                    testTimeStamp.setThu_morning("8");
                }

                if (testTimeStamp.getFri_morning().equals("") || testTimeStamp.getFri_morning().isEmpty() || testTimeStamp.getFri_morning() == null) {
                    testTimeStamp.setFri_morning("8");
                }

                // evening,

                if (testTimeStamp.getMon_evening().equals("") || testTimeStamp.getMon_evening().isEmpty() || testTimeStamp.getMon_evening() == null) {
                    testTimeStamp.setMon_evening("5");
                }
                if (testTimeStamp.getTue_evening().equals("") || testTimeStamp.getTue_evening().isEmpty() || testTimeStamp.getTue_evening() == null) {
                    testTimeStamp.setTue_evening("5");
                }

                if (testTimeStamp.getWed_evening().equals("") || testTimeStamp.getWed_evening().isEmpty() || testTimeStamp.getWed_evening() == null || testTimeStamp.getWed_evening().equals("0")) {
                    testTimeStamp.setWed_evening("5");
                }
                if (testTimeStamp.getThu_evening().equals("") || testTimeStamp.getThu_evening().isEmpty() || testTimeStamp.getThu_evening() == null) {
                    testTimeStamp.setThu_evening("5");
                }
                if (testTimeStamp.getFri_evening().equals("") || testTimeStamp.getFri_evening().isEmpty() || testTimeStamp.getFri_evening() == null) {
                    testTimeStamp.setFri_evening("5");
                }


                //we could remap timeStampList, single out all morning entry into single morning list.,, remap into hasmap

                HashMap<String, Float> remap = new HashMap<>();

                Float mon_morning_remap = Float.valueOf(testTimeStamp.getMon_morning());
                remap.put("mon_morning_map", mon_morning_remap);

                Float tue_morning_remap = Float.valueOf(testTimeStamp.getTue_morning());
                remap.put("tue_morning_map", tue_morning_remap);

                Float wed_morning_remap = Float.valueOf(testTimeStamp.getWed_morning());
                remap.put("wed_morning_map", wed_morning_remap);

                Float thu_morning_remap = Float.valueOf(testTimeStamp.getTue_morning());
                remap.put("thu_morning_map", thu_morning_remap);

                Float fri_morning_remap = Float.valueOf(testTimeStamp.getFri_morning());
                remap.put("fri_morning_map", fri_morning_remap);

                // .. 1pm , we could add evening , and set it up direct as well.

                Float mon_evening_remap = Float.valueOf(testTimeStamp.getMon_evening());
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



                //problem is we cannot reuse this entry remap again outside this.

                for (Map.Entry<String, Float> entry : remap.entrySet()) {

                        if(entry.getKey().equals("mon_morning_map")) {

                            Float afterAdjusted=0f;

                            if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                                Float beforeAdjusted = entry.getValue();
                                entry.setValue(beforeAdjusted + 12f); // here we adjusted right?

                                afterAdjusted = entry.getValue();

                            }

                            if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                                Float beforeAdjusted = entry.getValue();
                                entry.setValue(beforeAdjusted + 10f);
                                afterAdjusted = entry.getValue();
                            }

                            if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                                Float beforeAdjusted = entry.getValue();
                                entry.setValue(beforeAdjusted + 8f);
                                afterAdjusted = entry.getValue();
                            }

                            if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                                Float beforeAdjusted = entry.getValue();
                                entry.setValue(beforeAdjusted + 6f);
                                afterAdjusted = entry.getValue();
                            }


                            if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                                Float beforeAdjusted = entry.getValue();
                                entry.setValue(beforeAdjusted + 4f);
                                afterAdjusted = entry.getValue();

                            }

                            if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                                Float beforeAdjusted = entry.getValue();
                                entry.setValue(beforeAdjusted + 2f);

                                afterAdjusted = entry.getValue();
                            }

                            if (entry.getValue() >= 12f && entry.getValue() < 13f) {
//                           Float beforeAdjusted = entry.getValue();
//                           entry.setValue(beforeAdjusted);

                                afterAdjusted= entry.getValue();
                            }

                            remapAfter.put("mon_morning",afterAdjusted);

                        } //finish monday

                    if(entry.getKey().equals("tue_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()+12f;

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            afterAdjusted = entry.getValue()+10f;
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            afterAdjusted = entry.getValue()+8f;
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            afterAdjusted = entry.getValue()+6f;
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            afterAdjusted = entry.getValue()+4f;
                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            afterAdjusted = entry.getValue()+2f;

                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("tue_morning",afterAdjusted);

                    } // tue morning

                    if(entry.getKey().equals("wed_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()+12f;

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            afterAdjusted = entry.getValue()+10f;
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            afterAdjusted = entry.getValue()+8f;
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            afterAdjusted = entry.getValue()+6f;
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            afterAdjusted = entry.getValue()+4f;
                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            afterAdjusted = entry.getValue()+2f;

                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("wed_morning",afterAdjusted);

                    } // wed morning

                    if(entry.getKey().equals("thu_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()+12f;

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            afterAdjusted = entry.getValue()+10f;
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            afterAdjusted = entry.getValue()+8f;
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            afterAdjusted = entry.getValue()+6f;
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            afterAdjusted = entry.getValue()+4f;
                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            afterAdjusted = entry.getValue()+2f;

                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("thu_morning",afterAdjusted);

                    } // thursday morning

                    if(entry.getKey().equals("fri_morning_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 6f && entry.getValue() < 7f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()+12f;

                        }

                        if (entry.getValue() >= 7f && entry.getValue() < 8f) {
                            afterAdjusted = entry.getValue()+10f;
                        }

                        if (entry.getValue() >= 8f && entry.getValue() < 9f) {
                            afterAdjusted = entry.getValue()+8f;
                        }

                        if (entry.getValue() >= 9f && entry.getValue() < 10f) {
                            afterAdjusted = entry.getValue()+6f;
                        }


                        if (entry.getValue() >= 10f && entry.getValue() < 11f) {
                            afterAdjusted = entry.getValue()+4f;
                        }

                        if (entry.getValue() >= 11f && entry.getValue() < 12f) {
                            afterAdjusted = entry.getValue()+2f;

                        }

                        if (entry.getValue() >= 12f && entry.getValue() < 13f) {

                            afterAdjusted= entry.getValue();
                        }

                        remapAfter.put("fri_morning",afterAdjusted);

                    }


                    //////////// evening partt


                    if(entry.getKey().equals("mon_evening_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 15f && entry.getValue() < 16f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()-6f;
                        }

                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            afterAdjusted = entry.getValue()-8f;
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            afterAdjusted = entry.getValue()-10f;
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            afterAdjusted = entry.getValue()-12f;
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            afterAdjusted = entry.getValue()-14f;

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            afterAdjusted = entry.getValue()-16f;
                        }

                        if (entry.getValue() >= 22f && entry.getValue() < 22f) {
                            afterAdjusted = entry.getValue()-18f;
                        }

                        remapAfter.put("mon_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("tue_evening_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 15f && entry.getValue() < 16f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()-6f;
                        }

                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            afterAdjusted = entry.getValue()-8f;
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            afterAdjusted = entry.getValue()-10f;
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            afterAdjusted = entry.getValue()-12f;
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            afterAdjusted = entry.getValue()-14f;

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            afterAdjusted = entry.getValue()-16f;
                        }

                        if (entry.getValue() >= 22f && entry.getValue() < 22f) {
                            afterAdjusted = entry.getValue()-18f;
                        }

                        remapAfter.put("tue_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("wed_evening_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 15f && entry.getValue() < 16f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()-6f;
                        }

                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            afterAdjusted = entry.getValue()-8f;
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            afterAdjusted = entry.getValue()-10f;
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            afterAdjusted = entry.getValue()-12f;
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            afterAdjusted = entry.getValue()-14f;

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            afterAdjusted = entry.getValue()-16f;
                        }

                        if (entry.getValue() >= 22f && entry.getValue() < 22f) {
                            afterAdjusted = entry.getValue()-18f;
                        }

                        remapAfter.put("wed_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("thu_evening_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 15f && entry.getValue() < 16f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()-6f;
                        }

                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            afterAdjusted = entry.getValue()-8f;
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            afterAdjusted = entry.getValue()-10f;
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            afterAdjusted = entry.getValue()-12f;
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            afterAdjusted = entry.getValue()-14f;

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            afterAdjusted = entry.getValue()-16f;
                        }

                        if (entry.getValue() >= 22f && entry.getValue() < 22f) {
                            afterAdjusted = entry.getValue()-18f;
                        }

                        remapAfter.put("thu_evening",afterAdjusted);

                    }

                    if(entry.getKey().equals("fri_evening_map")) {

                        Float afterAdjusted=0f;

                        if (entry.getValue() >= 15f && entry.getValue() < 16f) { // which entry got extracted,
                            afterAdjusted = entry.getValue()-6f;
                        }

                        if (entry.getValue() >= 16f && entry.getValue() < 17f) {
                            afterAdjusted = entry.getValue()-8f;
                        }

                        if (entry.getValue() >= 17f && entry.getValue() < 18f) {
                            afterAdjusted = entry.getValue()-10f;
                        }

                        if (entry.getValue() >= 18f && entry.getValue() < 19f) {
                            afterAdjusted = entry.getValue()-12f;
                        }

                        if (entry.getValue() >= 19f && entry.getValue() < 20f) {
                            afterAdjusted = entry.getValue()-14f;

                        }

                        if (entry.getValue() >= 20f && entry.getValue() < 21f) {
                            afterAdjusted = entry.getValue()-16f;
                        }

                        if (entry.getValue() >= 22f && entry.getValue() < 22f) {
                            afterAdjusted = entry.getValue()-18f;
                        }

                        remapAfter.put("fri_evening",afterAdjusted);

                    }






                } //fnisih hashmap remap loop

                //checking remapAfter size,

                int sizeRemap = remapAfter.size();

                int sizemap = remap.size();





                    // >> 1120AM manual maipulation
                    //manipulate entry for chart manipulation. ,, handle morning first
//
//                    Float mon_morning_manipulated = 0f;
//                    Float tue_morning_manipulated = 0f;
//                    Float wed_morning_manipulated = 0f;
//                    Float thu_morning_manipulated = 0f;
//                    Float fri_morning_manipulated = 0f;
//
//                    for (int i = 0; i < 5; i++) { //cannot loop 5 times only,
//                        //setting up manually might cause issue, if number is out of range, like, if database gettime, by user is corrupted,
//                        //of backup user time value is wrong out of bound
//
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 6f && Float.valueOf(testTimeStamp.getMon_morning()) < 7f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning() + 12f);
//                            i++;
//                        }
//
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 7f && Float.valueOf(testTimeStamp.getMon_morning()) < 8f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning() + 10f);
//
//                            i++;
//                        }
//
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 8f && Float.valueOf(testTimeStamp.getMon_morning()) < 9f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning() + 8f);
//
//                            i++;
//                        }
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 9f && Float.valueOf(testTimeStamp.getMon_morning()) < 10f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning() + 6f);
//
//                            i++;
//                        }
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 10f && Float.valueOf(testTimeStamp.getMon_morning()) < 11f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning() + 4f);
//
//                            i++;
//                        }
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 11f && Float.valueOf(testTimeStamp.getMon_morning()) < 12f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning() + 2f);
//                            i++;
//                        }
//
//                        if (Float.valueOf(testTimeStamp.getMon_morning()) >= 12f && Float.valueOf(testTimeStamp.getMon_morning()) < 13f) {
//
//                            mon_morning_manipulated = Float.valueOf(testTimeStamp.getMon_morning());
//                        }
//
//                        ////////// up , is setting up monday morning manipulation 6AM to 12PM.
//
//                        //>> tuesday
//
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 6f && Float.valueOf(testTimeStamp.getTue_morning()) < 7f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning() + 12f);
//                            i++;
//                        }
//
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 7f && Float.valueOf(testTimeStamp.getTue_morning()) < 8f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning() + 10f);
//
//                            i++;
//                        }
//
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 8f && Float.valueOf(testTimeStamp.getTue_morning()) < 9f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning() + 8f);
//
//                            i++;
//                        }
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 9f && Float.valueOf(testTimeStamp.getTue_morning()) < 10f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning() + 6f);
//
//                            i++;
//                        }
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 10f && Float.valueOf(testTimeStamp.getTue_morning()) < 11f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning() + 4f);
//
//                            i++;
//                        }
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 11f && Float.valueOf(testTimeStamp.getTue_morning()) < 12f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning() + 2f);
//                            i++;
//                        }
//
//                        if (Float.valueOf(testTimeStamp.getTue_morning()) >= 12f && Float.valueOf(testTimeStamp.getTue_morning()) < 13f) {
//
//                            tue_morning_manipulated = Float.valueOf(testTimeStamp.getTue_morning());
//                        }
//
//
//                        ///
//
//
//                    }

                  //  ArrayList<Entry> entryArrayList = new ArrayList<>();

                //here we pull back, insert into entry

                    entryArrayList.add(new Entry(0, remapAfter.get("mon_morning")));
                    entryArrayList.add(new Entry(1, remapAfter.get("tue_morning")));
                    entryArrayList.add(new Entry(2, remapAfter.get("wed_morning")));
                    entryArrayList.add(new Entry(3, remapAfter.get("thu_morning")));
                    entryArrayList.add(new Entry(4, remapAfter.get("fri_morning")));

//                    entryArrayList.add(new Entry(0, Float.valueOf(testTimeStamp.getMon_morning())));
//                    entryArrayList.add(new Entry(1, Float.valueOf(testTimeStamp.getTue_morning())));
//                    entryArrayList.add(new Entry(2, Float.valueOf(testTimeStamp.getWed_morning())));
//                    entryArrayList.add(new Entry(3, Float.valueOf(testTimeStamp.getThu_morning())));
//                    entryArrayList.add(new Entry(4, Float.valueOf(testTimeStamp.getFri_morning())));

                    //entryArrayList.add(new Entry(0, mon_morning_remap))


                    for (int i = 0; i < entryArrayList.size(); i++) {

                        Float checkConstraintTime = entryArrayList.get(i).getY();

                        if (checkConstraintTime > 16f) {
                            entryArrayList.get(i).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));
                        }
                    }

                    entryMorningArrayList.add(new EntryMorning(testTimeStamp.getName(), entryArrayList)); // hence this list, will always correspond to how big our document/employee is
                    //settle morning
                }

                //evening

                for (TestTimeStamp testTimeStamp : finalListRemap) {


//                    if (testTimeStamp.getMon_evening().equals("") || testTimeStamp.getMon_evening().isEmpty() || testTimeStamp.getMon_evening() == null) {
//                        testTimeStamp.setMon_evening("5");
//                    }
//                    if (testTimeStamp.getTue_evening().equals("") || testTimeStamp.getTue_evening().isEmpty() || testTimeStamp.getTue_evening() == null) {
//                        testTimeStamp.setTue_evening("5");
//                    }
//
//                    if (testTimeStamp.getWed_evening().equals("") || testTimeStamp.getWed_evening().isEmpty() || testTimeStamp.getWed_evening() == null || testTimeStamp.getWed_evening().equals("0")) {
//                        testTimeStamp.setWed_evening("5");
//                    }
//                    if (testTimeStamp.getThu_evening().equals("") || testTimeStamp.getThu_evening().isEmpty() || testTimeStamp.getThu_evening() == null) {
//                        testTimeStamp.setThu_evening("5");
//                    }
//                    if (testTimeStamp.getFri_evening().equals("") || testTimeStamp.getFri_evening().isEmpty() || testTimeStamp.getFri_evening() == null) {
//                        testTimeStamp.setFri_evening("5");
//                    }

                    ArrayList<Entry> entryArrayList = new ArrayList<>();

                    entryArrayList.add(new Entry(0, remapAfter.get("mon_evening")));
                    entryArrayList.add(new Entry(1, remapAfter.get("tue_evening")));
                    entryArrayList.add(new Entry(2, remapAfter.get("wed_evening")));
                    entryArrayList.add(new Entry(3, remapAfter.get("thu_evening")));
                    entryArrayList.add(new Entry(4, remapAfter.get("fri_evening")));

                    // figure out which time stamp not following threshold.

                    // entryArrayList.get(0).setIcon();

                    for (int i = 0; i < entryArrayList.size(); i++) {

                        Float checkConstraintTime = entryArrayList.get(i).getY();

                        if (checkConstraintTime < 7f) {
                            entryArrayList.get(i).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_error_small_16dp));


                        }
                    }

                    entryEveningArrayList.add(new EntryEvening(testTimeStamp.getName(), entryArrayList));
                }

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


//
//            for(int j=0;j<dataSetArrayList_Final.size();j++){
//
//             LegendEntry entry = new LegendEntry();
//                 //we want to
//                                    // color will hit null
//                 entry.formColor = colors[j]; //color only 3, data set 6,
//                 dataSetArrayList_Final.get(j).setColor(colors[j]);
//
//                 dataSetArrayList_Final.get(j).notifyDataSetChanged();
//                 entry.label = finalListRemap.get(j).getName();
//                 entrieList.add(entry);
//             }

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


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.bottomNav_floatButtonResetiD:

                chart.fitScreen();

                break;

            case R.id.bottomNav_floatButtonShowLateTodayiD:


//


                chart.setVisibleXRange(0f, 2f);
                chart.setVisibleYRange(0f, 2f, dataSetArrayList_Final.get(4).getAxisDependency());

                chart.centerViewToAnimated(dataSetArrayList_Final.get(4).getEntryForIndex(2).getX(), 16, dataSetArrayList_Final.get(4).getAxisDependency(), 2500);

                Log.i("checkLate ", " name :" + dataSetArrayList_Final.get(4).getLabel() + " , x: " + dataSetArrayList_Final.get(4).getEntryForIndex(2).getX() + " , y: " + dataSetArrayList_Final.get(4).getEntryForIndex(2).getY());


                break;


        }
    }
}