package com.sirin.piemojiskin;
import android.inputmethodservice.InputMethodService;
import android.view.*;
import android.view.inputmethod.InputConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.*;

public class PiKeyboardService extends InputMethodService {
 private LinearLayout root;
 @Override public View onCreateInputView(){
  root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(5,5,5,5); root.setBackgroundColor(Color.rgb(245,245,245));
  addRow(new String[]{"Q","W","E","R","T","Y","U","I","O","P"});
  addRow(new String[]{"A","S","D","F","G","H","J","K","L"});
  addRow(new String[]{"⇧","Z","X","C","V","B","N","M","⌫"});
  LinearLayout row=new LinearLayout(this); row.setGravity(Gravity.CENTER);
  addKey(row,"123"); addEmojiKey(row); addKey(row,"SPACE"); addKey(row,"↵");
  root.addView(row,new LinearLayout.LayoutParams(-1,0,1)); return root;
 }
 private void addRow(String[] keys){LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER);for(String k:keys)addKey(row,k);root.addView(row,new LinearLayout.LayoutParams(-1,0,1));}
 private void addKey(LinearLayout row,String text){Button b=new Button(this);b.setText(text);b.setTextSize(text.equals("SPACE")?13:16);b.setAllCaps(false);b.setPadding(0,0,0,0);b.setBackground(round(Color.WHITE));b.setOnClickListener(v->press(text));row.addView(b,new LinearLayout.LayoutParams(0,-1,1));}
 private void addEmojiKey(LinearLayout row){ImageButton b=new ImageButton(this);b.setImageResource(R.drawable.ninja_sample);b.setContentDescription("🥷 özel emoji");b.setScaleType(ImageView.ScaleType.CENTER_INSIDE);b.setPadding(10,10,10,10);b.setBackground(round(Color.WHITE));b.setOnClickListener(v->commit("🥷"));row.addView(b,new LinearLayout.LayoutParams(0,-1,1));}
 private GradientDrawable round(int color){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(10);g.setStroke(1,Color.LTGRAY);return g;}
 private void press(String key){InputConnection ic=getCurrentInputConnection();if(ic==null)return;if(key.equals("SPACE")){ic.commitText(" ",1);return;}if(key.equals("↵")){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));return;}if(key.equals("⌫")){ic.deleteSurroundingText(1,0);return;}if(key.equals("⇧")||key.equals("123"))return;commit(key.toLowerCase());}
 private void commit(String s){InputConnection ic=getCurrentInputConnection();if(ic!=null)ic.commitText(s,1);}
}