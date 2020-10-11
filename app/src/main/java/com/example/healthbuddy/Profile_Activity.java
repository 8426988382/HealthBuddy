package com.example.healthbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Context context;
    GoogleSignInClient mGoogleSignInClient;
    Button logout;

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    TextView psyco;
    TextView psych;


    CircleImageView img;

    TextView userName  ,  userEmail;

    TextView txt1 , txt2 , txt3 , txt4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Profile_Activity.this , R.color.blue));


        setContentView(R.layout.profile_fragment);

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("userName");
        String email = bundle.getString("userEmail");
        Bitmap bm = (Bitmap) bundle.get("profilepic");

        logout = findViewById(R.id.signout_btn);
        txt1 = findViewById(R.id.textView6);
        txt2 = findViewById(R.id.textView7);
        txt3 = findViewById(R.id.meditations);
        txt4 = findViewById(R.id.textView8);


        mAuth = FirebaseAuth.getInstance();
        context = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Profile_Activity.this, gso);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mGoogleSignInClient.signOut();
                finish();
            }
        });

        userEmail = findViewById(R.id.mail_id);
        userName = findViewById(R.id.textView);

        userName.setText(name);
        userEmail.setText(email);


        img = findViewById(R.id.circleImageView);
        img.setImageBitmap(bm);


        chart = findViewById(R.id.chart1);

        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();

        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Expressions");

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);

        chart.animateY(3000);

//        int sum = Integer.parseInt(BARENTRY.get(4).toString())+ Integer.parseInt(BARENTRY.get(2).toString()) + Integer.parseInt(BARENTRY.get(1).toString());

        psyco = findViewById(R.id.pyso_id1);
        psyco.setText("Psychologist: None");
        psyco.setTextColor(getResources().getColor(R.color.green));

        psych = findViewById(R.id.psych);
        psych.setTextColor(getResources().getColor(R.color.red));
        psych.setText("Psychatrist: Dr. Ram Kumar (+919023092309)");



        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this , DetailsActivity.class);
                intent.putExtra("value" , "1");
                startActivity(intent);
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this , DetailsActivity.class);
                intent.putExtra("value" , "2");
                startActivity(intent);

            }
        });

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this , DetailsActivity.class);
                intent.putExtra("value" , "3");
                startActivity(intent);
            }
        });

        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this , DetailsActivity.class);
                intent.putExtra("value" , "4");
                startActivity(intent);
            }
        });

    }
    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(6f, 0));
        BARENTRY.add(new BarEntry(2f, 1));
        BARENTRY.add(new BarEntry(1f, 2));
        BARENTRY.add(new BarEntry(7f, 3));
        BARENTRY.add(new BarEntry(1.2f, 4));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("Happy");
        BarEntryLabels.add("Sad");
        BarEntryLabels.add("Fearful");
        BarEntryLabels.add("Excited");
        BarEntryLabels.add("Anger");

    }


}
