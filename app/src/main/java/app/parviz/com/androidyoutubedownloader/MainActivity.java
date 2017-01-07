package app.parviz.com.androidyoutubedownloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;

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
    private ProgressBar progressBar;
    private ProgressDialog progress;


    //@TODO CHALLENGE -
    // 1) define video length
    // 2) calculate step : length / size
    // 3) calc ratio between selected range in sec and step to determine byte range
    // 4) solve metadata problem
    //FSJ BFD /Social year Germany


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        qualitySpinner = (Spinner) findViewById(R.id.video_quality_spinner);
        downloadStreamButton = (Button) findViewById(R.id.download_video_stream);
        downloadStreamURL = (TextView) findViewById(R.id.download_url);
        addressBox = (EditText) findViewById(R.id.address_box);
        progressWrapper = (RelativeLayout) findViewById(R.id.progress_bar_wrapper);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progress=new ProgressDialog(this);
        progress.setMessage("Downloading Stream");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);

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

//                String[] cmd = new String[]{"-y","-i",
//                        "/storage/emulated/0/Movies/YouTubeDownloader/астанавитесь.mp4","-ss","00:00:00.00",
//                         "-t","00:00:04.0", "-async", "1"
//                        , "/storage/emulated/0/Movies/YouTubeDownloader/астанавитесь.mp4"};

//                String[] cmd = new String[]{"-y","-i",
//                        "/storage/emulated/0/Movies/YouTubeDownloader/астанавитесь.mp4",
//                        "-vf","trim=0:3"
//                        , "/storage/emulated/0/Movies/YouTubeDownloader/астанавитесь.mp4"};

//                String[] cmd = new String[]{"-y","-i",
//                        "/storage/emulated/0/Movies/YouTubeDownloader/астанавитесь.mp4","-ss","00:00:00.00",
//                        "-vcodec","copy","-acodec","copy", "-t","00:00:03.0", "-strict", "-2"
//                        , "/storage/emulated/0/Movies/YouTubeDownloader/астанавитесь.mp4"};
//                executeFFMPEG(getApplicationContext(),cmd);
            }
        });

        STREAM_URL = addressBox.getText().toString();

        loadBinaryFFMPEG(getApplicationContext());
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

        qualitySpinner.setSelection(mappedResult.size() - 1);
        setSpinnerSelection(mappedResult.size() - 1,data);

        qualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               setSpinnerSelection(i,data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerSelection(int pos,ArrayList<String[]> data){
        final ArrayList<String> mappedURL = getValueByKey(data,"url=");
        downloadStreamURL.setVisibility(View.VISIBLE);
        downloadStreamButton.setVisibility(View.VISIBLE);
        downloadStreamURL.setText(decodeURIComponent(mappedURL.get(pos)));
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

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(url, new HashMap<String, String>());
                int videoLength = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                int length = getVideoSize(url);

                int stepSize = length / videoLength;

                int minRange = 0;
                int maxRange = 4000 * stepSize; // in milleseconds

                connection = null;
                connection = (HttpURLConnection) (new URL(url)).openConnection();

                connection.setRequestMethod("HEAD");
                connection.setRequestProperty("Range", "bytes=" + minRange + "-" + maxRange);
                connection.connect();

                in = new BufferedInputStream(connection.getInputStream());
                fout = new FileOutputStream(mFile);

                final byte data[] = new byte[length];
                int count;

                long total = 0;

                Log.e("YOYO","content length :" + length + "");

//                progress.setProgress(0);
//                progress.setMax(length);
//                progress.setCancelable(false);
//                progress.show();

                while ((count = in.read(data, 0, data.length)) != -1  ) {

                    total += count;
                  //  progress.setProgress((int) (total * 100 / length));

                    fout.write(data, 0, count);
                }

                status = true;

                String[] cmd = new String[]{"-ss","00:00:00.00","-i",
                        mFile.getAbsolutePath(),
                        "-to","00:00:03.00", "-c","copy"
                        ,mFile.getParent() + "/" + mFile.getName().replace(".mp4","") + "1" + ".mp4"};

                executeFFMPEG(getApplicationContext(),cmd);

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

    private int getVideoSize(String url){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.connect();

            return connection.getContentLength();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(connection != null)
                connection.disconnect();
        }

        return 0;
    }

    class YoutubeDownloader extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

        //    progress.hide();
        //    progress.dismiss();
            progressWrapper.setVisibility(View.GONE);
        }
    }

    public void loadBinaryFFMPEG(Context context){
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            e.printStackTrace();
        }
    }

    public void executeFFMPEG(Context context,String[] cmd){
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {
                    Log.e("YOYO","FAILURE :" + message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.e("YOYO",message);
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            e.printStackTrace();
        }
    }

}
