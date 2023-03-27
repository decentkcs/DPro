package kr.co.decent.dpro.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nexacro.NexacroResourceManager;
import com.nexacro.NexacroUpdatorActivity;

import kr.co.decent.dpro.android.common.CommonConstants;
import kr.co.decent.dpro.android.nexacro.NexacroActivityExt;

public class MainActivity extends NexacroUpdatorActivity implements View.OnClickListener {
    public MainActivity() {
        super();

        setBootstrapURL(CommonConstants.BASE_URL + CommonConstants.MOBILE_PATH + CommonConstants.START_FILE);
        setProjectURL(CommonConstants.BASE_URL + CommonConstants.MOBILE_PATH);

        setStartupClass(NexacroActivityExt.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NexacroResourceManager.createInstance(this);
        NexacroResourceManager.getInstance().setDirect(true);
        Intent intent = getIntent();
        if(intent != null) {
            String bootstrapURL = intent.getStringExtra("bootstrapURL");
            String projectUrl = intent.getStringExtra("projectUrl");
            if(bootstrapURL != null) {
                setBootstrapURL(bootstrapURL);
                setProjectURL(projectUrl);
            }
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void onClick(View v) {
    }



}