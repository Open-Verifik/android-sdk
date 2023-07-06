package co.mat.verifikkit;

public interface VerifikCallback {
    void initVerifikSuccess();
    void appRegisterSuccessful(String token);
    void appLoginSuccessful(String token);
    void appPhotoIDScanSuccessful(String token);
    void configError(String error);
    void sessionError(String error);
    void enrollmentError(String error);
    void authError(String error);
    void photoIDMatchError(String error);
    void photoIDScanError(String error);
    void appRegisterError(String error);
    void appLoginError(String error);
    void appPhotoIDScanError(String error);
}
