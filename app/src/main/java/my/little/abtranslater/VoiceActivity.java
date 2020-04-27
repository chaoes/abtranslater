package my.little.abtranslater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.translate.asr.OnRecognizeListener;
import com.baidu.translate.asr.TransAsrClient;
import com.baidu.translate.asr.TransAsrConfig;
import com.baidu.translate.asr.data.RecognitionResult;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

public class VoiceActivity extends AppCompatActivity {
    public String APPKEY;
    public String APPID;
    public static int CODEVOICE = 1;
    public Toolbar toolbar;
    public CoordinatorLayout coordinatorLayout;
    public AppCompatButton vbtn;
    public MaterialEditText vtext;
    public String vfrom;
    public String vto;
    public Spinner vfroms;
    public Spinner vtos;
    TransAsrConfig config;
    TransAsrClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        toolbar = findViewById(R.id.vtoolbar);
        toolbar.setTitle(R.string.voice_trans);
        vtext = findViewById(R.id.vtext);
        vfroms = findViewById(R.id.vfrom);
        vtos = findViewById(R.id.vto);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        vtext.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        vtext.setGravity(Gravity.TOP);
        vtext.setSingleLine(false);
        vtext.setHorizontallyScrolling(false);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("idkey");
        APPID = bundle.getString("id");
        APPKEY = bundle.getString("key");
//        Log.d("BB","voidce id"+APPID+"key"+APPKEY);
        coordinatorLayout = findViewById(R.id.vcoord);
//        Snackbar.make(coordinatorLayout,APPID+"  "+APPKEY,Snackbar.LENGTH_SHORT).show();
        vbtn= findViewById(R.id.vbtn);
        vbtn.setVisibility(View.INVISIBLE);
        config = new TransAsrConfig(APPID,APPKEY);
        client = new TransAsrClient(this, config);
        int hasWritereadphonestatePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_PHONE_STATE);
        int hasWritevoicerecordPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECORD_AUDIO);
        if (hasWritereadphonestatePermission == PackageManager.PERMISSION_GRANTED&&hasWritevoicerecordPermission== PackageManager.PERMISSION_GRANTED) {
            vbtn.setVisibility(View.VISIBLE);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO},CODEVOICE);
        }
        vfroms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vfrom = vfroms.getSelectedItem().toString().split(":")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vfroms.setSelection(0);
            }
        });
        vtos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vto = vtos.getSelectedItem().toString().split(":")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vtos.setSelection(1);
            }
        });
        client.setRecognizeListener(new OnRecognizeListener() {
            @Override
            public void onRecognized(int i, RecognitionResult recognitionResult) {
                if (i == OnRecognizeListener.TYPE_PARTIAL_RESULT) { // 中间结果
                    vtext.setText(recognitionResult.getAsrResult());
                    Snackbar.make(coordinatorLayout,recognitionResult.getAsrResult(),Snackbar.LENGTH_SHORT).show();

                } else if (i == OnRecognizeListener.TYPE_FINAL_RESULT) { // 最终结果
                    if (recognitionResult.getError() == 0) { // 表示正常，有识别结果
                        Log.d("BB","youjieguo");
                        vtext.setText("");
                        vtext.setText(recognitionResult.getTransResult());
//                        Snackbar.make(coordinatorLayout,recognitionResult.getTransResult(),Snackbar.LENGTH_SHORT).show();
                    } else { // 翻译出错
                        Snackbar.make(coordinatorLayout,recognitionResult.getErrorMsg(),Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        vbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float dy = 0;
                float my = 0;
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(vfrom.equals(vto)){
                        Snackbar.make(coordinatorLayout,"目标语言与源语言不能一样",Snackbar.LENGTH_SHORT).show();
                        return false;
                    }
                    dy = event.getY();
                    client.startRecognize(vfrom,vto);
                    vbtn.setText("上滑取消");

                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    client.stopRecognize();
                    vbtn.setText(R.string.holdbtn);
                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    my = event.getY();
                    Log.d("BB","dy mv"+dy+" "+my);
                    if(dy-my>100){
                        vbtn.setText("松开取消");
                        client.cancelRecognize();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODEVOICE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[grantResults.length-1] == PackageManager.PERMISSION_GRANTED){
                vbtn.setVisibility(View.VISIBLE);
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.voice_permissions_remind)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(VoiceActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO}, CODEVOICE);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }
    }
}
