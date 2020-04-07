/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 9*/


package com.example.week9_smartpost;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private Spinner sijaintiSpnr;
    private Spinner automaattiSpnr;
    private Spinner availabilitySpnr;
    private Spinner pickupTimeSpnr;
    private Button searchBtn;
    private RecyclerView recyclerView;
    private TimePickerDialog timeFrom;
    private TimePickerDialog timeTo;
    private EditText timeFrom_txt;
    private EditText timeTo_txt;
    private BackgroundService bgservice;
    private ArrayList<SmartPost> fiArray;
    private ArrayList<SmartPost> eeArray;
    private ArrayList<SmartPost> allArray;
    private ArrayList<SmartPost> recyclerArrayList;
    private ArrayList<SmartPost> selectedCountryaList;
    private ArrayList<PickupTime> pickupTimeArray;
    private RecyclerView.LayoutManager recyclerLayoutMgr;
    int selectedCountry;
    int selectedAutomate;
    int selectedDay;
    String pickupTime;
    int fromHour, fromMinute;
    int toHour, toMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgservice = BackgroundService.getInstance();
        sijaintiSpnr = findViewById(R.id.sijaintiSpnr);
        automaattiSpnr = findViewById(R.id.automaattiSpnr);
        availabilitySpnr = findViewById(R.id.availabilitySpnr);
        pickupTimeSpnr = findViewById(R.id.pickuptimeSpnr);
        searchBtn = findViewById(R.id.searchBtn);
        recyclerView = findViewById(R.id.recyclerView);
        timeFrom_txt = findViewById(R.id.timeFrom_txt);
        timeTo_txt = findViewById(R.id.timeTo_txt);

        //Set country and availability spinners
        setCountrySpinner();
        setAvailabilitySpinner();
        setPickupTimeSpnr();


        timeFrom_txt.setInputType(InputType.TYPE_NULL);
        timeTo_txt.setInputType(InputType.TYPE_NULL);


        //Set Fromtime when editText is pressed and change text
        timeFrom_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cldr = Calendar.getInstance();
                fromHour = cldr.get(Calendar.HOUR_OF_DAY);
                fromMinute = cldr.get(Calendar.MINUTE);
                //TimePicker
                timeFrom = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeFrom_txt.setText(hourOfDay + "." + minute);
                    }
                }, fromHour, fromMinute, true);
            timeFrom.show();
            }
        });

        //Set Totime when editText is pressed and change text
        timeTo_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                toHour = calendar.get(Calendar.HOUR_OF_DAY);
                toMinute = calendar.get(Calendar.MINUTE);
                //TimePicker
                timeTo = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeTo_txt.setText(hourOfDay + "." + minute);
                    }
                }, toHour, toMinute, true);
            timeTo.show();
            }
        });

        //Gets all arraylists when done loading XML
        if (bgservice.isDone() == true){
            fiArray = bgservice.getFiArrayList();
            eeArray = bgservice.getEeArrayList();
            allArray = bgservice.getAllArrayList();
            pickupTimeArray = bgservice.getEePickupArray();
        }else{
            System.out.println("##########EI VALMIS##########");
        }

        //Country Spinner onItemSelected Listener
        sijaintiSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = sijaintiSpnr.getSelectedItemPosition();
                //Set spinner to display desired country's automates.
                if (selectedCountry == 0){
                    selectedCountryaList = allArray;
                }
                if(selectedCountry == 1){
                    selectedCountryaList = eeArray;
                }
                if(selectedCountry == 2){
                    selectedCountryaList = fiArray;
                }
                setAutomateSpinner(selectedCountryaList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Availability Spinner onItemSelected Listener
        availabilitySpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = availabilitySpnr.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Automate Spinner onItemSelected Listener
        automaattiSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAutomate = automaattiSpnr.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //PickupTime Spinner onItemSelected Listener
        pickupTimeSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pickupTime = pickupTimeSpnr.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Search button onClickListener
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get time from and time to values
                String time1 = timeFrom_txt.getText().toString();
                String time2 = timeTo_txt.getText().toString();


                //Search list for matches (Availability, automate, country)
                 recyclerArrayList = searchList(selectedCountryaList, pickupTimeArray , selectedAutomate, selectedDay, time1, time2, pickupTime);
                //Set RecyclerView to display desired automate(s)
                setRecyclerView(recyclerArrayList);
                //Clear availability to-from selections
                timeFrom_txt.getText().clear();
                timeTo_txt.getText().clear();
            }
        });
    }

    //Making and setting an adapter for country spinner
    public void setCountrySpinner(){
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this, R.array.country_array, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sijaintiSpnr.setAdapter(countryAdapter);
    }
    //Making and setting an adapter for availability spinner
    public void setAvailabilitySpinner(){
        ArrayAdapter<CharSequence> availabilityAdapter = ArrayAdapter.createFromResource(this, R.array.availability_array, android.R.layout.simple_spinner_item);
        availabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availabilitySpnr.setAdapter(availabilityAdapter);
    }

    //Making and setting an adapter for automate spinner
    public void setAutomateSpinner(ArrayList<SmartPost> arrayList){
        ArrayAdapter<SmartPost> automateSpnrAdapter = new ArrayAdapter<SmartPost>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        automateSpnrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        automaattiSpnr.setAdapter(automateSpnrAdapter);
    }

    //Make and set adapter for pickup time spinner
    public void setPickupTimeSpnr(){
        ArrayAdapter<CharSequence> pickuptimeAdapter = ArrayAdapter.createFromResource(this, R.array.pickup_times, android.R.layout.simple_spinner_item);
        pickuptimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickupTimeSpnr.setAdapter(pickuptimeAdapter);
    }

    public void setRecyclerView(ArrayList<SmartPost> aList){
        //Linear layout manager
        recyclerLayoutMgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutMgr);

        //Specify adapter for recyclerView
        recyclerView.setAdapter(new RecyclerViewAdapter(this, aList));
    }

    public ArrayList<SmartPost> searchList(ArrayList<SmartPost> arrayList, ArrayList<PickupTime> ptimeArray, int selectedAutomate, int selectedDay, String Tfrom, String Tto, String wantedPickupT){
        ArrayList<SmartPost> aListFiltered = new ArrayList<SmartPost>();
        aListFiltered.clear();
        //First object "All"
        aListFiltered.add(0,new SmartPost("0", "All", "All", "All", "0", "All", "All"));

        //If availability day and times from-to are not set, will use time now
        if((Tfrom.equals("")) && (Tto.equals("")) && (selectedDay == 0)) {
            Calendar timeNow = new GregorianCalendar();
            timeNow.setTimeZone(TimeZone.getTimeZone("Europe/Helsinki"));
            DateFormat dfDay = new SimpleDateFormat("E");
            DateFormat dfH = new SimpleDateFormat("HH");
            DateFormat dfMin = new SimpleDateFormat("mm");
            dfDay.setCalendar(timeNow);
            dfH.setCalendar(timeNow);
            dfMin.setCalendar(timeNow);
            String currDay = dfDay.format(timeNow.getTime());
            String currHour = dfH.format(timeNow.getTime());
            String currMin = dfMin.format(timeNow.getTime());

            //Replace weekdays with numbers
            currDay = currDay.replace("Mon", "1");
            currDay = currDay.replace("Tue", "2");
            currDay = currDay.replace("Wed", "3");
            currDay = currDay.replace("Thu", "4");
            currDay = currDay.replace("Fri", "5");
            currDay = currDay.replace("Sat", "6");
            currDay = currDay.replace("Sun", "7");

            selectedDay = Integer.parseInt(currDay);
            Tfrom = (currHour + "." + currMin).toString();
            Tto = (currHour + "." + currMin).toString();
        }

        //if certain automate is selected
        if(selectedAutomate != 0){
            aListFiltered.add(arrayList.get(selectedAutomate));

        //if chosen country is estonia and pickup time and day is set
        }else if(selectedCountry == 1 && !wantedPickupT.equals("All") && selectedDay != 0){
            for(int i = 0; i<ptimeArray.size(); i++){
                if(ptimeArray.get(i).getExpress_out().equals(wantedPickupT) && ptimeArray.get(i).getDay() == selectedDay){

                    //using the id on pickuptimeArray search for matches on EE-smartpost arraylsit
                    for (int j = 0; j<arrayList.size(); j++){
                        //ID's match->Add to filtered list and break inner forloop.
                        if (ptimeArray.get(i).getId().equals(arrayList.get(j).getID())){
                            aListFiltered.add(arrayList.get(j));
                            break;
                        }
                    }
                }
            }
        }

        //Else - search according to day/time input. If both are empty, use present time.
        else{
            boolean dateMatches;
            boolean timeMatches;
            for(int i = 0; i < arrayList.size(); i++){
                if(arrayList.get(i).getAvailability().equals("24h")) {
                    aListFiltered.add(arrayList.get(i));

                }else if(arrayList.get(i).getAvailability().equals("All")){
                    continue;
                }else{
                    //If availability consists of many dates we must split it
                    String[] sDates = arrayList.get(i).getAvailability().split(";");
                    for(int j = 0; j < sDates.length; j++){
                        String partAvailability = sDates[j];
                        //Timerange is not set but day is set
                        if((Tfrom.equals("") && Tto.equals("")) && (selectedDay != 0)){
                            //function to check day
                            dateMatches = checkDay(partAvailability, selectedDay);
                            if(dateMatches == true){
                                aListFiltered.add(arrayList.get(i));
                                break;
                            }else{

                            }
                        }
                        //Timerange is set but day is not set
                        else if((!Tfrom.equals("") && !Tto.equals("")) && (selectedDay == 0)){
                            //function to check hours
                            timeMatches = checkTime(Tfrom, Tto, partAvailability);
                            if(timeMatches == true){
                                aListFiltered.add(arrayList.get(i));
                                break;
                            }
                        }
                        //Both have values.
                        else{

                            dateMatches = checkDay(partAvailability, selectedDay);
                            timeMatches = checkTime(Tfrom, Tto, partAvailability);
                            //If both are true, smartpost object will be added to filtered list.
                            if(timeMatches == true && dateMatches == true){
                                aListFiltered.add(arrayList.get(i));
                                break;
                            }else{
                                System.out.println(arrayList.get(i).getName());
                            }
                        }
                    }
                }
            }
        }
        return aListFiltered;
    }

    public boolean checkDay(String part, int day){
        boolean daymatch = false;
        int start, end, single;
        String first;
        String second;
        String singleDay;

        //Replace weekdays with numbers.
        part = part.replace("Mon", "1");
        part = part.replace("Tue", "2");
        part = part.replace("Wed", "3");
        part = part.replace("Thu", "4");
        part = part.replace("Fri", "5");
        part = part.replace("Sat", "6");
        part = part.replace("Sun", "7");

        String[] days = part.split(" ");
        String dayrange = days[0];
        if(dayrange.contains("-")){
            String[] numbers = dayrange.split("-");
            first = numbers[0];
            second = numbers[1];
            start = Integer.parseInt(first);
            end = Integer.parseInt(second);
            //If wanted day fits between the gap it is accepted day
            if((start <= day) && (day <= end)){
                daymatch = true;
            }

        }else{
            String[] number = dayrange.split(" ");
            singleDay = number[0];
            if(!singleDay.equals("")){
                single = Integer.parseInt(singleDay);
                //If single day equals wanted day then it is accepted day
                if (single == day){
                    daymatch = true;
                }
            }
        }
        return daymatch;
    }
    public boolean checkTime(String Tfrom, String Tto, String part) {
        boolean timeMatch = false;

        Date WantedStartTime = new Date();
        Date WantedEndTime = new Date();
        Date DataStartTime = new Date();
        Date DataEndTime = new Date();

        String from = Tfrom;
        String to = Tto;

        String availabilityHours = part.split(" ")[1];

        //parse to and from times from SP data
        String fromData = availabilityHours.split("-")[0];
        String toData = availabilityHours.split("-")[1];

        if(!fromData.contains(".")) {
            fromData = fromData + ".00";
        }
        if(!toData.contains(".")) {
            toData = toData + ".00";
        }

        //Datetime objects of from-to times.
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
        try {
            WantedStartTime = sdf.parse(from);
            WantedEndTime = sdf.parse(to);
            DataStartTime = sdf.parse(fromData);
            DataEndTime = sdf.parse(toData);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        assert WantedStartTime != null;
        assert WantedEndTime != null;
        //Comparison
        //StartTime is later than opening time
        if(WantedStartTime.after(DataStartTime)){
            //Ending time is earlier than closing time(Closing time before 00.00)
            if(WantedEndTime.before(DataEndTime)) {
                timeMatch = true;

            //Closing time after 00.00
            }else if(DataEndTime.before(DataStartTime)){
                if(WantedEndTime.after(DataEndTime)){
                    timeMatch = true;
                }
            }
        }
        return timeMatch;
    }
}