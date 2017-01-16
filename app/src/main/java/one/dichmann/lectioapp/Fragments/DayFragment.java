package one.dichmann.lectioapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import downloadLectio.GetSchedule;
import downloadLectio.parseLesson;
import one.dichmann.lectioapp.R;

public class DayFragment extends Fragment {

    public Context context;

    public String gymID, nameID;
    public String file, lessons;
    public String timeStamp, parse, todayDate;
    public String week, year;
    public int intweek;
    public boolean downloaded, replace;

    private LinearLayout.LayoutParams moduleLLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    public Calendar c = Calendar.getInstance();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity().getApplicationContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moduleLLParams.setMargins(15,20,15,20);

        View v = inflater.inflate(R.layout.day_frag, container, false);

        TextView day = (TextView) v.findViewById(R.id.schedule_DayAndDate_Day);
        TextView date = (TextView) v.findViewById(R.id.schedule_DayAndDate_Date);

        c.setTimeInMillis(getArguments().getLong("Date"));
        gymID = getArguments().getString("gymID");
        nameID = getArguments().getString("nameID");

        SimpleDateFormat s = new SimpleDateFormat("dd/MM-yyyy");//today down to every detail
        String format = s.format(c.getTime()); //new Date should have todays date after parsed since Date is generated by currentmillis

        String[] dateint = format.split("");

        todayDate = dateint[1].replace("0", "") + dateint[2] + "/" + dateint[4].replace("0", "") + dateint[5] + "-" + format.split("-")[1];

        day.setText(Weekday());
        date.setText(todayDate);
        day.setTextColor(getResources().getColor(R.color.schedule_TextColor));
        date.setTextColor(getResources().getColor(R.color.schedule_TextColor));

        int intweek = c.get(Calendar.WEEK_OF_YEAR);
        int length = String.valueOf(intweek).length(); //gets the amount of digits on the number
        if (length == 1) { //checks if the int is only one digit
            week = "0" + intweek;//lectio needs it to be doubledigit
        } else {
            week = "" + intweek;//function returns String
        }

        if  (new permissions.fileManagement().fileExists(context, gymID + nameID + week)) { //checks if a file with the schedule already exists
            System.out.println(new permissions.fileManagement().fileExists(context, gymID + nameID + week));
            timeStamp = schedule.Weekday.Today(); // creates a new timestamp whcih should be equal to the time of execution
            file = permissions.fileManagement.getFile(context, gymID + nameID + week);//loads the file to a string from Storage with the GetFile method from fileManagement
            parse = ("(.*?)(\\d\\d)(:)(\\d\\d)(:)(\\d\\d)"); // creates a pattern for the date method
            Pattern p = Pattern.compile(parse); //compiles the pattern
            Matcher m = p.matcher(timeStamp); //matches the pattern against the entire file
            Matcher m2 = p.matcher(file); //matches the pattern against the entire file
            if (m.find() && m2.find()) { //if both of them are equal they both contain valid date information and therefore we can see how old the file is
                int hour = Integer.parseInt(m.group(2)); //sets the hour of the timestamp
                int hour2 = Integer.parseInt(m2.group(2)); //sets the hour of the file´s timestamp

                if (!(hour2 + 1 < hour)) {//compares the 2 hour numbers. the schedule can at max be 2 hours old.
                    lessons = file.replace(String.valueOf(m2.group(0)), ""); //removes the date tag from the file before the content of the file is placed as our schedule
                }
            }

            if (lessons != null) {
                System.out.println(lessons);
                String[] lesson = lessons.split("£");
                for (int i = 0; i < lesson.length; i++) {
                    String time = parseLesson.getDate(lesson[i]);
                    System.out.println(lesson[i]);
                    System.out.println("time "+time);
                    if (todayDate.equals(time)) {
                        String team = parseLesson.getTeam(lesson[i]);
                        String teacher = parseLesson.getTeacher(lesson[i]);
                        String room = parseLesson.getRoom(lesson[i]);
                        if (team != null) {
                            Pattern teamRegex = Pattern.compile("Alle");
                            Matcher teamMatcher = teamRegex.matcher(team);
                            if (room !=null) {
                                Pattern roomRegex = Pattern.compile("\\,(.*?)(\\,|$)");
                                Matcher roomMatcher = roomRegex.matcher(room);
                                if (roomMatcher.find()) {
                                    room = roomMatcher.group(1);
                                }
                            }
                            if (!teamMatcher.find()) {
                                String[] module = new String[]{team, teacher, room};
                                LinearLayout moduleLL = new LinearLayout(context);
                                moduleLL.setOrientation(LinearLayout.VERTICAL);
                                moduleLL.setLayoutParams(moduleLLParams);
                                moduleLL.setGravity(Gravity.CENTER);
                                moduleLL.setBackgroundColor(getResources().getColor(R.color.schedule_Regular));
                                for (int u = 0; u < 3; u++) {
                                    TextView moduleVertical = new TextView(context);
                                    moduleVertical.setText(module[u]);
                                    moduleVertical.setTextSize(25);
                                    moduleVertical.setPadding(10, 10, 10, 10);
                                    moduleVertical.setGravity(Gravity.CENTER);
                                    moduleVertical.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    moduleVertical.setTextColor(getResources().getColor(R.color.schedule_TextColor));
                                    moduleLL.addView(moduleVertical);
                                }
                                ((LinearLayout) v.findViewById(R.id.schedule_Modules)).addView(moduleLL);
                            }
                        }
                    }
                }
            }
        }
        return v;
    }

    public static DayFragment newInstance(Bundle b) {

        DayFragment f = new DayFragment();

        f.setArguments(b);

        return f;
    }

    public String Weekday() {
        String weekDay = null;
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); //gets a integer between to be compared with the weekday

        //checks which day it is
        if (Calendar.MONDAY == dayOfWeek) weekDay = "Mandag";
        else if (Calendar.TUESDAY == dayOfWeek) weekDay = "Tirsdag";
        else if (Calendar.WEDNESDAY == dayOfWeek) weekDay = "Onsdag";
        else if (Calendar.THURSDAY == dayOfWeek) weekDay = "Torsdag";
        else if (Calendar.FRIDAY == dayOfWeek) weekDay = "Fredag";
        else if (Calendar.SATURDAY == dayOfWeek) weekDay = "Lørdag";
        else if (Calendar.SUNDAY == dayOfWeek) weekDay = "Søndag";

        return weekDay;//returns day after it is convertet to a string
    }
}
