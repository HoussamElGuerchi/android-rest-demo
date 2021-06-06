package org.mql.android.demorest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mql.android.demorest.http.AsyncHttpTask;
import org.mql.android.demorest.models.Post;

public class MainActivity extends AppCompatActivity implements HttpServiceHandler {
    private ProgressBar mProgressBar;
    private TextView mTitle;
    private TextView mBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTitle = (TextView) findViewById(R.id.title_txt);
        mBody = (TextView) findViewById(R.id.body_txt);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String path = "https://jsonplaceholder.typicode.com/posts/1";
        AsyncHttpTask httpTask = new AsyncHttpTask(this, path, Post.class);
        httpTask.execute();
    }

    public void getHttpResponse(Object response) {
        Post post = (Post) response;
        mProgressBar.setVisibility(View.GONE);
        mTitle.setText(post.getTitle());
        mBody.setText(post.getBody());
        Log.i("HTTP-Service", "Result (MainActivity) = " + post);
    }
}