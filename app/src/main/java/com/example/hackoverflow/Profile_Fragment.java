package com.example.hackoverflow;

import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Profile_Fragment extends Fragment {

    ImageView expandImage;
    CardView  cardView2 , cardView;
    FirebaseAuth mAuth;
    Context context;
    GoogleSignInClient mGoogleSignInClient;
    Button logout;

    String personName, email;
    Uri personPhoto;


    TextView userName  ,  userEmail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.profile_fragment, container, false);

        logout =(Button) v.findViewById(R.id.signout_btn);
        mAuth = FirebaseAuth.getInstance();
        context = v.getContext();
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
                getActivity().finish();
            }
        });

        profileInfo();

        userEmail = v.findViewById(R.id.mail_id);
        userName = v.findViewById(R.id.textView);

        userName.setText(personName);
        userEmail.setText(email);

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

        return v;
    }

    private void profileInfo(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        personName = account.getDisplayName();
        email = account.getEmail();
         personPhoto = account.getPhotoUrl();
    }
}
