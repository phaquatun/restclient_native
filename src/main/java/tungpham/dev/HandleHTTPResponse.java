package tungpham.dev;

import com.mycompany.restclientnative.OnFailed;
import com.mycompany.restclientnative.OnSuccess;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;

@Getter
public class HandleHTTPResponse<T> {

    private T type;
    HandleHTTPClient httpclient;
    HttpResponse<T> response;

    public HandleHTTPResponse handleHttpClient(HandleHTTPClient httpclient) {
        this.httpclient = httpclient;
        return this;
    }

    public HandleHTTPResponse setResponse(HttpResponse<T> response) {
        this.response = response;
        return this;
    }

    public HandleHTTPResponse onSuccess(OnSuccess success) {

        if (response.statusCode() == 200) {
            success.handle();
        }
        return this;
    }
    
    public HandleHTTPResponse onFailed(OnFailed failed) {
        
        if (response.statusCode() != 200) {
            failed.handle(response.statusCode());
        }
        return this;
    }

}
