package com.example.odishawarrior.classes;

import static com.example.odishawarrior.activities.QuestionsDisplayActivity.allQuesDialogRV;
import static com.example.odishawarrior.activities.QuestionsDisplayActivity.allQuestionsDialog;
import static com.example.odishawarrior.activities.QuestionsDisplayActivity.currentQuestionNo;
import static com.example.odishawarrior.activities.QuestionsDisplayActivity.previouslySelectedQues;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.QuestionsDisplayActivity;

public class AllQuestionsAdapter extends RecyclerView.Adapter<AllQuestionsAdapter.ViewHolder> {

    private int noOfQuestions;

    public AllQuestionsAdapter(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    @NonNull
    @Override
    public AllQuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.allquestion_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllQuestionsAdapter.ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return noOfQuestions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView questionNoTv;
        private RecyclerView.ViewHolder vh;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            questionNoTv = itemView.findViewById(R.id.questionNoTVonDialog);
            //changeCurrentQuestionIndicator();

        }

        private void setData(){

            int a = getAdapterPosition() + 1;
            questionNoTv.setText("" + a);

           // changeCurrentQuestionIndicator();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    changeCurrentQuestionIndicator();
                    allQuestionsDialog.dismiss();
                }
            });

        }

        public void changeCurrentQuestionIndicator(){
             vh = allQuesDialogRV.findViewHolderForAdapterPosition(previouslySelectedQues-1);

            vh.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F8F4F4")));

            QuestionsDisplayActivity.currentQuestionNo = Integer.parseInt((String) questionNoTv.getText());
            previouslySelectedQues = currentQuestionNo;
            itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4BB543")));
        }
    }
}
