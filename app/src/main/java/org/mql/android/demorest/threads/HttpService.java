package org.mql.android.demorest.threads;

import android.util.Log;

import com.owlike.genson.Genson;

import org.mql.android.demorest.models.Post;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpService implements Runnable {
    private HttpURLConnection connection;
    private URL url;
    private String method;
    private Object result;

    public HttpService(URL url) {
        connect(url, "GET");
    }

    public HttpService(URL url, String method) {
        connect(url, method);
    }

    public Object getResult() {
        return result;
    }

    public void connect(URL url, String method) {
        this.url = url;
        this.method = method;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            Log.i("JSON-Result", "HTTP connection established");
        } catch (Exception e) {
            Log.e("JSON-Result", "Failed to established HTTP connection");
            connection = null;
        }
    }

    @Override
    public void run() {
        if (connection != null) {
            Post post = new Genson().deserialize( "{   \"userId\": 1,   \"id\": 1,   \"title\": \"test title\",   \"body\": \"test body\" }" , Post.class);
            result = post;
            Log.i("JSON-Result", "Result = " + post);
        } else {
            result = null;
        }
    }
}
