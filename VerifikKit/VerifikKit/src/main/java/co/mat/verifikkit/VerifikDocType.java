package co.mat.verifikkit;

public enum VerifikDocType {
    LICENSE("useLicense"),
    PASSPORT("usePassport"),
    GOVERNMENT_ID("useGovernmentID"),
    AUTOMATIC_DETECTION("automatic");

    private String type;

    VerifikDocType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
