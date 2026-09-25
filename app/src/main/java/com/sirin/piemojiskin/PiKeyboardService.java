package com.sirin.piemojiskin;
import android.inputmethodservice.InputMethodService;
import android.view.*;
import android.view.inputmethod.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.*;
import android.os.Build;
import android.content.ClipDescription;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.*;

public class PiKeyboardService extends InputMethodService {
 private LinearLayout root; private boolean shift;
 @Override public View onCreateInputView(){
  root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(3,3,3,3);root.setBackgroundColor(Color.rgb(238,238,238));
  addRow(new String[]{"Q","W","E","R","T","Y","U","I","O","P"});
  addRow(new String[]{"A","S","D","F","G","H","J","K","L"});
  addRow(new String[]{"⇧","Z","X","C","V","B","N","M","⌫"});
  LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER);
  addKey(row,"123");addEmojiKey(row);addKey(row,"SPACE");addKey(row,".");addKey(row,"↵");
  root.addView(row,new LinearLayout.LayoutParams(-1,0,1));return root;
 }
 private void addRow(String[] keys){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER);for(String k:keys)addKey(row,k);root.addView(row,new LinearLayout.LayoutParams(-1,0,1));}
 private void addKey(LinearLayout row,String text){Button b=new Button(this);b.setText(text);b.setTextSize(text.equals("SPACE")?12:15);b.setAllCaps(false);b.setPadding(0,0,0,0);b.setBackground(keyBackground());b.setOnClickListener(v->press(text));row.addView(b,new LinearLayout.LayoutParams(0,-1,1));}
 private void addEmojiKey(LinearLayout row){ImageButton b=new ImageButton(this);b.setImageResource(R.drawable.ninja_sample);b.setContentDescription("Özel 🥷");b.setScaleType(ImageView.ScaleType.CENTER_INSIDE);b.setPadding(8,8,8,8);b.setBackground(keyBackground());b.setOnClickListener(v->insertNinja());row.addView(b,new LinearLayout.LayoutParams(0,-1,1));}
 private GradientDrawable keyBackground(){GradientDrawable g=new GradientDrawable();g.setColor(Color.WHITE);g.setCornerRadius(12);g.setStroke(1,Color.LTGRAY);return g;}
 private void press(String key){InputConnection ic=getCurrentInputConnection();if(ic==null)return;if(key.equals("SPACE")){ic.commitText(" ",1);return;}if(key.equals("↵")){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));return;}if(key.equals("⌫")){ic.deleteSurroundingText(1,0);return;}if(key.equals("⇧")){shift=!shift;return;}if(key.equals("123"))return;ic.commitText(shift?key:key.toLowerCase(),1);if(shift)shift=false;}
 private void insertNinja(){
  InputConnection ic=getCurrentInputConnection();if(ic==null)return;
  if(Build.VERSION.SDK_INT>=25){try{
   File f=new File(getCacheDir(),"ninja.png");
   try(InputStream in=getResources().openRawResource(R.drawable.ninja_sample);FileOutputStream out=new FileOutputStream(f)){byte[] buf=new byte[8192];int n;while((n=in.read(buf))>0)out.write(buf,0,n);}
   Uri uri=FileProvider.getUriForFile(this,"com.sirin.piemojiskin.fileprovider",f);
   ClipDescription desc=new ClipDescription("Pi Ninja",new String[]{"image/png"});
   InputContentInfo info=new InputContentInfo(uri,desc,null);
   ic.commitContent(info,InputConnection.INPUT_CONTENT_GRANT_READ_URI_PERMISSION,null);return;
  }catch(Exception ignored){}
  }
  ic.commitText("🥷",1);
 }
}