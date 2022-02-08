package com.example.odishawarrior.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.odishawarrior.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.scroll.ScrollHandle;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.util.ByteBufferInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NotesPdfViewerActivity extends AppCompatActivity {

    private String pdfTitle, pdfUrl;
    private PDFView pdfView;

    // declare the dialog as a member field of your activity
   // private ProgressDialog mProgressDialog;
    private CircularProgressIndicator circularProgressIndicator;
    private RetrievePdfFromUrl retrievePdfFromUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);


        setContentView(R.layout.activity_notes_pdf_viewer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        pdfView = findViewById(R.id.pdfViewer);

        circularProgressIndicator = findViewById(R.id.pdfViewerCircularProgress);
        getSupportActionBar().hide();

        if(getIntent() != null){
            pdfTitle = getIntent().getStringExtra("pdf_title");
            pdfUrl = getIntent().getStringExtra("pdf_url");
           // pdfUrl = "https://firebasestorage.googleapis.com/v0/b/odisha-warrior.appspot.com/o/NotesPdf%2FWater%20Pollution_study_material.pdf?alt=media&token=89eb2c1f-fcc6-428b-9353-17d2e06ee89f";

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(pdfTitle);
//
//            pdfView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if(event.getAction() == MotionEvent.ACTION_DOWN){
//                        if(getSupportActionBar().isShowing()){
//                            getSupportActionBar().hide();
//                        }
//                        else {
//                            getSupportActionBar().show();
//                        }
//                    }
//                    return true;
//                }
//            });





// instantiate it within the onCreate method
            //circularProgressIndicator = new CircularProgressIndicator(NotesPdfViewerActivity.this);
            //circularProgressIndicator.setIndeterminate(true);
           // circularProgressIndicator.setVisibility(View.VISIBLE);


//            mProgressDialog = new ProgressDialog(NotesPdfViewerActivity.this);
//            mProgressDialog.setMessage("A message");
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mProgressDialog.setCancelable(true);

//// execute this when the downloader must be fired
//            final DownloadTask downloadTask = new DownloadTask(NotesPdfViewerActivity.this);
//            downloadTask.execute(pdfUrl);


             retrievePdfFromUrl = new RetrievePdfFromUrl();
            retrievePdfFromUrl.execute(pdfUrl);

//            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    retrievePdfFromUrl.cancel(true);
//                }
//            });

            //pdfView.fromUri(Uri.parse(pdfUrl)).load();
           // pdfView.fromAsset("environmental_pollution_study_material.pdf").load();
           // new RetrievePdfFromUrl().execute(pdfUrl);



        }
    }

//    private class DownloadTask extends AsyncTask<String, Integer, String>{
//
//        private Context context;
//        private PowerManager.WakeLock mWakeLock;
//        private InputStream input;
//
//        public DownloadTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//             input = null;
//
//            HttpURLConnection connection = null;
//            try {
//                URL url = new URL(strings[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                // expect HTTP 200 OK, so we don't mistakenly save error report
//                // instead of the file
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return "Server returned HTTP " + connection.getResponseCode()
//                            + " " + connection.getResponseMessage();
//                }
//
//                // this will be useful to display download percentage
//                // might be -1: server did not report the length
//                int fileLength = connection.getContentLength();
//
//                // download the file
//                input = connection.getInputStream();
//
//                byte data[] = new byte[4096];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    // allow canceling with back button
//                    if (isCancelled()) {
//                        input.close();
//                        return null;
//                    }
//                    total += count;
//                    // publishing the progress....
//                    if (fileLength > 0) // only if total length is known
//                        publishProgress((int) (total * 100 / fileLength));
//
//                }
//            } catch (Exception e) {
//                return e.toString();
//            } finally {
//                try {
//
//                    if (input != null)
//                        input.close();
//                } catch (IOException ignored) {
//                }
//
//                if (connection != null)
//                    connection.disconnect();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // take CPU lock to prevent CPU from going off if the user
//            // presses the power button during download
////            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
////            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
////                    getClass().getName());
////            mWakeLock.acquire();
//            mProgressDialog.show();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            super.onProgressUpdate(progress);
//            // if we get here, length is known, now set indeterminate to false
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setMax(100);
//            mProgressDialog.setProgress(progress[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//           // mWakeLock.release();
//            mProgressDialog.dismiss();
//            pdfView.fromStream(input).load();
////            if (result != null)
////                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
////            else
////                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
//        }
//    }


    class RetrievePdfFromUrl extends AsyncTask<String,Integer, InputStream>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressIndicator.show();
        }

        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;

            try {

                URL url = new URL(pdfUrl);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                if(httpsURLConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                }

            }catch (IOException e){
               // e.printStackTrace();
            }

            return inputStream;
        }


        @Override
        protected void onPostExecute(InputStream inputStream) {

            Toast.makeText(getApplicationContext(), "loaded", Toast.LENGTH_SHORT).show();

            ScrollHandle h = new DefaultScrollHandle(getApplicationContext());


            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    circularProgressIndicator.hide();
                }
            }).enableAnnotationRendering(true).defaultPage(0).scrollHandle(h).spacing(8).load();
            pdfView.setPositionOffset(pdfView.getOptimalPageHeight());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            retrievePdfFromUrl.cancel(true);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        retrievePdfFromUrl.cancel(true);
    }

}