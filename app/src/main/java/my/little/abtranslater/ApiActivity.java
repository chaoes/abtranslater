package my.little.abtranslater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.rengwuxian.materialedittext.MaterialEditText;


public class ApiActivity extends AppCompatActivity {
    MaterialEditText idin;
    MaterialEditText keyin;
    Toolbar toolbar;
    AppCompatButton btnsave;
    AppCompatButton btnreset;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ask = findViewById(R.id.pask);
        idin= findViewById(R.id.idin);
        keyin = findViewById(R.id.keyin);
        btnsave = findViewById(R.id.save);
        btnreset = findViewById(R.id.reset);
        idin.setSingleLine(true);
        keyin.setSingleLine(true);
        sp = this.getSharedPreferences("api",MODE_PRIVATE);
        editor = sp.edit();
        String nid=sp.getString("id",null);
        String nkey=sp.getString("key",null);
        if(nid!=null&&nkey!=null){
            idin.setText(nid);
            keyin.setText(nkey);
        }
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idin.getText().toString().trim().isEmpty()||keyin.getText().toString().trim().isEmpty()){
                    Toast.makeText(ApiActivity.this,"fail",Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putString("id",idin.getText().toString());
                editor.putString("key",keyin.getText().toString());
                if(editor.commit()){
                    Toast.makeText(ApiActivity.this,"ok",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ApiActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("id");
                editor.remove("key");
                if(editor.commit()) {
                    idin.setText("");
                    keyin.setText("");
                    Toast.makeText(ApiActivity.this,"ok",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ApiActivity.this,"fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.helpurl));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }
}
