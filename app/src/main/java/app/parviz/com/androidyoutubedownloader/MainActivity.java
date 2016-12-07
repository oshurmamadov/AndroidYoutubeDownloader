package app.parviz.com.androidyoutubedownloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String STREAM_URL = "6t7glvl6rwU";
    private static final String VIDEO_INFO_URL = "http://www.youtube.com/get_video_info?video_id=";

    private boolean unicodedStreamLoaded = false;

    private Spinner qualitySpinner;
    private Button downloadStreamButton;
    private TextView downloadStreamURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qualitySpinner = (Spinner) findViewById(R.id.video_quality_spinner);
        downloadStreamButton = (Button) findViewById(R.id.download_video_stream);
        downloadStreamURL = (TextView) findViewById(R.id.download_url);

        findViewById(R.id.load_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unicodedStreamLoaded = false;
                new Loader().execute();
            }
        });
    }

    private String loadStreamInfo(){
        BufferedInputStream bufferedInputStream = null;
        String result = "";
        try {
            System.setProperty("http.keepAlive", "false");

            URL url = new URL(VIDEO_INFO_URL + STREAM_URL);

            bufferedInputStream = new BufferedInputStream(url.openStream());

//            byte[] buffer = new byte[5 * 1024];
//            int count = bufferedInputStream.read(buffer);
//            String info = new String(buffer, 0, 5 * 1024);
//            bufferedInputStream.close();

            int ch;
            StringBuilder builder = new StringBuilder();
            while( (ch = bufferedInputStream.read()) != -1 ){
                builder.append((char)ch);
            }

            String[] array = builder.toString().split("&");

            String encodedStreamURL = "";
            for(String item : array){
                if(item.contains("url_encoded_fmt_stream_map")) {
                    Log.e("YOYO",item);
                    encodedStreamURL = item.split("=").length >= 2 ? item.split("=")[1] : "";
                }

            }

            result = encodedStreamURL;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    class Loader extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";

            Log.e("YOYO","loading encoded stream url...");

            while(!unicodedStreamLoaded){
                result = loadStreamInfo();
                if(result.length() > 0)
                    unicodedStreamLoaded = true;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("YOYO","encoded_url --> " + s);
            String decodedURL = decodeURIComponent(s);
            Log.e("YOYO","decoded_url --> " + decodedURL);

            String[] arr = decodedURL.split(",");
            ArrayList<String[]> formedResult = new ArrayList<>();
            for(String str : arr){
                formedResult.add(str.split("&"));
            }

            fillSpinnerData(formedResult);
        }
    }

    private String decodeURIComponent(String url){
        String result = null;
        if(url == null || url.length() == 0) return null;
        try{
            result = URLDecoder.decode(url,"UTF-8");
        }catch (UnsupportedEncodingException exc){
            result = url;
        }
        return result;
    }

    private void fillSpinnerData(final ArrayList<String[]> data){

        if(data == null || data.size() == 0)
            return;

        qualitySpinner.setVisibility(View.VISIBLE);

        final ArrayList<String> mappedQuality = getValueByKey(data,"quality=");
        final ArrayList<String> mappedType = getValueByKey(data,"type=");
        final ArrayList<String> mappedResult = new ArrayList<>();

        for(int i = 0; i < mappedQuality.size(); i++){
            String type = mappedType.get(i)
                    .replaceAll("\\%2F", "/")
                    .replaceAll("\\%3B", ";")
                    .replaceAll("\\%20", "\"")
                    .split(";")[0];
            if(!mappedResult.contains(mappedQuality.get(i) + " : " +type))
                mappedResult.add(mappedQuality.get(i) + " : " + type);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,mappedResult);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitySpinner.setAdapter(adapter);

        qualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final ArrayList<String> mappedURL = getValueByKey(data,"url=");
                downloadStreamURL.setVisibility(View.VISIBLE);
                downloadStreamButton.setVisibility(View.VISIBLE);
                downloadStreamURL.setText(mappedURL.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private ArrayList<String> getValueByKey(ArrayList<String[]> data,String key){
        final ArrayList<String> mapped = new ArrayList<>();
        for(String[] array : data){
            for(String str: array){
                if(str.contains(key))
                    if(str.split("=").length >= 2)
                        mapped.add(str.split("=")[1]);
            }
        }

        return mapped;
    }

}