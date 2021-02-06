 package com.example.healthbuddy.UI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.airbnb.lottie.LottieAnimationView;
import com.example.healthbuddy.Api.ApiGetQuestions;
import com.example.healthbuddy.Api.ApipostCheckAns;
import com.example.healthbuddy.Api.CheckAnsResponse;
import com.example.healthbuddy.Api.QuestionsResponse;
import com.example.healthbuddy.Model.QuestionData;
import com.example.healthbuddy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Question_Fragment extends Fragment implements View.OnClickListener, QuestionsResponse , CheckAnsResponse {



    TextureView textureView;
    TextView previewText;
    ImageButton imageButton;
    private CameraX.LensFacing lensFacing = CameraX.LensFacing.BACK;
    ImageCapture imageCapture;
    private FaceServiceClient faceServiceClient = new FaceServiceRestClient("https://faceapi0.cognitiveservices.azure.com/face/v1.0/", "69f242e5b8fc415280709d29d97cb3b8");
    JSONObject jsonObject,jsonObject1;
    ImageView imageView;
    Bitmap mBitmap;
    boolean takePicture = false;
    private ProgressDialog detectionProgressDialog;
    Face[] facesDetected;

    Preview preview;


    LottieAnimationView lottieAnimationView;
    LottieAnimationView allCaughtUp;
    public static final String SHARED_PREF = "shared_prefs";
    SharedPreferences Prefs;
    private ScrollView scrollView;
    TextView QuestionText, QuoteText;
    Button  Option2 ;
    EditText Option1;
    static int cnt = 0;
    static int score = 0;
    GoogleSignInAccount account;
    ArrayList<QuestionData> Questions = new ArrayList<>();
    ApiGetQuestions apiGetQuestions;
    Question_Fragment thiscontext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
          account = GoogleSignIn.getLastSignedInAccount(getActivity());
        thiscontext = this;
        apiGetQuestions = new ApiGetQuestions(getContext(), account.getId()) ;
        apiGetQuestions.questionsResponse = (QuestionsResponse) this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.questions_fragment, container, false);

        // initialise
        lottieAnimationView = v.findViewById(R.id.startid);
        allCaughtUp = v.findViewById(R.id.allcaughtup);
        Prefs = this.getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        scrollView = v.findViewById(R.id.scrollView2);
        QuestionText = v.findViewById(R.id.textView);
        Option1 = v.findViewById(R.id.option1_id);
        Option2 = v.findViewById(R.id.option2_id);
        previewText = v.findViewById(R.id.textView5);


        QuoteText = v.findViewById(R.id.quote_text_id);
        textureView = v.findViewById(R.id.textureView);
        detectionProgressDialog = new ProgressDialog(getContext());
        jsonObject = new JSONObject();
        jsonObject1 = new JSONObject();






        CameraX.unbindAll();
        Rational aspectratio = new Rational(360,480);
        Size screensize = new Size(360,480);
        PreviewConfig previewConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectratio).
                setTargetResolution(screensize).setLensFacing(CameraX.LensFacing.FRONT).build();
          preview = new Preview(previewConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent  = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView);
                textureView.setSurfaceTexture(output.getSurfaceTexture());
                updatetransform();
            }
        });


        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).setTargetResolution(screensize).
                setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation()).setLensFacing(CameraX.LensFacing.FRONT).build();
        imageCapture = new ImageCapture(imageCaptureConfig);

        imageButton =  v.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takephoto();

            }
        });





        final String Uid;

        if (account != null) {
            Uid = account.getId();

            lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {
                    CameraX.bindToLifecycle(getActivity(),preview,imageCapture);

                    takephoto();
                    long firstTime = Prefs.getLong("FirstTime", Long.parseLong("0"));
                    long loginTime = Prefs.getLong("time", Long.parseLong("0"));
                    long diff = loginTime - firstTime;
                    long diffs = diff / (60 * 60 * 1000);
                    Log.e("LAST TIME DIFFERENCE", String.valueOf(diffs));
                    Log.e("First Time", String.valueOf(firstTime));
                    Log.e("Login Time", String.valueOf(loginTime));

                    lottieAnimationView.playAnimation();

                  //  int hrs = 86400000;

                    if (diffs >= 0) {

                        apiGetQuestions.execute();

                        lottieAnimationView.setVisibility(View.GONE);
                    } else {
                        lottieAnimationView.setVisibility(View.GONE);
                        allCaughtUp.setVisibility(View.VISIBLE);
                        allCaughtUp.playAnimation();
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        Log.e("check","Done ");
                        CameraX.unbindAll();
                        textureView.setVisibility(View.INVISIBLE);
                    }
                }
            });


        } else {
            Toast.makeText(getContext(), "some error has occurred", Toast.LENGTH_SHORT).show();
        }

        return v;
    }
void takephoto(){
    try{
        imageCapture.takePicture(new ImageCapture.OnImageCapturedListener() {
            @Override
            public void onCaptureSuccess(ImageProxy image, int rotationDegrees) {
//                        imageButton.setImageBitmap(imageProxyToBitmap(image) );
//                        imageButton.setRotation(rotationDegrees);
                detectAndFrame(imageProxyToBitmap(image, rotationDegrees));
//                Toast.makeText(getContext(), rotationDegrees + "s", Toast.LENGTH_SHORT).show();
                super.onCaptureSuccess(image, rotationDegrees);

            }

            @Override
            public void onError(ImageCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause) {
                super.onError(useCaseError, message, cause);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }catch (Exception e)
    {
        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}

    void PerformAction(ArrayList<QuestionData> questionData) {
        this.Questions = questionData;
        Prefs = this.getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        Log.e("Questions Data", Questions.toString());

        scrollView.setVisibility(View.VISIBLE);
        previewText.setVisibility(View.VISIBLE);
        textureView.setVisibility(View.VISIBLE);
        QuoteText.setVisibility(View.GONE);
        Option1.setVisibility(View.GONE);
        Option2.setVisibility(View.GONE);


        if (cnt == 0) {
            QuestionText.setText(Questions.get(0).getQuestion());

            QuestionData questionData1 = Questions.get(0);
            int options = 0;
            Option1.setVisibility(View.VISIBLE);
            Option2.setVisibility(View.VISIBLE);


        }


         Option2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

         String userans  = Option1.getText().toString().trim();
        Log.e("Error", "Error" + userans);
        if(!userans.equals("")) {
            takephoto();

            Option1.setText("");
            cnt += 1;
            QuestionData questionData2 = Questions.get(cnt - 1);
            String ans = questionData2.getMap().get(questionData2.getQuestion());
            ans = ans.replace("*",userans);
            JSONObject ansJson = new JSONObject();
            try {
                ansJson.put("Ans",ans);
                Log.e("JsonAns",ansJson.toString());
                ApipostCheckAns apipostCheckAns = new ApipostCheckAns(getContext(),ansJson,"Analysing Your Ans");
                apipostCheckAns.checkAnsResponse =thiscontext;
                apipostCheckAns.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }


//            switch (id) {
//                case R.id.option1_id:
//                    score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option1.getText().toString()))));
//                    break;
//                case R.id.option2_id:
//                    score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option2.getText().toString()))));
//                    break;
//
//                default:
//                    Log.e("Error", "Error in QuestionFragment");
//            }



            Option1.setVisibility(View.GONE);
            Option2.setVisibility(View.GONE);


            Log.e("ID", String.valueOf(score));

            if (cnt <= 4) {
                Option1.setVisibility(View.GONE);
                Option2.setVisibility(View.GONE);

                QuestionText.setText(Questions.get(cnt).getQuestion());

                QuestionData questionData1 = Questions.get(cnt);
                int options = 0;
                Option1.setVisibility(View.VISIBLE);
                Option2.setVisibility(View.VISIBLE);

                Option2.setOnClickListener(this);



            }


            if (cnt >= 5) {
                EndAction();
            }


        }
        else {
            Toast.makeText(getContext(),"Ans this Question before Preceding",Toast.LENGTH_SHORT).show();
        }



    }

    @SuppressLint("SetTextI18n")
    void EndAction() {
        scrollView.setVisibility(View.GONE);
        allCaughtUp.setVisibility(View.VISIBLE);
        previewText.setVisibility(View.GONE);
        Option1.setVisibility(View.GONE);
        Option2.setVisibility(View.GONE);

        QuoteText.setVisibility(View.VISIBLE);
        QuoteText.setText("You are all Caught up!");

        Prefs.edit().putInt("scores", score).apply();
        allCaughtUp.playAnimation();
        Log.e("check","Done ");
        CameraX.unbindAll();
        textureView.setVisibility(View.GONE);
    }

    @Override
    public void getQuestions(ArrayList<QuestionData> data) {
        Option1.setVisibility(View.GONE);
        Option2.setVisibility(View.GONE);


        PerformAction(data);

    }



    private void updatetransform() {
        Matrix mx  = new Matrix();
        float h = textureView.getMeasuredHeight();
        float w = textureView.getMeasuredWidth();
        float cX = w/2f;
        float cY  = h/2f;
        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case  Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;

        }
        mx.postRotate((float)rotationDgr,cX,cY);
        textureView.setTransform(mx);

    }

    private void detectAndFrame(final Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        @SuppressLint("StaticFieldLeak")
        AsyncTask<InputStream, String, Face[]> detectTask =

                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    true    ,        // returnFaceLandmarks
                                    // returnFaceAttributes:
                                    new FaceServiceClient.FaceAttributeType[] {
                                            FaceServiceClient.FaceAttributeType.Emotion,
                                            FaceServiceClient.FaceAttributeType.Gender }
                            );

                            for (int i=0;i<result.length;i++) {
                                jsonObject.put("happiness" , result[i].faceAttributes.emotion.happiness);
                                jsonObject.put("sadness" , result[i].faceAttributes.emotion.sadness);
                                jsonObject.put("surprise" , result[i].faceAttributes.emotion.surprise);
                                jsonObject.put("neutral"  , result[i].faceAttributes.emotion.neutral);
                                jsonObject.put("anger" , result[i].faceAttributes.emotion.anger);
                                jsonObject.put("contempt" , result[i].faceAttributes.emotion.contempt);
                                jsonObject.put("disgust" , result[i].faceAttributes.emotion.disgust);
                                jsonObject.put("fear" , result[i].faceAttributes.emotion.fear);
                                Log.e(TAG, "doInBackground: "+jsonObject.toString()  );

                                jsonObject1.put(  (String.valueOf(i)),jsonObject);
                            }
//
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"DATA"+jsonObject1.toString(),Toast.LENGTH_LONG).show();
                                }});

                            if (result == null) {
                                publishProgress(
                                        "Detection Finished. Nothing detected");
                                return null;
                            }
                            Log.e("TAG", "doInBackground: "+"   "+result.length );
                            publishProgress(String.format(
                                    "Detection Finished. %d face(s) detected",
                                    result.length));

                            return result;
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        //TODO: show progress dialog
                        detectionProgressDialog.show();
                    }

                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //TODO: update progress
                        detectionProgressDialog.setMessage(progress[0]);
                    }

                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames
                        detectionProgressDialog.dismiss();

                        facesDetected = result;

                        if (!exceptionMessage.equals("")) {
                            if (facesDetected == null) {
//                                showError(exceptionMessage + "\nNo faces detected.");
                            } else {
//                                showError(exceptionMessage);
                            }
                        }
                        if (result == null) {
                            if (facesDetected == null) {
//                                showError("No faces detected");
                            }
                        }
                        Log.e("TAG", "onPostExecute: "+facesDetected );

                        imageButton.setImageBitmap(
                                drawFaceRectanglesOnBitmap(imageBitmap, result));
                        imageBitmap.recycle();
//                        Toast.makeText(getApplicationContext(), "Now you can identify the person by pressing the \"Identify\" Button", Toast.LENGTH_LONG).show();
                        takePicture = true;
                    }
                };

        detectTask.execute(inputStream);
    }



    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }

    private Bitmap imageProxyToBitmap(ImageProxy image, int rotation)
    {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Bitmap bitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Matrix matrix = new Matrix();

        matrix.postRotate(rotation);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, image.getWidth(), image.getHeight(), true);

        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

    }

    @Override
    public void getAnsResponse(JSONObject data) {
        Log.e("Error", "Error" + data.toString());
        try {
            score+=data.getInt("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //
//    Button btn1, btn2, btn3, btn4;
//    TextView quotetext;
//    TextView txt;
//    int s1=4;
//    int s2=3;
//    int s3=2;
//    int s4=1;
//    LottieAnimationView view;
//
//
//
//    String key="";
//    SharedPreferences sharedPreferences;
//    static int count = -1;
//    static int i = 0;
//    SharedPreferences.Editor myEdit;
//    String info[] = {
//            "Turnout of the quarrel?","I won the debate", "compromised for peace", "forgot the " +
//            "fight and started talking","fight led to complications in relationship",
//            "Anticipation of next morning?","Excited (big day)", "As usual (regular stuffs)",
//            "Nothing much","Exhausted","Do I like and enjoy my current physical state?",
//            "Absolutely", "Look to improve everyday", "Do look for motivation", "low self-esteem",
//            "When were you the most happy?", "Morning", "Afternoon", "Evening","Night", "Where " +
//            "you anxious, worried or scared about anything?","Never", "Sometimes", "Often",
//            "Constantly", "Did you felt restless, agitated, frantic or tense?", "Never", "A few " +
//            "times","Often", "Constantly", "Did your worry was out of your control?","Never", "A few " +
//            "times","Often", "Constantly"
//    };
//
//
//    @SuppressLint("WrongConstant")
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.questions_fragment, container, false);
//        String[] qtnQuotes = {
//                "No medicine cures what happiness cannot.", "They say a person needs just three " +
//                "things to be truly happy in this world: Someone to love, something to do, and something to hope for.",
//                "Let us be grateful to the people who make us happy; they are the charming " +
//                        "gardeners who make our souls blossom.", "Happiness is a warm puppy.",
//                "There's nothing like deep breaths after laughing that hard. Nothing in the world" +
//                        " like a sore stomach for the right reasons.",
//                "Sanity and happiness are an impossible combination.", "You cannot protect " +
//                "yourself from sadness without protecting yourself from happiness.", "It isn't what you have or who you are or where you are or what you are doing that makes you happy or unhappy. It is what you think about it.",
//                "Happiness is a state of mind. It's just according to the way you look at things."
//        };
//
//
//
//
//          sharedPreferences = getActivity().getSharedPreferences("MySharedPref",
//                MODE_APPEND);
//          GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
//        key = account.getId();
//          myEdit = sharedPreferences.edit();
//
//
//
//        quotetext = v.findViewById(R.id.quote_text_id);
//        txt = v.findViewById(R.id.textView);
//        btn1 = v.findViewById(R.id.option1_id);
//        btn2 = v.findViewById(R.id.option2_id);
//        btn3 = v.findViewById(R.id.option3_id);
//        btn4 = v.findViewById(R.id.option4_id);
//
//
//
//        Random rand = new Random();
//        int x = rand.nextInt(qtnQuotes.length);
//        quotetext.setText(qtnQuotes[x]);
//
//        btn1.setOnClickListener(this);
//        btn2.setOnClickListener(this);
//        btn3.setOnClickListener(this);
//        btn4.setOnClickListener(this);
//
//
////
////        btn1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                option = btn1.getText().toString().trim();
////                flag = 1;
////
////            }
////        });
////
////        btn2.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                option = btn2.getText().toString().trim();
////                flag = 1;
////            }
////        });
////
////        btn3.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                option = btn3.getText().toString().trim();
////                flag = 1;
////            }
////        });
////
////        btn4.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                option = btn4.getText().toString().trim();
////                flag = 1;
////            }
////        });
//
//
////        if(flag == 1){
////
////            Log.e("Selected Option" , option);
////            ViewGroup parent=(ViewGroup) quotetext.getParent();                 //get parent of the TextView
////            parent.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);            //enable animations
////            quotetext.setText("Hey! You Clicked something");
////
////            Toast.makeText(getActivity().getApplicationContext() , "Option Selected" , Toast.LENGTH_SHORT).show();
////
////        }
//
//
//        return v;
//    }
//
//    @Override
//    public void onClick(View v) {
//        Log.e(TAG, "onClick: 1 "+   sharedPreferences.getInt(key, 0) );
//
//        if(count >= info.length){
//            Toast.makeText(getActivity().getApplicationContext() , "You Are All Caught Up!", Toast.LENGTH_LONG).show();
//        }
//        else{
//            count = count +5;
//
//
//        int id = v.getId();
//
//        String optiontext = null;
//
//        switch (id) {
//            case R.id.option1_id:
//                myEdit.putInt(key,s1+sharedPreferences.getInt(key, 0)).commit();
//                        optiontext = btn1.getText().toString();
//                break;
//            case R.id.option2_id:
//                myEdit.putInt(key,s2+sharedPreferences.getInt(key, 0)).commit();
//
//                optiontext = btn2.getText().toString();
//                break;
//            case R.id.option3_id:
//                myEdit.putInt(key,s3+sharedPreferences.getInt(key, 0)).commit();
//
//                optiontext = btn3.getText().toString();
//                break;
//            case R.id.option4_id:
//                myEdit.putInt(key,s4+sharedPreferences.getInt(key, 0)).commit();
//
//                optiontext = btn4.getText().toString();
//                break;
//            default:
//
//                break;
//
//        }
//
//        if(info.length >= count)
//        {
//            txt.setText(info[i]);
//            i++;
//            btn1.setText(info[i]);
//            i++;
//            btn2.setText(info[i]);
//            i++;
//            btn3.setText(info[i]);
//            i++;
//            btn4.setText(info[i]);
//            i++;
//            Log.e(TAG, "onClick: "+i );
//            Log.e(TAG, "onClick: "+count );
//
//        }
//        else{
//            Toast.makeText(getActivity().getApplicationContext() , "You Are All Caught Up!", Toast.LENGTH_LONG).show();
//
//        }}
//    }
}
