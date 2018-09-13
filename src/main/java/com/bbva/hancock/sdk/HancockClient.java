package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;

import okhttp3.MediaType;

public class HancockClient {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private HancockConfig config;

    public HancockClient() {

        this.config = new HancockConfig
                .Builder()
                .build();

    }

    public HancockClient(HancockConfig config) throws Exception {

        this.config = config;
    }

    public HancockConfig getConfig(){

        return config;

    }

    public MediaType getContentType(){

        return CONTENT_TYPE_JSON;

    }

}
