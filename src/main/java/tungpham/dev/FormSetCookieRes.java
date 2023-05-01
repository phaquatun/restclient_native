/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tungpham.dev;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormSetCookieRes {

    String valueHeader, name, value, domain, path, expires, httpOnly, secure, maxAge;
    String[] arr;

    public FormSetCookieRes(String valueHeader) {
        this.valueHeader = valueHeader;
        handle();
    }

    public FormSetCookieRes(String... val) {
        this.arr = val;
    }

    public FormSetCookieRes handle(String valueHeader) {

        for (String string : arr) {
            if (valueHeader.contains(string.concat("="))) {
                String[] arrV = valueHeader.split(";");
                for (String string1 : arrV) {
                    if (string1.contains(string.concat("="))) {
                        value = string1.replace(string.concat("="), "");
                    }
                }
                break;
            }
        }

        return this;
    }

    void handle() {
        arr = valueHeader.split(";");
        for (String string : arr) {
            if (string.contains("Domain=")) {
                int local = string.indexOf("Domain=");
                domain = string.substring(local);
            }
            if (string.contains("Expires=")) {
                int local = string.indexOf("Expires=");
                expires = string.substring(local);
            }
            if (string.contains("HttpOnly")) {
                httpOnly = "HttpOnly";
            }
            if (string.contains("Secure")) {
                secure = "Secure";
            }
            if (string.toLowerCase().contains("max-age=")) {
                int local = string.toLowerCase().indexOf("max-age=");
                maxAge = string.substring(local);
            }
            if (string.contains("Path=")) {
                int local = string.indexOf("Path=");
                path = string.substring(local);;
            }
            boolean check = !string.contains("Domain=") & !string.contains("Expires=") & !string.contains("HttpOnly") & !string.contains("Secure")
                    & !string.toLowerCase().contains("max-age=") & !string.contains("Path=");
            if (check) {
                int local = string.indexOf("=");
                name = string.substring(0, local);
                value = string.substring(local + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "FormSetCookieResponse{" + "valueHeader=" + valueHeader + ", name=" + name + ", value=" + value + ", domain=" + domain + ", path=" + path + ", expires=" + expires + ", httpOnly=" + httpOnly + ", secure=" + secure + ", maxAge=" + maxAge + '}';
    }
}
