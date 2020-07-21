package curiousfreaks.com.gre333;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by gasaini on 3/8/2018.
 */

public class WordDetails extends AppCompatActivity implements imageRecyclerAdapter.imageClickInterface{

    TextView word,type,meaning,sentence,synonym,antonym;
    ImageView speak,bookmark,share;
    Button next,previous;
    DatabaseHandler dbHandler;
    SQLiteDatabase database;
    wordDefinition aWord = null;
    String ACTIVITY_TYPE="";
    List<wordDefinition> wordsList;
    TextToSpeech textToSpeech;
    boolean isTTSInitialized = false;
    LinearLayout imageLayout;
    RecyclerView imageRecyclerView;
    imageRecyclerAdapter imageAdapter;
    List<imageDefinition> imagesList;
    public String jsonString = "";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor prefEditor;
    int start_index,end_index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_details);

        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("ACTIVITY_TYPE")){
            ACTIVITY_TYPE=getIntent().getExtras().getString("ACTIVITY_TYPE");
        }

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    isTTSInitialized = true;
                }

            }
        });

        dbHandler=new DatabaseHandler(getApplicationContext());
        database = dbHandler.getReadableDatabase();
        if(ACTIVITY_TYPE.equals("BARRON333")) {
            start_index = getIntent().getExtras().getInt("start_index");
            end_index = getIntent().getExtras().getInt("end_index");
            if(start_index != -1 || end_index !=-1)
                wordsList = dbHandler.getSelectedRangeWords(getAssets(),start_index,end_index,database);
            else
                wordsList = dbHandler.getBarron333(getAssets(),database);
        }

        sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        prefEditor=sharedPreferences.edit();

        word=findViewById(R.id.word);
        speak=findViewById(R.id.speakWord);
        bookmark=findViewById(R.id.bookmarkWord);
        type=findViewById(R.id.type);
        meaning=findViewById(R.id.meaning);
        sentence=findViewById(R.id.sentence);
        synonym=findViewById(R.id.synonym);
        antonym=findViewById(R.id.antonym);
        previous=findViewById(R.id.previousWord);
        next=findViewById(R.id.nextWord);
        imageLayout=findViewById(R.id.wordImagesLayout);
        imageRecyclerView=findViewById(R.id.imagesRecycler);
        share=findViewById(R.id.shareAWord);

        imagesList=new ArrayList<>();
        imageAdapter=new imageRecyclerAdapter(imagesList);
        imageAdapter.setImageClickListner(this);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL,false);
        imageRecyclerView.setLayoutManager(lm);
        imageRecyclerView.setAdapter(imageAdapter);


        findAWord();
        initDisplay();

        database.close();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(wordDefinition oneWord:wordsList)
                {
                    if(oneWord.getWord().equals(aWord.getWord()))
                    {
                        aWord = oneWord;
                        break;
                    }

                }
                int index=wordsList.indexOf(aWord);
                index--;
                aWord = wordsList.get(index);
                if(next.getVisibility()==View.INVISIBLE)
                    next.setVisibility(View.VISIBLE);
                if(index==0) {
                    previous.setVisibility(View.INVISIBLE);
                }
                initDisplay();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(wordDefinition oneWord:wordsList)
                {
                    if(oneWord.getWord().equals(aWord.getWord()))
                    {
                        aWord = oneWord;
                        break;
                    }
                }
                int index=wordsList.indexOf(aWord);
                ++index;
                aWord=wordsList.get(index);
                if(previous.getVisibility()==View.INVISIBLE)
                    previous.setVisibility(View.VISIBLE);
                if(index==(wordsList.size()-1))
                {
                    next.setVisibility(View.INVISIBLE);

                }
                initDisplay();
            }
        });
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //textToSpeech.setSpeechRate(0.7f);
                String speech;
                if (!aWord.getAttr1().equals(""))
                    speech = aWord.getWord() + "." + aWord.getAttr1();
                else
                    speech = aWord.getWord() + "." + aWord.getMeaning();
                //textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null);
                speakIt(speech);
            }
        });
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler dbHandler=new DatabaseHandler(getApplicationContext());
                SQLiteDatabase db = dbHandler.getWritableDatabase();
                if(aWord.getBookmarked().equals("YES"))
                {
                    bookmark.setImageResource(R.mipmap.button_uncheck);
                    aWord.setBookmarked("NO");
                    dbHandler.updateLearntOrBookmarked(db,aWord,"null","NO");
                }else
                {
                    bookmark.setImageResource(R.mipmap.button_check);
                    aWord.setBookmarked("YES");
                    dbHandler.updateLearntOrBookmarked(db,aWord,"null","YES");
                }
                db.close();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body="Word: "+aWord.getWord()+"\n"+
                        "Meaning: "+aWord.getMeaning()+"\n"+
                        "\""+aWord.getSentence()+"\""
                        ;

                intent.putExtra(Intent.EXTRA_SUBJECT,"Curious Freaks");
                intent.putExtra(Intent.EXTRA_STREAM,R.mipmap.ic_launcher_round);
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"Share using.."));
            }
        });

    }

    public void findAWord()
    {
        if(ACTIVITY_TYPE.equals("VOICE_SEARCH")) {
            wordsList=dbHandler.getAllWords();
            wordDefinition foundWord;
            ArrayList<String> voiceRegnognizedWords=getIntent().getStringArrayListExtra("VOICE_SEARCH_RESULT");
            for(String word:voiceRegnognizedWords)
            {
                Iterator itr = wordsList.iterator();
                while(itr.hasNext())
                {
                    foundWord = (wordDefinition) itr.next();
                    if (foundWord.getWord().equalsIgnoreCase(word)) {
                        aWord=foundWord;
                        return;
                    }
                }
            }
            return;
        }
        if(ACTIVITY_TYPE.equals("NOTIFICATION_OPEN")) {
            int word_id = getIntent().getExtras().getInt("NOTIFICATION_ID");
            SQLiteDatabase db = dbHandler.getReadableDatabase();
            aWord = dbHandler.getAWord(word_id,db);
            db.close();
        }
        if(ACTIVITY_TYPE.equals("BARRON333")) {

            long word_id=  getIntent().getExtras().getInt("WORD_ID");
            aWord = dbHandler.getAWord(word_id,database);
        }
    }

    public void initDisplay()
    {
        if(aWord==null)
        {
            Toast.makeText(getApplicationContext(),"Couldn't find word in library.. try again!!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra("STATUS",-2);
            setResult(RESULT_CANCELED,intent);
            finish();
            return;
        }
        if(ACTIVITY_TYPE.equals("VOICE_SEARCH")) {
            previous.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
            String speech;
            if (!aWord.getAttr1().equals(""))
                speech = aWord.getWord() + ".\n" + aWord.getAttr1();
            else
                speech = aWord.getWord() + ".\n" + aWord.getMeaning();

            speakIt(speech);
        }
        if(ACTIVITY_TYPE.equals("NOTIFICATION_OPEN")){
            previous.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);

        }if(ACTIVITY_TYPE.equals("BARRON333")){
            for(wordDefinition oneWord:wordsList)
            {
                if(oneWord.getWord().equals(aWord.getWord()))
                {
                    aWord =oneWord;
                }
            }
            if(wordsList.indexOf(aWord)==0)
            {
                previous.setVisibility(View.INVISIBLE);
            }
            if(wordsList.indexOf(aWord)==(wordsList.size()-1))
            {
                next.setVisibility(View.INVISIBLE);
            }
        }

        word.setText(aWord.getWord());

        if(!aWord.getType().equals(""))
            type.setText(aWord.getType());
        else
            type.setText("");

        if(!aWord.getMeaning().equals(""))
            meaning.setText(aWord.getMeaning());
        else
            meaning.setText("");

        if(!aWord.getSentence().equals(""))
            sentence.setText("\""+aWord.getSentence()+"\"");
        else
            sentence.setText("");


        if(aWord.getBookmarked().equals("YES"))
        {
            bookmark.setImageResource(R.mipmap.button_check);
        }else
        {
            bookmark.setImageResource(R.mipmap.button_uncheck);
        }

        smartDisplaySynonymsAntonyms();
        initImages();

    }

    public void initImages()
    {
        if(!imagesList.isEmpty())
            imagesList.clear();
        if(jsonString.equals(""))
            jsonString = dbHandler.getJsonStingUsingAssetManager(getAssets(),"DataMap.json");
        try {
            JSONObject jRootObject = new JSONObject(jsonString);
            JSONArray jArray= jRootObject.optJSONArray("FDATA");
            for(int i=0;i<jArray.length();++i) {
                JSONObject jsonObject=jArray.getJSONObject(i);
                if(aWord.getId()==jsonObject.getLong("MAPID")){
                    Iterator<String> itr=jsonObject.keys();
                    while(itr.hasNext())
                    {
                        String key=itr.next();
                        if(!key.equals("MAPID")){

                            InputStream imageStream = getAssets().open("images/"+jsonObject.getString(key));
                            Bitmap image = BitmapFactory.decodeStream(imageStream);
                            imageDefinition aImageDef = new imageDefinition(image);
                            aImageDef.setId(aWord.getId());
                            aImageDef.setName(jsonObject.getString(key));
                            imagesList.add(aImageDef);
                            imageAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        imageAdapter.notifyDataSetChanged();

    }
    public void speakIt(String speech1)
    {
        final String speech = speech1;
        Thread th = new Thread(){
            @Override
            public void run() {
                super.run();
                int waitCount=0;
                while(!isTTSInitialized){
                    try{
                        if(waitCount==10)
                            break;
                        Thread.sleep(1000);
                        waitCount++;
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                if(isTTSInitialized){
                    try{
                        textToSpeech.setSpeechRate(0.7f);
                        textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"TTS initialization in progress",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"TTS initialization error",Toast.LENGTH_SHORT).show();
                }
            }
        };
        th.start();
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.word_details_menu,menu);
        menu.findItem(R.id.id_synonymCheck).setChecked(sharedPreferences.getBoolean("SYNONYMS",false));
        menu.findItem(R.id.id_antonymCheck).setChecked(sharedPreferences.getBoolean("ANTONYMS",false));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.id_synonymCheck)
        {
            boolean checked = item.isChecked();
            if (checked)
            {
                prefEditor.putBoolean("SYNONYMS",false);
                item.setChecked(false);
            }
            else {
                prefEditor.putBoolean("SYNONYMS",true);
                item.setChecked(true);
            }
            prefEditor.apply();
            prefEditor.commit();
            smartDisplaySynonymsAntonyms();
        }

        if(item.getItemId() == R.id.id_antonymCheck)
        {
            if (item.isChecked())
            {
                prefEditor.putBoolean("ANTONYMS",false);
                item.setChecked(false);
            }
            else {
                prefEditor.putBoolean("ANTONYMS",true);
                item.setChecked(true);
            }
            prefEditor.apply();
            prefEditor.commit();
            smartDisplaySynonymsAntonyms();
        }
        if(item.getItemId()==R.id.reportWordIssue)
        {
            LayoutInflater li = LayoutInflater.from(this);
            View reportView = li.inflate(R.layout.report_issue,null);
            AlertDialog.Builder diallog_builder = new AlertDialog.Builder(this);

            final TextView report_word = reportView.findViewById(R.id.report_word);
            final EditText report_addition_info = reportView.findViewById(R.id.report_addition_info);
            final Button report_send_button = reportView.findViewById(R.id.report_send_button);

            report_word.setText("Would you like to report an issue in " + aWord.getWord()+"?");
            diallog_builder
                    .setView(reportView)
                    .setCancelable(true);

            final AlertDialog report_dialog = diallog_builder.create();

            report_send_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String str_addition_info = report_addition_info.getText().toString();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL,new String[]{"thecuriousfreak007@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT,"Important alert message..issue reported in word "+aWord.getWord());
                    email.putExtra(Intent.EXTRA_TEXT,"Word: " +aWord.getId()+" #"+aWord.getId()+" "+
                            "Addition Information :" +
                            ""+str_addition_info);
                    email.setType("plain/text");
                    startActivity(Intent.createChooser(email,"Choose an Email client:"));

                    report_dialog.dismiss();

                }
            });


            report_dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void smartDisplaySynonymsAntonyms()
    {
        boolean showSynonyms = sharedPreferences.getBoolean("SYNONYMS",false);
        boolean showAntonyms = sharedPreferences.getBoolean("ANTONYMS",false);
        if(!aWord.getSynonyms().equals("") && showSynonyms) {
            synonym.setVisibility(View.VISIBLE);
            synonym.setText("Synonyms - " + aWord.getSynonyms());
            if(!aWord.getAntonyms().equals("") && showAntonyms){
                antonym.setVisibility(View.VISIBLE);
                antonym.setText("Antonyms - "+aWord.getAntonyms());
            }
            else {
                if(antonym.getVisibility()==View.VISIBLE){
                    antonym.setVisibility(View.INVISIBLE);
                }
            }
        }
        else {
            if (!aWord.getAntonyms().equals("") && showAntonyms) {
                synonym.setVisibility(View.VISIBLE);
                synonym.setText("Antonyms - " + aWord.getAntonyms()); // if synonyms is not marked visible then show antonyms in place of synonyms text box
                if(antonym.getVisibility() == View.VISIBLE)
                {
                    antonym.setVisibility(View.INVISIBLE);
                }
            }else {
                if (synonym.getVisibility() == View.VISIBLE)
                {
                    synonym.setVisibility(View.INVISIBLE);
                }

            }
        }
    }


    @Override
    public void OnImageClick(List<imageDefinition> imagesList) {

        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
        LayoutInflater inflator = LayoutInflater.from(this);
        View dialog_layout = inflator.inflate(R.layout.image_dialog,null);
        LinearLayout image_liner_layout = dialog_layout.findViewById(R.id.image_dialog_linear_layout);
        final TextView textView = dialog_layout.findViewById(R.id.dialog_word_details);
        textView.setText(aWord.getWord()+": "+aWord.getMeaning());

        int imgMaxWidth = (int)(getResources().getDisplayMetrics().widthPixels*0.80);

        for (imageDefinition imgDef: imagesList)
        {
            ImageView aImage= new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            aImage.setPadding(15,15,15,15);

            aImage.setLayoutParams(params);
            Bitmap tempBitmap = getScaledBitmap(imgDef.getBitmap(),imgMaxWidth);

            aImage.setImageBitmap(tempBitmap);
            image_liner_layout.addView(aImage);
        }

        dialog_builder.setView(dialog_layout);

        AlertDialog dialog = dialog_builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public Bitmap getScaledBitmap(Bitmap originalBitmap, float scaledWidth)
    {
        Bitmap scaledBitmap=null;
        if(originalBitmap!=null) {
            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();
            float widthRatio=scaledWidth/originalWidth;

            int finalWidth=(int)Math.floor(originalWidth*widthRatio);
            int finalHeight=(int)Math.floor(originalHeight*widthRatio);

            scaledBitmap=Bitmap.createScaledBitmap(originalBitmap,finalWidth,finalHeight,true);


        }

        return scaledBitmap;
    }

    @Override
    public void onBackPressed() {
        prefEditor.putBoolean("REFRESH_MAIN",true);
        prefEditor.putInt("START_INDEX",start_index);
        prefEditor.putInt("END_INDEX",end_index);
        prefEditor.apply();
        prefEditor.commit();
        super.onBackPressed();
    }
}
