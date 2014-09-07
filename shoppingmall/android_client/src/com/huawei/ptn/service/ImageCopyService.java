package com.huawei.ptn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.huawei.ptn.util.CacheImageUtil;
import com.huawei.ptn.util.SdCardUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class ImageCopyService extends Service {

	private static final String TAG = ImageCopyService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public void onCreate() {
        super.onCreate();
		Log.d(TAG, "start Service");
		
		SdCardUtil.init();
		
		String assetDir = "image";
		String cacheDir = SdCardUtil.IMAGE_FILE_PATH + "/";
		CopyAssetsImageToCache(assetDir, cacheDir);
		
		Log.d(TAG,"end service");
    }
    
    public void onStart(Intent intent){
    	
    }
    
    private void CopyAssetsImageToCache(String assetDir, String cacheDir) {  
        String[] files = null;  
        try {
            files = this.getResources().getAssets().list(assetDir);  
            Log.d(TAG, "files.length = " + files.length);
        } catch (IOException e1) {  
            Log.d(TAG, e1.toString());
        }
        
        File mWorkingPath = new File(cacheDir);  

        for (int i = 0; i < files.length; i++) {  
            try {  
                String fileName = files[i];
                Log.d(TAG, fileName);
                
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists()){
                	outFile.delete();
                	Log.d(TAG, fileName + "delete");
                }
                    
                InputStream in = getAssets().open(assetDir + "/" + fileName); 
    
                FileOutputStream out = new FileOutputStream(outFile);
  
                // Transfer bytes from in to out  
                byte[] buf = new byte[1024];  
                int len;  
                while ((len = in.read(buf)) > 0) {  
                    out.write(buf, 0, len);  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
  
        }  
        
    }  
}
