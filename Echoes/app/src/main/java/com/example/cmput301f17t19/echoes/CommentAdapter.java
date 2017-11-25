/*
 * Copyright (c) Team cmput301f17t19, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta
 */

package com.example.cmput301f17t19.echoes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shanlu on 2017-11-25.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    public CommentAdapter() {

    }

    /**
     * This function creates a view holder for each comment item displayed in comments list
     *
     * @param parent: The parent which contains the ViewHolder
     * @param viewType: The view type of this view holder
     * @return CommentAdapter.CommentViewHolder
     */
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);

        return commentViewHolder;
    }

    /**
     * Bind the CommentViewHolder with the comment object at the specific position
     *
     * @param holder: CommentViewHolder for the comment object at the position in the List
     * @param position: The position of the comment object in the List to be bound with the viewHolder
     */
    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        // Return the size of comments list of the given UserHabitKudosComments
        if (CommentsActivity.getmUserHabitKudosComments() != null) {
            return Math.min(CommentsActivity.getmUserHabitKudosComments().getComments_usernames().size(),
                    CommentsActivity.getmUserHabitKudosComments().getComments_contents().size());
        } else {
            return 0;
        }
    }

    /**
     * View Holder for each Comment object displayed in CommentsActivity
     */
    class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commentUsername;
        private TextView commentContent;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentUsername = (TextView) itemView.findViewById(R.id.comment_username);
            commentContent = (TextView) itemView.findViewById(R.id.comment_content);
        }

        /**
         * Bind the CommentViewHolder with the comment object at the specific position
         *
         * @param position: The position of the comment object in the List to be bound with the viewHolder
         */
        private void bind(int position) {
            UserHabitKudosComments userHabitKudosComments = CommentsActivity.getmUserHabitKudosComments();

            if (userHabitKudosComments != null) {
                commentUsername.setText(userHabitKudosComments.getComments_usernames().get(position));
                commentContent.setText(userHabitKudosComments.getComments_contents().get(position));
            }
        }
    }
}
