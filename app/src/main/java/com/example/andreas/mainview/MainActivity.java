package com.example.andreas.mainview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.ViewFlipper;
import java.util.*;

import com.example.andreas.mainview.documents.Save;
import com.example.andreas.mainview.shroom.ShroomActivity;
import com.example.andreas.mainview.slashy.SlashyActivity;


public class MainActivity extends Activity {
    private Lore lore=new Lore();
    private Save save;
    private  MissionLogic ml=new MissionLogic();
    private boolean music=true;

    private RelativeLayout layout;
    private ProgressBar loadProg;
    private HorizontalScrollView map;
    private ImageView imgMap;
    private Context context =this;
    private SlidingDrawer menuSlide;

    private   Intent intent;

    private ViewFlipper flip;

    private RelativeLayout scrollLay;
    private RelativeLayout itemScroll;

    private MissionCollection mc;
    private ItemCollection ic;

    private RelativeLayout mgLay;
    private RelativeLayout missLay;
    private RelativeLayout itemLay;
    private RelativeLayout questLay;
    private RelativeLayout loreLay;

    private RelativeLayout mgShroom;
    private RelativeLayout mgSlashy;
    private RelativeLayout mgMemory;

    private TextView txtGold;
    private int gold=200;
    private Button btnCred;
    private Button btnItem;
    private Button btnMG;
    private Button btnMiss;
    private MediaPlayer mp;
    private Partquest pQ;
    private Button handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        setContentView(R.layout.activity_main);

        save=new Save(context);
        gold=save.getGold();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           gold+= Integer.parseInt(getIntent().getExtras().get("moneyVar").toString());
            save.writeGold(this.gold);
        }


        layout=(RelativeLayout)findViewById(R.id.layout);
        layout.setBackground(getResources().getDrawable(R.drawable.menu_loadingscreen));
        handle=(Button)findViewById(R.id.handle);

        handle.setBackground(getResources().getDrawable(R.drawable.menu_arrow_up));

        loadProg=(ProgressBar)findViewById(R.id.loadProg);
        loadProg.setMax(100);

        menuSlide=(SlidingDrawer)findViewById(R.id.menuSlide);
        menuSlide.setEnabled(Boolean.FALSE);
        menuSlide.setVisibility(View.INVISIBLE);

        //To change the "Slide Icon" when Menu is Open/Closed.

        menuSlide.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                handle.setBackground(getResources().getDrawable(R.drawable.menu_arrow_up));
            }
        });


            menuSlide.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                @Override
                public void onDrawerOpened() {
                    handle.setBackground(getResources().getDrawable(R.drawable.menu_arrow_down));
                }
            });


        flip=(ViewFlipper)findViewById(R.id.flip);

        map=(HorizontalScrollView)findViewById(R.id.map);
        map.setVisibility(View.INVISIBLE);
        map.setEnabled(Boolean.FALSE);

        mgLay=(RelativeLayout)findViewById(R.id.minigameLayout);
        missLay=(RelativeLayout)findViewById(R.id.missionLayout);
        itemLay=(RelativeLayout)findViewById(R.id.itemsLayout);
        questLay=(RelativeLayout)findViewById(R.id.questLayout);
        loreLay=(RelativeLayout)findViewById(R.id.loreLay);

        mgShroom=(RelativeLayout)findViewById(R.id.mg1lay);
        mgSlashy=(RelativeLayout)findViewById(R.id.mg2lay);
        mgMemory=(RelativeLayout)findViewById(R.id.mg3lay);

        scrollLay =(RelativeLayout)findViewById(R.id.scrollLay);
        itemScroll=(RelativeLayout)findViewById(R.id.relativeLayout2);

        btnMG=(Button)findViewById(R.id.btnGame);
        btnMiss=(Button)findViewById(R.id.btnMission);
        btnItem=(Button)findViewById(R.id.btnItem);
        btnCred=(Button)findViewById(R.id.btnQuest);

        txtGold=(TextView)findViewById(R.id.txtGold);
        txtGold.setVisibility(View.INVISIBLE);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.loadingsound);
        mp.start();
        pQ=new Partquest(this,save);

        loading();      //Loading/StartScreen

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //********************KOD*****************

    public void loading(){          //StartupScreen Loader
        new CountDownTimer(4000,25){
            //Sounds???
            int progress;
            @Override
            public void onTick(long millisUntilFinished) {  //Update Loading Bar.
                progress+=1;
                loadProg.setProgress(progress);
            }

            @Override
            public void onFinish() {

                layout.removeView(loadProg);
                map.setEnabled(Boolean.TRUE);
                map.setVisibility(View.VISIBLE);

                loreLay.setBackground(pQ.getMap());


                menuSlide.setEnabled(Boolean.TRUE);
                menuSlide.setVisibility(View.VISIBLE);
                txtGold.setVisibility(View.VISIBLE);
                gold=save.getGold();

                txtGold.setText(gold+" G");

                fillItemsList(save.getItems());
                fillMissionList();
                mp = MediaPlayer.create(getApplicationContext(), R.raw.tripping);
                mp.start();
                music();
                //Kalla på Karta och kontrollera mot sparad fil vilken karta som ska laddas.

            }
        }.start();
    }
    public void markMission(View lay){

        unMarkMissions();
        for(int i=0;i<mc.getMissions().size();i++){

            if(mc.getMissions().get(i).getId()==lay.getId()) {
                mc.getMissions().get(i).mark();
            }
        }


    }

    public void unMarkMissions(){
        for(int i=0;i<mc.getMissions().size();i++){
            mc.getMissions().get(i).unMark();
        }
    }

    public void startMission(View btn){
        for(int i=0;i<mc.getMissions().size();i++){
          if(mc.getMissions().get(i).isMarked()){

                int missiongold=ml.doMission(mc.getMissions().get(i).getProbability(),
                         mc.getMissions().get(i).getGold());

                dialogBox(ml.getDialog(),null);

              gold = gold +missiongold;
              save.writeGold(gold);
              gold=save.getGold();
              txtGold.setText(gold + " G");

            }
        }

    }


    public void resetButtoncolor(){
        btnMiss.setBackgroundColor(Color.parseColor("#8c213e42"));
        btnMG.setBackgroundColor(Color.parseColor("#8c213e42"));
        btnItem.setBackgroundColor(Color.parseColor("#8c213e42"));
        btnCred.setBackgroundColor(Color.parseColor("#8c213e42"));

    }
    public void missions(View button){
        resetButtoncolor();
        button.setBackgroundColor(Color.parseColor("#9e330e80"));
        flip.setDisplayedChild(flip.indexOfChild(missLay));
    }
    public void minigame(View button){
        resetButtoncolor();
        button.setBackgroundColor(Color.parseColor("#9e330e80"));
        flip.setDisplayedChild(flip.indexOfChild(mgLay));
    }
    public void items(View button){
        resetButtoncolor();
        button.setBackgroundColor(Color.parseColor("#9e330e80"));
        flip.setDisplayedChild(flip.indexOfChild(itemLay));
    }
    public void quest(View button){
        resetButtoncolor();
        button.setBackgroundColor(Color.parseColor("#9e330e80"));
        flip.setDisplayedChild(flip.indexOfChild(questLay));
    }

    public void startSlashy(View layout){
        unMarkGames();
         intent = new Intent(context, SlashyActivity.class);
        mgSlashy.setBackgroundColor(Color.parseColor("#b5aaaaaa"));
    }
    public void startShroom(View layout){
        unMarkGames();
        intent = new Intent(context, ShroomActivity.class);

      //  layout.setBackgroundColor(Color.parseColor("#00000000"));
        mgShroom.setBackgroundColor(Color.parseColor("#b5aaaaaa"));
    }
    public void startMemory(View layout){
        unMarkGames();
        //intent = new Intent(context, MemoryActivity.class);
        mgMemory.setBackgroundColor(Color.parseColor("#b5aaaaaa"));
    }
    public void unMarkGames(){
          mgShroom.setBackgroundColor(Color.parseColor("#00000000"));
          mgSlashy.setBackgroundColor(Color.parseColor("#00000000"));
          mgMemory.setBackgroundColor(Color.parseColor("#00000000"));
    }
    public void startMinigame(View button){
        music=false;
        mp.stop();

        startActivity(intent);
    }

    public void fillMissionList(){
        scrollLay.removeAllViews();


        mc=new MissionCollection(pQ.getPart(),ic.getItemList(),context);

        for(int i=0;i<mc.getMissions().size();i++){
            if(i>0){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.addRule(RelativeLayout.BELOW,mc.getMissions().get(i-1).getId());
                scrollLay.addView(mc.getMissions().get(i),params);
            }else{
                scrollLay.addView(mc.getMissions().get(i));
            }
        }
    }
    public void fillItemsList(ArrayList<String> saveList){
        itemScroll.removeAllViews();
        ic=new ItemCollection(pQ.getPart(),context);

        for(int i=0;i<ic.getItemList().size();i++){
            for(int k=0;k<saveList.size();k++){
                if(ic.getItemList().get(i).getName().equals(saveList.get(k))){
                    ic.getItemList().get(i).buy();
                }
            }
            if(i>0){
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                params.addRule(RelativeLayout.BELOW,ic.getItemList().get(i-1).getId());
                itemScroll.addView(ic.getItemList().get(i),params);
            }else{
                itemScroll.addView(ic.getItemList().get(i));
            }
        }

    }

    public void markItem(View item){

        unMarkItems();

        for(int i=0;i<ic.getItemList().size();i++){

            if(ic.getItemList().get(i).getId()==item.getId()) {
                ic.getItemList().get(i).mark();
            }
        }
    }
    public void unMarkItems(){


        for(int i=0;i<ic.getItemList().size();i++){
            ic.getItemList().get(i).unMark();

        }
    }

    public void buyItem(View btn) {
        for (int i = 0; i < ic.getItemList().size(); i++) {
            if (ic.getItemList().get(i).isMarked()) {
                if (!ic.getItemList().get(i).isBought()) {
                    if (ic.getItemList().get(i).getPrice() <= gold) {
                        //Dialog ruta KÖP GENOMFÖRT

                        gold = gold - ic.getItemList().get(i).getPrice();
                        save.writeGold(gold);
                        gold=save.getGold();
                        txtGold.setText(gold + " G");

                        ic.getItemList().get(i).buy();
                        save.writeItem( ic.getItemList().get(i).getName());   //save item to file



                        scrollLay.removeAllViews();

                        fillMissionList();


                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setMessage(ic.getItemList().get(i).getName() + " is now Yours.")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                .show();

                    }else{
                        dialogBox("Sorry,You need more GOLD.",null);
                    }
                }
                    //Uppdatera ItemList,MissionList osv.
                    else {

                        dialogBox("You already own this ITEM.",null);
                    }
                }

            }


        }
    public void dialogBox(String msg,String title){

        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();


    }
    public void unlockPart(View btn){   //Try to unlock next Part.
       int unlockPrice=pQ.getGoldLimit();
       boolean ul= pQ.unlockPart(ic.getItemList(),gold);
       if(ul){
           gold = gold - unlockPrice;
           save.writeGold(gold);
           gold=save.getGold();
           txtGold.setText(gold + " G");

           dialogBox(pQ.getDialogMsg(ul),null);
           fillItemsList(save.getItems());
           fillMissionList();
           loreLay.setBackground(pQ.getMap());

       }else{
           dialogBox(pQ.getDialogMsg(ul),null);
       }
    }
    public void loreBtn1(View btn){
    if(pQ.getPart()>=1){
        dialogBox(lore.getLore1(),"Lore, Part I");
    }
    }
    public void loreBtn2(View btn){
        if(pQ.getPart()>=2) {
            dialogBox(lore.getLore2(), "Lore, Part II");
        }
    }
    public void loreBtn3(View btn) {
        if (pQ.getPart() >= 3) {
            dialogBox(lore.getLore3(), "Lore, Part III");
        }
    }
    public void loreBtn4(View btn) {
        if (pQ.getPart() >= 4) {
            dialogBox(lore.getLore4(), "Lore, Part IV");
        }
    }
    public void loreBtn5(View btn) {
        if (pQ.getPart() >= 5) {
            dialogBox(lore.getLore5(), "Lore, Part V");
        }
    }
    public void music() {

        new Thread() {
            public void run() {


                while (music) {
                    if (mp.isPlaying() == false) {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.tripping);
                        mp.start();
                    }
                }


            }
        }.start();
    }



}
