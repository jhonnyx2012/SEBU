package uneg.software.sebu.models;

/**
 * Created by Jhonny on 28/2/2016.
 */
public class BaseResponse {
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BaseResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    boolean status;
    String message;
}
