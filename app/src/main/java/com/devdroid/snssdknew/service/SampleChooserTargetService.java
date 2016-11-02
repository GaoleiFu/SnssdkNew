package com.devdroid.snssdknew.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;

import com.devdroid.snssdknew.R;
import com.devdroid.snssdknew.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.M)
public class SampleChooserTargetService  extends ChooserTargetService {
    public SampleChooserTargetService() {
    }

    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName targetActivityName, IntentFilter matchedFilter) {
//        ComponentName componentName = new ComponentName(getPackageName(), MainActivity.class.getCanonicalName());
//        ArrayList<ChooserTarget> targets = new ArrayList<>();
//        for (int i = 0; i < 2; ++i) {
//            Bundle extras = new Bundle();
//            extras.putInt(i + "", i);
//            targets.add(new ChooserTarget(
//                    // The name of this target.
//                    "测试" + i,
//                    // The icon to represent this target.
//                    Icon.createWithResource(this, R.mipmap.ic_launcher),
//                    // The ranking score for this target (0.0-1.0); the system will omit items with
//                    // low scores when there are too many Direct Share items.
//                    0.5f,
//                    // The name of the component to be launched if this target is chosen.
//                    componentName,
//                    // The extra values here will be merged into the Intent when this target is
//                    // chosen.
//                    extras));
//        }
        return null;
    }
}
