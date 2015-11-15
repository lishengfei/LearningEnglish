package carloslobo.com.finalproject.Modules;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Fragments.Student.LetterIntroductionFragment;
import carloslobo.com.finalproject.Fragments.Student.LetterPracticeFragment;
import carloslobo.com.finalproject.Fragments.Student.LetterTestFragment;
import carloslobo.com.finalproject.Modules.Interfaces.Async;
import carloslobo.com.finalproject.R;

/**
 * Created by camilo on 11/14/15.
 */
public class GameManager {

    //Basic
    private final String TAG = GameManager.class.getName();
    private final MainActivity mContext;

    //Transaction TAGs
    private String TARGET_TAG;
    private String NEXT_TARGET_TAG;

    //For Question fetching and navigating
    private int nExercises = 0;
    private int CurrentExercise = 1;

    //To retreive info from Parse
    private ParseObject WorkingLetter;
    private String LETTER;
    private ParseObject WorkingGroup;
    private ArrayList<Question> QuestionsSet = new ArrayList();

    //Exams Params
    private ParseObject Progress;
    private int nWrong;
    private int nOK;
    private float Score;
    private String MODULE;


    public GameManager(MainActivity mContext){
        this.mContext = mContext;
        this.Progress = new ParseObject("Grades");
        Progress.put("Student", ParseUser.getCurrentUser());
        this.nExercises =  3;
    }

    public String getLetter(){
        return LETTER;
    }

    public int getExercisesNumber() {
        return nExercises;
    }

    public int getCurrentExercise() {
        return CurrentExercise - 1;
    }

    public Question getQuestion(int index){
        return QuestionsSet.get(index);
    }

    public void NextQuestion() {
        this.CurrentExercise +=1;
    }

    private ParseObject NewParseObject(String TableName, String Id) throws ParseException {
        ParseObject mObject = ParseObject.createWithoutData(TableName, Id);
        return mObject;
    }

    public void setWorkingGroup(String Id) throws ParseException {
        WorkingGroup = NewParseObject("Group",Id);
    }

    public void setWorkingLetter(String Id) throws ParseException {
        WorkingLetter = NewParseObject("Letter",Id);
    }

    public ParseObject getWorkingLetter() {
        return WorkingLetter;
    }

    public ParseObject getWorkingGroup() {
        return WorkingGroup;
    }

    public void StartMode(String Mode){

        int i = mContext.getFragmentCount();
        MODULE = Mode;
        TARGET_TAG = Mode + " " + i;

        mContext.addFragmentCount();

        Log.d(TAG, "Fragment " + Mode + " " + i + " going in");

        new GetQuestions().execute();
    }


    public void ModeReady() {
        Collections.shuffle(QuestionsSet);

        Log.d(TAG, "Shuffled Questions");

        FragmentTransaction mTransaction = mContext.getSupportFragmentManager().beginTransaction();

        if(MODULE.equals("Introduction"))
            mTransaction.replace(R.id.main_container, new LetterIntroductionFragment(), TARGET_TAG);

        if(MODULE.equals("Practice"))
            mTransaction.replace(R.id.main_container, new LetterPracticeFragment(), TARGET_TAG);

        if(MODULE.equals("Test"))
            mTransaction.replace(R.id.main_container, new LetterTestFragment(), TARGET_TAG);

        mTransaction.commit();
    }

    public boolean isCompleted(){
        if(CurrentExercise > nExercises)
            return true;
        else
            return false;
    }

    public void Terminate(){
        this.QuestionsSet.clear();
    }

    public void Finish(){

        Score = (float) ((nOK * 100.0) / (nOK + nWrong));
        Progress.put("Score",Score);
        Progress.put("Letter",WorkingLetter);
        Progress.put("Teacher", ParseObject.createWithoutData("_User", mContext.getCurrentTeacher()));
        Progress.put("Module", MODULE);

        new SaveProgress().execute();

    }

    public void SaveAnswer(String Input){

        if (this.isCompleted()) {
            this.Finish();}
        else
            {
                this.UpdateProgress(Input);
                this.FetchNewQuestion();
            }
    }

    private void UpdateProgress(String Input){
            Question CurrentQuestion = getQuestion(getCurrentExercise());
            if(Input.equals(CurrentQuestion.getAnswer()))
                Progress.put("Good",++nOK);
            else
                Progress.put("Wrong",++nWrong);
    }

    public void FetchNewQuestion(){
        if(QuestionsSet.size() > 0)
            QuestionsSet.set(getCurrentExercise(),null);

        this.NextQuestion();

        int i = mContext.getFragmentCount();
        String FRAGMENT_TYPE = "";

        FragmentManager FM = mContext.getSupportFragmentManager();

        //Prepare the fragments for the transactions

        Fragment NEW_FRAGMENT = null;

        if(MODULE.equals("Introduction")) {
            NEW_FRAGMENT = new LetterIntroductionFragment();
            FRAGMENT_TYPE = "Introduction ";}

        else if(MODULE.equals("Practice")) {
            NEW_FRAGMENT = new LetterPracticeFragment();
            FRAGMENT_TYPE = "Practice ";}

        else if(MODULE.equals("Test")) {
            NEW_FRAGMENT = new LetterTestFragment();
            FRAGMENT_TYPE = "Test ";}

        TARGET_TAG = FRAGMENT_TYPE + (i-1);
        NEXT_TARGET_TAG = FRAGMENT_TYPE + i;

        Fragment OLD_FRAGMENT = FM.findFragmentByTag(TARGET_TAG);


        //Add the new fragment
        FragmentTransaction mTransaction = FM.beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        mTransaction.replace(R.id.main_container, NEW_FRAGMENT , NEXT_TARGET_TAG);
        mTransaction.commit();

        //Delete the old fragment
        FragmentTransaction nTransaction = FM.beginTransaction();
        nTransaction.remove(OLD_FRAGMENT);
        nTransaction.commit();
        FM.executePendingTransactions();

        mContext.addFragmentCount();
    }

    private class GetQuestions extends AsyncTask<Void,Void,Void> implements Async {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Setup();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try { Query(); }

            catch (ParseException e) {  e.printStackTrace();    }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ModeReady();
        }

        @Override
        public void Setup() {
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Retrieveing Questions");
            pDialog.setMessage("This may take a while");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        public void Query() throws ParseException {
            //Get all the questions with the letter the user has choosen
            ParseQuery<ParseObject> QUESTIONS_QUERY = new ParseQuery<>("Questions");
            QUESTIONS_QUERY.whereEqualTo("Letter", WorkingLetter);
            List<ParseObject> QUESTIONS_RESULT = QUESTIONS_QUERY.find();

            //Find the Letter
            ParseQuery<ParseObject> LETTER_QUERY = new ParseQuery<>("Letter");
            LETTER_QUERY.whereEqualTo("objectId", WorkingLetter.getObjectId());
            List<ParseObject> LETTER_RESULT = LETTER_QUERY.find();

            if(QUESTIONS_RESULT==null||QUESTIONS_RESULT.size()==0){
                Finalize(false);}
            else {
                LETTER = LETTER_RESULT.get(0).getString("Letter");

                for (ParseObject JSON:QUESTIONS_RESULT){
                    ProcessQuery(JSON);}
                Finalize(true);

            }
        }

        @Override
        public void ProcessQuery(ParseObject JSON) throws ParseException {
            //Find the attributes
            String Id = JSON.getObjectId();

            ParseFile imageFile = JSON.getParseFile("Image");
            byte[] IMG_BYTES = imageFile.getData();
            Bitmap questionImage = BitmapFactory.decodeByteArray(IMG_BYTES, 0, IMG_BYTES.length);

            String questionAnswer = JSON.getString("Answer");
            JSONArray Options = JSON.getJSONArray("Options");

            //I should fix this part, a double request per question seems bad.
            ParseQuery<ParseObject> mParseQuery2 = new ParseQuery<>("QuestionOptions");
            mParseQuery2.whereContainedIn("objectId",toList(Options));
            List<ParseObject> OptionsList = mParseQuery2.find();

            //Add new question to the set
            QuestionsSet.add(new Question(Id, LETTER, questionAnswer, questionImage, toArray(OptionsList) ));

            Log.d(TAG, "A JSON object was processed");
            Log.d(TAG,"Assets Loaded " + questionAnswer);
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if (!Success) {
                Log.d(TAG,"No questions were found");
            } else {
                Log.d(TAG, "Questions were loaded");
            }

        }

    }

    private class SaveProgress extends AsyncTask<Void,Void,Void> implements Async{

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Setup();
        }

        @Override
        public void Setup() {
            pDialog = new ProgressDialog(mContext);
            pDialog.setTitle("Saving this intent");
            pDialog.setMessage("This may take a moment");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Score = (float) ((nOK * 100.0) / (nOK + nWrong));
                Progress.put("Score",Score);
                Progress.put("Letter",WorkingLetter);
                Progress.put("Teacher", ParseObject.createWithoutData("_User",mContext.getCurrentTeacher() ));
                Progress.put("Module", MODULE);

                Progress.save();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            Terminate();
            mContext.onBackPressed();
        }

        @Override
        public void Query() throws ParseException {
            return;
        }
        @Override
        public void ProcessQuery(ParseObject JSON) throws ParseException {
            return;
        }
        @Override
        public void Finalize(boolean Success) {
            return;
        }

    }

    //Utility Arrays to convert Collections.
    public List toList(JSONArray J){
        List L = new ArrayList();
        for (int i=0; i<J.length(); i++){
            try {   L.add(J.get(i));    }
            catch (JSONException e) {   e.printStackTrace();    }
        }

        return L;
    }

    public String[] toArray(List<ParseObject> J)  {
        String[] arr = new String[J.size()];
        for (int i=0; i<J.size();i++){
            try {
                arr[i] = J.get(i).getString("Option");
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        return arr;
    }


}
