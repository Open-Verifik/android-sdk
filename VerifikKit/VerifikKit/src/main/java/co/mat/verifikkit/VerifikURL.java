package co.mat.verifikkit;

class VerifikURL {
    //protected static final String Base = "https://staging-api.verifik.co/v2";
    protected static final String Base = "https://api.verifik.co/v2";
    protected static final String BaseKYC = "https://api.verifik.co/v2";
    protected static final String Credentials = "/biometrics/config";
    protected static final String Session = "/biometrics/session";
    protected static final String Enroll = "/biometrics/enrollment-3d";
    protected static final String Authenticate = "/biometrics/match-3d-3d";
    protected static final String MatchID = "/biometrics/match-3d-2d-idscan";
    protected static final String IDScan = "/biometrics/idscan-only";
    protected static final String AppRegisterKYC = "/projects/kyc/biometrics-sign-up";
    protected static final String AppLoginKYC = "/projects/kyc/biometrics-login";
    protected static final String AppIDScan = "/projects/photo-id";
}
