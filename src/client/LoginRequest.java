package client;

public class LoginRequest {
    private String handle;
    private String password;

    LoginRequest(String handle, String password) {
        this.handle = handle;
        this.password = password;
    }

    public LoginRequest() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
}
