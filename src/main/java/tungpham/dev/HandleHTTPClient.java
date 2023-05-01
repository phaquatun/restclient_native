package tungpham.dev;

import com.mycompany.restclientnative.DefaultHttpClient;
import com.mycompany.restclientnative.DefaultHttpRequest;
import com.mycompany.restclientnative.OnFailed;
import com.mycompany.restclientnative.OnSuccess;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
public class HandleHTTPClient {

    HttpClient httpClient;
    HttpRequest request;

    int statusCode;

    public HandleHTTPClient create(DefaultHttpClient defClient) {
        var builder = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2);
        defClient.create(builder);

        httpClient = builder.build();
        return this;
    }

    public HandleHTTPClient useProperties() {
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        return this;
    }

    public HandleHTTPClient request(DefaultHttpRequest defRequest) {
        var builder = HttpRequest.newBuilder();
        defRequest.request(builder);

        request = builder.build();
        return this;
    }

    public HandleHTTPClient statusCode(int statusCode) {
        this.statusCode = statusCode;

        return this;
    }

    public HandleHTTPClient onSuccess(OnSuccess success) {

        if (statusCode == 200) {
            success.handle();
        }
        return this;
    }

    public HandleHTTPClient onFailed(OnFailed failed) {
        if (statusCode == 407) {
            throw new RuntimeException(">>> err proxy authen " + statusCode);
        }
        if (statusCode != 200) {
            failed.handle(statusCode);
        }
        return this;
    }

    public String mapCookieToString(Map<String, String> mapCookieResponse) {
        String value = mapCookieResponse.entrySet().stream()
                .map((ent) -> ent.getKey() + "=" + ent.getValue())
                .collect(Collectors.joining(","));

        return value;
    }

    public HandleHTTPClient logErr(Exception e) {
        StringBuilder sb = new StringBuilder();

        StackTraceElement[] arrStack = e.getStackTrace();
        for (StackTraceElement stackElement : arrStack) {
            sb.append(stackElement.toString());
        }
        return this;
    }

}
