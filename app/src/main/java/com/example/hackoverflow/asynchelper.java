package com.example.hackoverflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class asynchelper extends AsyncTask<Void , Void , Bitmap> {

    String userName , userEmail;
    Uri uri;
    Bitmap bm;
    Context sContext;
    ProgressDialog dialog;

    public asynchelper(Context context , String userName, String userEmail, Uri uri) {
        sContext = context;
        this.userName = userName;
        this.userEmail = userEmail;
        this.uri = uri;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(sContext);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(String.valueOf(uri)).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }



        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap aVoid) {
        super.onPostExecute(aVoid);

        if(dialog.isShowing()){
            dialog.dismiss();
        }

        Intent intent = new Intent(sContext , Profile_Activity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail" , userEmail);
        intent.putExtra("profilepic" , aVoid);
        sContext.startActivity(intent);
    }
}

