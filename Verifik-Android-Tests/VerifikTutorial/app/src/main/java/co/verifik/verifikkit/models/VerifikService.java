package co.verifik.verifikkit.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

import co.verifik.verifikkit.R;

public class VerifikService implements Serializable {
    private String group;
    private String title;
    private boolean ready;
    private String description;
    private String time;
    private String parameters;
    private String successCriteria;
    private String step1;
    private String step2;
    private VerifikType verifikType;

    VerifikService(String group,
                   String title,
                   boolean ready,
                   String description,
                   String time,
                   String parameters,
                   String successCriteria,
                   String step1,
                   String step2,
                   VerifikType verifikType){
        this.group = group;
        this.title = title;
        this.ready = ready;
        this.description = description;
        this.time = time;
        this.parameters = parameters;
        this.successCriteria = successCriteria;
        this.step1 = step1;
        this.step2 = step2;
        this.verifikType = verifikType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getSuccessCriteria() {
        return successCriteria;
    }

    public void setSuccessCriteria(String successCriteria) {
        this.successCriteria = successCriteria;
    }

    public String getStep1() {
        return step1;
    }

    public void setStep1(String step1) {
        this.step1 = step1;
    }

    public String getStep2() {
        return step2;
    }

    public void setStep2(String step2) {
        this.step2 = step2;
    }

    public VerifikType getVerifikType() {
        return verifikType;
    }

    public void setVerifikType(VerifikType verifikType) {
        this.verifikType = verifikType;
    }

    static public VerifikService[] getAllVerifikServices(Context context) {
        return new VerifikService[]{
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.liveness_title),
                        true,
                        context.getString(R.string.liveness_desc),
                        "10 min",
                        context.getString(R.string.liveness_params),
                        context.getString(R.string.liveness_criteria),
                        context.getString(R.string.liveness_step1),
                        context.getString(R.string.liveness_step2),
                        VerifikType.LIVENESS
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.register_title),
                        true,
                        context.getString(R.string.register_desc),
                        "10 min",
                        context.getString(R.string.register_params),
                        context.getString(R.string.register_criteria),
                        context.getString(R.string.register_step1),
                        context.getString(R.string.register_step2),
                        VerifikType.REGISTER
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.login_title),
                        true,
                        context.getString(R.string.login_desc),
                        "5 min",
                        context.getString(R.string.login_params),
                        context.getString(R.string.login_criteria),
                        context.getString(R.string.login_step1),
                        context.getString(R.string.login_step2),
                        VerifikType.LOGIN
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.matchid_title),
                        true,
                        context.getString(R.string.matchid_desc),
                        "10 min",
                        context.getString(R.string.matchid_params),
                        context.getString(R.string.matchid_criteria),
                        context.getString(R.string.matchid_step1),
                        context.getString(R.string.matchid_step2),
                        VerifikType.MATCHID
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.ocr_title),
                        true,
                        context.getString(R.string.ocr_desc),
                        "10 min",
                        context.getString(R.string.ocr_params),
                        context.getString(R.string.ocr_criteria),
                        context.getString(R.string.ocr_step1),
                        context.getString(R.string.ocr_step2),
                        VerifikType.OCR
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.imgage_title),
                        true,
                        context.getString(R.string.imgage_desc),
                        "10 min",
                        context.getString(R.string.imgage_parameters),
                        context.getString(R.string.imgage_criteria),
                        context.getString(R.string.imgage_step1),
                        context.getString(R.string.imgage_step2),
                        VerifikType.AGE2D
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.livenessimage_title),
                        true,
                        context.getString(R.string.livenessimage_desc),
                        "10 min",
                        context.getString(R.string.livenessimage_parameters),
                        context.getString(R.string.livenessimage_criteria),
                        context.getString(R.string.livenessimage_step1),
                        context.getString(R.string.livenessimage_step2),
                        VerifikType.LIVENESSIMAGE
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.age_title),
                        true,
                        context.getString(R.string.age_desc),
                        "10 min",
                        context.getString(R.string.age_parameters),
                        context.getString(R.string.age_criteria),
                        context.getString(R.string.age_step1),
                        context.getString(R.string.age_step2),
                        VerifikType.AGE
                ),
                new VerifikService(
                        context.getString(R.string.biometric),
                        context.getString(R.string.facescanimage_title),
                        true,
                        context.getString(R.string.facescanimage_desc),
                        "10 min",
                        context.getString(R.string.facescanimage_params),
                        context.getString(R.string.facescanimage_criteria),
                        context.getString(R.string.facescanimage_step1),
                        context.getString(R.string.facescanimage_step2),
                        VerifikType.FACESCANIMAGE
                )
        };
    }
}