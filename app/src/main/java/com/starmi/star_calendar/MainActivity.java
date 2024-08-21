package com.starmi.star_calendar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("YEAR", year);
            intent.putExtra("MONTH", month + 1); // Calendar month is 0-based
            intent.putExtra("DAY", dayOfMonth);
            startActivity(intent);
        });
    }
}