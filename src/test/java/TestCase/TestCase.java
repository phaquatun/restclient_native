/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestCase;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import tungpham.dev.FormSetCookieRes;
import tungpham.dev.HandleHTTPClient;
import tungpham.dev.HandleHTTPResponse;
import tungpham.dev.RestClient;

/**
 *
 * @author Admin
 */
public class TestCase {

    private HandleHTTPClient client;
    String valErr;

    public TestCase() {
    }

    public TestCase(String valErr) {
        this.valErr = valErr;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new TestCase().runRequest()
                .thenCompose((t) -> {
                    return new TestCase("ee").runRequest();
                })
                .whenComplete((t, u) -> {
                    if (u != null) {
                        u.printStackTrace();
                    }
                })
                .join();

    }

    public CompletableFuture<Void> runRequest() {
        return futSendRequest();
    }

    private CompletableFuture<Void> futSendRequest() {
        return CompletableFuture.runAsync(() -> client = RestClient.getHandle.useProperties().create(this::createHTTPClient).request(this::handleRequest))
                .thenApplyAsync((un) -> {
                    HandleHTTPResponse<String> handleResponse = new HandleHTTPResponse<>().handleHttpClient(client);
                    return handleResponse;
                })
                .thenAccept(this::handleResponse);
    }

    void handleResponse(HandleHTTPResponse<String> handleResponse) {
        client.getHttpClient().sendAsync(client.getRequest(), HttpResponse.BodyHandlers.ofString())
                .thenApplyAsync((t) -> {
                    client.statusCode(t.statusCode())
                            .onSuccess(() -> handleSuccess(t))
                            .onFailed((statusCode) -> handleFailed(statusCode, t));

                    // handleResponse.setResponse(t).onSuccess(() -> handleSuccess(t)).onFailed((statusCode) -> handleFailed(statusCode, t));
                    return t;
                })
                .join();
    }

    void createHTTPClient(HttpClient.Builder httpclient) {
        httpclient.followRedirects(HttpClient.Redirect.NEVER);
    }

    void handleRequest(HttpRequest.Builder builder) {
        String url = "https://httpbin.org/get";
        builder.GET().uri(URI.create(url));
    }

    void handleSuccess(HttpResponse<String> response) {
        String value = response.body();
        
        Map<String,String> mapCookieResponse = new HashMap<>();
        response.headers().allValues("set-cookie").forEach((t) -> {
            var formCookie = new FormSetCookieRes(t);
            mapCookieResponse.put(formCookie.getName(), formCookie.getValue());
        });
        
        if (value.contains("headers") & valErr != null) {
            throw new RuntimeException("err xxx");
        }
        System.out.println("value " + value);
    }

    void handleFailed(int statusCode, HttpResponse<String> response) {
        System.out.println("value statusCode " + statusCode + " - " + response.body());
    }

}
