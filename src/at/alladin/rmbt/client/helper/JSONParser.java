/*******************************************************************************
 * Copyright 2015 SPECURE GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package at.alladin.rmbt.client.helper;

import com.google.gson.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

import static at.alladin.rmbt.client.RMBTClientRunner.RMBT_CUSTOMER_HEADER;
import static at.alladin.rmbt.client.RMBTClientRunner.RMBT_CUSTOMER_VALUE_DEFAULT;

public class JSONParser {
    // Start filing Errors
    JSONArray errorList = null;

    // constructor
    public JSONParser() {
        // Start filing Errors
        errorList = new JSONArray();
    }

    /**
     * @param object
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    /**
     * @param array
     * @return
     * @throws JSONException
     */
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    /**
     * @param json
     * @return
     * @throws JSONException
     */
    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public JSONObject getURL(final URI uri) {
        JSONObject jObj = null;
        String responseBody;

        try {
//            final HttpParams params = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(params, 20000);
//            HttpConnectionParams.setSoTimeout(params, 20000);
//            final HttpClient client = new DefaultHttpClient(params);
            final CloseableHttpClient client = HttpClients.createDefault();
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000)
                    .setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000)
                    .build();

            final HttpGet httpget = new HttpGet(uri);
            httpget.setConfig(requestConfig);

            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = client.execute(httpget, responseHandler);

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(responseBody);
            } catch (final JSONException e) {
                writeErrorList("Error parsing JSON " + e.toString());
            }

        } catch (final UnsupportedEncodingException e) {
            writeErrorList("Wrong encoding");
            // e.printStackTrace();
        } catch (final HttpResponseException e) {
            writeErrorList("Server responded with Code " + e.getStatusCode() + " and message '" + e.getMessage() + "'");
        } catch (final ClientProtocolException e) {
            writeErrorList("Wrong Protocol");
            // e.printStackTrace();
        } catch (final IOException e) {
            writeErrorList("IO Exception");
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }

        // return JSONObject
        return jObj;
    }

    public JSONObject sendJSONToUrl(final String customer, final URI uri, final JSONObject data) {
        JSONObject jObj = null;
        String responseBody;

        try {
//            final HttpParams params = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(params, 20000);
//            HttpConnectionParams.setSoTimeout(params, 20000);
            final CloseableHttpClient client = HttpClients.createDefault();
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000)
                    .setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000)
                    .build();


            final HttpPost httppost = new HttpPost(uri);
            httppost.setConfig(requestConfig);

            final StringEntity se = new StringEntity(data.toString(), "UTF-8");

            httppost.setEntity(se);
            httppost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httppost.setHeader(new BasicHeader(RMBT_CUSTOMER_HEADER, customer != null && !customer.isEmpty() ? customer : RMBT_CUSTOMER_VALUE_DEFAULT));

            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = client.execute(httppost, responseHandler);

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(responseBody);
            } catch (final JSONException e) {
                writeErrorList("Error parsing JSON " + e.toString());
            }

        } catch (final UnsupportedEncodingException e) {
            writeErrorList("Wrong encoding");
            // e.printStackTrace();
        } catch (final HttpResponseException e) {
            writeErrorList("Server responded with Code " + e.getStatusCode() + " and message '" + e.getMessage() + "'");
        } catch (final ClientProtocolException e) {
            writeErrorList("Wrong Protocol");
            // e.printStackTrace();
        } catch (final ConnectTimeoutException e) {
            writeErrorList("ConnectionTimeoutException");
            //e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (final IOException e) {
            writeErrorList("IO Exception");
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }

        if (jObj == null)
            jObj = createErrorJSON();

        // return JSONObject
        return jObj;
    }

    public JsonObject sendJsonToUrl(final String customer, final URI uri, final JsonObject data) {
        JsonObject jObj = null;
        String responseBody;

        try {
//            final HttpParams params = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(params, 20000);
//            HttpConnectionParams.setSoTimeout(params, 20000);
//            final HttpClient client = new DefaultHttpClient(params);
            final CloseableHttpClient client = HttpClients.createDefault();
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000)
                    .setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000)
                    .build();

            final HttpPost httppost = new HttpPost(uri);
            httppost.setConfig(requestConfig);

            final StringEntity se = new StringEntity(data.toString(), "UTF-8");

            httppost.setEntity(se);
            httppost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            httppost.setHeader(new BasicHeader(RMBT_CUSTOMER_HEADER, customer != null && !customer.isEmpty() ? customer : RMBT_CUSTOMER_VALUE_DEFAULT));

            //System.out.println("Request to:",uri.toString() + "\n" + data.toString());

            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = client.execute(httppost, responseHandler);

            // try parse the string to a JSON object
            try {
                //Log.e("Response to:",uri.toString() + "\n" + responseBody.toString());
//                JsonObject json = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                JsonElement jelem = gson.fromJson(responseBody, JsonElement.class);
//                JsonObject jobj = jelem.getAsJsonObject();
                jObj = jelem.getAsJsonObject();
//                jObj = (JsonObject) gson.toJsonTree(responseBody);
//                jObj = new JsonObject(responseBody);
            } catch (final JsonParseException e) {
                writeErrorList("Error parsing JSON " + e.toString());
            }

        } catch (final UnsupportedEncodingException e) {
            writeErrorList("Wrong encoding");
            // e.printStackTrace();
        } catch (final HttpResponseException e) {
            writeErrorList("Server responded with Code " + e.getStatusCode() + " and message '" + e.getMessage() + "'");
        } catch (final ClientProtocolException e) {
            writeErrorList("Wrong Protocol");
            // e.printStackTrace();
        } catch (final ConnectTimeoutException e) {
            writeErrorList("ConnectionTimeoutException");
            //e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (final IOException e) {
            writeErrorList("IO Exception");
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }

        if (jObj == null)
            jObj = createErrorJson();

        // return JSONObject
        return jObj;
    }

    private void writeErrorList(final String errorText) {
        try {
            errorList.put(errorList.length(), errorText);
            System.out.println(errorText);
        } catch (final JSONException e) {
            System.out.println("Error writing ErrorList: " + e.toString());
        }
    }

    private JSONObject createErrorJSON() {
        final JSONObject errorAnswer = new JSONObject();
        try {
            errorAnswer.putOpt("error", errorList);
        } catch (final JSONException e) {
            System.out.println("Error saving ErrorList: " + e.toString());
        }
        return errorAnswer;
    }

    private JsonObject createErrorJson() {
        final JsonObject errorAnswer = new JsonObject();
        Gson gson = new Gson();
        try {
            errorAnswer.add("error", gson.toJsonTree(errorList));
        } catch (final JsonParseException e) {
            System.out.println("Error saving ErrorList: " + e.toString());
        }
        return errorAnswer;
    }
}
