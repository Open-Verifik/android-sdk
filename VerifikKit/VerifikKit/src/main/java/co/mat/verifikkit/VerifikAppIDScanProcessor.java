package co.mat.verifikkit;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facetec.sdk.FaceTecCustomization;
import com.facetec.sdk.FaceTecIDScanProcessor;
import com.facetec.sdk.FaceTecIDScanResult;
import com.facetec.sdk.FaceTecIDScanResultCallback;
import com.facetec.sdk.FaceTecIDScanStatus;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecSessionActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VerifikAppIDScanProcessor extends Processor implements FaceTecIDScanProcessor {
    private boolean success = false;
    private Context context;
    private String verifikToken;
    private String project;
    private VerifikDocType verifikDocType;
    private VerifikCallback verifikCallback;

    public VerifikAppIDScanProcessor(Context context,
                                     String sessionToken,
                                     String verifikToken,
                                     String project,
                                     VerifikDocType verifikDocType,
                                     VerifikCallback verifikCallback) {
        this.context = context;
        this.verifikToken = verifikToken;
        this.project = project;
        this.verifikDocType = verifikDocType;
        this.verifikCallback = verifikCallback;

        // In v9.2.2+, configure the messages that will be displayed to the User in each of the possible cases.
        // Based on the internal processing and decision logic about how the flow gets advanced, the FaceTec SDK will use the appropriate, configured message.
        FaceTecCustomization.setIDScanUploadMessageOverrides(
                "Uploading\nEncrypted\nID Scan", // Upload of ID front-side has started.
                "Still Uploading...\nSlow Connection", // Upload of ID front-side is still uploading to Server after an extended period of time.
                "Upload Complete", // Upload of ID front-side to the Server is complete.
                "Processing ID Scan", // Upload of ID front-side is complete and we are waiting for the Server to finish processing and respond.
                "Uploading\nEncrypted\nBack of ID", // Upload of ID back-side has started.
                "Still Uploading...\nSlow Connection", // Upload of ID back-side is still uploading to Server after an extended period of time.
                "Upload Complete", // Upload of ID back-side to Server is complete.
                "Processing Back of ID", // Upload of ID back-side is complete and we are waiting for the Server to finish processing and respond.
                "Uploading\nYour Confirmed Info", // Upload of User Confirmed Info has started.
                "Still Uploading...\nSlow Connection", // Upload of User Confirmed Info is still uploading to Server after an extended period of time.
                "Upload Complete", // Upload of User Confirmed Info to the Server is complete.
                "Processing", // Upload of User Confirmed Info is complete and we are waiting for the Server to finish processing and respond.
                "Uploading Encrypted\nNFC Details", // Upload of NFC Details has started.
                "Still Uploading...\nSlow Connection", // Upload of NFC Details is still uploading to Server after an extended period of time.
                "Upload Complete", // Upload of NFC Details to the Server is complete.
                "Processing\nNFC Details", // Upload of NFC Details is complete and we are waiting for the Server to finish processing and respond.
                "Uploading Encrypted\nID Details", // Upload of ID Details has started.
                "Still Uploading...\nSlow Connection", // Upload of ID Details is still uploading to Server after an extended period of time.
                "Upload Complete", // Upload of ID Details to the Server is complete.
                "Processing\nID Details" // Upload of ID Details is complete and we are waiting for the Server to finish processing and respond.
        );

        //
        // Part 1:  Starting the FaceTec Photo ID Scan Session
        //
        // Required parameters:
        // - Context:  Unique for Android, a Context is passed in, which is required for the final onActivityResult function after the FaceTec SDK is done.
        // - PhotoIDScanProcessor:  A class that implements FaceTecIDScanProcessor, which handles the Photo ID Scan when the User completes a Session.  In this example, "self" implements the class.
        // - sessionToken:  A valid Session Token you just created by calling your API to get a Session Token from the Server SDK.
        //
        FaceTecSessionActivity.createAndLaunchSession(context, VerifikAppIDScanProcessor.this, sessionToken);
    }

    //
    // Part 2:  Handling the Result of an IDScan
    //
    public void processIDScanWhileFaceTecSDKWaits(final FaceTecIDScanResult idScanResult, final FaceTecIDScanResultCallback idScanResultCallback) {
        //
        // DEVELOPER NOTE:  These properties are for demonstration purposes only so the Sample App can get information about what is happening in the processor.
        // In the code in your own App, you can pass around signals, flags, intermediates, and results however you would like.
        //

        //
        // Part 3:  Handles early exit scenarios where there is no IDScan to handle -- i.e. User Cancellation, Timeouts, etc.
        //
        if(idScanResult.getStatus() != FaceTecIDScanStatus.SUCCESS) {
            verifikCallback.appPhotoIDScanError("User cancel App Photo ID Scan or there was a connection error");
            NetworkingHelpers.cancelPendingRequests();
            idScanResultCallback.cancel();
            return;
        }

        // IMPORTANT:  FaceTecSDK.FaceTecIDScanStatus.Success DOES NOT mean the IDScan was Successful.
        // It simply means the User completed the Session. You still need to perform the IDScan on your Servers.

        //
        // Part 4: Get essential data off the FaceTecIDScanResult
        //
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("idScan", idScanResult.getIDScanBase64());

            ArrayList<String> frontImagesCompressedBase64 = idScanResult.getFrontImagesCompressedBase64();
            ArrayList<String> backImagesCompressedBase64 = idScanResult.getBackImagesCompressedBase64();
            if(frontImagesCompressedBase64.size() > 0) {
                parameters.put("idScanFrontImage", frontImagesCompressedBase64.get(0));
            }
            if(backImagesCompressedBase64.size() > 0) {
                parameters.put("idScanBackImage", backImagesCompressedBase64.get(0));
            }
            if(!verifikDocType.getType().isEmpty() &&
                    verifikDocType != VerifikDocType.AUTOMATIC_DETECTION) {
                parameters.put("selectedDocument",verifikDocType.getType());
            }
            parameters.put("sessionId", idScanResult.getSessionId());
            parameters.put("id", project);
        }
        catch(JSONException e) {
            e.printStackTrace();
            verifikCallback.appPhotoIDScanError("App Photo Scan parameters exception");
            Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to create JSON payload for upload.");
            return;
        }

        //
        // Part 5:  Make the Networking Call to Your Servers.  Below is just example code, you are free to customize based on how your own API works.
        //
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(VerifikURL.BaseKYC + VerifikURL.AppIDScan)
                .header("Content-Type", "application/json")
                .header("X-Device-Key", Config.getShared().DeviceKeyIdentifier)
                .header("User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(idScanResult.getSessionId()))
                .header("X-User-Agent", FaceTecSDK.createFaceTecAPIUserAgentString(idScanResult.getSessionId()))
                .header("Authorization", "Bearer "+verifikToken)

                //
                // Part 6:  Demonstrates updating the Progress Bar based on the progress event.
                //
                .post(new ProgressRequestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters.toString()),
                        new ProgressRequestBody.Listener() {
                            @Override
                            public void onUploadProgressChanged(long bytesWritten, long totalBytes) {
                                final float uploadProgressPercent = ((float)bytesWritten) / ((float)totalBytes);
                                idScanResultCallback.uploadProgress(uploadProgressPercent);
                            }
                        }))
                .build();

        //
        // Part 7:  Actually send the request.
        //
        NetworkingHelpers.getApiClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {

                //
                // Part 8:  In our Sample, we evaluate a boolean response and treat true as was successfully processed and should proceed to next step,
                // and handle all other responses by cancelling out.
                // You may have different paradigms in your own API and are free to customize based on these.
                //

                String responseString = response.body().string();
                response.body().close();
                try {
                    JSONObject responseJSON = new JSONObject(responseString);
                    if(responseJSON.has("code") &&
                            responseJSON.getString("code").equals("NotFound")){
                        verifikCallback.appPhotoIDScanError("User not found on Database");
                        idScanResultCallback.cancel();
                        return;
                    }
                    if(responseJSON.has("code") &&
                            responseJSON.getString("code").equals("Conflict") &&
                            responseJSON.has("message")){
                        verifikCallback.appPhotoIDScanError(responseJSON.getString("message"));
                        idScanResultCallback.cancel();
                        return;
                    }
                    if(responseJSON.has("code") ||
                            responseJSON.has("message")){
                        String errTitle = responseJSON.has("code") ? responseJSON.getString("code") : "";
                        String errMessage = responseJSON.has("message") ? responseJSON.getString("message") : "";
                        String err = errTitle + " " + errMessage;
                        verifikCallback.appPhotoIDScanError(err);
                        idScanResultCallback.cancel();
                        return;
                    }
                    JSONObject responseData = responseJSON.getJSONObject("data");
                    boolean wasProcessed = responseData.getBoolean("success");
                    String scanResultBlob = responseData.getString("scanResultBlob");

                    // In v9.2.0+, we key off a new property called wasProcessed to determine if we successfully processed the Session result on the Server.
                    // Device SDK UI flow is now driven by the proceedToNextStep function, which should receive the scanResultBlob from the Server SDK response.
                    if(wasProcessed) {

                        // In v9.2.0+, configure the messages that will be displayed to the User in each of the possible cases.
                        // Based on the internal processing and decision logic about how the flow gets advanced, the FaceTec SDK will use the appropriate, configured message.
                        // Please note that this programmatic API overrides these same Strings that can also be set via our standard, non-programmatic Text Customization & Localization APIs.
                        FaceTecCustomization.setIDScanResultScreenMessageOverrides(
                                "ID Scan Complete", // Successful scan of ID front-side (ID Types with no back-side).
                                "Front of ID\nScanned", // Successful scan of ID front-side (ID Types that have a back-side).
                                "Front of ID\nScanned", // Successful scan of ID front-side (ID Types that do have NFC but do not have a back-side).
                                "ID Scan Complete", // Successful scan of the ID back-side (ID Types that do not have NFC).
                                "Back of ID\nScanned", // Successful scan of the ID back-side (ID Types that do have NFC).
                                "Passport Scan Complete", // Successful scan of a Passport that does not have NFC.
                                "Passport Scanned", // Successful scan of a Passport that does have NFC.
                                "Photo ID Scan\nComplete", // Successful upload of final IDScan containing User-Confirmed ID Text.
                                "ID Scan Complete", // Successful upload of the scanned NFC chip information.
                                "Face Didn't Match\nHighly Enough", // Case where a Retry is needed because the Face on the Photo ID did not Match the User's Face highly enough.
                                "ID Document\nNot Fully Visible", // Case where a Retry is needed because a Full ID was not detected with high enough confidence.
                                "ID Text Not Legible", // Case where a Retry is needed because the OCR did not produce good enough results and the User should Retry with a better capture.
                                "ID Type Mismatch\nPlease Try Again", // Case where there is likely no OCR Template installed for the document the User is attempting to scan.
                                "ID Details\nUploaded" // Case where NFC Scan was skipped due to the user's interaction or an unexpected error.
                        );
                        if(responseData.has("_id")) {
                            String resultID = responseData.getString("_id");
                            verifikCallback.appPhotoIDScanSuccessful(resultID);
                        }

                        // In v9.2.0+, simply pass in scanResultBlob to the proceedToNextStep function to advance the User flow.
                        // scanResultBlob is a proprietary, encrypted blob that controls the logic for what happens next for the User.
                        // Cases:
                        //   1.  User must re-scan the same side of the ID that they just tried.
                        //   2.  User succeeded in scanning the Front Side of the ID, there is no Back Side, and the User is now sent to the User OCR Confirmation UI.
                        //   3.  User succeeded in scanning the Front Side of the ID, there is a Back Side, and the User is sent to the Auto-Capture UI for the Back Side of their ID.
                        //   4.  User succeeded in scanning the Back Side of the ID, and the User is now sent to the User OCR Confirmation UI.
                        //   5.  The entire process is complete.  This occurs after sending up the final IDScan that contains the User OCR Data.
                        success = idScanResultCallback.proceedToNextStep(scanResultBlob);
                    }
                    else {
                        // CASE:  UNEXPECTED response from API.  Our Sample Code keys off a wasProcessed boolean on the root of the JSON object --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                        verifikCallback.appPhotoIDScanError("Can't scan App ID");
                        idScanResultCallback.cancel();
                    }
                }
                catch(JSONException e) {
                    // CASE:  Parsing the response into JSON failed --> You define your own API contracts with yourself and may choose to do something different here based on the error.  Solid server-side code should ensure you don't get to this case.
                    e.printStackTrace();
                    verifikCallback.appPhotoIDScanError("There was an error parsing ID app scan resulting data, please contact Verifik Support Team");
                    Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to parse JSON result.");
                    idScanResultCallback.cancel();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // CASE:  Network Request itself is erroring --> You define your own API contracts with yourself and may choose to do something different here based on the error.
                verifikCallback.appPhotoIDScanError("There was an error parsing ID app scan resulting data 2, please contact Verifik Support Team");
                Log.d("FaceTecSDKSampleApp", "Exception raised while attempting HTTPS call.");
                idScanResultCallback.cancel();
            }
        });
    }
    public boolean isSuccess() {
        return this.success;
    }
}
