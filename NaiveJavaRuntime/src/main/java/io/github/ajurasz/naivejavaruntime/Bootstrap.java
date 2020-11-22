package io.github.ajurasz.naivejavaruntime;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static java.lang.String.format;

public class Bootstrap {
    private static final LambdaRuntimeAPIClient client = new LambdaRuntimeAPIClient();

    public static void main(String[] args) {
        while (true) {
            var nextInvocation = client.nextInvocation();
            invokeFunction(nextInvocation.requestId(), nextInvocation.body());
        }
    }

    private static void invokeFunction(String requestId, String event) {
        var fn = new HelloFunction();
        try {
            String result = fn.handle(event);
            client.success(requestId, result);
        } catch (Exception e) {
            e.printStackTrace();
            client.error(requestId, "Failed to invoke function");
        }
    }

    private static class LambdaRuntimeAPIClient {
        private static final HttpClient client = HttpClient.newHttpClient();
        private static final String runtimeAPI = format("http://%s/2018-06-01/runtime/", System.getenv("AWS_LAMBDA_RUNTIME_API"));

        Response nextInvocation() {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(runtimeAPI + "invocation/next"))
                    .GET()
                    .build();

            return send(request);
        }

        void success(String requestId, String result) {
            post(URI.create(runtimeAPI + "invocation/" + requestId + "/response"), result);
        }

        void error(String requestId, String message) {
            post(URI.create(runtimeAPI + "invocation/" + requestId + "/error"), message);
        }

        private void post(URI uri, String body) {
            var request = HttpRequest.newBuilder(uri)
                    .POST(BodyPublishers.ofString(body))
                    .build();
            send(request);
        }

        private Response send(HttpRequest request) {
            try {
                return new Response(client.send(request, BodyHandlers.ofString()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        static class Response {
            private static final String REQUEST_ID_HEADER = "Lambda-Runtime-Aws-Request-Id";
            HttpResponse<String> response;

            Response(HttpResponse<String> response) {
                this.response = response;
            }

            private String requestId() {
                return response.headers()
                        .firstValue(REQUEST_ID_HEADER)
                        .orElseThrow();
            }

            private String body() {
                return response.body();
            }
        }
    }
}
