package com.example.fourthAndroidApp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstandroidapp.R;

public class ShowDetails extends AppCompatActivity {
    private TextView fname,lname,email,country,gender;
    private Button btn;

    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_details);

        fname=findViewById(R.id.first_text);
        lname=findViewById(R.id.last_text);
        email=findViewById(R.id.email_text);
        country=findViewById(R.id.country_text);
//        gender=findViewById(R.id.gender_text);
        btn=findViewById(R.id.logout_button);


        pref=getSharedPreferences("login_details",MODE_PRIVATE);
        String fn=pref.getString("fname",null);
        String ln=pref.getString("lname",null);
        String em=pref.getString("email",null);
        String co=pref.getString("country",null);
//        String Male=pref.getString("genderMale",null);
//        String Female=pref.getString("genderMale",null);
//        String Others=pref.getString("genderMale",null);
//
        if(fn!=null && ln!=null){
            fname.setText("Full Name   :   "+ fn);
            lname.setText("Last-Name   :   "+ln);
            email.setText("Email   :   "+em);
            country.setText("Country   :   "+co);
//            gender.setText("Gender   :   "+gen);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=pref.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(ShowDetails.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });








        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}