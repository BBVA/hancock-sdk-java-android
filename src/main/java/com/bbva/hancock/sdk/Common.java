package com.bbva.hancock.sdk;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.exception.HancockErrorEnum;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.exception.HancockTypeErrorEnum;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(Common.class);

    public static <T> T checkStatus(final Response response, final Class<T> tClass) throws HancockException {

        try (final ResponseBody responseBody = response.body()) {

            final Gson gson = new Gson();
            // HTTP status code between 200 and 299
            if (!response.isSuccessful()) {
                final HancockException resultAux = gson.fromJson(responseBody.string(), HancockException.class);
                LOGGER.error("Api error: {}", resultAux.getInternalError());
                throw new HancockException(HancockTypeErrorEnum.ERROR_API, resultAux.getInternalError(), resultAux.getError(), resultAux.getMessage(), resultAux.getExtendedMessage());
            }
            return gson.fromJson(responseBody.string(), tClass);

        } catch (final IOException error) {
            LOGGER.error("Gson error: ", error);
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50001", 500, HancockErrorEnum.ERROR_API.getMessage(), HancockErrorEnum.ERROR_API.getMessage(), error);

        }

    }

    public static String getResourceUrl(final HancockConfig config, final String encode) {
        return config.getAdapter().getHost() + ':' +
                config.getAdapter().getPort() +
                config.getAdapter().getBase() +
                config.getAdapter().getResources().get(encode);
    }

    public static Request getRequest(final String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    public static Request getRequest(final String url, final RequestBody body) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    public static Response makeCall(final Request request) throws HancockException {

        try {
            final OkHttpClient httpClient = new OkHttpClient();
            return httpClient.newCall(request).execute();
        } catch (final Exception error) {
            LOGGER.error("Hancock error: ", error);
            throw new HancockException(HancockTypeErrorEnum.ERROR_INTERNAL, "50001", 500, HancockErrorEnum.ERROR_API.getMessage(), HancockErrorEnum.ERROR_API.getMessage(), error);

        }

    }
}
