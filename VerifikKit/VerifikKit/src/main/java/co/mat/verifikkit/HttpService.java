package co.mat.verifikkit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facetec.sdk.FaceTecSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

class HttpService {
    protected void getCredentials(String token, String appID, CredentialsCallback callback){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(VerifikURL.Base + VerifikURL.Credentials).newBuilder();
        urlBuilder.addQueryParameter("type", "mobile");
        String url = urlBuilder.build().toString();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .get()
                .build();

        NetworkingHelpers.getApiClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

                String responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    String data = responseJSON.getString("data");
                    JSONArray dataArray = new JSONArray(data);
                    if(dataArray.length() < 2){
                        callback.configError("Credentials with wrong format, please contact Verifik Support Team");
                        return;
                    }

                    String prodKeyText = HttpUtils.parseProdKeyText(dataArray.getString(0));
                    String deviceKeyIdentifier = dataArray.getString(1);
                    String publicFaceScanEncryptionKey = dataArray.getString(2);

                    if(!prodKeyText.contains(appID)){
                        callback.configError("Bundle ID isn't registered, please contact Verifik Support Team");
                        return;
                    }
                    callback.finishConfig(prodKeyText,deviceKeyIdentifier,publicFaceScanEncryptionKey);
                }
                catch(JSONException e) {
                    // CASE:  Parsing the response into JSON failed --> You define your own API contracts with yourself and may choose to do something different here based on the error.  Solid server-side code should ensure you don't get to this case.
                    e.printStackTrace();
                    Log.d("Verifik", "Exception raised while attempting to parse JSON result.");
                    callback.configError("There was an error configuring Verifik SDK, please check your keys");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                // CASE:  Network Request itself is erroring --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                Log.d("Verifik", "Exception raised while attempting HTTPS call.");
                callback.configError("Credentials with wrong format, please contact Verifik Support Team");
            }
        });
    }

    protected void getSessionToken(String token, SessionCallback callback){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(VerifikURL.Base + VerifikURL.Session).newBuilder();
        String url = urlBuilder.build().toString();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token)
                .header("User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(""))
                .header("X-User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(""))
                .get()
                .build();

        NetworkingHelpers.getApiClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

                String responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    String sessionToken = responseJSON.getString("data");
                    callback.obtainedSession(sessionToken);
                }
                catch(JSONException e) {
                    // CASE:  Parsing the response into JSON failed --> You define your own API contracts with yourself and may choose to do something different here based on the error.  Solid server-side code should ensure you don't get to this case.
                    e.printStackTrace();
                    Log.d("Verifik", "Exception raised while attempting to parse JSON result.");
                    callback.sessionError("Exception raised while setting session data, please contact Verifik Support Team");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                // CASE:  Network Request itself is erroring --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                Log.d("Verifik", "Exception raised while attempting HTTPS call.");
                callback.sessionError("Exception raised while getting session data, please contact Verifik Support Team");
            }
        });
    }

    protected interface CredentialsCallback{
        void finishConfig(String prodKeyText, String deviceKeyIdentifier, String publicFaceScanEncryptionKey);
        void configError(String error);
    }

    protected interface SessionCallback{
        void obtainedSession(String sessionToken);
        void sessionError(String error);
    }
}
