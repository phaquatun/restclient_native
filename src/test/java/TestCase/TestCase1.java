/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestCase;

import java.util.HashMap;
import java.util.Map;
import tungpham.dev.RestClient;

/**
 *
 * @author Admin
 */
public class TestCase1 {
    public static void main(String[] args) {
        Map<String,String> mapCookie = new HashMap<>();
        mapCookie.put("c_user", "12346");
        mapCookie.put("fbb_tds", "xxxxx");
        mapCookie.put("sb", "rrrrrr");
        
        String val =  RestClient.getHandle.mapCookieToString(mapCookie);
        System.out.println(val);
    }
}
