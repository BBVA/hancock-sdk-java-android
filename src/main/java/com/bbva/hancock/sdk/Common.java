package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class Common {

    public static <T> T checkStatus(Response response, Class<T> tClass) throws HancockException {

        try (ResponseBody responseBody = response.body()) {

            Gson gson = new Gson();
            // HTTP status code between 200 and 299
            if (!response.isSuccessful()){
                HancockException resultAux = gson.fromJson(responseBody.string(), HancockException.class);
                System.out.println("Api error: " + resultAux.getInternalError());
                throw new HancockException(HancockTypeErrorEnum.ERROR_API, resultAux.getInternalError(), resultAux.getError(), resultAux.getMessage() , resultAux.getExtendedMessage());
            }
            return gson.fromJson(responseBody.string(), tClass);

        }
        catch (IOException error) {

            System.out.println("Gson error: " + error.toString());
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50001", 500, HancockErrorEnum.ERROR_API.getMessage() , HancockErrorEnum.ERROR_API.getMessage(), error);

        }

    }

    public static String getResourceUrl(HancockConfig config, String encode) {
        return config.getAdapter().getHost() + ':' +
                config.getAdapter().getPort() +
                config.getAdapter().getBase() +
                config.getAdapter().getResources().get(encode);
    }

    public static Request getRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    public static Request getRequest(String url, RequestBody body) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    public static Response makeCall(Request request) throws HancockException {

        try{
            OkHttpClient httpClient = new OkHttpClient();
            Response response = httpClient.newCall(request).execute();
            return response;
        } catch (Exception error) {

            System.out.println("Hancock error: " + error.toString());
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50001", 500, HancockErrorEnum.ERROR_API.getMessage() , HancockErrorEnum.ERROR_API.getMessage(), error);

        }
    }

}
