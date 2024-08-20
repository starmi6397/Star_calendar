package com.starmi.star_calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.DayOfWeek;


public class DetailActivity extends AppCompatActivity {
    private TextView lunarDate, weekday, festival, solarTerms, historyToday;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lunarDate = findViewById(R.id.lunarDate);
        weekday = findViewById(R.id.weekday);
        festival = findViewById(R.id.festival);
        solarTerms = findViewById(R.id.solarTerms);
        historyToday = findViewById(R.id.historyToday);

        year = getIntent().getIntExtra("YEAR", 2024);
        month = getIntent().getIntExtra("MONTH", 1);
        day = getIntent().getIntExtra("DAY", 1);

        updateDetails();
    }

    private void updateDetails() {
        // Update with lunar date, weekday, festival, solar terms
        // Placeholder text as actual implementation depends on libraries or API
        lunarDate.setText("农历日期: Placeholder");
        weekday.setText("星期: " + getWeekday());
        festival.setText("节日: Placeholder");
        solarTerms.setText("节气: Placeholder");

        // Fetch historical data
        new Thread(() -> {
            try {
                String history = fetchHistory(day, month);
                runOnUiThread(() -> historyToday.setText("历史上的今天: " + history));
            } catch (Exception e) {
                Log.e("DetailActivity", "Failed to fetch history", e);
            }
        }).start();
    }

    private String getWeekday() {
        // 使用 LocalDate 创建日期
        LocalDate date = LocalDate.of(year, month, day);

        // 获取星期几
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 格式化为完整的星期几名称
        return dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault());
    }

    private String fetchHistory(int day, int month) throws Exception {
        String urlStr = String.format("http://www.wudada.online/Api/ScLsDay?month=%d&&day=%d", month, day);
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return parseHistoryResponse(response.toString());
        }
    }

    private String parseHistoryResponse(String response) {
        // Basic JSON parsing to extract data; implement according to API response
        // For simplicity, assume response is plain text here
        return response;
    }
}