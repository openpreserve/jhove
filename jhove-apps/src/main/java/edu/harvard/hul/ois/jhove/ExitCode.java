package edu.harvard.hul.ois.jhove;

/**
 * The set of possible exit codes returned by JHOVE applications.
 */
public enum ExitCode {

    /** General error. */
    ERROR(-1),

    /** Incompatible Java VM. */
    INCOMPATIBLE_VM(-2);


    private final int returnCode;

    ExitCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public int getReturnCode() {
        return returnCode;
    }
}
