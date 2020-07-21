package curiousfreaks.com.gre333;

import android.app.SearchManager;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by gasaini on 2/23/2018.
 */

public class CommonAllWords {/*extends AppCompatActivity implements wordListRecyclerAdapter.myRecyclerItemClickListenerInterface{

    public static List<wordDefinition> allWords;
    private RecyclerView recyclerView;
    private wordListRecyclerAdapter mAdapter =null;
    DatabaseHandler dbHandler;
    public SQLiteDatabase database;
    static String ACTIVITY_TYPE;  // ALL_WORDS,  MY_FAV_WORDS,  BARRON_333
    TextToSpeech textToSpeech;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor prefEditor;
    public String allWordsJson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_recycler);
        dbHandler = new DatabaseHandler(getApplicationContext());
        allWords = new ArrayList<>();
        recyclerView = findViewById(R.id.wordLIstRecycler);
        database = dbHandler.getReadableDatabase();
        allWordsJson = dbHandler.getJsonStingUsingAssetManager(getAssets(),"WordsList.json");

        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("ACTIVITY_TYPE")){
            ACTIVITY_TYPE=getIntent().getExtras().getString("ACTIVITY_TYPE");
        }

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!= TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }

            }
        });
        sharedPreferences=getApplicationContext().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        prefEditor=sharedPreferences.edit();
        String status = sharedPreferences.getString("INITIALIZE_CODE","FAILED");
        if(status.equals("FAILED"))
        {
            new CommonAllWords.initialize_data().execute(allWordsJson);
            prefEditor.putBoolean("SYNONYMS",false);
            prefEditor.putBoolean("ANTONYMS",false);
            prefEditor.apply();
            prefEditor.commit();
        }


   *//*     if(ACTIVITY_TYPE.equals("ALL_WORDS")) {
            allWords = dbHandler.getAllWords();
            if(allWords.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Initialization in progress.. Please wait!!.",Toast.LENGTH_SHORT).show();
            }
            else {
                initWords();
            }
        }
        if(ACTIVITY_TYPE.equals("MY_FAV_WORDS")) {
            allWords = dbHandler.getSelectedWords("SELECT * FROM "+DatabaseHandler.TABLE_NAME+" WHERE BOOKMARKED = \"YES\"");
            if(allWords.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"You don't have bookmarked words!!",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Click on star to bookmark a word.",Toast.LENGTH_SHORT).show();
            }else
            {
                initWords();
            }
        }*//*
        if(ACTIVITY_TYPE.equals("BARRON_333")) {
            AssetManager assetManager = getAssets();
            //allWords = dbHandler.getBarron333(assetManager);
            List<wordRangeDefinition> allBarronMainList = new ArrayList<>();
            allBarronMainList = dbHandler.getBarron333MainList(assetManager,database);
            if(allBarronMainList.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Initialization in progress.. Please wait!!.",Toast.LENGTH_SHORT).show();
            }else
            {
                initWords(allBarronMainList,null,"RANGE_LIST");
            }
        }
    }

    @Override
    public void onRangeOrWordClicked(View view, int position, int range_id, String word_range, int start_index, int end_index, String list_type) {
        //String list_type = "SELECTED_RANGE_WORDS";
        if(list_type.equals("SELECTED_RANGE_WORDS"))
        {
            Toast.makeText(getApplicationContext(),"opening details",Toast.LENGTH_SHORT).show();
        }
        if(list_type.equals("RANGE_LIST"))
        {
            List<wordDefinition> selected_range_words_list = dbHandler.getSelectedRangeWords(getAssets(), start_index, end_index,database);
            if(selected_range_words_list.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Initialization in progress.. Please wait!!.",Toast.LENGTH_SHORT).show();
            }else
            {
                initWords(null,selected_range_words_list,"SELECTED_RANGE_WORDS");
            }
        }

    }
    public void initWords(List<wordRangeDefinition> b333MainList, List<wordDefinition> selectedWordsList, String list_type)
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
            //mAdapter = new wordListRecyclerAdapter(b333MainList, list_type);
            mAdapter.setAdapterType("RANGE_LIST",null,b333MainList);
        }

        if(list_type.equals("SELECTED_RANGE_WORDS")) {
            //mAdapter = new wordListRecyclerAdapter(selectedWordsList, list_type, -1);
            mAdapter.setAdapterType("SELECTED_RANGE_WORDS",selectedWordsList,null);
        }

        mAdapter.notifyDataSetChanged();
    }
    *//*public void initWords()
    {
        mAdapter = new wordListRecyclerAdapter(allWords);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);

        *//**//*int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);*//**//*

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemCLickListener(this);
        mAdapter.notifyDataSetChanged();
    }*//*

    *//*@Override
    public void onIDClicked(View view, int position, long uniqueId) {

    }*//*

   *//* @Override
    public void onWordClicked(View view, int position, long uniqueId) {
        Intent i=new Intent(getApplicationContext(),WordDetails.class);
        i.putExtra("uniqueId",uniqueId);
        //i.putExtra("wordsList", (ArrayList<wordDefinition>) allWords);
        startActivity(i);
    }

    @Override
    public void onMeaningClicked(View view, int position, long uniqueId) {
        Intent i=new Intent(getApplicationContext(),WordDetails.class);
        i.putExtra("uniqueId",uniqueId);
        startActivity(i);
    }

    @Override
    public void onMainLayoutClicked(View view, int position, long uniqueId) {
        Intent i=new Intent(getApplicationContext(),WordDetails.class);
        i.putExtra("uniqueId",uniqueId);
        startActivity(i);
    }

    @Override
    public void onStarClicked(View view, int position, long uniqueId) {
        Log.v(MainActivity.TAG,"Entering On click star Image"+uniqueId);
        ImageView starImage=(ImageView)view;
        Iterator<wordDefinition> itr=allWords.iterator();
        wordDefinition aWord=new wordDefinition();
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int success=0;

        while(itr.hasNext())
        {
            aWord=itr.next();
            if(aWord.getId()==uniqueId)
            {

                if(ACTIVITY_TYPE.equals("ALL_WORDS") || ACTIVITY_TYPE.equals("BARRON_333")) {


                    if (aWord.getBookmarked().equals("YES")) {
                        aWord.setBookmarked("NO");
                        success = dbHandler.updateLearntOrBookmarked(db,aWord,null,"NO");
                        starImage.setImageResource(R.mipmap.outline_star_green);
                        break;
                    }
                    if (aWord.getBookmarked().equals("NO")) {
                        aWord.setBookmarked("YES");
                        success = dbHandler.updateLearntOrBookmarked(db,aWord,null,"YES");
                        starImage.setImageResource(R.mipmap.yellow_star);
                        break;
                    }
                }
                if(ACTIVITY_TYPE.equals("MY_FAV_WORDS"))
                {

                    success=dbHandler.updateLearntOrBookmarked(db,aWord,null,"NO");
                    allWords.remove(aWord);
                    mAdapter.notifyDataSetChanged();
                    break;
                }

            }
        }
        db.close();
        if(success==1)
        {
            Log.v(MainActivity.TAG,"Exiting On click star Image"+uniqueId);
        }
    }

    @Override
    public void onSpeakClicked(View view, int position, long uniqueId) {
        ImageView speak=(ImageView)view;
        Iterator<wordDefinition> itr=allWords.iterator();
        wordDefinition aWord;
        String speakWord;
        while(itr.hasNext())
        {
            aWord=itr.next();
            if(aWord.getId()==uniqueId)
            {
                speakWord=aWord.getWord();
                textToSpeech.setSpeechRate(0.7f);
                textToSpeech.speak(speakWord,TextToSpeech.QUEUE_FLUSH,null);
                break;
            }
        }
    }*//*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!allWords.isEmpty()) {
            MenuInflater mi = getMenuInflater();
            mi.inflate(R.menu.search_menu, menu);

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.searchOptionMenuId).getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    filterWords(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    filterWords(s);
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
    public void filterWords(String query)
    {
        List<wordDefinition> fitleredList=new ArrayList<>();
        for(wordDefinition aWord:allWords)
        {
            if(aWord.getWord().toLowerCase().startsWith(query.toLowerCase()))
            {
                fitleredList.add(aWord);
            }
        }
        mAdapter.setFilter(fitleredList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId()==R.id.sortAtoZ)
       {
           Collections.sort(allWords,wordDefinition.alphabeticallyAtoZ);
           mAdapter.notifyDataSetChanged();
       }
       if(item.getItemId()==R.id.sortZtoA)
       {
           Collections.sort(allWords,wordDefinition.alphabeticallyZtoA);
           mAdapter.notifyDataSetChanged();
       }
       if(item.getItemId()==R.id.random)
       {
           Collections.shuffle(allWords);
           mAdapter.notifyDataSetChanged();

       }
       return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if(textToSpeech!=null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
    public class initialize_data extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           *//* if(progressBar.getVisibility()==View.INVISIBLE)
            {
                *//**//*progressBar.setVisibility(View.VISIBLE);
                progressText.setVisibility(View.VISIBLE);
                //downloadBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_OUT);
                progressBar.setMax(100);
                progressBar.setProgress(0);*//**//*
            }*//*
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
                    //Thread.sleep(1);
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
            dbHandler.refreshOneWord(database,Long.parseLong(values[1]),values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9],values[10]);
           *//* progressBar.setProgress(Integer.parseInt(values[0]));
            progressText.setText("Initializing data files.. "+values[0]+"%");*//*
        }
        @Override
        protected void onPostExecute(String s) {
            *//*if(progressBar.getVisibility()==View.VISIBLE) {
                progressBar.setVisibility(View.INVISIBLE);
                progressText.setVisibility(View.INVISIBLE);
            }*//*
            if(s.equals("-1")) {
                prefEditor.putString("INITIALIZE_CODE","FAILED");
                prefEditor.commit();
                Toast.makeText(getApplicationContext(),"Error in initializing data from server!!",Toast.LENGTH_SHORT).show();
            }
            else {
                prefEditor.putString("INITIALIZE_CODE","SUCCESS");
                prefEditor.commit();
                Toast.makeText(getApplicationContext(),"Initialization complete!!",Toast.LENGTH_SHORT).show();
            }
            database.close();
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
*/
}