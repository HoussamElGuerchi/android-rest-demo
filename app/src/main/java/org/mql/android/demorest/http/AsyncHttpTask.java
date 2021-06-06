package org.mql.android.demorest.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.owlike.genson.Genson;

import org.mql.android.demorest.HttpServiceHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncHttpTask extends AsyncTask {
    private HttpURLConnection connection;
    private String path;
    private String method;
    private Class<?> cls;
    private Context context;

    public AsyncHttpTask(Context context, String path, Class<?> cls) {
        connect(path, "GET");
        this.context = context;
        this.cls = cls;
    }

    public AsyncHttpTask(Context context, String path, String method, Class<?> cls) {
        connect(path, method);
        this.context = context;
        this.cls = cls;
    }

    private void connect(String path, String method) {
        this.path = path;
        this.method = method;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            Log.i("HTTP-Service", "HTTP connection established");
        } catch (Exception e) {
            Log.e("HTTP-Service", "Failed to established HTTP connection", e);
        }
    }

    private String getDataFromStream(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer result = new StringBuffer("");
        String line;
        while ( (line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }

    protected Object doInBackground(Object[] objects) {
        if (connection != null) {
            try {
                String inputData = getDataFromStream(connection.getInputStream());
                Object result = new Genson().deserialize(inputData, cls);
                return result;
            } catch (Exception e) {
                Log.e("HTTP-Service", "Error: " + e.getMessage(), e);
            } finally {
                connection.disconnect();
            }
        }
        return null;
    }

    protected void onPostExecute(Object obj) {
        HttpServiceHandler handler = (HttpServiceHandler) context;
        handler.getHttpResponse(obj);
    }
}
