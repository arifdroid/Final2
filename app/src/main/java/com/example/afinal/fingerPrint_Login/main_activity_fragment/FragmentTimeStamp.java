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
import android.widget.Toast;

import com.example.afinal.R;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

public class FragmentTimeStamp extends Fragment implements Observer {

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
    private ArrayList<ArrayList<Entry>> listof_entryList;

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

        dataSetArrayList_Final = new ArrayList<>();
        finalListRemap = new ArrayList<>();

        listof_entryList = new ArrayList<>();

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



        Log.i("checkChart Flow: ", "1");

        // Setting Data
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1,5));
        entries.add(new Entry(2,10));
        entries.add(new Entry(3,5));
        entries.add(new Entry(4,0));


        //dataSet2 = new LineDataSet();
        dataSet = new LineDataSet(entries, "check out V1");

        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
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

    private void addEntryAfterFinish() {

    // https://stackoverflow.com/questions/42094577/update-fragment-from-async-task



    }

    // https://stackoverflow.com/questions/39945050/update-fragment-ui-from-service-or-broadcastreceiver-if-fragment-is-visible



    private void addNewEntry() {


        entries3.add(new Entry(0,3));
        entries3.add(new Entry(1,1));
        entries3.add(new Entry(2,10));

        //entries.add(new Entry(5,5));


        dataSet = new LineDataSet(entries3,"check out now");

        dataSet.setColor(ContextCompat.getColor(getContext(),R.color.check));
        dataSetArrayList.add(dataSet);

        dataSet.notifyDataSetChanged();
        data.addDataSet(dataSet);


        data.notifyDataChanged();

        chart.invalidate();

        return;
    }

    private void getTimeStampDataNow(CollectionReference collectionReferenceTest) {

        collectionReferenceTest.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    //puling out map into hashmap

                    Log.i("checkTimeStamp ", "flow: 2");
                    //for index reference, set document number as reference number,

                    int i =0;

                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()) {

                        Log.i("checkTimeStamp ", "flow: 3");
                        //snapshot of each employee data, pull out, every ts available for each document.
                        //process may take more than 10 secs.
                        //need to check from early check, what is today's day. then only pull relevant todays data.
                        //for faster doc, cached it, load daily only. but for testing purpose,
                        // load it all at once, update using observable.

                        //https://stackoverflow.com/questions/50107921/how-to-know-that-snapshot-listener-is-finished-firestore

                        Map<String, Object> map;
                        map = documentSnapshot.getData();

                        //set reference first for our list

                        i++;

                        //problem is we will always overwrite this object.
                        TestTimeStamp object = new TestTimeStamp(i);

                        //testTimeStampsList.add(new TestTimeStamp(i));

                        Log.i("checkTimeStamp ", "flow: before 4 : i is : " +i);


                        //we need to map back into a list, so that we can show it to the user, or
                        for (Map.Entry<String, Object> kk : map.entrySet()) { //remap back each document.


                            Log.i("checkTimeStamp ", "flow: 4");

                            if (kk.getKey().equals("name")) {
                                //first access value might be ts morning, hence, when found name, this field not available anymore
                                //this is the problem, since we have to retrieve evrything now.
                                //lets try first

                                String namehere = kk.getValue().toString();

                                Log.i("checkTimeStamp ", "flow: special 99 : " + namehere);


                                if (kk.getValue().equals("ryn")) {
//
//                                        if(kk.getKey().equals("ts_mon_morning")){
//
//                                            //try to get
//
//                                            //remap into single array of object.
//                                            String monday_morning = kk.getValue().toString();
//
//                                        }

                                    String name = kk.getValue().toString();

                                    Log.i("checkTimeStamp ", "flow: 5 : " + name);
                                    object.setName(name);
                                }else {

                                    object.setName(namehere);
                                }

                            }

                                if (kk.getKey().equals("ts_mon_morning")) { //this is never set? why?

                                    //try to get

                                    //remap into single array of object.
                                    //Float mon_morning = (Float) kk.getValue();

                                    String mon_morning =  kk.getValue().toString();

                                     object.setMon_morning(mon_morning);

                                    Log.i("checkTimeStamp ", "flow: 6" + mon_morning);


                                }

                                if (kk.getKey().equals("ts_tue_morning")) {

                                    //try to get

                                    //remap into single array of object.
                                    //Float tue_morning = (Float) kk.getValue();
                                    String tue_morning =  kk.getValue().toString();

                                    object.setTue_morning(tue_morning);

                                    Log.i("checkTimeStamp ", "flow: 7" + tue_morning);

                                }

                                if (kk.getKey().equals("ts_wed_morning")) {

                                    //try to get

                                    //remap into single array of object.
                                    //Float wed_morning = (Float) kk.getValue();

                                    String wed_morning =kk.getValue().toString();

                                    object.setWed_morning(wed_morning);

                                }

                                if (kk.getKey().equals("ts_thu_morning")) {

                                    //try to get

                                    //remap into single array of object.
                                    //Float thu_morning = (Float) kk.getValue();
                                    String thu_morning = kk.getValue().toString();


                                    object.setThu_morning(thu_morning);
                                }

                                if (kk.getKey().equals("ts_fri_morning")) {

                                    //try to get

                                    //remap into single array of object.
                                    //Float fri_morning = (Float) kk.getValue();

                                    String fri_morning = kk.getValue().toString();
                                    object.setFri_morning(fri_morning);
                                }




                            //

                        }
                        //once finisih loop, add object to our list.

                        Log.i("c ", "flow: 8");

   //                     if (!object.getName().isEmpty()) {      //if (!object.getName().isEmpty()) { //which object. when it exist.

                            if (object.getName().equals("ryn")) {


                                //for test purpose, only add ryn and view to graph.
                                testTimeStampsList.add(object);

                                Log.i("checkTimeStamp ", "flow: 9 , size" + testTimeStampsList.size());
                            }


                            //log to see if document data exist, successfully extracted.

                            Log.i("checkTimeStamp ", "flow: 10");

                            if(testTimeStampsList.size()>0) {

                                String name = testTimeStampsList.get(0).getName();

                                String monday = testTimeStampsList.get(0).getMon_morning();
                                String tuesday = testTimeStampsList.get(0).getTue_morning();
                                String wednesday = testTimeStampsList.get(0).getWed_morning();
                                String thursday = testTimeStampsList.get(0).getThu_morning();
                                if(thursday==null||thursday.isEmpty()||thursday==""||thursday.equals("")){
                                    thursday="0";
                                }
                                String friday = testTimeStampsList.get(0).getFri_morning();


                                Log.i("checkTimeStamp ", "name: " + name);
                                Log.i("checkTimeStamp ", "monday: " + monday);
                                Log.i("checkTimeStamp ", "tuesday: " + tuesday);
                                Log.i("checkTimeStamp ", "wednesday: " + wednesday);
                                Log.i("checkTimeStamp ", "thursday: " + thursday);
                                Log.i("checkTimeStamp ", "friday: " + friday);

                                Toast.makeText(getContext(), "check time stamp now", Toast.LENGTH_SHORT).show();

                                //
                                //                }

                                //draw

//                                entries.add(new Entry(0, Float.valueOf(object.getMon_morning()+"F")));
//                                entries.add(new Entry(1,Float.valueOf(object.getTue_morning()+"F")));
//                                entries.add(new Entry(2,Float.valueOf(object.getWed_morning()+"F")));
//                                entries.add(new Entry(3,0F));
//                                entries.add(new Entry(4,Float.valueOf(object.getFri_morning()+"F")));

                                Log.i("checkChart Flow: ", "3");

                                entriesV2.add(new Entry(0, 7));
                                entriesV2.add(new Entry(1,8));
                                entriesV2.add(new Entry(2,9));
                                entriesV2.add(new Entry(3,0));


                                //chart.clear();

                                dataSet = new LineDataSet(entriesV2,"check out Test");
                                //dataSet = new LineDataSet(entriesV2, "check out V2");
                                dataSet.setColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.colorPrimary));

                                //

                                dataSet.notifyDataSetChanged();
                                data.addDataSet(dataSet);
                                data.notifyDataChanged();
                                chart.invalidate();


                                Log.i("checkChart Flow: ", "4");

                                 //didnt return making our list

                            return;

                            }

                    }



                }else {

                    //task not successful
                }
            }
        });

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

           // >> >>>>>>>>


            for(TestTimeStamp testTimeStamp: finalListRemap){

            //handle null and zero

                //if ts_mon_morning not set, will have problem. crash, null pointer.

            if(testTimeStamp.getMon_morning().equals("")||testTimeStamp.getMon_morning().isEmpty()|| testTimeStamp.getMon_morning()==null){
                testTimeStamp.setMon_morning("0");
            }

            if(testTimeStamp.getTue_morning().equals("")||testTimeStamp.getTue_morning().isEmpty()|| testTimeStamp.getTue_morning()==null){
                    testTimeStamp.setTue_morning("0");
            }

            if(testTimeStamp.getWed_morning().equals("")||testTimeStamp.getWed_morning().isEmpty()|| testTimeStamp.getWed_morning()==null){
                    testTimeStamp.setWed_morning("0");
            }

            if(testTimeStamp.getThu_morning().equals("")||testTimeStamp.getThu_morning().isEmpty()|| testTimeStamp.getThu_morning()==null){
                    testTimeStamp.setThu_morning("0");
            }

            if(testTimeStamp.getFri_morning().equals("")||testTimeStamp.getFri_morning().isEmpty()|| testTimeStamp.getFri_morning()==null){
                    testTimeStamp.setFri_morning("0");
            }


            ArrayList<Entry> entryArrayList = new ArrayList<>();

            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_morning())));
            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getTue_morning())));
            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getWed_morning())));
            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getThu_morning())));
            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getFri_morning())));
            //now we created new single list of entry, add to return listsss

            listof_entryList.add(entryArrayList); //problem with this, though is that we dont return the data name.

        }





            /// then for each entry, extract each entry, create separate dataset.

      //      LineDataSet dataSet3 = new LineDataSet(listof_entryList.get(0))


            for(int hh=0; hh<listof_entryList.size(); hh++){

                //create list of dataset.
                dataSetArrayList_Final.add(new LineDataSet(listof_entryList.get(hh), finalListRemap.get(hh).getName()));

            }

            for(int jj=0; jj<listof_entryList.size();jj++){

                data.addDataSet(dataSetArrayList_Final.get(jj));
            }


    //         dataSet.notifyDataSetChanged();
//            dataSet2.notifyDataSetChanged();

            dataSet.notifyDataSetChanged();

            data.notifyDataChanged();
            chart.notifyDataSetChanged();


            // >> >>>>>>>> redone 3pm     // >> >>>>>>>> redone 3pm     // >> >>>>>>>> redone 3pm     // >> >>>>>>>> redone 3pm



       //     entriesV2 = (ArrayList<Entry>) ((TimeStampFireStore_Handler) o).returnEntry();


//
//            dataSet2 = new LineDataSet(entriesV2,"Final");
//            dataSet2.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//            dataSetArrayList.add(dataSet2);
//            dataSet2.notifyDataSetChanged();
//
//
//            data.addDataSet(dataSet2);
//
//            int i = data.getDataSetCount();
//            Log.i("checkDataSet : ", ""+i);
//
//            data.notifyDataChanged();

//            chart.invalidate();



//            if(dataSet!=null) {
//             dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//             data.addDataSet(dataSet);
//
//

            //chart.notifyDataSetChanged(); // THIS ONE FUKIN LINE
             Legend legend = chart.getLegend();
//

//
             List<LegendEntry> entrieList = new ArrayList<>();
//



            RandomColor randomColor = new RandomColor();

            int[] colors = randomColor.randomColor(finalListRemap.size());



            //colorList.add(colors[])

//             //colorList.add(ColorTemplate.getHoloBlue());
//             colorList.add(R.color.check);
//            colorList.add(R.color.colorPrimary);
//
//             //problem is only register 2 label.



//
             for(int j=0;j<dataSetArrayList_Final.size();j++){

                 LegendEntry entry = new LegendEntry();

                 entry.formColor = colors[j];
                 dataSetArrayList_Final.get(j).setColor(colors[j]);
                 dataSetArrayList_Final.get(j).notifyDataSetChanged();
                 entry.label = finalListRemap.get(j).getName();
                 entrieList.add(entry);
             }
//
             legend.setCustom(entrieList);
//


            data.notifyDataChanged();
            chart.notifyDataSetChanged();
//             //chart.setExtraBottomOffset(00);
//
//  //           chart.getLegend().setEnabled(true);
//            //chart.getLegend().isLegendCustom();
////  legend.setCustom(Color.BLACK,new String[]{"test","test2","test3"});
//
//           //  chart.getLegend().setEnabled(true);
//
            chart.invalidate();

            return;
//         }

        }


        /// if o return entry



    }
}
