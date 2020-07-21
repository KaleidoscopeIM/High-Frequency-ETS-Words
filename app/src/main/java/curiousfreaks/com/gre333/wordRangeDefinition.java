package curiousfreaks.com.gre333;

import java.util.Comparator;

public class wordRangeDefinition {

    private int id;
    private int start_index;
    private int end_index;
    private String word_range;
    private int selected_count;

    public wordRangeDefinition()
    {

    }
    public wordRangeDefinition(int id_, String word_range_, int learn_count_ )
    {
        this.id = id_;
        this.word_range = word_range_;
        this.selected_count = learn_count_;
    }

    public int getStart_index() {
        return start_index;
    }

    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }

    public int getEnd_index() {
        return end_index;
    }

    public void setEnd_index(int end_index) {
        this.end_index = end_index;
    }

    public int getSelected_count() {
        return selected_count;
    }

    public void setSelected_count(int selected_count) {
        this.selected_count = selected_count;
    }

    public void clear()
    {
        this.id = -1;
        this.word_range="";
        this.selected_count =-1;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord_range() {
        return word_range;
    }

    public void setWord_range(String word_range) {
        this.word_range = word_range;
    }

    public int getLearn_count() {
        return selected_count;
    }

    public void setLearn_count(int learn_count) {
        this.selected_count = learn_count;
    }
}
