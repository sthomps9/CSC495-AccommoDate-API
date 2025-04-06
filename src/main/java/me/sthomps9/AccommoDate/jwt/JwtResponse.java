package me.sthomps9.AccommoDate.jwt;

public class JwtResponse {
    private String token;
    private String id;

    public JwtResponse(String token, String id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }
}
