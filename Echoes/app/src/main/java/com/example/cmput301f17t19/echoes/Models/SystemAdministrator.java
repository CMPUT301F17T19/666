/*
 * Copyright (c) Team cmput301f17t19, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta
 */

package com.example.cmput301f17t19.echoes.Models;

/**
 * System Administrator's Profile, subclass of UserProfile
 *
 * @author Shan Lu
 * @version 1.0
 * @since 1.0
 */
public class SystemAdministrator extends UserProfile {

    // The maximum size that each photographic image can be stored (in bytes)
    private static int max_img_storage_bytes = 65536;

    /**
     * Constructor for System Administrator
     *
     * @param username: String, the username of the system administrator
     */
    public SystemAdministrator(String username) {
        super(username);
    }

    /**
     * System administrator can decide to change the maximum size of image stored
     *
     * @param maxImgStorageBytes: integer, the maximum size of image stored
     */
    public void setMax_img_storage_bytes(int maxImgStorageBytes) {
        this.max_img_storage_bytes = maxImgStorageBytes;
    }

    /**
     * Get the maximum size of image can be stored
     *
     * @return int: the the maximum size of image can be stored
     */
    public static int getMax_img_storage_bytes() {
        return max_img_storage_bytes;
    }
}
