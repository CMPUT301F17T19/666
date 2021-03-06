/*
 * Copyright (c) Team cmput301f17t19, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta
 */

package com.example.cmput301f17t19.echoes.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmput301f17t19.echoes.Activities.HabitEventDetailActivity;
import com.example.cmput301f17t19.echoes.Activities.HabitHistoryActivity;
import com.example.cmput301f17t19.echoes.Models.HabitEvent;
import com.example.cmput301f17t19.echoes.R;

import java.text.SimpleDateFormat;

/**
 * Habit Event Overview Recycler View Adapter
 *
 * @author Shan Lu
 * @version 1.0
 * @since 1.0
 */

public class HabitEventOverviewAdapter extends RecyclerView.Adapter<HabitEventOverviewAdapter.HabitEventOverviewViewHolder> {
    public static final String SELECTED_HABIT_EVENT_POSITION = "SELECTED_HABIT_EVENT_POSITION";

    private Context mContext;

    // The username of the user having this habit event
    private String mUserName;

    /**
     * Constructor for HabitEventOverview Adapter
     */
    public HabitEventOverviewAdapter(Context context, String userName) {
        mContext = context;
        mUserName = userName;
    }

    /**
     * This function creates a view holder for each HabitEventOverview item displayed in Habit History
     *
     * @param parent: The parent which contains the ViewHolder
     * @param viewType: The view type of this view holder
     * @return HabitEventOverviewAdapter.HabitEventOverviewViewHolder
     */
    @Override
    public HabitEventOverviewAdapter.HabitEventOverviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_habitevent_overview, parent, false);
        HabitEventOverviewViewHolder habitEventOverviewViewHolder = new HabitEventOverviewViewHolder(view);

        return habitEventOverviewViewHolder;
    }

    /**
     * Bind the HabitEventViewHolder with the HabitEvent object at the specific position
     *
     * @param holder: HabitEventOverviewViewHolder for the HabitEvent object at the position in the List
     * @param position: The position of the HabitEvent object in the List to be bound with the viewHolder
     */
    @Override
    public void onBindViewHolder(HabitEventOverviewAdapter.HabitEventOverviewViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * Return the total number of items in the list
     *
     * @return int: the total number of items in the list
     */
    @Override
    public int getItemCount() {
        return HabitHistoryActivity.getDisplayedHabitEventList().getHabitEvents().size();
    }

    /**
     * View Holder for each HabitEvent object displayed in HabitHistory or Following Habit Events
     */
    class HabitEventOverviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView habitEventHabitTypeTextView;
        private ImageView habitEventImgView;
        private TextView habitEventCommentTextView;
        private TextView habitEventDateTextView;

        /**
         * Constructor for HabitEventOverviewViewHolder
         *
         * @param itemView
         */
        public HabitEventOverviewViewHolder(View itemView) {
            super(itemView);

            habitEventHabitTypeTextView = (TextView) itemView.findViewById(R.id.habitevent_type_textView);
            habitEventImgView = (ImageView) itemView.findViewById(R.id.habitevent_photo);
            habitEventCommentTextView = (TextView) itemView.findViewById(R.id.habitevent_comment);
            habitEventDateTextView = (TextView) itemView.findViewById(R.id.habitevent_date);

            itemView.setOnClickListener(this);
        }

        /**
         * Bind the HabitEventViewHolder with the HabitEvent object at the specific position
         *
         * @param position: The position of the HabitEvent object in the List to be bound with the viewHolder
         */
        private void bind(int position) {

            HabitEvent habitEvent_pos = HabitHistoryActivity.getDisplayedHabitEventList().getHabitEvents().get(position);

            // Set habit type
            habitEventHabitTypeTextView.setText(habitEvent_pos.getTitle());
            // Set the comment and date
            habitEventCommentTextView.setText(habitEvent_pos.getComments());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            habitEventDateTextView.setText(simpleDateFormat.format(habitEvent_pos.getStartDate()));

            // Set image
            if (habitEvent_pos.getEventPhoto() != null) {
                habitEventImgView.setVisibility(View.VISIBLE);
                habitEventImgView.setImageBitmap(BitmapFactory.decodeByteArray(habitEvent_pos.getEventPhoto(), 0, habitEvent_pos.getEventPhoto().length));
            } else {
                habitEventImgView.setVisibility(View.GONE);
            }
        }

        /**
         * Send the intent to open the HabitEventDetail Activity of  the selected HabitEvent
         *
         * @param view: View, the view of HabitEvent clicked
         */
        @Override
        public void onClick(View view) {

            // The position of the HabitEvent in the list that the user clicks
            int adapterPosition = getAdapterPosition();

            // The selected HabitEvent object
            HabitEvent selected_HabitEvent = HabitHistoryActivity.getDisplayedHabitEventList().getHabitEvents().get(adapterPosition);

            // Start HabitEvent Detail Activity
            // Show the details of the selected HabitEvent object in HabitEvent Detail Screen
            Intent habitEventDetail_Intent = new Intent(mContext, HabitEventDetailActivity.class);
            // Pass the username of the user having this Habit Event to the Habit Event Detail Activity
            habitEventDetail_Intent.putExtra(HabitEventDetailActivity.UserNameHE_TAG, mUserName);
            // Pass the position of the selected HabitEvent
            habitEventDetail_Intent.putExtra(SELECTED_HABIT_EVENT_POSITION, adapterPosition);

            mContext.startActivity(habitEventDetail_Intent);
        }
    }
}
