//
// Welcome to the annotated FaceTec Device SDK core code for performing secure Enrollment!
//
package co.mat.verifikkit;

// This is an example self-contained class to perform Enrollment with the FaceTec SDK.
// You may choose to further componentize parts of this in your own Apps based on your specific requirements.

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facetec.sdk.FaceTecCustomization;
import com.facetec.sdk.FaceTecFaceScanProcessor;
import com.facetec.sdk.FaceTecFaceScanResultCallback;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecSessionActivity;
import com.facetec.sdk.FaceTecSessionResult;
import com.facetec.sdk.FaceTecSessionStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

// Android Note 1:  Some commented "Parts" below are out of order so that they can match iOS and Browser source for this same file on those platforms.
// Android Note 2:  Android does not have a onFaceTecSDKCompletelyDone function that you must implement like "Part 10" of iOS and Android Samples.  Instead, onActivityResult is used as the place in code you get control back from the FaceTec SDK.
public class EnrollmentProcessor extends Processor implements FaceTecFaceScanProcessor {
    private boolean success = false;
    private Context context;
    private String verifikToken;
    private String externalDatabaseRefID;
    private VerifikCallback verifikCallback;

    public EnrollmentProcessor(Context context, String sessionToken,
                               String verifikToken, String externalDatabaseRefID,
                               VerifikCallback verifikCallback) {
        this.context = context;
        this.verifikToken = verifikToken;
        this.externalDatabaseRefID = externalDatabaseRefID;
        this.verifikCallback = verifikCallback;
        //
        // Part 1:  Starting the FaceTec Session
        //
        // Required parameters:
        // - Context:  Unique for Android, a Context is passed in, which is required for the final onActivityResult function after the FaceTec SDK is done.
        // - FaceTecFaceScanProcessor:  A class that implements FaceTecFaceScanProcessor, which handles the FaceScan when the User completes a Session.  In this example, "self" implements the class.
        // - sessionToken:  A valid Session Token you just created by calling your API to get a Session Token from the Server SDK.
        //
        FaceTecSessionActivity.createAndLaunchSession(context, EnrollmentProcessor.this, sessionToken);
    }

    //
    // Part 2:  Handling the Result of a FaceScan
    //
    public void processSessionWhileFaceTecSDKWaits(final FaceTecSessionResult sessionResult, final FaceTecFaceScanResultCallback faceScanResultCallback) {
        //
        // DEVELOPER NOTE:  These properties are for demonstration purposes only so the Sample App can get information about what is happening in the processor.
        // In the code in your own App, you can pass around signals, flags, intermediates, and results however you would like.
        //

        //
        // Part 3:  Handles early exit scenarios where there is no FaceScan to handle -- i.e. User Cancellation, Timeouts, etc.
        //
        if(sessionResult.getStatus() != FaceTecSessionStatus.SESSION_COMPLETED_SUCCESSFULLY) {
            verifikCallback.enrollmentError("User cancel enrollment or there was a connection error");
            NetworkingHelpers.cancelPendingRequests();
            faceScanResultCallback.cancel();
            return;
        }

        //
        // Part 4:  Get essential data off the FaceTecSessionResult
        //
        JSONObject parameters = new JSONObject();
        try {
            String lowQualityAuditTrailImage = sessionResult.getLowQualityAuditTrailCompressedBase64()[0].replace("\n","");
            parameters.put("faceScan", sessionResult.getFaceScanBase64());
            parameters.put("auditTrailImage", sessionResult.getAuditTrailCompressedBase64()[0]);
            parameters.put("lowQualityAuditTrailImage", lowQualityAuditTrailImage);
            parameters.put("externalDatabaseRefID", externalDatabaseRefID);
            parameters.put("sessionId", sessionResult.getSessionId());

        }
        catch(JSONException e) {
            e.printStackTrace();
            verifikCallback.enrollmentError("Enrollment parameters exception");
            Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to create JSON payload for upload.");
        }

        //
        // Part 5:  Make the Networking Call to Your Servers.  Below is just example code, you are free to customize based on how your own API works.
        //
        okhttp3.Request request = new okhttp3.Request.Builder()
            .url(VerifikURL.Base + VerifikURL.Enroll)
            .header("Content-Type", "application/json")
            .header("X-Device-Key", Config.getShared().DeviceKeyIdentifier)
            .header("User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(sessionResult.getSessionId()))
            .header("X-User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(sessionResult.getSessionId()))
                .header("Authorization", "Bearer "+verifikToken)

            //
            // Part 7:  Demonstrates updating the Progress Bar based on the progress event.
            //
            .post(new ProgressRequestBody(RequestBody.create(parameters.toString(),MediaType.parse("application/json; charset=utf-8")),
                    new ProgressRequestBody.Listener() {
                        @Override
                        public void onUploadProgressChanged(long bytesWritten, long totalBytes) {
                            final float uploadProgressPercent = ((float)bytesWritten) / ((float)totalBytes);
                            faceScanResultCallback.uploadProgress(uploadProgressPercent);
                        }
                    }))
            .build();

        //
        // Part 8:  Actually send the request.
        //
        NetworkingHelpers.getApiClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

                //
                // Part 6:  In our Sample, we evaluate a boolean response and treat true as was successfully processed and should proceed to next step,
                // and handle all other responses by cancelling out.
                // You may have different paradigms in your own API and are free to customize based on these.
                //

                String responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    if(responseJSON.has("code") &&
                            responseJSON.getString("code").equals("Conflict")){
                        verifikCallback.enrollmentError("User already registered");
                        faceScanResultCallback.cancel();
                        return;
                    }
                    if(responseJSON.has("code") ||
                            responseJSON.has("message")){
                        String errTitle = responseJSON.has("code") ? responseJSON.getString("code") : "";
                        String errMessage = responseJSON.has("message") ? responseJSON.getString("message") : "";
                        String err = errTitle + " " + errMessage;
                        verifikCallback.enrollmentError(err);
                        faceScanResultCallback.cancel();
                        return;
                    }

                    JSONObject responseData = responseJSON.getJSONObject("data");
                    boolean wasProcessed = responseData.getBoolean("wasProcessed");
                    String scanResultBlob = responseData.getString("scanResultBlob");

                    // In v9.2.0+, we key off a new property called wasProcessed to determine if we successfully processed the Session result on the Server.
                    // Device SDK UI flow is now driven by the proceedToNextStep function, which should receive the scanResultBlob from the Server SDK response.
                    if(wasProcessed) {

                        // Demonstrates dynamically setting the Success Screen Message.
                        FaceTecCustomization.overrideResultScreenSuccessMessage = "Enrollment\nConfirmed";

                        // In v9.2.0+, simply pass in scanResultBlob to the proceedToNextStep function to advance the User flow.
                        // scanResultBlob is a proprietary, encrypted blob that controls the logic for what happens next for the User.
                        success = faceScanResultCallback.proceedToNextStep(scanResultBlob);
                    }
                    else {
                        // CASE:  UNEXPECTED response from API.  Our Sample Code keys off a wasProcessed boolean on the root of the JSON object --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                        verifikCallback.enrollmentError("No enrollment executed");
                        faceScanResultCallback.cancel();
                    }
                }
                catch(JSONException e) {
                    // CASE:  Parsing the response into JSON failed --> You define your own API contracts with yourself and may choose to do something different here based on the error.  Solid server-side code should ensure you don't get to this case.
                    e.printStackTrace();
                    verifikCallback.enrollmentError("There was an error parsing enrollment resulting data, please contact Verifik Support Team");
                    Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to parse JSON result.");
                    faceScanResultCallback.cancel();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @Nullable IOException e) {
                // CASE:  Network Request itself is erroring --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                verifikCallback.enrollmentError("There was an error parsing enrollment resulting data 2, please contact Verifik Support Team");
                Log.d("FaceTecSDKSampleApp", "Exception raised while attempting HTTPS call.");
                faceScanResultCallback.cancel();
            }
        });
    }
    public boolean isSuccess() {
        return this.success;
    }
}