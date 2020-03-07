package com.example.hackoverflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Fragment extends AppCompatActivity {

    ImageView expandImage;
    CardView  cardView2 , cardView;
    FirebaseAuth mAuth;
    Context context;
    GoogleSignInClient mGoogleSignInClient;
    Button logout;


    CircleImageView img;

    TextView userName  ,  userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_fragment);

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("userName");
        String email = bundle.getString("userEmail");
        Bitmap bm = (Bitmap) bundle.get("profile_pic");

        logout = (Button) findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();
        context = this;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

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

//        InputStream imageStream = null;
//        try {
//            imageStream = this.getContentResolver().openInputStream(uri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

        img = findViewById(R.id.circleImageView);
        img.setImageBitmap(bm);

      //  listView = v.findViewById(R.id.listView_);

//        expandImage = v.findViewById(R.id.expandimage_id);
//        cardView2 = v.findViewById(R.id.cardView2);
//        cardView = v.findViewById(R.id.cardView);
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(cardView2.getVisibility() == View.VISIBLE){
//
//                    expandImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24px));
//                    cardView2.setVisibility(View.GONE);
//
//                }
//                else{
//                   expandImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_24px));
//                    cardView2.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }



}
