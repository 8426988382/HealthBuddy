package com.example.healthbuddy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Context context;
    GoogleSignInClient mGoogleSignInClient;
    Button logout;
    CircleImageView img;

    TextView userName, userEmail;

    TextView txt1, txt2, txt4;

    ArrayList<SuggestionsData> data = new ArrayList<>();
    PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Profile_Activity.this, R.color.blue));


        setContentView(R.layout.profile_fragment);


        mAuth = FirebaseAuth.getInstance();
        context = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Profile_Activity.this, gso);

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("userName");
        String email = bundle.getString("userEmail");
        Bitmap bm = (Bitmap) bundle.get("profilepic");
        data = (ArrayList<SuggestionsData>) bundle.get("data");

        ArrayList<String> AudioData= new ArrayList<>();
        ArrayList<String> TextData= new ArrayList<>();
        ArrayList<String> General= new ArrayList<>();

        assert data != null;
        if(!data.isEmpty()){
            AudioData= data.get(0).getAudioBooks();
            TextData= data.get(0).getTextBooks();
            General= data.get(0).getGeneralActivities();
        }


        logout = findViewById(R.id.signout_btn);
        txt1 = findViewById(R.id.textView6);
        txt2 = findViewById(R.id.textView7);
        txt4 = findViewById(R.id.textView8);
        userEmail = findViewById(R.id.mail_id);
        userName = findViewById(R.id.textView);
        img = findViewById(R.id.circleImageView);
        pieChart= findViewById(R.id.pie_chart);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mGoogleSignInClient.signOut();
                finish();
            }
        });


        userName.setText(name);
        userEmail.setText(email);
        img.setImageBitmap(bm);


        final ArrayList<String> finalAudioData = AudioData;
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, DetailsActivity.class);
                intent.putExtra("data", finalAudioData);
                startActivity(intent);
            }
        });

        final ArrayList<String> finalTextData = TextData;
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, DetailsActivity.class);
                intent.putExtra("data", finalTextData);
                startActivity(intent);

            }
        });


        final ArrayList<String> finalGeneral = General;
        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, DetailsActivity.class);
                intent.putExtra("data", finalGeneral);
                startActivity(intent);
            }
        });

        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Integer.parseInt("30"),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        Integer.parseInt("40"),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        Integer.parseInt("5"),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Java",
                        Integer.parseInt("25"),
                        Color.parseColor("#29B6F6")));

        pieChart.startAnimation();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
