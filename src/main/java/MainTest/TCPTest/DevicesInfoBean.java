package MainTest.TCPTest;

public class DevicesInfoBean {


    /**
     * success : true
     * message : 操作成功.
     * errorCode : null
     * errorMessage : null
     * responseObject : {"brightness":60,"flag":"8a8a896a636b832601636baa0d6900181526872815780","memorySpace":4809728000,"memoryTotal":4852236288,"volume":100}
     */

    private boolean success;
    private String message;
    private Object errorCode;
    private Object errorMessage;
    private String responseObject;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(String responseObject) {
        this.responseObject = responseObject;
    }
}
