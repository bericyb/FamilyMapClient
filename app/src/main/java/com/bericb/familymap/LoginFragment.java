package com.bericb.familymap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup gender;




    private Button signIn;
    private Button register;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Set up buttons and disable by fault
        signIn = view.findViewById(R.id.signIn);
        signIn.setEnabled(false);
        register = view.findViewById(R.id.register);
        register.setEnabled(false);

        //Set up text fields and radio buttons
        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPortField);
        userName = view.findViewById(R.id.usernameField);
        password = view.findViewById(R.id.passwordField);
        firstName = view.findViewById(R.id.firstNameField);
        lastName = view.findViewById(R.id.lastNameField);
        email = view.findViewById(R.id.emailField);
        gender = view.findViewById(R.id.genderButtons);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               TODO: Login stuff that will make you cringe.
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO: Register stuff that will make me gouge my eyes out.
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                TODO: Maybe change a login/register object.
                if(userName.getText().toString() != null && password.getText().toString() != null) {
                    signIn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                TODO: Maybe change a login/register object.
                if(userName.getText().toString() != null && password.getText().toString() != null) {
                    signIn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}