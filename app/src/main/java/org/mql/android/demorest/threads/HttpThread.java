package org.mql.android.demorest.threads;

import android.util.Log;

import com.owlike.genson.Genson;

import org.mql.android.demorest.models.Post;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpThread extends Thread {
    private HttpURLConnection connection;
    private URL url;
    private String method;
    private Object result;
    private Class<?> cls;

    public HttpThread(URL url, Class<?> cls) {
        this.cls = cls;
        connect(url, "GET");
    }

    public HttpThread(URL url, String method, Class<?> cls) {
        this.cls = cls;
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

    private String getDataFromStream(InputStream in) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        Scanner scanner = new Scanner(bis);
        StringBuffer result = new StringBuffer("");

        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }

        bis.close();
        return result.toString();
    }

    @Override
    public void run() {
        if (connection != null) {
            try {
                String inputData = getDataFromStream(connection.getInputStream());
                result = new Genson().deserialize(inputData, cls);
                Log.i("JSON-Result", "Result (Thread) = " + result);
            } catch (Exception e) {
                Log.e("JSON-Result", "Error: " + e.getMessage(), e);
            } finally {
                connection.disconnect();
            }
        } else {
            result = null;
        }
    }
}
