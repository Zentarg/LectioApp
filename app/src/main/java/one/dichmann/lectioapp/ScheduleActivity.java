package one.dichmann.lectioapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import downloadLectio.AsyncResponse;
import downloadLectio.GetGyms;
import downloadLectio.GetSchedule;
import one.dichmann.lectioapp.Fragments.DayFragment;
import schedule.OnSwipeTouchListener;
import schedule.Schedule;
import schedule.Weekday;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScheduleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: {
                    Bundle b = new Bundle();
                    b.putString("Day","Mandag");
                    return DayFragment.newInstance(b);
                }
                case 1: {
                    Bundle b = new Bundle();
                    b.putString("Day","Tirsdag");
                    return DayFragment.newInstance(b);
                }
                case 2: {
                    Bundle b = new Bundle();
                    b.putString("Day","Onsdag");
                    return DayFragment.newInstance(b);
                }
                case 3: {
                    Bundle b = new Bundle();
                    b.putString("Day","Torsdag");
                    return DayFragment.newInstance(b);
                }
                case 4: {
                    Bundle b = new Bundle();
                    b.putString("Day","Fredag");
                    return DayFragment.newInstance(b);
                }
                case 5: {
                    Bundle b = new Bundle();
                    b.putString("Day","Lørdag");
                    return DayFragment.newInstance(b);
                }
                case 6: {
                    Bundle b = new Bundle();
                    b.putString("Day","Søndag");
                    return DayFragment.newInstance(b);
                }
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}


/*
public class ScheduleActivity extends AppCompatActivity implements AsyncResponse {

    private GestureDetector gestureDetector;

    private Date date = new Date(); //initialices date
    private int n = 0;
    private String todayDate;
    private String todayDay;
    private String todayWeek;
    private String weekDay = null;
    private SimpleDateFormat s;//today down to every detail
    private String[] dateint; //new Date should have todays date after parsed since Date is generated by currentmillis
    private String gymID;
    private String nameID;
    private Calendar c = Calendar.getInstance();//calender is created and called upon
    private ArrayList<ArrayList<ArrayList<TextView>>> object;
    private ScrollView mainLinearLayout;

    private LinearLayout[] day, week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("i started");
        setContentView(R.layout.activity_schedule);

        Intent intent = getIntent();
        gymID = intent.getStringExtra(LoginActivity.finalGymID);
        nameID = intent.getStringExtra(LoginActivity.finalNameID);
        System.out.println(gymID+"-"+nameID);

        getIntent();

        day = new LinearLayout[] {
                (LinearLayout) findViewById(R.id.schedule_Monday),
                (LinearLayout) findViewById(R.id.schedule_Tuesday),
                (LinearLayout) findViewById(R.id.schedule_Wednesday),
                (LinearLayout) findViewById(R.id.schedule_Thursday),
                (LinearLayout) findViewById(R.id.schedule_Friday),
        };

        week = new LinearLayout[] {
                (LinearLayout) findViewById(R.id.horizontal_Monday),
                (LinearLayout) findViewById(R.id.horizontal_Tuesday),
                (LinearLayout) findViewById(R.id.horizontal_Wednesday),
                (LinearLayout) findViewById(R.id.horizontal_Thursday),
                (LinearLayout) findViewById(R.id.horizontal_Friday),
        };

        c.setTime(date);

        mainLinearLayout = (ScrollView) findViewById(R.id.schedule_Scrollview);

        Schedule asyncTaskSchedule = new Schedule();
        asyncTaskSchedule.delegate = this;
        asyncTaskSchedule.gymID = gymID;
        asyncTaskSchedule.nameID = nameID;
        asyncTaskSchedule.context = this;
        asyncTaskSchedule.VerticalLinearLayout = (LinearLayout) findViewById(R.id.activity_schedule_Vertical);
        asyncTaskSchedule.HorizontalLinearLayout = (LinearLayout) findViewById(R.id.activity_schedule_Horizontal);
        asyncTaskSchedule.dayAndDate = findViewById(R.id.schedule_DayAndDate);
        asyncTaskSchedule.execute();

        mainLinearLayout.setOnTouchListener(new OnSwipeTouchListener(ScheduleActivity.this) {
            public void onSwipeLeft() {
                removeSchedule();
                c.add(Calendar.DATE, 1);
                setSchedule();
            }
            public void onSwipeRight() {
                removeSchedule();
                c.add(Calendar.DATE, -1);
                setSchedule();
            }
        });

    }


    View.setOnTouchListener (new OnSwipeTouchListener(this)) {
    public void onSwipeLeft() {
        c.add(Calendar.DATE, 1);
        setSchedule();
    }
    public void onSwipeRight() {
        c.add(Calendar.DATE, 1);
        setSchedule();
    }
    }


    @Override
    public void processFinish(String output) {
    }

    @Override
    public void processViews(Object objects) {
        object = (ArrayList<ArrayList<ArrayList<TextView>>>) objects;
        createdays();
        setSchedule();
    }

    public void removeSchedule() {
        day[n].setVisibility(View.GONE);
    }

    public void setSchedule() {
        s = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        dateint = s.format(c.getTime()).split("");

        //date were split due to us formatting it from a american standard to a more common danish way (not the Dansih standard)
        if (dateint[6].equals("0")) { //zero´s look bad in the monthday and months number
            dateint[6] = "";
        }
        if (dateint[9].equals("0")) { //zero´s look bad in the monthday and months number
            dateint[9] = "";
        }

        todayDate = dateint[6] + dateint[7] + "/" + dateint[9] + dateint[10] + "-" + dateint[1] + dateint[2] + dateint[3] + dateint[4];

        setDay();


        TextView schedule_Day = (TextView) findViewById(R.id.schedule_DayAndDate_Day);
        schedule_Day.setText(weekDay);

        TextView schedule_Date = (TextView) findViewById(R.id.schedule_DayAndDate_Date);
        schedule_Date.setText(todayDate);

        day[n].setVisibility(View.VISIBLE);
    }

    public void createdays() {
        LinearLayout Schedule = (LinearLayout) findViewById(R.id.activity_schedule_Vertical);
        LinearLayout.LayoutParams moduleLLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        moduleLLParams.setMargins(0, 20, 0, 0);

        ArrayList textViewModule;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        for (int n = 0; n < object.size(); n++) {
            for (int i = 0; i < object.get(n).size(); i++) {
                LinearLayout moduleLL = new LinearLayout(this);
                moduleLL.setOrientation(LinearLayout.VERTICAL);
                moduleLL.setLayoutParams(moduleLLParams);
                moduleLL.setGravity(Gravity.CENTER);
                moduleLL.setBackgroundColor(getResources().getColor(R.color.schedule_Regular));

                LinearLayout moduleLL2 = new LinearLayout(this);
                moduleLL2.setOrientation(LinearLayout.VERTICAL);
                moduleLL2.setLayoutParams(moduleLLParams);
                moduleLL2.setGravity(Gravity.CENTER);
                moduleLL2.setBackgroundColor(getResources().getColor(R.color.schedule_Regular));

                textViewModule = (ArrayList) object.get(n).get(i);

                for (int k = 0; k < textViewModule.size(); k=k+2) {
                    moduleLL.addView((TextView) textViewModule.get(k));
                    moduleLL2.addView((TextView) textViewModule.get(k+1));
                    System.out.println("added");
                }

                day[n].addView(moduleLL);
                week[n].addView(moduleLL2);
            }
        }
        }

    public void setDay() {
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); //gets a integer between to be compared with the weekday

        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Mandag";
            n = 0;
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tirsdag";
            n = 1;
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Onsdag";
            n = 2;
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Torsdag";
            n = 3;
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Fredag";
            n = 4;
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Lørdag";
            n = 5;
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Søndag";
            n = 6;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.activity_schedule_Vertical).setVisibility(View.GONE);
            findViewById(R.id.activity_schedule_Horizontal).setVisibility(View.VISIBLE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            findViewById(R.id.activity_schedule_Horizontal).setVisibility(View.GONE);
            findViewById(R.id.activity_schedule_Vertical).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void processViews(Object[] objects) {
        TextView[] textView = (TextView[]) objects[0];
        TextView[] textViewModule = new TextView[3];
        ((LinearLayout) findViewById(R.id.schedule_DayAndDate)).addView(textView[0]);
        ((LinearLayout) findViewById(R.id.schedule_DayAndDate)).addView(textView[1]);
        LinearLayout.LayoutParams moduleLLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        moduleLLParams.setMargins(0, 20, 0, 0);

        for (int i=1;i<objects.length;i++) {
            LinearLayout moduleLL = new LinearLayout(this);
            moduleLL.setOrientation(LinearLayout.VERTICAL);
            moduleLL.setLayoutParams(moduleLLParams);
            moduleLL.setGravity(Gravity.CENTER);
            moduleLL.setBackgroundColor(getResources().getColor(R.color.schedule_Regular));

            textViewModule = (TextView[]) objects[i];

            for (int k=0;k<textView.length+1;k++) {
                moduleLL.addView(textViewModule[k]);
            }

            ((LinearLayout) findViewById(R.id.activity_schedule)).addView(moduleLL);

        }
    }
    }
    */
