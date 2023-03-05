package com.example.solutionchallengeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.solutionchallengeapp.Models.EventModel;
import com.example.solutionchallengeapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<EventModel> eventsList;
    private Context mContext;

    public EventsAdapter(List<EventModel> eventsList, Context mContext) {
        this.eventsList = eventsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mContext).load(eventsList.get(position).getEventPicture()).apply(RequestOptions.centerCropTransform()).into(holder.eventPicture);
        if(eventsList.get(position).getOrganisationPicture()!=null){
            Glide.with(mContext).load(eventsList.get(position).getOrganisationPicture()).apply(RequestOptions.centerCropTransform()).into(holder.organisationPicture);
        }
        holder.eventTitle.setText(eventsList.get(position).getTitle());
        holder.eventDate.setText(eventsList.get(position).getDate());
        holder.organisationName.setText(eventsList.get(position).getOrganisationName());
    }

    @Override
    public int getItemCount() { return eventsList.size(); }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView eventPicture;
        ShapeableImageView organisationPicture;
        TextView eventTitle, eventDate, organisationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventPicture = itemView.findViewById(R.id.eventPicture);
            organisationPicture = itemView.findViewById(R.id.eventOrganisationPicture);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDate = itemView.findViewById(R.id.eventDate);
            organisationName = itemView.findViewById(R.id.eventOrganisationName);

        }
    }
}
