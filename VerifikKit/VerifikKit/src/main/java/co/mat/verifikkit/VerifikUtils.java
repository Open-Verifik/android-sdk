package co.mat.verifikkit;

import android.content.Context;

import com.facetec.sdk.FaceTecSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

class VerifikUtils {

    static void setOCRLocalization(Context context) {
        // Set the strings to be used for group names, field names, and placeholder texts for the FaceTec ID Scan User OCR Confirmation Screen.
        // DEVELOPER NOTE: For this demo, we are using the template json file, 'FaceTec_OCR_Customization.json,' as the parameter in calling this API.
        // For the configureOCRLocalization API parameter, you may use any object that follows the same structure and key naming as the template json file, 'FaceTec_OCR_Customization.json'.
        try {
            InputStream is = context.getAssets().open("FaceTec_OCR_Customization.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String ocrLocalizationJSONString = new String(buffer, "UTF-8");
            JSONObject ocrLocalizationJSON = new JSONObject(ocrLocalizationJSONString);

            FaceTecSDK.configureOCRLocalization(ocrLocalizationJSON);

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
}
