package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FragmentTimeStamp extends Fragment implements Observer {

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.bottom_nav_timestamp_fragment, container, false);
        //textView = rootView.findViewById(R.id.bottom_nav_fragment_timeStamp_textView);

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

        final String[] months = new String[]{"Mon", "Tue","Wed","Thu","Fri"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value];

            }
        };

        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setInverted(true);



        Log.i("checkChart Flow: ", "1");

        // Setting Data
        entries.add(new Entry(0, 0));
        entries.add(new Entry(1,0));
        entries.add(new Entry(2,0));
        entries.add(new Entry(3,0));
        entries.add(new Entry(4,0));


        //dataSet2 = new LineDataSet();
        dataSet = new LineDataSet(entries, "check out V1");

        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));



        dataSet.getEntryForIndex(1).setIcon(ContextCompat.getDrawable(getContext(),R.drawable.ic_error_outline_black_24dp));
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

        timeStampFireStore_handler = new TimeStampFireStore_Handler(getContext(),dataSet,data, chart);

        timeStampFireStore_handler.addObserver(this);

        Log.i("checkChartFlowFinal ", "observer, 2");

        timeStampFireStore_handler.startFecthData(); //will always be false, unless notified update, and update ui once return true

        Log.i("checkChartFlowFinal ", "observer, 3");


        //////// >>>


       // addNewEntry();

       // getTimeStampDataNow(collectionReferenceTest); // marked out because this is not resulting as expected. asynchronous

       // addEntryAfterFinish();


        return rootView;

    }


    @Override
    public void update(Observable o, Object arg) {

        Log.i("checkChartFlowFinal ", "observer, 4");

        if(o instanceof TimeStampFireStore_Handler){

//        TimeStampFireStore_Handler handler = (TimeStampFireStore_Handler) o;

            Log.i("checkChartFlowFinal ", "observer, 5");

        //this is updated dataset
         //dataSet = ((TimeStampFireStore_Handler) o).returnDataSet();

           finalListRemap= ((TimeStampFireStore_Handler) o).reTURNFINAL();


           ////////////>>>>>>>>>>>>>>>> 4 april 10AM

           for(TestTimeStamp testTimeStamp: finalListRemap){

//
//               entryMorningArrayList.add(new EntryMorning(testTimeStamp.getName(), new Entry(0, Float.valueOf(testTimeStamp.getMon_morning()))));
//
//               //entryMorningArrayList.add(new EntryMorning(1, Float.valueOf(testTimeStamp.getTue_morning())));
//                entryMorningArrayList.set()

               // create different structure

                // we need another for(){   }

               ArrayList<Entry> entryArrayList = new ArrayList<>();



                   if(testTimeStamp.getMon_morning().equals("")||testTimeStamp.getMon_morning().isEmpty()|| testTimeStamp.getMon_morning()==null){
                       testTimeStamp.setMon_morning("8");
                   }

                   if(testTimeStamp.getTue_morning().equals("")||testTimeStamp.getTue_morning().isEmpty()|| testTimeStamp.getTue_morning()==null){
                       testTimeStamp.setTue_morning("8");
                   }

                   if(testTimeStamp.getWed_morning().equals("")||testTimeStamp.getWed_morning().isEmpty()|| testTimeStamp.getWed_morning()==null){
                       testTimeStamp.setWed_morning("8");
                   }

                   if(testTimeStamp.getThu_morning().equals("")||testTimeStamp.getThu_morning().isEmpty()|| testTimeStamp.getThu_morning()==null){
                       testTimeStamp.setThu_morning("8");
                   }

                   if(testTimeStamp.getFri_morning().equals("")||testTimeStamp.getFri_morning().isEmpty()|| testTimeStamp.getFri_morning()==null){
                       testTimeStamp.setFri_morning("8");
                   }

                   //ArrayList<Entry> entryArrayList = new ArrayList<>();

                   entryArrayList.add(new Entry(0, Float.valueOf(testTimeStamp.getMon_morning())));
                   entryArrayList.add(new Entry(1, Float.valueOf(testTimeStamp.getTue_morning())));
                   entryArrayList.add(new Entry(2, Float.valueOf(testTimeStamp.getWed_morning())));
                   entryArrayList.add(new Entry(3, Float.valueOf(testTimeStamp.getThu_morning())));
                   entryArrayList.add(new Entry(4, Float.valueOf(testTimeStamp.getFri_morning())));


               for(int i = 0; i< entryArrayList.size();i++){

                   Float checkConstraintTime = entryArrayList.get(i).getY();

                   if(checkConstraintTime>8f){
                       entryArrayList.get(i).setIcon(ContextCompat.getDrawable(getContext(),R.drawable.ic_error_small_16dp));
                   }
               }

               entryMorningArrayList.add(new EntryMorning(testTimeStamp.getName(),entryArrayList)); // hence this list, will always correspond to how big our document/employee is
            //settle morning
           }

            //evening

            for(TestTimeStamp testTimeStamp: finalListRemap){


                if(testTimeStamp.getMon_evening().equals("")||testTimeStamp.getMon_evening().isEmpty()|| testTimeStamp.getMon_evening()==null){
                    testTimeStamp.setMon_evening("5");
                }
                if (testTimeStamp.getTue_evening().equals("") || testTimeStamp.getTue_evening().isEmpty() || testTimeStamp.getTue_evening() == null) {
                    testTimeStamp.setTue_evening("5");
                }

                if (testTimeStamp.getWed_evening().equals("") || testTimeStamp.getWed_evening().isEmpty() || testTimeStamp.getWed_evening() == null||testTimeStamp.getWed_evening().equals("0")) {
                    testTimeStamp.setWed_evening("5");
                }
                if (testTimeStamp.getThu_evening().equals("") || testTimeStamp.getThu_evening().isEmpty() || testTimeStamp.getThu_evening() == null) {
                    testTimeStamp.setThu_evening("5");
                }
                if (testTimeStamp.getFri_evening().equals("") || testTimeStamp.getFri_evening().isEmpty() || testTimeStamp.getFri_evening() == null) {
                    testTimeStamp.setFri_evening("5");
                }

                ArrayList<Entry> entryArrayList = new ArrayList<>();

                 entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_evening())+12f));
                 entryArrayList.add(new Entry(1,Float.valueOf(testTimeStamp.getTue_evening())+12f));
                 entryArrayList.add(new Entry(2,Float.valueOf(testTimeStamp.getWed_evening())+12f));
                 entryArrayList.add(new Entry(3,Float.valueOf(testTimeStamp.getThu_evening())+12f));
                 entryArrayList.add(new Entry(4,Float.valueOf(testTimeStamp.getFri_evening())+12f));

                 // figure out which time stamp not following threshold.

                // entryArrayList.get(0).setIcon();

                for(int i = 0; i< entryArrayList.size();i++){

                    Float checkConstraintTime = entryArrayList.get(i).getY();

                    if(checkConstraintTime<17f){
                        //entryArrayList.get(i).setIcon(ContextCompat.getDrawable(getContext(),R.drawable.));



                    }
                }

                entryEveningArrayList.add(new EntryEvening(testTimeStamp.getName(),entryArrayList));
            }

           ////////////>>>>>>>>>>>>>>>>

           // >> >>>>>>>>

            chart.clearValues();


//            for(TestTimeStamp testTimeStamp: finalListRemap){
//
//            //handle null and zero
//
//                //if ts_mon_morning not set, will have problem. crash, null pointer.
//
//            if(testTimeStamp.getMon_morning().equals("")||testTimeStamp.getMon_morning().isEmpty()|| testTimeStamp.getMon_morning()==null){
//                testTimeStamp.setMon_morning("0");
//            }
//
//            if(testTimeStamp.getTue_morning().equals("")||testTimeStamp.getTue_morning().isEmpty()|| testTimeStamp.getTue_morning()==null){
//                    testTimeStamp.setTue_morning("0");
//            }
//
//            if(testTimeStamp.getWed_morning().equals("")||testTimeStamp.getWed_morning().isEmpty()|| testTimeStamp.getWed_morning()==null){
//                    testTimeStamp.setWed_morning("0");
//            }
//
//            if(testTimeStamp.getThu_morning().equals("")||testTimeStamp.getThu_morning().isEmpty()|| testTimeStamp.getThu_morning()==null){
//                    testTimeStamp.setThu_morning("0");
//            }
//
//            if(testTimeStamp.getFri_morning().equals("")||testTimeStamp.getFri_morning().isEmpty()|| testTimeStamp.getFri_morning()==null){
//                    testTimeStamp.setFri_morning("0");
//            }
//
//
//             ArrayList<Entry> entryArrayList = new ArrayList<>();
//
//            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_morning())));
//            entryArrayList.add(new Entry(1,Float.valueOf(testTimeStamp.getTue_morning())));
//            entryArrayList.add(new Entry(2,Float.valueOf(testTimeStamp.getWed_morning())));
//            entryArrayList.add(new Entry(3,Float.valueOf(testTimeStamp.getThu_morning())));
//            entryArrayList.add(new Entry(4,Float.valueOf(testTimeStamp.getFri_morning())));
//
//
//
//            listof_entryList_Morning.add(entryArrayList); //problem with this, though is that we dont return the data name.
//
//        }

        // evening list,

//            for (TestTimeStamp testTimeStamp : finalListRemap){
//
//                //evening setup
//
//                if(testTimeStamp.getMon_evening().equals("")||testTimeStamp.getMon_evening().isEmpty()|| testTimeStamp.getMon_evening()==null){
//                    testTimeStamp.setMon_evening("0");
//                }
//                if (testTimeStamp.getTue_evening().equals("") || testTimeStamp.getTue_evening().isEmpty() || testTimeStamp.getTue_evening() == null) {
//                    testTimeStamp.setTue_evening("0");
//                }
//
//                if (testTimeStamp.getWed_evening().equals("") || testTimeStamp.getWed_evening().isEmpty() || testTimeStamp.getWed_evening() == null) {
//                    testTimeStamp.setWed_evening("0");
//                }
//                if (testTimeStamp.getThu_evening().equals("") || testTimeStamp.getThu_evening().isEmpty() || testTimeStamp.getThu_evening() == null) {
//                    testTimeStamp.setThu_evening("0");
//                }
//                if (testTimeStamp.getFri_evening().equals("") || testTimeStamp.getFri_evening().isEmpty() || testTimeStamp.getFri_evening() == null) {
//                    testTimeStamp.setFri_evening("0");
//                }
//
//
//                //entry for evening.
//                ArrayList<Entry> entryArrayList = new ArrayList<>(); //problem is we cant access this later?
//
//                //cant do this, since, we have two data for the same entry point.
//                //solution, make entire different data set, but keep color legend same, and keep size for
//
//                entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_evening())));
//                entryArrayList.add(new Entry(1,Float.valueOf(testTimeStamp.getTue_evening())));
//                entryArrayList.add(new Entry(2,Float.valueOf(testTimeStamp.getWed_evening())));
//                entryArrayList.add(new Entry(3,Float.valueOf(testTimeStamp.getThu_evening())));
//                entryArrayList.add(new Entry(4,Float.valueOf(testTimeStamp.getFri_evening())));
//
//                //now we created new single list of entry, add to return listsss
//
//                listof_entryList_Evening.add(entryArrayList);
//
//            }

            // listof_entryList_Morning.get(0).get(0). // this already towards particular entry

            // one way, create custom entry list, then match it when we need it, extract it when we need exact value.

            // we create custom evening / morning entry


            /// then for each entry, extract each entry, create separate dataset.

      //      LineDataSet dataSet3 = new LineDataSet(listof_entryList_Morning.get(0))

//
//            for(int hh = 0; hh< listof_entryList_Morning.size(); hh++){
//
//                //create list of dataset.
//                dataSetArrayList_Final.add(new LineDataSet(listof_entryList_Morning.get(hh), finalListRemap.get(hh).getName()));
//
//            }
//
////
//            for(int jj = 0; jj< listof_entryList_Morning.size(); jj++){
//
//                data.addDataSet(dataSetArrayList_Final.get(jj));
//            }

            //4 april 10.40AM , create list of data set, from entry, then add to data.

            //first add morning.

            for(int listDataSet=0; listDataSet<entryMorningArrayList.size(); listDataSet++){


                dataSetArrayList_Final.add(new LineDataSet(entryMorningArrayList.get(listDataSet).getEntryArrayList(),entryMorningArrayList.get(listDataSet).getName()));


                int sizeHere2 = dataSetArrayList_Final.size();
            }

            //

            for (int listDataSet2=0; listDataSet2<entryEveningArrayList.size(); listDataSet2++){

                //adding to the same list of data set
                dataSetArrayList_Final.add(new LineDataSet(entryEveningArrayList.get(listDataSet2).getEntryArrayList(),entryEveningArrayList.get(listDataSet2).getName()));




            }


            //then add to the data.

            //add all
            for(int dataCount = 0; dataCount< (entryEveningArrayList.size()*2); dataCount++){

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

            //go through colors, if detect red variant generate again., can improve by editing [ randomColor() library ]


            for(int i = 0; i<colors.length;i++){

                Log.i("checkColor now: "," flow : 2"+"  >> i:"+i);

                //while((colors[i]<-90000 && colors[i]>-180000 )|| colors[i]>-5000 || (colors[i]>-67000 && colors[i]<-40000)||(colors[i]>-1838656 && colors[i]<-1238656) ) {  // non of the while loop gets triggered

                while((colors[i]>-67000 && colors[i]<-40000)) {  // non of the while loop gets triggered

                        Log.i("checkColor now: "," flow : 3"+"  >> i:"+i);

                    colors[i] = randomColor.randomColor(); //making sure color not too bright.

                    Log.i("checkColor now: "," flow : 3"+ " random color generated: "+colors[i]+"  >> i:"+i);


                        if(i<(colors.length-1)) {

                            Log.i("checkColor now: "," flow : 5" );

                            int check = -(colors[i]) + colors[i + 1];

                            Log.i("checkColor diff: ", "check difference 1 : " + colors[i]+"  >> i:"+i);
                            Log.i("checkColor diff: ", "check difference 2 : " + colors[i+1]+"  >> i:"+i);
                            Log.i("checkColor diff: ", "check difference color : " + check+"  >> i:"+i);

                            if(colors[i]<-900035) { //for other variant

                                int check2 = -(colors[i]) + colors[i + 1];

                                while (check2<=100000 && check2>=-100000){ // numbers could be
                                  colors[i] = randomColor.randomColor();


                                    check2 = -colors[i] + colors[i + 1]; //everytime we create we check this

                                    Log.i("checkColor now: "," flow : 6" +"  >> i:"+i);
                                }

                            }


                            while (check<=10000 && check>=-10000){
                                colors[i] = randomColor.randomColor();
                                check = -colors[i] + colors[i + 1]; //everytime we create we check this

                                Log.i("checkColor now: "," flow : 6" +"  >> i:"+i);
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

             for(int j = 0; j< entryMorningArrayList.size(); j++){

                 LegendEntry legendEntry_Morning = new LegendEntry();

                 legendEntry_Morning.formColor = colors[j];
                 dataSetArrayList_Final.get(j).setColor(colors[j]);
                 dataSetArrayList_Final.get(j).notifyDataSetChanged();
                 legendEntry_Morning.label = entryMorningArrayList.get(j).getName();
                 entrieList.add(legendEntry_Morning);
             }

             int offset = dataSetArrayList_Final.size()-entryMorningArrayList.size();

            for(int j = 0; j< entryEveningArrayList.size(); j++){

                LegendEntry legendEntry_Evening = new LegendEntry();

               // legendEntry_Evening.formColor = colors[j];
                dataSetArrayList_Final.get(j+offset).setColor(colors[j]);
                dataSetArrayList_Final.get(j+offset).notifyDataSetChanged();
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
}
