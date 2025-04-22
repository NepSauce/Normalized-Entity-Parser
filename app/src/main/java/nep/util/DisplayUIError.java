package nep.util;

public class DisplayUIError {
    private String errorMessage;
    private int errorType;
    private int length;

    public DisplayUIError(String errorMessage, int errorType){
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.length = 3;
    }

    public void displayError(String errorMessage, int errorType, int length){
        // Show UI Level error
    }
}
