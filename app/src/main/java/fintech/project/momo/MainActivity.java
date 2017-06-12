package fintech.project.momo;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;


public class MainActivity extends AppCompatActivity {
    private WebView WebView;
    private final String PERMISSIN_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String PERMISSIN_READ_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private final String PERMISSIN_INTERNET = "android.permission.INTERNET";
    String search_item="";//接收查詢關鍵字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //與使用者取得權限
        setContentView(R.layout.activity_main);
        if(!hasPermission()){
            if(!needCheckPremission()){
                return;
            }
        }

        WebView = (WebView) findViewById(R.id.activity_main_webview);
        WebView.getSettings().setJavaScriptEnabled(true);
        WebView.requestFocus();
        WebView.setVisibility(View.VISIBLE);
        WebView.setHorizontalScrollBarEnabled(true);
        WebView.setVerticalScrollBarEnabled(true);
        WebView.getSettings().setBuiltInZoomControls(true);
        WebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        WebView.loadUrl("https://m.momoshop.com.tw/search.momo?searchKeyword="+search_item); //載入網頁
        WebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        Thread thread = new Thread(mutiThread);
        thread.start();

    }

    private Runnable mutiThread = new Runnable(){
        public void run(){
            // 運行網路連線的程式
            String r = sendPostDataToInternet();
            if (r != null)
                Log.d("first0.0", r);

        }
    };

    private String sendPostDataToInternet(){

        try {
            HttpClient upload_client = new DefaultHttpClient();
            String upload_url="https://www.google.co.in/searchbyimage/upload";

            HttpClient client = new DefaultHttpClient();
            String url="https://www.google.co.in/searchbyimage/upload";
            //String imageFile="/sdcard/test.jpg";
            HttpPost post = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("encoded_image", new FileBody(new File("/sdcard/","test.jpg")));
            entity.addPart("image_url",new StringBody(""));
            entity.addPart("image_content",new StringBody(""));
            entity.addPart("filename",new StringBody(""));
            entity.addPart("h1",new StringBody("en"));
            entity.addPart("bih",new StringBody("179"));
            entity.addPart("biw",new StringBody("1600"));

            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                Log.d("html",line);
                if (line.indexOf("HREF")>0){
                    System.out.println(line.substring(8));
                }
            }
        }catch (ClientProtocolException cpx){
            cpx.printStackTrace();
        }catch (IOException ioex){
            ioex.printStackTrace();
        }
        return null;
    }

    private  boolean needCheckPremission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] perms ={PERMISSIN_WRITE_STORAGE,PERMISSIN_READ_STORAGE,PERMISSIN_INTERNET};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
            return true;
        }
        return false;
    }

    private boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return (ActivityCompat.checkSelfPermission(this,PERMISSIN_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,PERMISSIN_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,PERMISSIN_INTERNET) == PackageManager.PERMISSION_GRANTED
            );
        }
        return true;
    }

    @Override
    public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==200){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(">>>","get access");
                }
            }
        }
    }


}
