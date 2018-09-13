package com.bbva.hancock.sdk.dlt.ethereum.clients;

import com.bbva.hancock.sdk.HancockClient;
import com.bbva.hancock.sdk.config.HancockConfig;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolAction;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDecodeRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDecodeResponse;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolDlt;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolEncodeRequest;
import com.bbva.hancock.sdk.dlt.ethereum.models.protocol.HancockProtocolEncodeResponse;
import com.bbva.hancock.sdk.exception.HancockException;
import com.google.gson.Gson;

import java.math.BigInteger;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bbva.hancock.sdk.Common.checkStatus;
import static com.bbva.hancock.sdk.Common.getRequest;
import static com.bbva.hancock.sdk.Common.getResourceUrl;
import static com.bbva.hancock.sdk.Common.makeCall;

public class HancockEthereumProtocolClient extends HancockClient {

    public HancockEthereumProtocolClient() {
    }

    public HancockEthereumProtocolClient(HancockConfig config) throws Exception {
        super(config);
    }

    public HancockProtocolDecodeResponse decodeProtocol(String code) throws HancockException {

        String url = getResourceUrl(getConfig(),"decode");

        Gson gson = new Gson();
        HancockProtocolDecodeRequest hancockRequest = new HancockProtocolDecodeRequest(code);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(getContentType(), json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        HancockProtocolDecodeResponse responseModel = checkStatus(response, HancockProtocolDecodeResponse.class);

        return responseModel;
    }

    public HancockProtocolEncodeResponse encodeProtocol(HancockProtocolAction action, BigInteger value, String to, String data, HancockProtocolDlt dlt) throws HancockException {

        String url = getResourceUrl(getConfig(),"encode");

        Gson gson = new Gson();
        HancockProtocolEncodeRequest hancockRequest = new HancockProtocolEncodeRequest(action, value, to, data, dlt);
        String json = gson.toJson(hancockRequest);
        RequestBody body = RequestBody.create(getContentType(), json);

        Request request = getRequest(url, body);

        Response response = makeCall(request);
        HancockProtocolEncodeResponse responseModel = checkStatus(response, HancockProtocolEncodeResponse.class);

        return responseModel;
    }

}
