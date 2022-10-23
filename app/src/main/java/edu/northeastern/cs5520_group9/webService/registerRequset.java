package edu.northeastern.cs5520_group9.webService;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class registerRequset extends StringRequest {
    private static final  String RR_URL = "http://bobdb123.shop/register.php";
    private Map<String, String> parmas;

    public registerRequset(String name, String username, String password, Response.Listener<String> listener){
        super(Method.POST, RR_URL, listener, null);
        parmas = new HashMap<>();
        parmas.put("name", name);
        parmas.put("username", username);
        parmas.put("password", password);
    }

    public Map<String, String> getParmas() {
        return parmas;
    }
}
