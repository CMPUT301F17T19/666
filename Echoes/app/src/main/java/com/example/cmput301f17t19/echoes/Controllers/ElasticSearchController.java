/*
 * Copyright (c) Team cmput301f17t19, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta
 */

package com.example.cmput301f17t19.echoes.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.cmput301f17t19.echoes.Models.UserFollowingList;
import com.example.cmput301f17t19.echoes.Models.UserHabitKudosComments;
import com.example.cmput301f17t19.echoes.Models.UserProfile;
import com.example.cmput301f17t19.echoes.Models.UserReceivedRequestsList;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Elastic Search Controller
 *
 * Reference: https://github.com/searchbox-io/Jest/tree/master/jest
 *
 * @author Shan Lu
 * @version 1.0
 * @since 1.0
 */
public class ElasticSearchController {
    private static JestDroidClient client;

    private static final String SEARCH_INDEX = "cmput301f17t19";
    private static final String SEARCH_TYPE = "userprofile";

    private static final String SEARCH_TYPE_REQUESTS = "userreceivedrequests";
    private static final String SEARCH_TYPE_FOLLOWINGS = "userfollowings";

    private static final String SEARCH_TYPE_KUDOS_COMMENTS = "userkudoscomments";

    /**
     *  Add new user profile to online database
     *
     *  @params UserProfile: The UserProfile object of this user
     *  @progress Void
     *  @return Void
     */
    public static class AddNewUserProfileTask extends AsyncTask<UserProfile, Void, Void> {

        @Override
        protected Void doInBackground(UserProfile... userProfiles) {
            verifySettings();

            UserProfile userProfile = userProfiles[0];

            // Set the unique ID for the document of this userProfile to be the uniqueUsername of this user
            String doc_ID = userProfile.getUserName();

            // Add this new user to online database
            Index index = new Index.Builder(userProfile)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE)
                    .id(doc_ID)
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if(execute.isSucceeded()){
                    Log.d("addUser", "Add userProfile Success");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to build and send the tweets");
            }

            return null;
        }
    }

    /**
     * Check if the username has already been registered
     *
     * @params String: The username of this user, id of the UserProfile doc in elasticsearch
     * @progress Void
     * @return Boolean: true: The input username has already exist in online database
     *                  false: The input username does not exist
     */
    public static class CheckUserProfileExistTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            verifySettings();

            // input UserName
            String userName_IN = strings[0];

            // Check if UserProfile with id equals userName_IN exists
            Get get = new Get.Builder(SEARCH_INDEX, userName_IN)
                    .type(SEARCH_TYPE)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    Log.d("checkUser", "Check userProfile UserName Unique Success");
                    return true;
                } else {
                    Log.d("checkUser", "Check userProfile UserName Unique Fail");
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();

                return null;
            }
        }
    }

    /**
     * Get UserProfile of the given user
     *
     * @params String: The username of this user, id of the UserProfile doc in elasticsearch
     * @progress Void
     * @return UserProfile: The UserProfile with the input username
     */
    public static class GetUserProfileTask extends AsyncTask<String, Void, UserProfile> {

        @Override
        protected UserProfile doInBackground(String... strings) {
            verifySettings();

            // input UserName
            String userName_IN = strings[0];

            // Get the Document with ID equals the input username
            Get get = new Get.Builder(SEARCH_INDEX, userName_IN)
                    .type(SEARCH_TYPE)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    UserProfile userProfile = result.getSourceAsObject(UserProfile.class);

                    return userProfile;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Update the UserProfile
     *
     * @params UserProfile: The UserProfile object of this user
     * @progress Void
     * @return Void
     */
    public static class UpdateUserProfileTask extends AsyncTask<UserProfile, Void, Void> {

        @Override
        protected Void doInBackground(UserProfile... userProfiles) {
            verifySettings();

            UserProfile userProfile = userProfiles[0];

            // Update this userProfile to online database
            Index index = new Index.Builder(userProfile)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE)
                    .id(userProfile.getUserName())
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if (execute.isSucceeded()) {
                    Log.d("update", "Update User Habits Success");
                } else {
                    Log.d("update", "Update User Habits Fail");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Get a list of user whose username matches with given string
     *
     * @params String: The username searched
     * @progress Void
     * @return UserProfile: The UserProfile of the searched user
     */
    public static class SearchUsernameTask extends AsyncTask<String, Void, UserProfile> {

        @Override
        protected UserProfile doInBackground(String... strings) {
            verifySettings();

            String searchedWord = strings[0];

            // search user whose _id(username) matches the searched word (Exact match so far)
            String query = "{\n" +
                    "    \"query\" : {\n" +
                    "        \"term\" : { \"_id\" : \"" + searchedWord + "\" }\n" +
                    "    }\n" +
                    "}";

            Search search = new Search.Builder(query)
                    .addIndex(SEARCH_INDEX)
                    .addType(SEARCH_TYPE)
                    .build();

            try {
                // get the results of the query
                SearchResult result = client.execute(search);
                if(result.isSucceeded()){
                    UserProfile searchedUserProfile = result.getSourceAsObject(UserProfile.class);

                    return searchedUserProfile;

                } else {
                    Log.i("Error", "the search query failed to find any username that matched");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return null;
        }
    }

    /**
     *  Add new user's Received Request list to online database
     *
     *  @params UserReceivedRequestsList: The Received Requests list object of this user
     *  @progress Void
     *  @return Void
     */
    public static class AddNewUserReceivedRequestsTask extends AsyncTask<UserReceivedRequestsList, Void, Void> {

        @Override
        protected Void doInBackground(UserReceivedRequestsList... userReceivedRequestsLists) {
            verifySettings();

            UserReceivedRequestsList userReceivedRequestsList = userReceivedRequestsLists[0];

            // Set the unique ID for the document of this UserReceivedRequestList to be the uniqueUsername of this user
            String doc_ID = userReceivedRequestsList.getUserName();

            // Add this new user to online database
            Index index = new Index.Builder(userReceivedRequestsList)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE_REQUESTS)
                    .id(doc_ID)
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if(execute.isSucceeded()){
                    Log.d("addUser", "Add user received request Success");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to build and send the tweets");
            }

            return null;
        }
    }

    /**
     * Check if the received requests of the username has already exist
     *
     * @params String: The username of this user, id of the UserReceivedRequest doc in elasticsearch
     * @progress Void
     * @return Boolean: true: The input username's requests list has already exist in online database
     *                  false: The input username's requests list does not exist
     */
    public static class CheckUserReceivedRequestsExistTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            verifySettings();

            // input UserName
            String userName_IN = strings[0];

            // Check if UserProfile with id equals userName_IN exists
            Get get = new Get.Builder(SEARCH_INDEX, userName_IN)
                    .type(SEARCH_TYPE_REQUESTS)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    Log.d("checkUser", "Check userProfile UserName Unique Success");
                    return true;
                } else {
                    Log.d("checkUser", "Check userProfile UserName Unique Fail");
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    /**
     * Get UserReceivedRequestList of the given user
     *
     * @params String: The username of this user, id of the UserReceivedRequest doc in elasticsearch
     * @progress Void
     * @return UserReceivedRequestsList: The UserReceivedRequestsList with the input username
     */
    public static class GetUserReceivedRequestsTask extends AsyncTask<String, Void, UserReceivedRequestsList> {

        @Override
        protected UserReceivedRequestsList doInBackground(String... strings) {
            verifySettings();

            // input UserName
            String userName_IN = strings[0];

            // Get the Document with ID equals the input username
            Get get = new Get.Builder(SEARCH_INDEX, userName_IN)
                    .type(SEARCH_TYPE_REQUESTS)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    UserReceivedRequestsList userReceivedRequestsList = result.getSourceAsObject(UserReceivedRequestsList.class);

                    return userReceivedRequestsList;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Update the UserReceivedRequestsList
     *
     * @params UserReceivedRequestsList: The UserReceivedRequestsList object of this user
     * @progress Void
     * @return Void
     */
    public static class UpdateUserReceivedRequestsListTask extends AsyncTask<UserReceivedRequestsList, Void, Void> {

        @Override
        protected Void doInBackground(UserReceivedRequestsList... userReceivedRequestsLists) {
            verifySettings();

            UserReceivedRequestsList userReceivedRequestsList = userReceivedRequestsLists[0];

            // Update this userProfile to online database
            Index index = new Index.Builder(userReceivedRequestsList)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE_REQUESTS)
                    .id(userReceivedRequestsList.getUserName())
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if (execute.isSucceeded()) {
                    Log.d("update", "Update User Habits Success");
                } else {
                    Log.d("update", "Update User Habits Fail");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     *  Add new user's Following list to online database
     *
     *  @params UserFollowingList: The followings list object of this user
     *  @progress Void
     *  @return Void
     */
    public static class AddNewUserFollowingsTask extends AsyncTask<UserFollowingList, Void, Void> {

        @Override
        protected Void doInBackground(UserFollowingList... userFollowingLists) {
            verifySettings();

            UserFollowingList userFollowingList = userFollowingLists[0];

            // Set the unique ID for the document of this UserFollowingList to be the uniqueUsername of this user
            String doc_ID = userFollowingList.getUserName();

            // Add this new user to online database
            Index index = new Index.Builder(userFollowingList)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE_FOLLOWINGS)
                    .id(doc_ID)
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if(execute.isSucceeded()){
                    Log.d("addUser", "Add user following list Success");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to build and send the tweets");
            }

            return null;
        }
    }

    /**
     * Check if the followings list of the username has already exist
     *
     * @params String: The username of this user, id of the UserFollowings doc in elasticsearch
     * @progress Void
     * @return Boolean: true: The input username's following list has already exist in online database
     *                  false: The input username's following list does not exist
     */
    public static class CheckUserFollowingsExistTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            verifySettings();

            // input UserName
            String userName_IN = strings[0];

            // Check if UserProfile with id equals userName_IN exists
            Get get = new Get.Builder(SEARCH_INDEX, userName_IN)
                    .type(SEARCH_TYPE_FOLLOWINGS)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    Log.d("checkUser", "Check userFollowing UserName Unique Success");
                    return true;
                } else {
                    Log.d("checkUser", "Check userFollowing UserName Unique Fail");
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    /**
     * Get UserFollowingList of the given user
     *
     * @params String: The username of this user, id of the UserFollowingList doc in elasticsearch
     * @progress Void
     * @return UserFollowingList: The UserFollowingList with the input username
     */
    public static class GetUserFollowingListTask extends AsyncTask<String, Void, UserFollowingList> {

        @Override
        protected UserFollowingList doInBackground(String... strings) {
            verifySettings();

            // input UserName
            String userName_IN = strings[0];

            // Get the Document with ID equals the input username
            Get get = new Get.Builder(SEARCH_INDEX, userName_IN)
                    .type(SEARCH_TYPE_FOLLOWINGS)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    UserFollowingList userFollowingList = result.getSourceAsObject(UserFollowingList.class);

                    return userFollowingList;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Update the UserFollowingList
     *
     * @params UserFollowingList: The UserFollowingList object of this user
     * @progress Void
     * @return Void
     */
    public static class UpdateUserFollowingListTask extends AsyncTask<UserFollowingList, Void, Void> {

        @Override
        protected Void doInBackground(UserFollowingList... userFollowingLists) {
            verifySettings();

            UserFollowingList userFollowingList = userFollowingLists[0];

            // Update this userProfile to online database
            Index index = new Index.Builder(userFollowingList)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE_FOLLOWINGS)
                    .id(userFollowingList.getUserName())
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if (execute.isSucceeded()) {
                    Log.d("update", "Update User Habits Success");
                } else {
                    Log.d("update", "Update User Habits Fail");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     *  Add new user's UserHabitKudosComments item to online database
     *
     *  @params UserHabitKudosComments: The UserHabitKudosComments object of this user
     *  @progress Void
     *  @return Void
     */
    public static class AddNewUserHabitKudosCommentsTask extends AsyncTask<UserHabitKudosComments, Void, Void> {

        @Override
        protected Void doInBackground(UserHabitKudosComments... userHabitKudosCommentses) {
            verifySettings();

            UserHabitKudosComments userHabitKudosComments = userHabitKudosCommentses[0];

            // Set the unique ID for the document of this UserHabitKudosComments
            String doc_ID = userHabitKudosComments.getElasticSearchID();

            // Add this new user to online database
            Index index = new Index.Builder(userHabitKudosComments)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE_KUDOS_COMMENTS)
                    .id(doc_ID)
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if(execute.isSucceeded()){
                    Log.d("addUser", "Add user kudos comments Success");
                }
            }
            catch (Exception e) {
                Log.i("Error", "The application failed to build and send the tweets");
            }

            return null;
        }
    }

    /**
     * Check if the UserHabitKudosComments of the id has already exist
     *
     * @params String: the following username + the following habit title
     * @progress Void
     * @return Boolean: true: The id has already exist in online database
     *                  false: id does not exist
     *                  null: search failed
     */
    public static class CheckUserHabitKudosCommentsExistTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            verifySettings();

            // input search id
            String searchID = strings[0];

            // Check if UserProfile with id equals userName_IN exists
            Get get = new Get.Builder(SEARCH_INDEX, searchID)
                    .type(SEARCH_TYPE_KUDOS_COMMENTS)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    Log.d("checkUser", "Check userFollowing UserName Unique Success");
                    return true;
                } else {
                    Log.d("checkUser", "Check userFollowing UserName Unique Fail");
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Get UserHabitKudosComments of the given user
     *
     * @params String: the id of the UserHabitKudosComments doc in elasticsearch
     * @progress Void
     * @return UserHabitKudosComments: The UserHabitKudosComments with the id
     */
    public static class GetUserHabitKudosCommentsTask extends AsyncTask<String, Void, UserHabitKudosComments> {

        @Override
        protected UserHabitKudosComments doInBackground(String... strings) {
            verifySettings();

            // input search id
            String searchID = strings[0];

            // Get the Document with ID equals the input username
            Get get = new Get.Builder(SEARCH_INDEX, searchID)
                    .type(SEARCH_TYPE_KUDOS_COMMENTS)
                    .build();

            try {
                JestResult result = client.execute(get);

                if (result.isSucceeded()) {
                    UserHabitKudosComments userHabitKudosComments = result.getSourceAsObject(UserHabitKudosComments.class);

                    return userHabitKudosComments;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Update the UserHabitKudosComments
     *
     * @params UserHabitKudosComments: The UserHabitKudosComments object of this user
     * @progress Void
     * @return Void
     */
    public static class UpdateUserHabitKudosCommentsTask extends AsyncTask<UserHabitKudosComments, Void, Void> {

        @Override
        protected Void doInBackground(UserHabitKudosComments... userHabitKudosCommentses) {
            verifySettings();

            UserHabitKudosComments userHabitKudosComments= userHabitKudosCommentses[0];

            // Update this userProfile to online database
            Index index = new Index.Builder(userHabitKudosComments)
                    .index(SEARCH_INDEX)
                    .type(SEARCH_TYPE_KUDOS_COMMENTS)
                    .id(userHabitKudosComments.getElasticSearchID())
                    .build();

            try {
                DocumentResult execute = client.execute(index);

                if (execute.isSucceeded()) {
                    Log.d("update", "Update User Habits Success");
                } else {
                    Log.d("update", "Update User Habits Fail");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Method from lonelyTwitter class
     * Reference: https://github.com/AmyShanLu/lonelyTwitter/blob/elasticsearch/app/src/main/java/ca/ualberta/cs/lonelytwitter/ElasticsearchTweetController.java
     */
    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    /**
     * Sync online User Profile data with offline file
     *
     * @param offlineUserProfile: UserProfile, the user profile of the user read from the offline file
     */
    public static void syncOnlineWithOffline(UserProfile offlineUserProfile) {
        // Sync the User Profile from the offline file
        ElasticSearchController.UpdateUserProfileTask updateUserProfileTask = new UpdateUserProfileTask();
        updateUserProfileTask.execute(offlineUserProfile);
    }
}
