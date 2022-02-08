package com.example.odishawarrior.fragments;

import static com.example.odishawarrior.classes.UserDetailsVariables.TYPE_NOTIFICATION;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.odishawarrior.MainActivity;
import com.example.odishawarrior.R;
import com.example.odishawarrior.classes.NotificationAdapter;
import com.example.odishawarrior.classes.NotificationModelList;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        MainActivity.currently_present_on_fragment = TYPE_NOTIFICATION;


        recyclerView = view.findViewById(R.id.notification_recycler);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<NotificationModelList> list = new ArrayList<>();

        list.add(new NotificationModelList("Grapes", 40));
        list.add(new NotificationModelList("Apples", 50));
        list.add(new NotificationModelList("Mango", 30));
        list.add(new NotificationModelList("Banana", 60));
        list.add(new NotificationModelList("Watermelon", 20));
        list.add(new NotificationModelList("Orange", 40));



       LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new NotificationAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}