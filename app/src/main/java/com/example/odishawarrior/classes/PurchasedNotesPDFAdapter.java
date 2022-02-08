package com.example.odishawarrior.classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odishawarrior.R;
import com.example.odishawarrior.activities.NotesPdfViewerActivity;

import java.util.ArrayList;
import java.util.List;

public class PurchasedNotesPDFAdapter extends RecyclerView.Adapter<PurchasedNotesPDFAdapter.ViewHolder> {

     List<String> list;
     List<String> urllist;

    public PurchasedNotesPDFAdapter(List<String> list, List<String> urllist) {
        this.list = list;
        this.urllist = urllist;
    }

    @NonNull
    @Override
    public PurchasedNotesPDFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchased_notes_pdfs_display_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedNotesPDFAdapter.ViewHolder holder, int position) {
        holder.setData(list.get(position), urllist.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pdfName;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            pdfName = itemView.findViewById(R.id.notesTitleTv);

        }

        private void setData(String pdfTitle, String url){
            pdfName.setText(pdfTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), NotesPdfViewerActivity.class);
                    intent.putExtra("pdf_title", pdfTitle);
                    intent.putExtra("pdf_url", url);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
