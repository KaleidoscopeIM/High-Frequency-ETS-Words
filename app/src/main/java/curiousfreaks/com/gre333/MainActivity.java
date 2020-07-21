package curiousfreaks.com.gre333;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements wordListRecyclerAdapter.myRecyclerItemClickListenerInterface{

    public static String TAG = "Curious Freaks";
    private RecyclerView recyclerView;
    private wordListRecyclerAdapter mAdapter =null;
    private DatabaseHandler dbHandler, threadDbHandler;
    private SQLiteDatabase database, threadDatabase;
    private TextToSpeech textToSpeech;
    public SharedPreferences sharedPreferences;
    private InterstitialAd interstitialAd;
    public SharedPreferences.Editor prefEditor;
    int saved_start_index=-1, saved_end_index=-1;
    int saved_range_click_position =0,saved_word_click_position=0;
    Bundle savedInstanceState1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState1);
        savedInstanceState1=savedInstanceState;
        sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        prefEditor=sharedPreferences.edit();
        String status = sharedPreferences.getString("INITIALIZE_CODE","FAILED");
        if(status.equals("FAILED")) {
            threadDbHandler = new DatabaseHandler(getApplicationContext());
            threadDatabase = threadDbHandler.getReadableDatabase();
            String allWordsJson = threadDbHandler.getJsonStingUsingAssetManager(getAssets(),"WordsList.json");  // BARRON_333
            new MainActivity.initialize_data().execute(allWordsJson);
            prefEditor.putBoolean("SYNONYMS", false);
            prefEditor.putBoolean("ANTONYMS", false);
            prefEditor.apply();
            prefEditor.commit();
        }else {

            setTheme(R.style.AppTheme);
            initMainUI();
        }
        MobileAds.initialize(this,"ca-app-pub-1290738907415765~6555714037");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1290738907415765~6555714037");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.show();

        //Toast.makeText(getApplicationContext(),sharedPreferences.getString("INITIALIZE_CODE","FAILED"),Toast.LENGTH_SHORT).show();
       /* textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!= TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });*/

        /*int count = 0;
        while (true)
        {
            String initCode = sharedPreferences.getString("INITIALIZE_CODE","FAILED");
            if (count == 120)
                break;
            if (initCode.equals("SUCCESS"))
            {

                break;
            }else {
                count++;
                try{
                    Thread.sleep(1000);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }*/
    }

    public void initMainUI()
    {
        setContentView(R.layout.list_recycler);
        dbHandler = new DatabaseHandler(getApplicationContext());
        recyclerView = findViewById(R.id.wordLIstRecycler);

        database = dbHandler.getReadableDatabase();
        List<wordRangeDefinition> allBarronMainList = dbHandler.getBarron333MainList(getAssets(),database);
        initWords(allBarronMainList,null,"RANGE_LIST",-1,-1);
        database.close();
    }
    public void initWords(List<wordRangeDefinition> RangeList, List<wordDefinition> WordsList, String list_type, int start_index, int end_index)
    {
        if(mAdapter == null) {
            mAdapter = new wordListRecyclerAdapter();
            RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(lm);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemCLickListener(this);
        }
        if(list_type.equals("RANGE_LIST")){
            recyclerView.smoothScrollToPosition(saved_range_click_position);
            mAdapter.setAdapterType(RangeList,null,"RANGE_LIST",start_index,end_index);
        }

        if(list_type.equals("RANGE_WORDS")) {
            recyclerView.smoothScrollToPosition(saved_word_click_position);
            mAdapter.setAdapterType(null, WordsList,"RANGE_WORDS", start_index,end_index);
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRangeOrWordClicked(View view, int position, int range_or_word_id, String word_range, int start_index, int end_index, String list_type) {
        DatabaseHandler tempDbHandler = new DatabaseHandler(getApplicationContext());
        SQLiteDatabase tempDatabase = tempDbHandler.getReadableDatabase();
        saved_start_index=start_index;
        saved_end_index=end_index;
        if(list_type.equals("RANGE_WORDS"))
        {
            saved_word_click_position=position;
            Intent i=new Intent(getApplicationContext(),WordDetails.class);
            i.putExtra("WORD_ID",range_or_word_id);
            i.putExtra("ACTIVITY_TYPE","BARRON333");
            i.putExtra("start_index",start_index);
            i.putExtra("end_index",end_index);
            startActivity(i);
        }
        if(list_type.equals("RANGE_LIST"))
        {
            saved_range_click_position=position;
            prefEditor.putString("LIST_TYPE","RANGE_WORDS"); // updating next type in pref... don't confuse as coming is RANGE list and opening in selected words list
            prefEditor.apply();
            prefEditor.commit();

            List<wordDefinition> range_words = tempDbHandler.getSelectedRangeWords(getAssets(), start_index, end_index,tempDatabase);
            if(range_words.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Initialization in progress.. Please wait!!.",Toast.LENGTH_SHORT).show();
            }else
            {
                initWords(null, range_words,"RANGE_WORDS",start_index,end_index);
            }
        }
        tempDatabase.close();
    }

    @Override
    public void onButtonCheckClicked(View view, int position, int word_id, String bookmarked, wordDefinition aWord, int start_index, int end_index) {

        DatabaseHandler tempDbHandler = new DatabaseHandler(getApplicationContext());
        SQLiteDatabase tempDatabase = tempDbHandler.getReadableDatabase();
        saved_word_click_position = position;
        if (bookmarked.equals("YES")) {
            dbHandler.updateLearntOrBookmarked(tempDatabase, aWord, null, "NO");
        }
        else
            dbHandler.updateLearntOrBookmarked(tempDatabase,aWord,null,"YES");

        List<wordDefinition> range_words = tempDbHandler.getSelectedRangeWords(getAssets(), start_index, end_index,tempDatabase);
        initWords(null, range_words,"RANGE_WORDS",start_index,end_index);
        tempDatabase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void filterWords(String query)
    {
        /*List<wordDefinition> fitleredList=new ArrayList<>();
        for(wordDefinition aWord:allWords)
        {
            if(aWord.getWord().toLowerCase().startsWith(query.toLowerCase()))
            {
                fitleredList.add(aWord);
            }
        }
        mAdapter.setFilter(fitleredList);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public class initialize_data extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject jRootObject = new JSONObject(strings[0]);
                JSONArray jArray= jRootObject.optJSONArray("words");
                for(int i = 0; i<((JSONArray) jArray).length(); ++i) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    Long id=Long.parseLong(jObject.getString("ID"));
                    String word=jObject.getString("WORD");
                    String type=jObject.getString("TYPE");
                    String meaning=jObject.getString("MEANING");
                    String sentense=jObject.getString("SENTENCE");
                    String synonyms=jObject.getString("SYNONYMS");
                    String antonyms=jObject.getString("ANTONYMS");
                    String link=jObject.getString("LINK");
                    String attr1=jObject.getString("ATTR1");
                    String attr2=jObject.getString("ATTR2");
                    int progress = ((i*100)/jArray.length());
                    publishProgress(Integer.toString(progress),Long.toString(id),word,type,meaning,sentense,synonyms,antonyms,link,attr1,attr2);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return "";
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            threadDbHandler.refreshOneWord(threadDatabase,Long.parseLong(values[1]),values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("-1")) {
                prefEditor.putString("INITIALIZE_CODE","FAILED");
                prefEditor.commit();
                Toast.makeText(getApplicationContext(),"Error in initializing data from server!!",Toast.LENGTH_SHORT).show();
            }
            else {
                prefEditor.putString("INITIALIZE_CODE","SUCCESS");
                prefEditor.commit();
                Toast.makeText(getApplicationContext(),"Initialization complete!!",Toast.LENGTH_SHORT).show();
                setTheme(R.style.AppTheme);
                initMainUI();
            }
            threadDatabase.close();
            threadDbHandler = null;
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled(String s) {
            Toast.makeText(getApplicationContext(),"Initialization cancelled!!",Toast.LENGTH_SHORT).show();
            prefEditor.putString("INITIALIZE_CODE","FAILED");
            prefEditor.commit();
            super.onCancelled(s);
        }
    }

    @Override
    public void onBackPressed() {

        String type = sharedPreferences.getString("LIST_TYPE","");
        if (type.equals("") || type.equals("RANGE_LIST"))
        {
            super.onBackPressed();
        }
        if (type.equals("RANGE_WORDS"))
        {
            SQLiteDatabase db = dbHandler.getReadableDatabase();
            List<wordRangeDefinition> allBarronMainList = dbHandler.getBarron333MainList(getAssets(),db);
            initWords(allBarronMainList,null,"RANGE_LIST",-1,-1);
            prefEditor.putString("LIST_TYPE","RANGE_LIST");
            prefEditor.apply();
            prefEditor.commit();
            db.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean refresh_main = sharedPreferences.getBoolean("REFRESH_MAIN",false);
        if(refresh_main)
        {
            prefEditor.putBoolean("REFRESH_MAIN",false);
            prefEditor.apply();
            prefEditor.commit();
            SQLiteDatabase db = dbHandler.getReadableDatabase();
            prefEditor.putString("LIST_TYPE","RANGE_WORDS");
            List<wordDefinition> range_words = dbHandler.getSelectedRangeWords(getAssets(), saved_start_index, saved_end_index,db);
            initWords(null,range_words,"RANGE_WORDS",saved_start_index,saved_end_index);
            db.close();
        }
    }
}
