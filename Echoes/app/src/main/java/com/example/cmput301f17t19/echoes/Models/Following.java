/*
 * Copyright (c) Team cmput301f17t19, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta
 */

package com.example.cmput301f17t19.echoes.Models;

import com.example.cmput301f17t19.echoes.Controllers.ElasticSearchController;

import java.util.concurrent.ExecutionException;

/**
 * Follower has attribute of unique username each user that followed by the logged-in user
 *
 * @author Peter Liang, Shan Lu
 * @version 1.0
 * @since 1.0
 */
public class Following extends Follow_Request {
    /**
     * Constructor for the Following object, set the username of the following
     *
     * @param Username: String, the username of the follower
     */
    public Following(String Username) {
        super(Username);
    }

    /**
     * Get the UserProfile of the user that followed by me from Online database
     *
     * @return followingUserProfile: UserProfile, the user profile of the following user
     */
    public UserProfile getUserProfileOnline() {
        // Read the UserProfile with username from online database
        ElasticSearchController.GetUserProfileTask getUserProfileTask = new ElasticSearchController.GetUserProfileTask();
        getUserProfileTask.execute(super.getUsername());

        try {
            UserProfile followingUserProfile = getUserProfileTask.get();

            return followingUserProfile;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
