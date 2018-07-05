package com.example.matthewspc.financeapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.support.v4.app.NotificationCompat.Builder;
import static java.lang.Math.toIntExact;

public class MainActivity extends AppCompatActivity
{
    private String spendGoal;
    private String spendDate;
    private String spendLeft;
    private TextView goalDate;
    private TextView budgetLeftResult;
    private ProfileDatabase profile;


    private float spendGoalNum=1;
    private float spendLeftNum=1;


    private float groceries=0;
    private float diningOut=0;
    private float rent=0;
    private float utilities=0;
    private float travel=0;
    private float clothes=0;
    private float other=0;

    private float spent = 1;//spendGoalNum - spendLeftNum;
    ExpensesHelper Finances = new ExpensesHelper(this);

    private float[] yData = {groceries*100/spent, diningOut*100/spent, rent*100/spent, utilities*100/spent, travel*100/spent, clothes*100/spent, other*100/spent};
    private String[] xData = {"Groceries", "Dining Out","Rent","Utilities","Travel","Clothes","Other"};
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalDate=(TextView)this.findViewById(R.id.goalDate);
        budgetLeftResult=(TextView)this.findViewById(R.id.budgetLeftResult);
        Log.d("MainActivity", "onCreate: starting to create chart");
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        profile = new ProfileDatabase(this);

        try {
            convertDatabase();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Description text = new Description();
        if(profile.checkDatabase()) {
            Cursor cursor = profile.getLog();
            String work = cursor.getString(cursor.getColumnIndex(profile.GOAL));
            text.setText("Total budget: $" + work);
        }
        else
        {
            text.setText("Total budget: $0");
        }

        pieChart.setDescription(text);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(20);

        Cursor financeCursor = Finances.getAllExpenses();
        if (financeCursor == null) {//and check if null, etc

        } else if(financeCursor.moveToFirst()) {
            do {
                int k = financeCursor.getInt(financeCursor.getColumnIndex("_id"));
                ExpenseLogEntryData row = Finances.getExpense(k);
                String priceString = row.getCost();
                float price = Float.valueOf(priceString);
                String tag = row.getTag();
                System.out.println(tag);
                System.out.println(price);

                if (tag.equals("Groceries")) {
                    groceries += price;
                    System.out.println("groceries" + groceries);
                } else if (tag.equals("Dining Out")) {
                    diningOut += price;
                } else if (tag.equals("Rent")) {
                    rent += price;
                } else if (tag.equals("Utilities")) {
                    utilities += price;
                } else if (tag.equals("Travel")) {
                    travel += price;
                } else if (tag.equals("Clothes")) {
                    clothes += price;
                } else {
                    other += price;
                }
                spendLeftNum -= price;
            } while (financeCursor.moveToNext());
        }
        yData = new float[]{groceries*100/spent, diningOut*100/spent, rent*100/spent, utilities*100/spent, travel*100/spent, clothes*100/spent, other*100/spent};
        addDataSet();
    }

    public void notifyMe(View view){

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("fuq", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        Cursor cursor = profile.getLog();
        if (profile.checkDatabase()) {
            spendGoal = cursor.getString(cursor.getColumnIndex(profile.GOAL));
            spendLeft = cursor.getString(cursor.getColumnIndex(profile.SPENT));
            spendDate = cursor.getString(cursor.getColumnIndex(profile.DATE));
            double goal = Double.parseDouble(spendGoal);
            double spent = Double.parseDouble(spendLeft);
            String spentLeft = Double.toString(goal-spent);

            goalDate.setText(spendDate);
            budgetLeftResult.setText("$" + spentLeft);
        }

        Builder builder = new Builder(this, "fuq")
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Your total budget is: "+spendGoal)
                .setContentText("You've spent "+spendLeft+" of it!");

        mNotificationManager.notify(1, builder.build());

    }

    public void updateProfile(View view) //defines listener for the UpdateProfile Activity
    {
        startActivityForResult(new Intent(MainActivity.this, UpdateProfile.class), 1);
    }

    public void groupsButton(View view)//defines listener for the Groups Activity
    {
        startActivity(new Intent(MainActivity.this, RegisterPage.class));
        //the listener is defined in the XML file
    }

    public void expensesButton(View view) //defines listener for the ExpensesTable Activity
    {
        startActivity(new Intent(MainActivity.this, ExpensesTable.class));
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //when we come back from the UpdateProfile activity
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                spendGoal = data.getStringExtra("spendGoal");//get extras
                spendDate = data.getStringExtra("spendDate");

                spendGoalNum = Float.parseFloat(spendGoal);
                spendLeftNum = Float.parseFloat(spendGoal);
                profile.updateProfile(spendGoal,spendDate);//adds new log with values
                try {
                    Finances.removeAll();
                    convertDatabase();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Description text = new Description();
                if(profile.checkDatabase()) {
                    Cursor cursor = profile.getLog();
                    String work = cursor.getString(cursor.getColumnIndex(profile.GOAL));
                    text.setText("Total budget: $" + work);
                }
                else
                {
                    text.setText("Total budget: $0");
                }

                pieChart.setDescription(text);
                pieChart.setRotationEnabled(true);
                pieChart.setHoleRadius(25f);
                pieChart.setTransparentCircleAlpha(0);
                pieChart.setDrawEntryLabels(true);
                pieChart.setEntryLabelTextSize(20);
                yData = new float[]{0, 0,0, 0, 0, 0, 0};
            }
        }
        addDataSet();
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Expense Percentages");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(17);
        System.out.println(yEntrys.toString());

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(253,205,224));
        colors.add(Color.rgb(187,226,252));

        colors.add(Color.rgb(250,154,154));
        colors.add(Color.rgb(146,156,243));

        colors.add(Color.rgb(246,206,206));
        colors.add(Color.rgb(102,115,230));

        colors.add(Color.rgb(233,106,106));

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private Date convertDate(String date) throws ParseException //converts string to date object
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(date);
    }

    private Date getDate() throws ParseException//gets the current date
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return date;
    }

    private int diffTime(Date today,Date goal)//finds the number of days between today and goal date
    {
        long diff = goal.getTime() - today.getTime();
        return toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    private void convertDatabase() throws ParseException
    {//converts sqlite cursor to the textview
        Cursor cursor = profile.getLog();
        if (profile.checkDatabase()) {
            spendGoal = cursor.getString(cursor.getColumnIndex(profile.GOAL));
            spendLeft = cursor.getString(cursor.getColumnIndex(profile.SPENT));
            spendDate = cursor.getString(cursor.getColumnIndex(profile.DATE));
            double goal = Double.parseDouble(spendGoal);
            double spentI = Double.parseDouble(spendLeft);
            spent = Float.parseFloat(spendLeft);
            String spentLeft = Double.toString(goal-spentI);

            goalDate.setText(spendDate);
            budgetLeftResult.setText("$" + spentLeft);
        }
    }
}
