package com.starmi.star_calendar;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;

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
        // Update with weekday
        weekday.setText(getWeekday());

        // Fetch lunar date, festival, and solar terms
        new Thread(() -> {
            try {
                LunarData lunarInfo = fetchLunarDate(year, month, day);
                runOnUiThread(() -> {
                    lunarDate.setText(lunarInfo.lunarDate);
                    festival.setText(lunarInfo.festival != null ? lunarInfo.festival : "无节日");
                    solarTerms.setText(lunarInfo.Term);
                });
            } catch (Exception e) {
                Log.e("DetailActivity", "Failed to fetch lunar details", e);
            }
        }).start();

        // Fetch historical data
        new Thread(() -> {
            try {
                String history = fetchHistory(day, month);
                runOnUiThread(() -> historyToday.setText(history));
            } catch (Exception e) {
                Log.e("DetailActivity", "Failed to fetch history", e);
            }
        }).start();
    }

    private String getWeekday() {
        LocalDate date = LocalDate.of(year, month, day);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault());
    }

    private String fetchHistory(int day, int month) throws Exception {
        @SuppressLint("DefaultLocale")
        String urlStr = String.format("http://www.wudada.online/Api/ScLsDay?month=%d&&day=%d", month, day);
        String jsonResponse = fetchFromUrl(urlStr);

        // 使用 Gson 解析 JSON
        Gson gson = new Gson();
        HistoryResponse response = gson.fromJson(jsonResponse, HistoryResponse.class);

        if (!response.code.equals("200")) {
            throw new Exception("Failed to fetch history: " + response.msg);
        }

        StringBuilder historyBuilder = new StringBuilder();
        for (HistoryEvent event : response.data) {
            historyBuilder.append(event.date).append(": ").append(event.title).append("\n");
        }

        return historyBuilder.toString().trim(); // 返回格式化的字符串
    }

    // 定义响应类
    private class HistoryResponse {
        String code;
        String msg;
        List<HistoryEvent> data;
    }

    private class HistoryEvent {
        String id;
        String date;
        String title;
    }


    // fetchLunarDate 方法
    private LunarData fetchLunarDate(int year, int month, int day) throws Exception {
        @SuppressLint("DefaultLocale") String date = String.format("%d-%d-%d", year, month, day);
        String urlStr = String.format("https://api.mu-jie.cc/lunar?date=%s", date);
        String jsonResponse = fetchFromUrl(urlStr);

        // 使用 Gson 解析 JSON
        Gson gson = new Gson();
        LunarResponse response = gson.fromJson(jsonResponse, LunarResponse.class);

        if (response.code != 200) {
            throw new Exception("Failed to fetch lunar data: " + response.msg);
        }

        return response.data; // 返回 LunarData 对象
    }

    private String fetchFromUrl(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    // 定义响应类
    private class LunarResponse {
        int code;
        String msg;
        LunarData data;
    }

    private class LunarData {
        String date;
        String lunarDate;
        String festival;
        String lunarFestival;
        int lYear;
        int lMonth;
        int lDay;
        String Animal;
        String IMonthCn;
        String IDayCn;
        int cYear;
        int cMonth;
        int cDay;
        String gzYear;
        String gzMonth;
        String gzDay;
        boolean isToday;
        boolean isLeap;
        int nWeek;
        String ncWeek;
        boolean isTerm;
        String Term;
        String astro;
    }
}