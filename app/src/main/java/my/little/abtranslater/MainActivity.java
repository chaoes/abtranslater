package my.little.abtranslater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import my.little.abtranslater.bean.FromWord;
import my.little.abtranslater.bean.ToWord;
import my.little.abtranslater.utils.ClipBoardUtil;
import my.little.abtranslater.utils.baiduapi.Trans;

public class MainActivity extends AppCompatActivity {
    String fromh;
    String toh;
    String id;
    String key;
    Trans trans;
    String host;
    MaterialEditText output;
    ImageButton btn_paste;
    ImageButton btn_copy;
    ImageButton btn_trans;
    ImageButton btn_clear;
    ImageButton btn_change;
    Spinner countryfrom;
    Spinner countryto;
    MaterialEditText input;
    CoordinatorLayout coordinatorLayout;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navgation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_app_bar_open_drawer_description,R.string.nav_app_bar_open_drawer_description);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        SharedPreferences sp = this.getSharedPreferences("api",MODE_PRIVATE);
        Properties properties = new Properties();

        try {
            properties.load(getAssets().open("baidu.properties"));
        } catch (IOException e) {
            Log.d("BB", e.toString());
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("default_id",properties.getProperty("default_id"));
        editor.putString("default_key",properties.getProperty("default_key"));
        editor.commit();
        if ((sp.getString("id",null)!= null)&&(sp.getString("key",null)!=null)){
            id = sp.getString("id",null);
            key = sp.getString("key",null);
        } else {
            id = sp.getString("default_id",null);
            key = sp.getString("default_key",null);
        }
        Log.d("BB","id"+sp.getString("id",null)+"key"+sp.getString("key",null));
        if (properties.getProperty("baiduapi_host") == null) {
            finish();
        } else {
            host = properties.getProperty("baiduapi_host");
        }
        trans = new Trans(id, key, host);
        coordinatorLayout = findViewById(R.id.coord);
        btn_paste = (ImageButton) findViewById(R.id.buttonpaste);
        btn_copy = (ImageButton) findViewById(R.id.buttoncopy);
        btn_trans = (ImageButton) findViewById(R.id.buttontrans);
        btn_clear = (ImageButton) findViewById(R.id.buttonclear);
        btn_change = (ImageButton) findViewById(R.id.buttonchange);
        countryfrom = (Spinner) findViewById(R.id.spinner_from);
        countryto = (Spinner) findViewById(R.id.spinner_to);
        input = (MaterialEditText) findViewById(R.id.input);
        output = (MaterialEditText) findViewById(R.id.output);
//        output.setInputType(InputType.TYPE_NULL);
        btn_trans.setColorFilter(getResources().getColor(R.color.unfocus));
        btn_clear.setColorFilter(getResources().getColor(R.color.unfocus));
        btn_change.setColorFilter(getResources().getColor(R.color.unfocus));
        btn_paste.setColorFilter(getResources().getColor(R.color.focus));
        btn_copy.setColorFilter(getResources().getColor(R.color.unfocus));
        btn_change.setColorFilter(getResources().getColor(R.color.unfocus));
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setGravity(Gravity.TOP);
        input.setSingleLine(false);
        input.setHorizontallyScrolling(false);
        output.setGravity(Gravity.TOP);
        output.setHorizontallyScrolling(false);
        btn_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText(ClipBoardUtil.paste());
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = output.getText().toString();
                ClipBoardUtil.write(str);
                Snackbar.make(coordinatorLayout,R.string.copyok,Snackbar.LENGTH_SHORT).show();
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim()!=""){
                    btn_trans.setColorFilter(getResources().getColor(R.color.focus));
                    btn_clear.setColorFilter(getResources().getColor(R.color.focus));
                }else {
                    btn_trans.setColorFilter(getResources().getColor(R.color.unfocus));
                    btn_clear.setColorFilter(getResources().getColor(R.color.unfocus));
                }

            }
        });
        output.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim()!=""){
                    btn_copy.setColorFilter(getResources().getColor(R.color.focus));
                }else {
                    btn_copy.setColorFilter(getResources().getColor(R.color.unfocus));
                }
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event!=null&&event.getKeyCode() == KeyEvent.KEYCODE_ENTER)||actionId== EditorInfo.IME_ACTION_SEARCH){
                    btn_trans.performClick();
                }
                return false;
            }
        });
        btn_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BB", "input:###" + input.getText().toString() + "###");
                if (!input.getText().toString().isEmpty()) {
                    new Thread() {
                        FromWord fromWord;
                        ToWord toWord;

                        @Override
                        public void run() {
                            super.run();
                            synchronized (this) {
                                fromWord = new FromWord(input.getText().toString(), fromh, toh);
                                try {
                                    toWord = trans.translate(fromWord);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String textfinal = "";
                                ArrayList<HashMap<String,String>> array = toWord.getTrans_result();
                                if (toWord != null) {
                                    Log.d("BB", toWord.getError_code()+toWord.getError_msg());
                                    if (toWord.getTrans_result() != null) {
//                                        if (toWord == null || toWord.getTrans_result() == null)
                                            Log.d("BB", "???");
                                        for (HashMap<String, String> hashMap : array) {
                                            textfinal = textfinal + hashMap.get("dst") + "\n";
                                        }
                                        Message msg = Message.obtain();
                                        msg.what = 1;
                                        msg.obj = textfinal;
                                        handler.sendMessage(msg);
                                    }else {
                                        if(toWord.getError_code()!=null){
                                            Message msg2 = Message.obtain();
                                            msg2.what=0;
                                            msg2.obj=toWord.getError_msg();
                                            Log.d("BB","code:"+toWord.getError_code()+"msg;"+toWord.getError_msg());
                                            handler.sendMessage(msg2);
                                        }
                                    }
                                    fromWord = null;
                                    toWord = null;
                                }
                            }
                        }
                    }.start();
                }
            }
        });
        countryfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromh = countryfrom.getSelectedItem().toString().split(":")[1];
                if(position>0){
                    btn_change.setColorFilter(getResources().getColor(R.color.focus));
                } else {
                    btn_change.setColorFilter(getResources().getColor(R.color.unfocus));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                countryfrom.setSelection(0);
            }
        });
        countryto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toh = countryto.getSelectedItem().toString().split(":")[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                countryto.setSelection(0);
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long intf = countryfrom.getSelectedItemId();
                long intt = countryto.getSelectedItemId();
                if (intf == 0) {
                    return;
                } else {
                    countryfrom.setSelection((int) (intt + 1));
                    countryto.setSelection((int) intf - 1);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.setapi:
                        Intent intent = new Intent(MainActivity.this,ApiActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:
                        Intent intent1 = new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = this.getSharedPreferences("api",MODE_PRIVATE);
        if ((sp.getString("id",null)!= null)&&(sp.getString("key",null)!=null)){
            id = sp.getString("id",null);
            key = sp.getString("key",null);
        } else {
            id = sp.getString("default_id",null);
            key = sp.getString("default_key",null);
        }
        trans = new Trans(id, key, host);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                String finaltext = (String) msg.obj;
                output.setText(finaltext);
            }else if(msg.what==0){
                Snackbar.make(coordinatorLayout,(String)msg.obj,Snackbar.LENGTH_SHORT).show();
            }
        }
    };



}
