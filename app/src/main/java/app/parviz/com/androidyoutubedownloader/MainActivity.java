package app.parviz.com.androidyoutubedownloader;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
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
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private  String STREAM_URL = "";
    private static final String VIDEO_INFO_URL = "http://www.youtube.com/get_video_info?video_id=";

    private boolean unicodedStreamLoaded = false;

    private Spinner qualitySpinner;
    private Button downloadStreamButton;
    private TextView downloadStreamURL;
    private EditText addressBox;
    private RelativeLayout progressWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        qualitySpinner = (Spinner) findViewById(R.id.video_quality_spinner);
        downloadStreamButton = (Button) findViewById(R.id.download_video_stream);
        downloadStreamURL = (TextView) findViewById(R.id.download_url);
        addressBox = (EditText) findViewById(R.id.address_box);
        progressWrapper = (RelativeLayout) findViewById(R.id.progress_bar_wrapper);

        findViewById(R.id.load_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unicodedStreamLoaded = false;
                progressWrapper.setVisibility(View.VISIBLE);
                new Loader().execute();
            }
        });

        downloadStreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressWrapper.setVisibility(View.VISIBLE);
                new YoutubeDownloader().execute(downloadStreamURL.getText().toString());
            }
        });

        STREAM_URL = addressBox.getText().toString();
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

            int counter = 0;

            while(!unicodedStreamLoaded && counter < 10){
                result = loadStreamInfo();
                counter ++;
                if(result.length() > 0)
                    unicodedStreamLoaded = true;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.length() == 0) {
                Toast.makeText(getApplicationContext(),"Some problems with connection", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.e("YOYO","encoded_url --> " + s);
            String decodedURL = decodeURIComponent(s);
            Log.e("YOYO","decoded_url --> " + decodedURL);


            if(decodedURL == null) Toast.makeText(getApplicationContext(),"Got nothing to show",Toast.LENGTH_SHORT).show();

            String[] arr = decodedURL.split(",");
            ArrayList<String[]> formedResult = new ArrayList<>();
            for(String str : arr){
                formedResult.add(str.split("&"));
            }

            fillSpinnerData(formedResult);

            progressWrapper.setVisibility(View.GONE);
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
                downloadStreamURL.setText(decodeURIComponent(mappedURL.get(i)));
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

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "YouTubeDownloader");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("YOYO", "failed to create directory");
                return null;
            }
        }

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "астанавитесь" + ".mp4");

        return mediaFile;
    }

    private boolean downloadVideoStream(String url){
        boolean status = false;
        try {

            BufferedInputStream in = null;
            FileOutputStream fout = null;

            HttpURLConnection connection = null;

            File mFile = getOutputMediaFile();

            if(mFile == null) return status;

            try {

                connection = null;
                connection = (HttpURLConnection) (new URL(url)).openConnection();

                connection.setRequestMethod("HEAD");
                connection.connect();

                int length = connection.getContentLength();

                in = new BufferedInputStream(connection.getInputStream());
                fout = new FileOutputStream(mFile);

                //length = length / 2;

                final byte data[] = new byte[length];
                int count;

                int cTest = 0;

                while ((count = in.read(data, 0, data.length)) != -1  ) {
                    fout.write(data, 0, count);

                    cTest += count;

                    if(cTest > length/2)
                        break;
                }

                Log.e("YOYO", " -->" + cTest + " : " + length);

//                ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
//                fout.getChannel().transferFrom(rbc, 0, length);


                status = true;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (fout != null) {
                    fout.close();
                }
                connection.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    class YoutubeDownloader extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            return downloadVideoStream(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
            if(status)
                Toast.makeText(getApplicationContext(),"Yay ! Done !", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Crap ! Something went wrong !", Toast.LENGTH_SHORT).show();

            progressWrapper.setVisibility(View.GONE);
        }
    }

}
