package my.little.abtranslater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView versioncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        versioncode = findViewById(R.id.versionview);
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            versioncode.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
