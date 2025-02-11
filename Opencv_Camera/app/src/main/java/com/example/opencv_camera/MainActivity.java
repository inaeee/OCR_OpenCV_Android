package com.example.opencv_camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
MainActivity에서는 Tesseract 모듈 초기화 및 버튼을 클릭했을 때 CameraView를 띄우는 작업을 수행한다.
두 Activity간 데이터 전송은 startActivityForResult로 선언한 Intent를 띄우고,
onActivityResult 메소드에서 if 문을 통해 해당 requestcode에 맞을 때 알맞는 코드를 입력하면 된다.
또한 AssetManager를 통해 아까 assets 폴더에 넣었던 tessdata를 기기에 다운받을 수 있게 된다.
Tesseract 객체는 static으로 선언하여 CameraView에서도 사용될 수 있게 한다.
 */

public class MainActivity extends AppCompatActivity {

    private Button mBtnCameraView;
    private EditText mEditOcrResult;
    private String datapath = "";
    private String lang = "";

    private int ACTIVITY_REQUEST_CODE = 1;

    static TessBaseAPI sTess;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //뷰 선언
        mBtnCameraView = (Button) findViewById(R.id.btn_camera);
        mEditOcrResult = (EditText) findViewById(R.id.edit_ocrresult);
        sTess = new TessBaseAPI();

        //Tesseract 인식 언어를 한국어로 설정 및 초기화
        lang = "kor";
        datapath = getFilesDir()+ "/tesseract";

        if(checkFile(new File(datapath+"/tessdata"))) {
            sTess.init(datapath, lang);
        }

        mBtnCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 시
                // Camera 화면 띄우기
                Intent mIttCamera = new Intent(MainActivity.this, CameraView.class);
                startActivityForResult(mIttCamera, ACTIVITY_REQUEST_CODE);
            }
        });

        /*
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        */
    }

    boolean checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그 후에 파일을 카피
        if(!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath + "/tessdata/" + lang + ".traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
        return true;
    }

    void copyFiles() {
        AssetManager assetMgr = this.getAssets();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = assetMgr.open("tessdata/"+lang+".traineddata");
            String destFile = datapath + "/tessdata/" + lang + ".traineddata";
            os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }

            is.close();
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if(requestCode== ACTIVITY_REQUEST_CODE) {
                // 받아온 OCR 결과 출력
                mEditOcrResult.setText(data.getStringExtra("STRING_OCR_RESULT"));
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
