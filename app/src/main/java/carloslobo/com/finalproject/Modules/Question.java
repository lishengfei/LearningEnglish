package carloslobo.com.finalproject.Modules;

import android.graphics.Bitmap;

import org.json.JSONArray;

/**
 * Created by camilo on 11/14/15.
 */
public class Question {
    private String objId;
    private String Answer;
    private Bitmap Image;
    private String[] Options;
    private String Letter;


    public Question(String Id,String Letter, String answer, Bitmap image, String[] Options) {
        this.objId = Id;
        this.Letter = Letter;
        Answer = answer;
        Image = image;
        this.Options = Options;
    }

    public String getAnswer() {
        return Answer;
    }

    public Bitmap getImage() {
        return Image;
    }

    public String getOption(int index){
        if(index<Options.length)
            return Options[index];
        return "";
    }

}
