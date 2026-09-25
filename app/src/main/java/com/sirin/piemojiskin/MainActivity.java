package com.sirin.piemojiskin;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.net.Uri;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity {
    private ImageView preview; private TextView status; private Uri selected;
    @Override public void onCreate(Bundle b){
        super.onCreate(b); setContentView(R.layout.activity_main);
        preview=findViewById(R.id.preview); status=findViewById(R.id.status);
        findViewById(R.id.select).setOnClickListener(v->pick());
        findViewById(R.id.save).setOnClickListener(v->save());
        findViewById(R.id.change_keyboard).setOnClickListener(v->showKeyboardPicker());
    }
    private void showKeyboardPicker(){
        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if(imm!=null) imm.showInputMethodPicker();
        status.setText("Klavye seçicisinden “Pi Keyboard”ı seç.");
    }
    private void pick(){
        Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT); i.setType("image/png");
        i.addCategory(Intent.CATEGORY_OPENABLE); startActivityForResult(i,7);
    }
    @Override protected void onActivityResult(int r,int c,Intent d){
        super.onActivityResult(r,c,d);
        if(r==7&&c==RESULT_OK&&d!=null){ selected=d.getData();
            try{getContentResolver().takePersistableUriPermission(selected,d.getFlags()&Intent.FLAG_GRANT_READ_URI_PERMISSION);}catch(Exception ignored){}
            preview.setImageURI(selected); status.setText("🥷 PNG seçildi.");
        }
    }
    private void save(){
        if(selected==null){status.setText("Önce PNG seç.");return;}
        try(InputStream in=getContentResolver().openInputStream(selected); FileOutputStream out=openFileOutput("ninja.png",MODE_PRIVATE)){
            byte[] buf=new byte[8192]; int n; while((n=in.read(buf))>0)out.write(buf,0,n);
            status.setText("PNG kaydedildi. Pi Keyboard içindeki 🥷 tuşu özel emojiyi ekler.");
        }catch(Exception e){status.setText("Kaydetme hatası: "+e.getMessage());}
    }
}