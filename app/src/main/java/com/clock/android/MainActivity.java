package com.clock.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private TextView status;
    private final int bg = Color.rgb(13, 17, 23);
    private final int green = Color.rgb(0, 230, 118);
    private final int muted = Color.rgb(150, 160, 174);

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(28, 40, 28, 28);
        root.setBackgroundColor(bg);

        TextView title = text("CLOCK", 22, green);
        title.setLetterSpacing(0.15f);
        root.addView(title, new LinearLayout.LayoutParams(-1, 58));

        TextClock clock = new TextClock(this);
        clock.setFormat12Hour("h:mm");
        clock.setFormat24Hour("HH:mm");
        clock.setTextSize(72);
        clock.setTextColor(Color.WHITE);
        clock.setGravity(Gravity.CENTER);
        root.addView(clock, new LinearLayout.LayoutParams(-1, 130));

        TextClock date = new TextClock(this);
        date.setFormat12Hour("EEEE, MMMM d");
        date.setFormat24Hour("EEEE, MMMM d");
        date.setTextSize(17);
        date.setTextColor(muted);
        date.setGravity(Gravity.CENTER);
        root.addView(date, new LinearLayout.LayoutParams(-1, 45));

        TextView description = text("ROOT TOOL • SYSTEM LOGCAT", 13, muted);
        description.setPadding(0, 34, 0, 16);
        root.addView(description, new LinearLayout.LayoutParams(-1, 55));

        TextView clear = text("CLEAR LOGCAT BUFFERS", 16, Color.rgb(0, 25, 12));
        clear.setTypeface(null, android.graphics.Typeface.BOLD);
        clear.setBackgroundColor(green);
        clear.setPadding(12, 16, 12, 16);
        clear.setOnClickListener(v -> confirmClear());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(-1, 62);
        buttonParams.setMargins(0, 12, 0, 12);
        root.addView(clear, buttonParams);

        status = text("Ready. Root access is required.", 14, muted);
        status.setPadding(0, 12, 0, 12);
        root.addView(status, new LinearLayout.LayoutParams(-1, 60));

        TextView note = text("This clears Android logcat buffers only. It does not remove app audit databases or uninstall history.", 12, muted);
        note.setPadding(8, 24, 8, 0);
        root.addView(note, new LinearLayout.LayoutParams(-1, 90));

        setContentView(root);
    }

    private void confirmClear() {
        new AlertDialog.Builder(this)
            .setTitle("Clear system logcat?")
            .setMessage("This will clear current Android logcat buffers using root. It cannot clear records stored inside monitoring apps.")
            .setNegativeButton("Cancel", (d, w) -> d.dismiss())
            .setPositiveButton("Clear", (d, w) -> clearLogs())
            .show();
    }

    private void clearLogs() {
        status.setText("Requesting root and clearing buffers…");
        new Thread(() -> {
            String output = "";
            int code = -1;
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "logcat -c"});
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line).append("\n");
                code = p.waitFor();
                output = sb.toString();
            } catch (Exception e) { output = e.toString(); }
            final int result = code;
            final String detail = output;
            runOnUiThread(() -> {
                if (result == 0) {
                    status.setText("Logcat buffers cleared successfully.");
                    Toast.makeText(this, "Logcat cleared", Toast.LENGTH_SHORT).show();
                } else {
                    status.setText("Failed (root denied or command error). " + detail);
                    Toast.makeText(this, "Clear failed — check root prompt", Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }
}
