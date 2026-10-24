package com.corex.config;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {

    public static Cloudinary cloudinary;
    
    public static Cloudinary getCloudinary(){

        if(cloudinary == null){

            Map<String, Object>config = new HashMap<>();

            config.put("cloud_name", "andbslb0");
            config.put("api_key", "926883718692634");
            config.put("api_secret", "qrHsv8xcB5ZjMtGvlbkMc7ke5sY");
            config.put("secure", true);

            cloudinary = new Cloudinary(config);
        }
        return cloudinary;
    }
}
