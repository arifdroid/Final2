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

            //evening setup

            if(testTimeStamp.getMon_evening().equals("")||testTimeStamp.getMon_evening().isEmpty()|| testTimeStamp.getMon_evening()==null){
                    testTimeStamp.setMon_evening("0");
            }
            if (testTimeStamp.getTue_evening().equals("") || testTimeStamp.getTue_evening().isEmpty() || testTimeStamp.getTue_evening() == null) {
                    testTimeStamp.setTue_evening("0");
            }

            if (testTimeStamp.getWed_evening().equals("") || testTimeStamp.getWed_evening().isEmpty() || testTimeStamp.getWed_evening() == null) {
                    testTimeStamp.setWed_evening("0");
            }
            if (testTimeStamp.getThu_evening().equals("") || testTimeStamp.getThu_evening().isEmpty() || testTimeStamp.getThu_evening() == null) {
                    testTimeStamp.setThu_evening("0");
             }
            if (testTimeStamp.getFri_evening().equals("") || testTimeStamp.getFri_evening().isEmpty() || testTimeStamp.getFri_evening() == null) {
                    testTimeStamp.setFri_evening("0");
             }


             ArrayList<Entry> entryArrayList = new ArrayList<>();

            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_morning())));
            entryArrayList.add(new Entry(1,Float.valueOf(testTimeStamp.getTue_morning())));
            entryArrayList.add(new Entry(2,Float.valueOf(testTimeStamp.getWed_morning())));
            entryArrayList.add(new Entry(3,Float.valueOf(testTimeStamp.getThu_morning())));
            entryArrayList.add(new Entry(4,Float.valueOf(testTimeStamp.getFri_morning())));

            //entry for evening.

            //cant do this, since, we have two data for the same entry point.
            //solution, make entire different data set, but keep color legend same, and keep size for

            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_evening())));
            entryArrayList.add(new Entry(1,Float.valueOf(testTimeStamp.getTue_evening())));
            entryArrayList.add(new Entry(2,Float.valueOf(testTimeStamp.getWed_evening())));
            entryArrayList.add(new Entry(3,Float.valueOf(testTimeStamp.getThu_evening())));
            entryArrayList.add(new Entry(4,Float.valueOf(testTimeStamp.getFri_evening())));

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




            dataSet.notifyDataSetChanged();

            data.notifyDataChanged();
            chart.notifyDataSetChanged();


            // >> >>>>>>>> redone 3pm     // >> >>>>>>>> redone 3pm     // >> >>>>>>>> redone 3pm     // >> >>>>>>>> redone 3pm





            //chart.notifyDataSetChanged(); // THIS ONE FUKIN LINE
             Legend legend = chart.getLegend();
//

//
             List<LegendEntry> entrieList = new ArrayList<>();
//



            RandomColor randomColor = new RandomColor();

            int[] colors = randomColor.randomColor(finalListRemap.size());

            //first need to validate, random color dont generate red Variant.


            for(int j=0;j<dataSetArrayList_Final.size();j++){

             LegendEntry entry = new LegendEntry();

                 entry.formColor = colors[j];
                 dataSetArrayList_Final.get(j).setColor(colors[j]);

                 dataSetArrayList_Final.get(j).notifyDataSetChanged();
                 entry.label = finalListRemap.get(j).getName();
                 entrieList.add(entry);
             }

             //for some data which exceeds pre-defined constraint time stamp, will be coloured red.

             legend.setCustom(entrieList);



            data.notifyDataChanged();
            chart.notifyDataSetChanged();

            chart.invalidate();

            return;


        }


    }
}
