package com.bbva.hancock.sdk.services;

import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.exception.HancockException;
import com.bbva.hancock.sdk.models.protocol.*;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.math.BigInteger;

import static com.bbva.hancock.sdk.Common.*;

public class ProtocolService {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final HancockConfig config;

    public ProtocolService(final HancockConfig config) {
        this.config = config;
    }

    /**
     * Decode content (a static predefined transaction for example) encoded Hancock's protocol
     *
     * @param code The encoded content
     * @return The encoded content successfully decoded
     * @throws HancockException
     */
    public HancockProtocolDecodeResponse decodeProtocol(final String code) throws HancockException {

        final String url = getResourceUrl(config, "decode");

        final Gson gson = new Gson();
        final HancockProtocolDecodeRequest hancockRequest = new HancockProtocolDecodeRequest(code);
        final String json = gson.toJson(hancockRequest);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final HancockProtocolDecodeResponse responseModel = checkStatus(response, HancockProtocolDecodeResponse.class);

        return responseModel;
    }

    /**
     * @param action The operation to perform
     * @param value  The amount of coins to use
     * @param to     The destination address of the operation
     * @param data   Data which will also encoded
     * @param dlt    The DLT where the tx will be sent
     * @return The operation to do successfully encoded
     * @throws HancockException
     */
    public HancockProtocolEncodeResponse encodeProtocol(final HancockProtocolAction action, final BigInteger value, final String to, final String data, final HancockProtocolDlt dlt) throws HancockException {

        final String url = getResourceUrl(config, "encode");

        final Gson gson = new Gson();
        final HancockProtocolEncodeRequest hancockRequest = new HancockProtocolEncodeRequest(action, value, to, data, dlt);
        final String json = gson.toJson(hancockRequest);
        final RequestBody body = RequestBody.create(CONTENT_TYPE_JSON, json);

        final Request request = getRequest(url, body);

        final Response response = makeCall(request);
        final HancockProtocolEncodeResponse responseModel = checkStatus(response, HancockProtocolEncodeResponse.class);

        return responseModel;
    }

}
