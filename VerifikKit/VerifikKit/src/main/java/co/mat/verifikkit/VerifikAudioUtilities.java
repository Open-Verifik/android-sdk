package co.mat.verifikkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.ContextThemeWrapper;

import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecVocalGuidanceCustomization;

public class VerifikAudioUtilities {
    enum VocalGuidanceMode {
        OFF,
        MINIMAL,
        FULL
    }
    private MediaPlayer vocalGuidanceOnPlayer;
    private MediaPlayer vocalGuidanceOffPlayer;
    static VocalGuidanceMode vocalGuidanceMode = VocalGuidanceMode.MINIMAL;

    void setUpVocalGuidancePlayers(Context context) {
        vocalGuidanceOnPlayer = MediaPlayer.create(context, R.raw.vocal_guidance_on);
        vocalGuidanceOffPlayer = MediaPlayer.create(context, R.raw.vocal_guidance_off);
        vocalGuidanceMode = VocalGuidanceMode.MINIMAL;
    }

    void setVocalGuidanceMode(Activity activity) {
        if(isDeviceMuted(activity)) {
            AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light)).create();
            alertDialog.setMessage("Vocal Guidance is disabled when the device is muted");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }
        if(vocalGuidanceOnPlayer.isPlaying() || vocalGuidanceOffPlayer.isPlaying()) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch(vocalGuidanceMode) {
                    case OFF:
                        vocalGuidanceMode = VocalGuidanceMode.MINIMAL;
                        //sampleAppActivity.activityMainBinding.vocalGuidanceSettingButton.setImageResource(R.drawable.vocal_minimal);
                        vocalGuidanceOnPlayer.start();
                        Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.MINIMAL_VOCAL_GUIDANCE;
                        break;
                    case MINIMAL:
                        vocalGuidanceMode = VocalGuidanceMode.FULL;
                        //sampleAppActivity.activityMainBinding.vocalGuidanceSettingButton.setImageResource(R.drawable.vocal_full);
                        vocalGuidanceOnPlayer.start();
                        Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.FULL_VOCAL_GUIDANCE;
                        break;
                    case FULL:
                        vocalGuidanceMode =  VocalGuidanceMode.OFF;
                        //sampleAppActivity.activityMainBinding.vocalGuidanceSettingButton.setImageResource(R.drawable.vocal_off);
                        vocalGuidanceOffPlayer.start();
                        Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.NO_VOCAL_GUIDANCE;
                        break;
                }

                VerifikAudioUtilities.setVocalGuidanceSoundFiles();
                FaceTecSDK.setCustomization(Config.currentCustomization);
            }
        });
    }

    public static void setVocalGuidanceSoundFiles() {
        Config.currentCustomization.vocalGuidanceCustomization.pleaseFrameYourFaceInTheOvalSoundFile = R.raw.please_frame_your_face_sound_file;
        Config.currentCustomization.vocalGuidanceCustomization.pleaseMoveCloserSoundFile = R.raw.please_move_closer_sound_file;
        Config.currentCustomization.vocalGuidanceCustomization.pleaseRetrySoundFile = R.raw.please_retry_sound_file;
        Config.currentCustomization.vocalGuidanceCustomization.uploadingSoundFile = R.raw.uploading_sound_file;
        Config.currentCustomization.vocalGuidanceCustomization.facescanSuccessfulSoundFile = R.raw.facescan_successful_sound_file;
        Config.currentCustomization.vocalGuidanceCustomization.pleasePressTheButtonToStartSoundFile = R.raw.please_press_button_sound_file;

        switch(vocalGuidanceMode) {
            case OFF:
                Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.NO_VOCAL_GUIDANCE;
                break;
            case MINIMAL:
                Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.MINIMAL_VOCAL_GUIDANCE;
                break;
            case FULL:
                Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.FULL_VOCAL_GUIDANCE;
                break;
        }
    }

    boolean isDeviceMuted(Context context) {
        AudioManager audio = (AudioManager) (context.getSystemService(Context.AUDIO_SERVICE));
        if(audio.getStreamVolume(AudioManager.STREAM_MUSIC) ==  0) {
            return true;
        }
        else {
            return  false;
        }
    }
}
