package carloslobo.com.finalproject.Fragments.Teacher;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


public class TeacherLoginFragment extends Fragment implements Init,View.OnClickListener{

    private final static String TAG = TeacherLoginFragment.class.getName();

    //Layout
    TextInputLayout mUser, mPassword;
    Button mButton;

    public TeacherLoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_teacher_login, container, false);

        initViews(rootView);
        initListeners();

        return rootView;
    }

    @Override
    public void initViews(View rootView){
        mUser = (TextInputLayout) rootView.findViewById(R.id.teacherUserName);
        mPassword = (TextInputLayout) rootView.findViewById(R.id.teacherPassword);
        mButton = (Button) rootView.findViewById(R.id.teacherLoginButton);
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private boolean validateInput() {
        String user = mUser.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        if (user.isEmpty() || password.isEmpty()) {
            if (user.isEmpty())
                mUser.setError("El campo de usuario esta vacío");

            if (password.isEmpty())
                mPassword.setError("El campo de contraseña esta vacío");

            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.teacherLoginButton:
                Log.d(TAG,"Teacher login attempt");

                if (validateInput())
                    LogInAttempt();

                break;
        }
    }

    //I should do an internal class
    private void LogInAttempt(){
        String userName = mUser.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();


        ParseUser.logInInBackground(userName,password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null && user != null) {
                    //User Exists
                    String role = user.get("Role").toString();

                    if(role.equals("Teacher"))
                        Success();
                    else
                        Mismatch();

                } else if (user == null) {
                    //Typo in something
                    Failure();

                } else {
                    Unknown();
                }
            }
        });
    }

    public void Success(){
        Log.d(TAG,"Teacher identified");
        Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
        Transaction();
    }

    public void Mismatch(){
        Log.d(TAG, "User identified but not a teacher.");
        Toast.makeText(getActivity(), "El usuario ingresado no es un profesor.", Toast.LENGTH_SHORT).show();
    }

    public void Failure(){
        Log.d(TAG,"Teacher not found.");
        Toast.makeText(getActivity(), "El usuario no pudo ser encontrado.", Toast.LENGTH_SHORT).show();
    }

    public void Unknown(){
        Log.d(TAG,"Something weird happened.");
        Toast.makeText(getActivity(),"Hubo un error inesperado",Toast.LENGTH_SHORT).show();
    }

    private void Transaction(){
        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.fadeout, R.anim.fadein, R.anim.fadeout, R.anim.fadein);
        mTransaction.replace(R.id.main_container, new TeacherMenuFragment(), "TeacherMenu");
        mTransaction.addToBackStack("TeacherMenu");
        mTransaction.commit();
    }
}
