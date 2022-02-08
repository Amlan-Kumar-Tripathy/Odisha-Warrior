package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.QuestionsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permissions;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

public class TestInstructionsActivity extends AppCompatActivity {

    private DatabaseReference reference;

    private TextView titleTv, maxMarksTv, timeLimitTv, basicInstructionsTv;
    private Button startTestBtn;

    private String productId, productTitle, testTitle;
    private int setNo, attemptsCount;

    private Long maxMarks, timeLimit;
    private List<QuestionsModel> englishQuestionPaper, hindiQuestionPaper;

    public static boolean isEnglish = true;

    private Dialog loadingDialog;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_instructions);




        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_bacgkround_white);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        reference = FirebaseDatabase.getInstance().getReference();

        titleTv = findViewById(R.id.testTitle);
        maxMarksTv = findViewById(R.id.maxMarksTv);
        timeLimitTv = findViewById(R.id.maxTimeTv);
        basicInstructionsTv = findViewById(R.id.basicInstrucTv);
        startTestBtn = findViewById(R.id.startTestBtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Instructions");

        if(getIntent() != null){

            productTitle = getIntent().getStringExtra("product_title");
            productId = getIntent().getStringExtra("product_id");
            attemptsCount = getIntent().getIntExtra("no_of_attempts", 0);
            // TODO: 04-01-2022  attempt count
            setNo = getIntent().getIntExtra("set_no", 0);
            testTitle = getIntent().getStringExtra("test_title");

            boolean successfullyFetchedData = fetchData();

            startTestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ContextCompat.checkSelfPermission(TestInstructionsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                        ) {
                        Toast.makeText(TestInstructionsActivity.this, "Grant permission to use this device's camera and storage location", Toast.LENGTH_SHORT).show();
                        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    }
                    else if(ContextCompat.checkSelfPermission(TestInstructionsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(TestInstructionsActivity.this, "Grant permission to use this device's camera and storage location", Toast.LENGTH_SHORT).show();
                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                    }
                    else{

                        if (successfullyFetchedData) {

                            Intent intent = new Intent(TestInstructionsActivity.this, QuestionsDisplayActivity.class);

                            intent.putExtra("product_title", productTitle);
                            intent.putExtra("product_id", productId);
                            intent.putExtra("attempts_count", attemptsCount);
                            intent.putExtra("set_no", setNo);
                            intent.putExtra("test_title", testTitle);
                            intent.putExtra("max_marks", maxMarks.intValue());
                            intent.putExtra("max_time", timeLimit.intValue());
                            intent.putExtra("is_english", isEnglish);


                            //MAKE AN INTENT
                            //pass the english and hindi ques papers

                            startActivity(intent);

                        } else {
                            Toast.makeText(TestInstructionsActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                            finish();
                        }


                    }

                }
            });

        }
    }

    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(TestInstructionsActivity.this, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(TestInstructionsActivity.this, new String[]{permission}, requestCode);
        }
        else {
            Toast.makeText(TestInstructionsActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(TestInstructionsActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(TestInstructionsActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(TestInstructionsActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TestInstructionsActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setData(){

        if(isEnglish){

            titleTv.setText(testTitle);
            maxMarksTv.setText("Max Marks: " + maxMarks );
            timeLimitTv.setText("Time Limit: " + timeLimit + " minutes");
            startTestBtn.setText("Start Test");

            String basicInsructionsinEnglish ="Basic Instructions:<br><br>" + "<ul><li>You have to finish the test in "+ timeLimit +" minutes.</li><li>This test can be attempted only twice.</li> <li>Each question contains only 4 options, out of which only one is correct.</li><li>Negative marking in each question will be displayed at the top in red color..</li> <li>Positive marking in each question will be displayed at the top in green</li><li>There is no negative marking for the question which you have not attempted.</li><li>After the time gets over, the test gets submitted automatically.</li><li>When you click on the start test button, you agree to the instruction provided above.</li></ul>";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                basicInstructionsTv.setText(Html.fromHtml(basicInsructionsinEnglish, Html.FROM_HTML_MODE_COMPACT));
            } else
                basicInstructionsTv.setText(Html.fromHtml(basicInsructionsinEnglish));

        }
        else{

            // TODO: 04-01-2022 change the language

            titleTv.setText(testTitle);
            maxMarksTv.setText("अधिकतम अंक: " + maxMarks );
            timeLimitTv.setText("समय सीमा: " + timeLimit + " मिनट");
            startTestBtn.setText("टेस्ट शुरू करें");

            String basicInsructionsinHindi ="बुनियादी निर्देश:<br><br>" + "<ul><li>आपको परीक्षा समाप्त करनी है "+ timeLimit +" मिनट.</li><li>इस परीक्षा का प्रयास केवल दो बार किया जा सकता है.</li> <li>प्रत्येक प्रश्न में केवल 4 विकल्प हैं, जिनमें से केवल एक सही है.</li><li>प्रत्येक प्रश्न में नकारात्मक अंकन लाल रंग में शीर्ष पर प्रदर्शित किया जाएगा.</li> <li>प्रत्येक प्रश्न में सकारात्मक अंकन सबसे ऊपर हरे रंग में प्रदर्शित किया जाएगा</li><li>जिस प्रश्न का आपने प्रयास नहीं किया उसके लिए कोई नकारात्मक अंकन नहीं है.</li><li>समय समाप्त होने के बाद, परीक्षण स्वचालित रूप से जमा हो जाता है.</li><li>जब आप स्टार्ट टेस्ट बटन पर क्लिक करते हैं, तो आप ऊपर दिए गए निर्देश से सहमत होते हैं.</li></ul>";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                basicInstructionsTv.setText(Html.fromHtml(basicInsructionsinHindi, Html .FROM_HTML_MODE_COMPACT));
            } else
                basicInstructionsTv.setText(Html.fromHtml(basicInsructionsinHindi));

            }

    }


    private boolean fetchData(){

        if(setNo != 0){

            englishQuestionPaper = new ArrayList<>();
            hindiQuestionPaper = new ArrayList<>();

            reference.child("QuestionsData").child(productId).child("set"+setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                maxMarks = snapshot.child("maxMarks").getValue(Long.class);
                timeLimit = snapshot.child("maxTime").getValue(Long.class);

                for(DataSnapshot shot : snapshot.child("english").getChildren()){
                    englishQuestionPaper.add(shot.getValue(QuestionsModel.class));
                }

                for(DataSnapshot shot : snapshot.child("odia").getChildren()){
                    hindiQuestionPaper.add(shot.getValue(QuestionsModel.class));
                }

                QuestionsDisplayActivity.englishQuestionData = englishQuestionPaper;
                QuestionsDisplayActivity.hindiQuestionData = hindiQuestionPaper;

                loadingDialog.dismiss();

               // Toast.makeText(TestInstructionsActivity.this, ""+ QuestionsDisplayActivity.englishQuestionData.size(), Toast.LENGTH_SHORT).show();

                setData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.dismiss();
            }
        });
            return true;

        }
        else{

            Toast.makeText(TestInstructionsActivity.this, "Something went wrong TIA", Toast.LENGTH_SHORT).show();
            finish();
            return false;

        }

    }

    private void runOnLanguageChange(){

        setData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.changeLanguageIcon){
            if(isEnglish){
                isEnglish = false;
                runOnLanguageChange();

            }
            else{
                isEnglish = true;
                runOnLanguageChange();
            }
            return true;
        }
        else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}