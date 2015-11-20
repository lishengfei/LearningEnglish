package carloslobo.com.finalproject.Core;


import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import carloslobo.com.finalproject.Fragments.About;
import carloslobo.com.finalproject.Fragments.Student.LetterIntroductionFragment;
import carloslobo.com.finalproject.Fragments.Student.LetterPracticeFragment;
import carloslobo.com.finalproject.Fragments.Student.LetterTestFragment;
import carloslobo.com.finalproject.Fragments.Student.StudentLetterMenuFragment;
import carloslobo.com.finalproject.Fragments.Teacher.TeacherMenuFragment;
import carloslobo.com.finalproject.Fragments.WelcomeFragment;
import carloslobo.com.finalproject.Modules.GameManager;
import carloslobo.com.finalproject.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getName();
    private int FragmentCount = 0;
    private FragmentManager mManager  = getSupportFragmentManager();
    private GameManager gManager;
    private String CurrentTeacher;
    private String CurrentGroup;

    public GameManager getGManager() {
        return gManager;
    }

    public void addFragmentCount() {
        this.FragmentCount = FragmentCount+1;
    }

    public int getFragmentCount() {
        return FragmentCount;
    }

    public void createGameManager(){
        gManager = new GameManager(this);
    }

    public void setStudentTeacher(String Id){
        CurrentTeacher = Id;
    }

    public String getCurrentTeacher() {
        return CurrentTeacher;
    }

    public void setCurrentGroup(String Id){
        CurrentGroup = Id;
    }

    public String getCurrentGroup(){
        return CurrentGroup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState==null){
            FragmentTransaction mTransaction = mManager.beginTransaction();
            mTransaction.add(R.id.main_container, new WelcomeFragment());
            mTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);}
        else {
                if(!CloseModes()) {
                    if (mManager.getBackStackEntryCount() > 0)
                        mManager.popBackStack();
                    else
                        this.finish();
                }

            HideBanners();
        }
    }


    private void HideBanners(){
        TeacherMenuFragment TeacherLogin = (TeacherMenuFragment) mManager.findFragmentByTag("TeacherMenu");
        StudentLetterMenuFragment StudentLogin = (StudentLetterMenuFragment) mManager.findFragmentByTag("StudentMenu");

        if (  (TeacherLogin != null && TeacherLogin.isVisible()) ||  (StudentLogin != null && StudentLogin.isVisible())  ) {
            AppBarLayout APL = (AppBarLayout) this.findViewById(R.id.test);
            APL.setExpanded(false, true);

        }
    }


    private boolean CloseModes(){

        if(gManager!=null)
            gManager.Terminate();

        /*We need to look for any of these 3 fragments,
        if they're visible they need to be removed since popBackStack doesn't remove them*/

        LetterIntroductionFragment I = (LetterIntroductionFragment) mManager.findFragmentByTag("Introduction " + (FragmentCount-1));
        LetterPracticeFragment P = (LetterPracticeFragment) mManager.findFragmentByTag("Practice " + (FragmentCount-1));
        LetterTestFragment T = (LetterTestFragment) mManager.findFragmentByTag("Test " + (FragmentCount-1));

        boolean I_cond = I!=null && I.isVisible();
        boolean P_cond = P!=null && P.isVisible();
        boolean T_cond = T!=null && T.isVisible();

        if(!I_cond && !P_cond && !T_cond) {
            /*Which means that popBackStack can be executed normally.*/
            return false;}
        else{
             /*In other case a mode has to be closed*/
            FragmentTransaction KillerTransaction, BackToTheMenu;
            KillerTransaction = mManager.beginTransaction();
            BackToTheMenu = mManager.beginTransaction();

            if(I_cond){
                KillerTransaction.remove(I);
                KillerTransaction.commit();
                mManager.executePendingTransactions();
                Log.d(TAG, "Introduction mode was closed");
                FragmentCount = 0;
            }


            if(P_cond){
                KillerTransaction.remove(P);
                KillerTransaction.commit();
                mManager.executePendingTransactions();
                Log.d(TAG, "Practice mode was closed");
                FragmentCount = 0;
            }



            if(T_cond){
                KillerTransaction.remove(T);
                KillerTransaction.commit();
                mManager.executePendingTransactions();
                Log.d(TAG, "Test mode was closed");
                FragmentCount = 0;
            }

            BackToTheMenu.replace(R.id.main_container, mManager.findFragmentByTag("StudentMenu"));
            BackToTheMenu.commit();
            Log.d(TAG, "Welcome back to the student menu <3");

            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.InstProfesores) {
            // Handle the camera action
        } else if (id == R.id.InstEstudiantes) {

        } else if (id == R.id.About) {
            FragmentTransaction mTransaction = mManager.beginTransaction();
            mTransaction.setCustomAnimations(R.anim.fadeout, R.anim.fadein,R.anim.fadeout,R.anim.fadein);
            mTransaction.replace(R.id.main_container,new About(), "About");
            mTransaction.addToBackStack("About");
            mTransaction.commit();

        } else if (id == R.id.Close) {
            finish();
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
