package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.AllQuestionsAdapter;
import com.example.odishawarrior.classes.QuestionsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import it.sephiroth.android.library.xtooltip.ClosePolicy;
import it.sephiroth.android.library.xtooltip.Tooltip;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class QuestionsDisplayActivity extends AppCompatActivity {

    private String productId, productTitle, testTitle;
    private int setNo, attemptsCount, maxMarks, timeLimit;
    private boolean isEnglish;

    private TextView timerTv, currentQuestionTv, positiveMarkTv, negativeMarkTv, maxMarkTv, questionTv, takeATourTv,
                        optionA,optionB,optionC,optionD ;
    private Button submitBtn, prevBtn, clearBtn, nextBtn;
    private ImageView allQuestionsIv, changeLanguageIv;

    public static List<QuestionsModel> englishQuestionData, hindiQuestionData;
    public static int currentQuestionNo = 1;
    public static int previouslySelectedQues = 1;

    public static Dialog allQuestionsDialog;

    public static RecyclerView allQuesDialogRV;
    private GridLayoutManager manager;
    private AllQuestionsAdapter adapter;

    private CountDownTimer c;

    private RadioGroup optionsRadioGroup;
    private RadioButton radBtnA, radBtnB, radBtnC, radBtnD;
    private int previouslyCheckedRadioBtn;

    private Map<Integer, String> userSelectedAnswers;
    public static Map<Integer, Integer> userSelectedAnswersID;

    private ConstraintLayout constraintLayout1, constraintLayout2, testSubmittedConstraintLayout;
    private TextView testScoreTv;
    private CircularProgressIndicator testScoreIndicator;

    private DatabaseReference reference;
    private FirebaseFirestore firestore;

    public static Dialog loadingDialog;

    private Tooltip currentQuestionsTooltip,languageChangeTooltip,allQuestionsTooltip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_display);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        if(getIntent() != null){

            productId = getIntent().getStringExtra("product_id");
            productTitle = getIntent().getStringExtra("product_title");
            testTitle = getIntent().getStringExtra("test_title");

            setNo = getIntent().getIntExtra("set_no", 0);
            maxMarks = getIntent().getIntExtra("max_marks", 0);
            timeLimit = getIntent().getIntExtra("max_time", 0);
            attemptsCount = getIntent().getIntExtra("attempts_count", 0);

            isEnglish = getIntent().getBooleanExtra("is_english", true);

            currentQuestionNo = 1;
            initializeLayoutElements();

            allQuestionsDialogBuilder();



            timerTv.setText(""+ timeLimit + ":" + "00");


            setTextLayouts();

            setCurrentQuestionsAndOptionsLayout();
            startTestTimer();

            setAllQuestionFunctionality();
            changeLanguageBtnFunctionality();
            setNextBtnFunctionality();
            setPreviousBtnFunctionality();
            setOnAnswerSelectedFunctionality();
            setClearBtnFunctionality();
            setSubmitBtnFunctionality();
            //createToolTips();
            setTakeATourBtnFunctionality();

            //allQuestionsDialog.show();

        }
        else{

            //toast
        }
    }

    private void setTextLayouts() {

        setMaximumMarksLayout();

    }

    private void createToolTips(){

         allQuestionsTooltip = new Tooltip.Builder(this)
                .anchor(allQuestionsIv,0,0,false)
                .text("Navigate to a particular question")
                .arrow(true)
                .floatingAnimation(Tooltip.Animation.Companion.getDEFAULT())
                .showDuration(3000L)
                .fadeDuration(200L)
                .overlay(true)
                .create();

         languageChangeTooltip = new Tooltip.Builder(this)
                .anchor(changeLanguageIv,0,0,false)
                .text("Change to your desired language")
                .arrow(true)
                .floatingAnimation(Tooltip.Animation.Companion.getDEFAULT())
                .showDuration(3000L)
                .fadeDuration(200L)
                .overlay(true)
                .create();

         currentQuestionsTooltip = new Tooltip.Builder(this)
                .anchor(currentQuestionTv,0,0,false)
                .text("Question No., you are currently attempting")
                .arrow(true)
                .floatingAnimation(Tooltip.Animation.Companion.getDEFAULT())
                .showDuration(3000L)
                .fadeDuration(200L)
                .overlay(true)
                .create();

         allQuestionsTooltip.doOnHidden(new Function1<Tooltip, Unit>() {
             @Override
             public Unit invoke(Tooltip tooltip) {

                 languageChangeTooltip.doOnHidden(new Function1<Tooltip, Unit>() {
                     @Override
                     public Unit invoke(Tooltip tooltip) {

                         currentQuestionsTooltip.doOnHidden(new Function1<Tooltip, Unit>() {
                             @Override
                             public Unit invoke(Tooltip tooltip) {

                                 return null;
                             }
                         }).show(currentQuestionTv, Tooltip.Gravity.RIGHT, true);

                         return null;
                     }
                 }).show(changeLanguageIv, Tooltip.Gravity.BOTTOM, true);

                 return null;
             }
         }).show(allQuestionsIv, Tooltip.Gravity.BOTTOM, true);

        //allQuestionsTooltip.show(allQuestionsIv, Tooltip.Gravity.BOTTOM, true);
        //languageChangeTooltip.show(changeLanguageIv, Tooltip.Gravity.BOTTOM, true);
        //currentQuestionsTooltip.show(currentQuestionTv, Tooltip.Gravity.BOTTOM, true);

        //tooltip.show(allQuestionsIv, Tooltip.Gravity.BOTTOM, true);

    }

    private void showTooltips(){



    }

    private void setTakeATourBtnFunctionality(){
        takeATourTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "tour clicked", Toast.LENGTH_SHORT).show();
                createToolTips();
            }
        });

    }

    //todo make changes
    private void submitTestAndCalculateScore(){

        c.cancel();

        loadingDialog.show();

        constraintLayout1.setVisibility(View.GONE);
        constraintLayout2.setVisibility(View.GONE);
        testSubmittedConstraintLayout.setVisibility(View.VISIBLE);

        int score = 0, positive_score = 0, negative_score = 0;
        String correct_answer = "";
        String user_answer = "";

        for(int i=0; i < englishQuestionData.size(); i++){
            if(userSelectedAnswers.containsKey(i+1)){

                correct_answer = englishQuestionData.get(i).getCorrect().trim();
                user_answer = userSelectedAnswers.get(i+1).trim();

                if(correct_answer.equals(user_answer)){
                    score = score + englishQuestionData.get(i).getPositive().intValue();
                    positive_score = positive_score + englishQuestionData.get(i).getPositive().intValue();
                }else {
                    score = score - englishQuestionData.get(i).getNegative().intValue();
                    negative_score = negative_score - englishQuestionData.get(i).getNegative().intValue();
                }
            }

        }

        Map<String , String> attempt_details = new HashMap<>();
        attempt_details.put("score", ""+score);
        attempt_details.put("positive_score", ""+positive_score);
        attempt_details.put("negative_score", ""+negative_score);

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);

        attempt_details.put("taken_on", dateToStr);

        firestore.collection("USER").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_TESTS").
                collection("ATTEMPTED_TESTS").document(productId).collection(""+ setNo + "_attempt_metadata" ).
                document(""+setNo+"_attempt_details").set(attempt_details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(QuestionsDisplayActivity.this, "Success", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionsDisplayActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });



        testScoreTv.setText(""+ score + "/" + maxMarks);
        int percentage = (score*100)/maxMarks;

        if(percentage <= 35){
            testScoreIndicator.setIndicatorColor(Color.parseColor("#a83244"));
        }else if(percentage > 35 && percentage <=80){
            testScoreIndicator.setIndicatorColor(Color.parseColor("#e6c43e"));
        }else {
            testScoreIndicator.setIndicatorColor(Color.parseColor("#4bb543"));
        }

        testScoreIndicator.setProgress(percentage,true);

    }

    private void setSubmitBtnFunctionality(){

        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsDisplayActivity.this);

        builder.setTitle("Submit Test");
        builder.setMessage("Are you sure, you want to submit the test?");
        builder.setPositiveButton("Submit my test", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //submit the test
                submitTestAndCalculateScore();
            }
        });
        builder.setNegativeButton("Continue with test", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog submitDialog = builder.create();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDialog.show();

                Button btn1 = submitDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button btn2 = submitDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) btn2.getLayoutParams();
                LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) btn1.getLayoutParams();

                positiveButtonLL.gravity = Gravity.CENTER;
                btn2.setLayoutParams(positiveButtonLL);

                negativeButtonLL.gravity = Gravity.CENTER;
                btn1.setLayoutParams(negativeButtonLL);
            }
        });

    }

    private void setClearBtnFunctionality(){
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    if(userSelectedAnswersID.containsKey(currentQuestionNo)){
                        optionsRadioGroup.clearCheck();
                        userSelectedAnswersID.remove(currentQuestionNo);
                        userSelectedAnswers.remove(currentQuestionNo);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    //todo make changes
    private void setOnAnswerSelectedFunctionality(){


        optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                try {
                    group.findViewById(previouslyCheckedRadioBtn).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

                try {

                    RadioButton btn = group.findViewById(checkedId);
                    userSelectedAnswers.put(currentQuestionNo, btn.getContentDescription().toString());
                    userSelectedAnswersID.put(currentQuestionNo, checkedId);

                    previouslyCheckedRadioBtn = checkedId;
                    //TextView selectedOption = findViewById(btn.getLabelFor());
                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4BB543")));

                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void allQuestionsDialogBuilder(){

        allQuestionsDialog = new Dialog(QuestionsDisplayActivity.this);
        View view = LayoutInflater.from(QuestionsDisplayActivity.this).inflate(R.layout.all_questions_dialog_layout, null);
        allQuestionsDialog.setContentView(view);
        allQuestionsDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        allQuestionsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        allQuesDialogRV = allQuestionsDialog.findViewById(R.id.allQuestionsDialogRV);
        manager = new GridLayoutManager(this,5);
        manager.setOrientation(RecyclerView.VERTICAL);
        allQuesDialogRV.setLayoutManager(manager);

//        TextClock t = new TextClock(this);
//        t.playSoundEffect(SoundEffectConstants.);

        allQuestionsDialog.setCancelable(true);

        allQuestionsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setCurrentQuestionsAndOptionsLayout();
            }
        });

        allQuestionsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                changeAnsweredQuestionsIndicator();
                changeCurrentQuestionIndicator();
            }
        });

        adapter = new AllQuestionsAdapter(englishQuestionData.size());

        allQuesDialogRV.setAdapter(adapter);
        previouslySelectedQues = 1;
        adapter.notifyDataSetChanged();

    }

    private void setAllQuestionFunctionality(){
        allQuestionsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allQuestionsDialog.show();

            }
        });
    }

    private void startTestTimer(){

         c = new CountDownTimer(timeLimit*60000,1000) {
            int minute = timeLimit;
            int second = 0;

            @Override
            public void onTick(long millisUntilFinished) {

                if(second > 0){
                    second--;

                }
                else{
                    second = 59;
                    minute--;
                }

                if(minute<10 && second<10){
                    timerTv.setText("0"+ minute + ":0" + second);
                }else if(minute<10){
                    timerTv.setText("0"+ minute + ":" + second);
                }else if(second<10){
                    timerTv.setText(""+ minute + ":0" + second);
                }
                else{
                    timerTv.setText(""+ minute + ":" + second);
                }


            }

            @Override
            public void onFinish() {

                // TODO: 07-01-2022 submit the test
                Toast.makeText(QuestionsDisplayActivity.this, "Test finished !!!", Toast.LENGTH_SHORT).show();
                //submitTestAndCalculateScore();

            }
        };

        c.start();

    }

    private void changeLanguageBtnFunctionality(){
        changeLanguageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEnglish){
                    isEnglish = false;
                    setTextLayouts();
                    setCurrentQuestionsAndOptionsLayout();
                }else{
                    isEnglish = true;
                    setTextLayouts();
                    setCurrentQuestionsAndOptionsLayout();
                }

            }

        });

    }


    private void changeCurrentQuestionIndicator(){

        RecyclerView.ViewHolder vh = allQuesDialogRV.findViewHolderForAdapterPosition(previouslySelectedQues-1);
        RecyclerView.ViewHolder currvh = allQuesDialogRV.findViewHolderForAdapterPosition(currentQuestionNo-1);

        vh.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F8F4F4")));

        previouslySelectedQues = currentQuestionNo;

        currvh.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4BB543")));

    }

    private void changeAnsweredQuestionsIndicator(){
        for(int i=0;i< englishQuestionData.size();i++){
            if(userSelectedAnswersID.containsKey(i+1)){
                RecyclerView.ViewHolder vh = allQuesDialogRV.findViewHolderForAdapterPosition(i);
                vh.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#67d7f0")));
            }
        }
    }

    private void setNextBtnFunctionality(){

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEnglish) {
                    if(currentQuestionNo < englishQuestionData.size()) {
                        currentQuestionNo++;

                        setCurrentQuestionsAndOptionsLayout();
                        try {
                            changeCurrentQuestionIndicator();
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                }
                else{
                    if(currentQuestionNo < hindiQuestionData.size()) {
                        currentQuestionNo++;

                        setCurrentQuestionsAndOptionsLayout();
                        try {
                            changeCurrentQuestionIndicator();
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }


            }
        });



    }

    private void setPreviousBtnFunctionality(){

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEnglish) {
                    if(currentQuestionNo > 1) {
                        currentQuestionNo--;

                        setCurrentQuestionsAndOptionsLayout();
                        try {
                            changeCurrentQuestionIndicator();
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    if(currentQuestionNo > 1) {
                        currentQuestionNo--;

                        setCurrentQuestionsAndOptionsLayout();
                        try {
                            changeCurrentQuestionIndicator();
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        });


    }

    private void setCurrentQuestionsAndOptionsLayout(){

        if(isEnglish) {

            currentQuestionTv.setText("Q. "+ currentQuestionNo + "/" + englishQuestionData.size());

            positiveMarkTv.setText("+" + englishQuestionData.get(currentQuestionNo-1).getPositive());
            negativeMarkTv.setText("-" + englishQuestionData.get(currentQuestionNo-1).getNegative());

            questionTv.setText(englishQuestionData.get(currentQuestionNo - 1).getQuestion());

            radBtnA.setText(hindiQuestionData.get(currentQuestionNo - 1).getA());
            radBtnB.setText(hindiQuestionData.get(currentQuestionNo - 1).getB());
            radBtnC.setText(hindiQuestionData.get(currentQuestionNo - 1).getC());
            radBtnD.setText(hindiQuestionData.get(currentQuestionNo - 1).getD());




//            optionA.setText(englishQuestionData.get(currentQuestionNo - 1).getA());
//            optionB.setText(englishQuestionData.get(currentQuestionNo - 1).getB());
//            optionC.setText(englishQuestionData.get(currentQuestionNo - 1).getC());
//            optionD.setText(englishQuestionData.get(currentQuestionNo - 1).getD());
        }
        else {

            currentQuestionTv.setText("Q. "+ currentQuestionNo + "/" + hindiQuestionData.size());

            positiveMarkTv.setText("+" + hindiQuestionData.get(currentQuestionNo-1).getPositive());
            negativeMarkTv.setText("-" + hindiQuestionData.get(currentQuestionNo-1).getNegative());

            questionTv.setText(hindiQuestionData.get(currentQuestionNo - 1).getQuestion());

            radBtnA.setText(hindiQuestionData.get(currentQuestionNo - 1).getA());
            radBtnB.setText(hindiQuestionData.get(currentQuestionNo - 1).getB());
            radBtnC.setText(hindiQuestionData.get(currentQuestionNo - 1).getC());
            radBtnD.setText(hindiQuestionData.get(currentQuestionNo - 1).getD());

//            optionA.setText(hindiQuestionData.get(currentQuestionNo - 1).getA());
//            optionB.setText(hindiQuestionData.get(currentQuestionNo - 1).getB());
//            optionC.setText(hindiQuestionData.get(currentQuestionNo - 1).getC());
//            optionD.setText(hindiQuestionData.get(currentQuestionNo - 1).getD());
        }

        if(userSelectedAnswersID.containsKey(currentQuestionNo)){
            int id = userSelectedAnswersID.get(currentQuestionNo);
            //RadioButton btn = findViewById(id);
            optionsRadioGroup.check(id);
            //btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4BB543")));
            Toast.makeText(QuestionsDisplayActivity.this, ""+userSelectedAnswers.toString(), Toast.LENGTH_SHORT).show();
        }else {
            optionsRadioGroup.clearCheck();
            userSelectedAnswersID.remove(currentQuestionNo);
            userSelectedAnswers.remove(currentQuestionNo);
            Toast.makeText(QuestionsDisplayActivity.this, ""+userSelectedAnswers.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setMaximumMarksLayout(){
        if(isEnglish) {
            maxMarkTv.setText("Max Marks: " + maxMarks);
        }
        else{
            maxMarkTv.setText("अधिकतम अंक: " + maxMarks);
        }
    }

    private void initializeLayoutElements(){

        timerTv = findViewById(R.id.timerTv);
        currentQuestionTv = findViewById(R.id.questionNoTv);
        positiveMarkTv = findViewById(R.id.positiveMarksTv);
        negativeMarkTv = findViewById(R.id.negativeMarksTv);
        maxMarkTv = findViewById(R.id.maxMarksTv);
        questionTv = findViewById(R.id.questionTv);
        optionA = findViewById(R.id.option1);
        optionB = findViewById(R.id.option2);
        optionC = findViewById(R.id.option3);
        optionD = findViewById(R.id.option4);
        takeATourTv = findViewById(R.id.takeATourTv);

       userSelectedAnswers = new HashMap<>();
       userSelectedAnswersID = new HashMap<>();

        submitBtn = findViewById(R.id.submitBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        clearBtn = findViewById(R.id.clearSelBtn);

        allQuestionsIv = findViewById(R.id.allQuestionsIv);
        changeLanguageIv = findViewById(R.id.changeLanguageIv);

        optionsRadioGroup = findViewById(R.id.radioGroup);
        radBtnA = findViewById(R.id.radioBtn1);
        radBtnB = findViewById(R.id.radioBtn2);
        radBtnC = findViewById(R.id.radioBtn3);
        radBtnD = findViewById(R.id.radioBtn4);

        constraintLayout1 = findViewById(R.id.constraintLayout);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        testSubmittedConstraintLayout = findViewById(R.id.testSubmittedConstraintLayout);

        testScoreTv = findViewById(R.id.testScore);
        testScoreIndicator = findViewById(R.id.testScoreIndicator);

        reference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionsDisplayActivity.this);

            builder.setTitle("Exit Test");
            builder.setMessage("Are you sure, you want to quit the test?");
            builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    c.cancel();
                    finish();
                }
            });
            builder.setNegativeButton("Continue with test", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setCancelable(true);

            AlertDialog exitDialog = builder.create();

            exitDialog.show();

            Button btn1 = exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button btn2 = exitDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) btn2.getLayoutParams();
            LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) btn1.getLayoutParams();

            positiveButtonLL.gravity = Gravity.RIGHT;
            positiveButtonLL.rightMargin = 0;
            positiveButtonLL.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            positiveButtonLL.weight = 4;
            btn2.setLayoutParams(positiveButtonLL);

            negativeButtonLL.gravity = Gravity.LEFT;
            negativeButtonLL.leftMargin = 0;
            negativeButtonLL.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            negativeButtonLL.weight = 1;
            btn1.setLayoutParams(negativeButtonLL);



        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.cancel();
    }
}