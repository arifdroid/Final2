package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.oop.TestTimeStamp;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import static java.security.AccessController.getContext;

public class TimeStampFireStore_Handler  extends Observable {

    private CollectionReference collectionReferenceTest;
    private List<Entry> entriesV2;

    // try return list of entry
    private List<Entry> entriesV3;

    private LineDataSet dataSet;

    private LineData data;

    private LineChart chart;

    private Context mContext;

    private ArrayList<TestTimeStamp> testTimeStampsList;

    //document size

    int sizeDoc;

    //final return

    private ArrayList<ArrayList<Entry>> listof_entryList;


    public TimeStampFireStore_Handler(Context context,LineDataSet dataSet, LineData data, LineChart chart) {
        this.mContext = context;
        this.chart = chart;
        this.data = data;
        this.dataSet=dataSet;

        entriesV2 = new ArrayList<>();
        entriesV3 = new ArrayList<>();

        testTimeStampsList = new ArrayList<>();

        collectionReferenceTest = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document("ariff+60190_doc")
                .collection("all_employee_thisAdmin_collection");


        Log.i("checkChartFlowFinal ", "handler, 1");



    }


    public void startFecthData(){


        collectionReferenceTest.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    int i = 0;
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                    sizeDoc = task.getResult().size();

                        Map<String, Object> map;
                        map = documentSnapshot.getData();

                        i++;
                        TestTimeStamp object = new TestTimeStamp(i); //setting object, with index i, as reference.
                        //what we want to do, we set each object, as references that we want to extract.

                        // then setup return array list of array list of entries,



                        for (Map.Entry<String, Object> kk : map.entrySet()) {

                            Log.i("checkChartFlowFinal ", "handler, 3");

                            if (kk.getKey().equals("name")) {
                                //first access value might be ts morning, hence, when found name, this field not available anymore
                                //this is the problem, since we have to retrieve evrything now.
                                //lets try first

                                String namehere = kk.getValue().toString();

                                Log.i("checkTimeStamp ", "flow: special 99 : " + namehere);


                                    object.setName(namehere);
                            } // end name hash

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


                            testTimeStampsList.add(object);

                        } //end hash-map loop

                            //for test purpose, only add ryn and view to graph.





                        //log to see if document data exist, successfully extracted.

                        Log.i("checkTimeStamp ", "flow: 10");

//                        if(testTimeStampsList.size()>0) {
//
////                            String name = testTimeStampsList.get(0).getName();
////
////                            String monday = testTimeStampsList.get(0).getMon_morning();
////                            String tuesday = testTimeStampsList.get(0).getTue_morning();
////                            String wednesday = testTimeStampsList.get(0).getWed_morning();
////                            String thursday = testTimeStampsList.get(0).getThu_morning();
////                            if(thursday==null||thursday.isEmpty()||thursday==""||thursday.equals("")){
////                                thursday="0";
////                            }
////                            String friday = testTimeStampsList.get(0).getFri_morning();
////
////
////                            Log.i("checkTimeStamp ", "name: " + name);
////                            Log.i("checkTimeStamp ", "monday: " + monday);
////                            Log.i("checkTimeStamp ", "tuesday: " + tuesday);
////                            Log.i("checkTimeStamp ", "wednesday: " + wednesday);
////                            Log.i("checkTimeStamp ", "thursday: " + thursday);
////                            Log.i("checkTimeStamp ", "friday: " + friday);
////
////                            Toast.makeText(mContext, "check time stamp now", Toast.LENGTH_SHORT).show();
////
////                            Log.i("checkChartFlowFinal ", "handler, 4");
//
//
//
//
//
//
//                        }

//


                    } //document loop
//
//                    if(sizeDoc==i){ //meaning, finish loop all document, then we can update returned result, return true with result.
//
//                    setReturnData();
//
//                    }

                    if(sizeDoc==i){ //meaning, finish loop all document, then we can update returned result, return true with result.

                        Log.i("checkChartFlowFinal ", "handler, 5");

                        // setReturnData();

                       // setReturnEntry();

                        setReturnListOfEntry(testTimeStampsList);


                    }

                    Log.i("checkChartFlowFinal ", "handler, 6");

                } //end task loop
                else {

                }//task not successful

            }


        });

        return;

     // return false;

} // end method

    private void setReturnListOfEntry(ArrayList<TestTimeStamp> testTimeStampsList) {

        //then we want to populate entry.
        int k=0;
//        for(TestTimeStamp testTimeStamp: testTimeStampsList){
//            k++;
//
//            ArrayList<Entry> entryArrayList = new ArrayList<>();
//
//            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getMon_morning())));
//            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getTue_morning())));
//            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getWed_morning())));
//            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getThu_morning())));
//            entryArrayList.add(new Entry(0,Float.valueOf(testTimeStamp.getFri_morning())));
//            //now we created new single list of entry, add to return listsss
//
//            listof_entryList.add(entryArrayList); //problem with this, though is that we dont return the data name.
//
//
//
//        }

        //            //create entry from each timestamplist, then create single point list of entry after finish.
//
//            Float.valueOf(testTimeStamp.getMon_morning());

        //listof_entryList.add(new ArrayList<Entry>((Collection<? extends Entry>) new Entry(0,Float.valueOf(testTimeStamp.getMon_morning()))));

        if(testTimeStampsList.size()==sizeDoc){

            setChanged();
            notifyObservers();

        }


    }

    public ArrayList<TestTimeStamp> reTURNFINAL(){

        return testTimeStampsList;
    }

    private void setReturnData() {

        Log.i("checkChartFlowFinal ", "handler, 7");

        entriesV2.add(new Entry(0, 7));
        entriesV2.add(new Entry(1,8));
        entriesV2.add(new Entry(2,9));
        entriesV2.add(new Entry(3,0));


        //chart.clear();

       this.dataSet = new LineDataSet(entriesV2,"check out Test"); //we never return this?
        //dataSet = new LineDataSet(entriesV2, "check out V2");
//        dataSet.setColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
//
//        //
//
//        dataSet.notifyDataSetChanged();
//        data.addDataSet(dataSet);
//        data.notifyDataChanged();
//        chart.invalidate();


       setChanged();
       notifyObservers();
    }

    public LineDataSet returnDataSet(){

       // this.dataSet = dataSet;

        Log.i("checkChartFlowFinal ", "handler, 1");

        if(dataSet!=null) {
            return this.dataSet;
        }
        return null;
    }

    public void setReturnEntry(){



        entriesV3.add(new Entry(0, 7));
        entriesV3.add(new Entry(1,2));
        entriesV3.add(new Entry(2,9));
        entriesV3.add(new Entry(3,3));

       setChanged();
       notifyObservers();
    }

    public List<Entry> returnEntry(){


        if(entriesV3!=null){
            return entriesV3;
        }

        return null;
    }




} //end class
