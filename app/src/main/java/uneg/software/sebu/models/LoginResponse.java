package uneg.software.sebu.models;

/**
 * Created by Jhonny on 28/2/2016.
 */
public class LoginResponse extends BaseResponse {
    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public LoginResponse(boolean status, String message, Usuario user) {
        super(status, message);
        this.user = user;
    }

    Usuario user;

    public LoginResponse(boolean status, String message) {
        super(status, message);
    }
}
