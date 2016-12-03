package app.parviz.com.androidyoutubedownloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String STREAM_URL = "6t7glvl6rwU";
    private static final String VIDEO_INFO_URL = "http://www.youtube.com/get_video_info?video_id=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Loader().execute();
            }
        });
    }

    private void loadStreamInfo(){
        BufferedInputStream bufferedInputStream = null;
        try {
            URL url = new URL(VIDEO_INFO_URL + STREAM_URL);

            bufferedInputStream = new BufferedInputStream(url.openStream());

            byte[] buffer = new byte[5 * 1024];

            int count = bufferedInputStream.read(buffer);

            String info = new String(buffer, 0, 5 * 1024);
            bufferedInputStream.close();

            String[] array = info.split("&");

            String encodedStreamURL = "";
            for(String item : array){
                if(item.contains("url_encoded_fmt_stream_map"))
                    encodedStreamURL = item;
            }

            if(encodedStreamURL.length() == 0) {
                loadStreamInfo();
            }

            Log.e("YOYO","--->" + encodedStreamURL);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class Loader extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            loadStreamInfo();

            return null;
        }
    }
}
