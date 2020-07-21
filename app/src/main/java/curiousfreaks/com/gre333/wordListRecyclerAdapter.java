package curiousfreaks.com.gre333;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gasaini on 2/24/2018.
 */

public class wordListRecyclerAdapter extends RecyclerView.Adapter<wordListRecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private List<wordDefinition> completeWordList;
    private List<wordRangeDefinition> completeRangeList;
    private String list_type;
    private int clickedItemPosition = 0;
    myRecyclerItemClickListenerInterface myListener;
    private int saved_start_index = -1;
    private int saved_end_index = -1;


    public wordListRecyclerAdapter(){}

    public void setAdapterType(List<wordRangeDefinition> wordRange, List<wordDefinition> wordsList, String list_type, int start, int end)
    {
        this.list_type = list_type;
        this.saved_start_index = start;
        this.saved_end_index = end;
        if(wordsList != null)
            completeWordList = wordsList;
        if(wordRange!=null)
            completeRangeList = wordRange;
    }

    public interface myRecyclerItemClickListenerInterface{
        void onRangeOrWordClicked(View view, int position, int range_id, String word_range, int start_index, int end_index, String list_type);
        void onButtonCheckClicked(View view, int position, int word_id, String bookmarked, wordDefinition aWord, int start_index, int end_index);
    }

    public void setOnItemCLickListener(myRecyclerItemClickListenerInterface listener) {
        this.myListener=listener;
    }

    @Override
    public void onClick(View view) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView row,word,meaning,word_or_range;
        CircularProgressBar circularProgressBar;
        ImageView starImage,speakImage, button_check;
        RelativeLayout mainLayout;
        LinearLayout circular_linear_layout;
        public ViewHolder(View view)
        {
            super(view);
            row=view.findViewById(R.id.id_row);
            word_or_range=view.findViewById(R.id.id_word_range);
            circularProgressBar = view.findViewById(R.id.id_words_progress_bar);
            button_check = view.findViewById(R.id.id_check_button);
            meaning = view.findViewById(R.id.id_word_meaning);
        }
    }

    @Override
    public wordListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_word_layout, parent, false);
        return (new ViewHolder(v));
    }

    @Override
    public void onBindViewHolder(final wordListRecyclerAdapter.ViewHolder holder, final int position) {

        if(list_type.equals("RANGE_LIST"))
        {
            wordRangeDefinition aWordRange = completeRangeList.get(position);
            holder.row.setVisibility(View.VISIBLE);
            holder.circularProgressBar.setVisibility(View.VISIBLE);
            holder.button_check.setVisibility(View.INVISIBLE);
            holder.meaning.setVisibility(View.GONE);

            holder.row.setText(Integer.toString(aWordRange.getId()));
            holder.word_or_range.setText(aWordRange.getWord_range());
            int progress = aWordRange.getLearn_count();

            if(aWordRange.getWord_range().contains("Zeal"))
                holder.circularProgressBar.setMaximum(2);
            else
                holder.circularProgressBar.setMaximum(10);
            holder.circularProgressBar.setProgress(progress);

            final int word_range_id= aWordRange.getId();
            final String word_range = aWordRange.getWord_range();
            final int start_index = aWordRange.getStart_index();
            final int end_index = aWordRange.getEnd_index();

            holder.word_or_range.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myListener.onRangeOrWordClicked(view,position, word_range_id,word_range,start_index,end_index,list_type);
                }
            });
        }
        if(list_type.equals("RANGE_WORDS"))
        {
            final wordDefinition aWord = completeWordList.get(position);

            //holder.word_or_range.setTypeface(null,Typeface.NORMAL);
            //holder.word_or_range.setGravity(Gravity.CENTER);
            holder.meaning.setVisibility(View.VISIBLE);
            holder.button_check.setVisibility(View.VISIBLE);
            holder.row.setVisibility(View.GONE);
            holder.circularProgressBar.setVisibility(View.GONE);

            holder.word_or_range.setText(aWord.getWord());
            holder.meaning.setText(aWord.getMeaning());

            final int word_id = (int)aWord.getId();
            final String word = aWord.getWord();
            final String bookmarked = aWord.getBookmarked();

            if (bookmarked.equals("YES"))
                holder.button_check.setImageResource(R.mipmap.button_check);
            else
                holder.button_check.setImageResource(R.mipmap.button_uncheck);

            holder.word_or_range.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myListener.onRangeOrWordClicked(view,position, word_id, word,saved_start_index,saved_end_index,list_type);
                }
            });
            holder.button_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myListener.onButtonCheckClicked(view, position, word_id, bookmarked, aWord,saved_start_index,saved_end_index);
                }
            });
        }

        clickedItemPosition=position;


        //setLIstAnimation(holder.itemView);
        /*holder.meaning.setText(wd.getMeaning());
        holder.word.setText(wd.getWord());
        if ((wd.getBookmarked()).equals("YES"))
            holder.starImage.setImageResource(R.mipmap.yellow_star);
        else
            holder.starImage.setImageResource(R.mipmap.outline_star_green);

        holder.word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.onWordClicked(view, position, uniqueID);
            }
        });
        holder.meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.onMeaningClicked(view, position, uniqueID);
            }
        });

        holder.speakImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.onSpeakClicked(view, position, uniqueID);
            }
        });
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClickItemAnimation(view);
                final View v = view;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myListener.onMainLayoutClicked(v,position,uniqueID);
                    }
                }, 105);


                //view.setBackgroundColor(Color.parseColor("#1b5e20"));
                //notifyItemChanged(selectedItemPosition);
            }
        });

       */

        //holder.itemView.setBackgroundColor(selectedItemPosition == position ? Color.GREEN : Color.TRANSPARENT);

    }

    @Override
    public int getItemCount() {

        if (list_type.equals("RANGE_LIST"))
        {
            return completeRangeList.size();
        }
        if (list_type.equals("RANGE_WORDS"))
        {
            return completeWordList.size();
        }
        else {
            return 0;
        }

        //return completeWordList.size();
    }

    public void setFilter(List<wordDefinition> filteredWords)
    {
        completeWordList = new ArrayList<>();
        completeWordList.addAll(filteredWords);
        notifyDataSetChanged();

    }
    public void autoScrollToPosition(RecyclerView recyclerView, int click_position)
    {
        recyclerView.smoothScrollToPosition(clickedItemPosition);
    }
    public void setLastClickedPosition(int click_position)
    {
        this.clickedItemPosition = click_position;
    }

    public void setLIstAnimation(View view) {

        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation trnsAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        trnsAnimation.setDuration(400);
        animationSet.addAnimation(trnsAnimation);

       /* ScaleAnimation scaleAnim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setDuration(100);
        animationSet.addAnimation(scaleAnim);*/

        view.startAnimation(animationSet);

    }
    public void setClickItemAnimation(View view) {

        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(100);
        view.startAnimation(scaleAnim);
    }
}
