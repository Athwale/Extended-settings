package com.example.android.extrasettings;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements
        Comparator<Model>,
        AdapterView.OnItemClickListener {

    private final Adapter adapter = new Adapter();

    private boolean getDeviceEncryptionStatus(Context context) {
        final DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService
                (Context.DEVICE_POLICY_SERVICE);
        int state = dpm.getStorageEncryptionStatus();
        if (state == DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE) {
            return false;
        } else return state == DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE ||
                state == DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE_DEFAULT_KEY;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
        try {
            Intent intent = new Intent();
            String target = adapter.getItem(index).label;

            if (target.equalsIgnoreCase("Settings")) {
                intent.setClassName("com.android.settings", "com.android.settings.WetaoSettings");
            } else if (target.equalsIgnoreCase("Tether")) {
                intent.setClassName("com.android.settings", "com.android.settings.TetherSettings");
            } else if (target.equalsIgnoreCase("Ethernet")) {
                intent.setClassName("com.android.settings", "com.android.settings.EthernetSettings");
            } else if (target.equalsIgnoreCase("Display")) {
                intent.setClassName("com.android.settings", "com.android.settings.DisplaySettings");
            } else if (target.equalsIgnoreCase("Security")) {
                intent.setClassName("com.android.settings", "com.android.settings.SecuritySettings");
            } else if (target.equalsIgnoreCase("Device admin")) {
                intent.setClassName("com.android.settings", "com.android.settings.DeviceAdminSettings");
            } else if (target.equalsIgnoreCase("Accessibility")) {
                intent.setClassName("com.android.settings", "com.android.settings.accessibility.AccessibilitySettingsForSetupWizardActivity");
            } else if (target.equalsIgnoreCase("USB")) {
                intent.setClassName("com.android.settings", "com.android.settings.deviceinfo.UsbModeChooserActivity");
            } else if (target.equalsIgnoreCase("Usage stats")) {
                intent.setClassName("com.android.settings", "com.android.settings.UsageStatsActivity");
            } else if (target.equalsIgnoreCase("Power usage")) {
                intent.setClassName("com.android.settings", "com.android.settings.fuelgauge.PowerUsageSummary");
            } else if (target.equalsIgnoreCase("Sound")) {
                intent.setClassName("com.android.settings", "com.android.settings.SoundSettings");
            } else if (target.equalsIgnoreCase("Demo mode")) {
                intent.setClassName("com.android.systemui", "com.android.systemui.DemoMode");
            } else if (target.equalsIgnoreCase("Easter egg")) {
                intent.setClassName("com.android.systemui", "com.android.systemui.DessertCase");
            } else if (target.equalsIgnoreCase("Encryption")) {
                intent.setClassName("com.android.settings", "com.android.settings.Settings$CryptKeeperSettingsActivity");
            } else if (target.equalsIgnoreCase("Device is encrypted")) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void populate() {
        ArrayList<Model> models = new ArrayList<>();
        long id = 0;

        if (getDeviceEncryptionStatus(this)) {
            models.add(new Model(++id, "Device is encrypted"));
        } else {
            models.add(new Model(++id, "Encryption"));
        }

        models.add(new Model(++id, "Settings"));
        models.add(new Model(++id, "Device admin"));
        models.add(new Model(++id, "Accessibility"));
        models.add(new Model(++id, "USB"));
        models.add(new Model(++id, "Usage stats"));
        models.add(new Model(++id, "Power usage"));
        models.add(new Model(++id, "Demo mode"));
        models.add(new Model(++id, "Easter egg"));
        models.sort(this);
        adapter.update(models);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        populate();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public int compare(Model model, Model m1) {
        return model.label.compareToIgnoreCase(m1.label);
    }
}