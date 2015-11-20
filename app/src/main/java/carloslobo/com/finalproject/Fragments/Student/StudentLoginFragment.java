package carloslobo.com.finalproject.Fragments.Student;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import carloslobo.com.finalproject.Fragments.Teacher.TeacherMenuFragment;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentLoginFragment extends Fragment implements Init, View.OnClickListener{

    private final static String TAG = StudentLoginFragment.class.getName();

    TextInputLayout mUser;
    Button mButton;

    public StudentLoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_student_login, container, false);

        initViews(rootview);
        initListeners();

        return rootview;
    }

    @Override
    public void initViews(View rootView){
        mUser = (TextInputLayout) rootView.findViewById(R.id.studentUserName);
        mButton = (Button) rootView.findViewById(R.id.studentLoginButton);
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private boolean validateInput() {
        String user = mUser.getEditText().getText().toString();

        if (user.isEmpty()) {
                mUser.setError("El campo de usuario esta vacío");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.studentLoginButton:
                Log.d(TAG,"Student loggin attempt");

                if(validateInput())
                    LogInAttempt();
            break;
        }
    }

    private void LogInAttempt(){
        String userName = mUser.getEditText().getText().toString();
        String password = userName;

        ParseUser.logInInBackground(userName,password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null && user != null) {
                    //User Exists
                    String role = user.get("Role").toString();

                    if(role.equals("Student"))
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
        Log.d(TAG,"Student identified");
        Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
        Transaction();
    }

    public void Mismatch(){
        Log.d(TAG, "User identified but not a student.");
        Toast.makeText(getActivity(), "El usuario ingresado no es un estudiante.", Toast.LENGTH_LONG).show();
    }

    public void Failure(){
        Log.d(TAG,"Student not found.");
        Toast.makeText(getActivity(), "El usuario no pudo ser encontrado.", Toast.LENGTH_LONG).show();
    }

    public void Unknown(){
        Log.d(TAG,"Something weird happened.");
        Toast.makeText(getActivity(),"Hubo un error inesperado",Toast.LENGTH_LONG).show();
    }

    private void Transaction(){
        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.fadeout, R.anim.fadein, R.anim.fadeout, R.anim.fadein);
        mTransaction.replace(R.id.main_container, new StudentLetterMenuFragment(), "StudentMenu");
        mTransaction.addToBackStack("StudentMenu");
        mTransaction.commit();
    }
}
