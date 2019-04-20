package be.pxl.basicSecurity.appDev.crypto;

/**
 * This enum specifies the various encryption algorithm valid for use in this application
 */

public enum UsableAlgorithm {

    AES(1),
    RSA(2),
    SHA256(3);

    private int algorithm;

    private UsableAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        String algo = "";
        switch (algorithm) {
            case 1:
                algo = "AES";
                break;
            case 2:
                algo = "RSA";
                break;
            case 3:
                algo = "SHA-256";
                break;
        }

        return algo;
    }
}
