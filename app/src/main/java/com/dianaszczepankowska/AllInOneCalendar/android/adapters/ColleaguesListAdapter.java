package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.coworkers.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColleaguesListAdapter extends RecyclerView.Adapter<ColleaguesListAdapter.ColleaguesViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<User> usersList;

    public ColleaguesListAdapter(Context context) {
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);
            ColleaguesListAdapter.context = context;
        }
    }

    public static Context getContext() {
        return context;
    }


    public void setUsersList() {
        usersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            usersList.add(new User("", "Krzysztof Kowalski", "kk@gmail.com"));
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ColleaguesListAdapter.ColleaguesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.friend_item, parent, false);
        return new ColleaguesListAdapter.ColleaguesViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ColleaguesListAdapter.ColleaguesViewHolder holder, int position) {
        if (usersList == null) {
            return;
        }


        holder.profilePhoto.setImageResource(R.drawable.ic_launcher_foreground);
        holder.userName.setText(usersList.get(position).getUserName());
        holder.userEmail.setText(usersList.get(position).getUserEmail());
    }


    @Override
    public int getItemCount() {
        if (usersList == null) {
            return 0;
        } else {
            return usersList.size();
        }
    }

    static class ColleaguesViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePhoto;
        private TextView userName;
        private TextView userEmail;

        ColleaguesViewHolder(View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.usersPhoto);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);

        }
    }


}

