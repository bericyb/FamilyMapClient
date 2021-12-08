package com.bericb.familymap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import requestResult.LoginRequest;
import requestResult.RegisterRequest;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static final String LOGIN_RESULT_STRING = "LoginResult";
    private static final String REGISTER_RESULT_STRING = "RegisterResult";

    private static final String LOGIN_SUCCESS = "success";

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
        setHasOptionsMenu(false);
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

        if (userName.getText().toString().length() != 0
                && password.getText().toString().length() != 0
                && serverPort.getText().toString().length() != 0
                && serverHost.getText().toString().length() != 0) {
            signIn.setEnabled(true);
        } else {
            signIn.setEnabled(false);
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginRequest req = new LoginRequest(userName.getText().toString(),
                        password.getText().toString());

                @SuppressLint("HandlerLeak") Handler uiMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String loginToast = bundle.getString(LOGIN_RESULT_STRING, "Login failed...");

                        //Toast displaying login result.
                        Toast.makeText(getActivity(), loginToast, Toast.LENGTH_SHORT).show();

                        //If the process was a success, then switch fragments after displaying toast.
                        //If the process was a success, then switch fragments after displaying toast.
                        if (bundle.getString(LOGIN_SUCCESS, "false") == "true") {
                            Fragment mapFrag = new MapFragment();
                            //Maybe getSupportFragmentManager()?
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fm.beginTransaction();
                            transaction.replace(R.id.fragment_container, mapFrag, "MAP_FRAG");
                            setHasOptionsMenu(true);

                            transaction.commit();
                        }
                    }
                };

                LoginTask loginTask = new LoginTask(uiMessageHandler, req, serverHost.getText().toString(), serverPort.getText().toString());

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonIndex = gender.getCheckedRadioButtonId();
                Button checkedButton = (RadioButton) gender.getChildAt((buttonIndex % 2));

                RegisterRequest req = new RegisterRequest(userName.getText().toString(),
                        password.getText().toString(),
                        email.getText().toString(), firstName.getText().toString(),
                        lastName.getText().toString(),
                        checkedButton.getText().toString(),
                        null);

                @SuppressLint("HandlerLeak") Handler uiMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        String registerToast = bundle.getString(REGISTER_RESULT_STRING, "Registration failed...");

                        //Toast displaying registration result
                        Toast.makeText(getActivity(), registerToast, Toast.LENGTH_SHORT).show();

                        //If the process was a success, then switch fragments after displaying toast.
                        if (registerToast != "Registration failed...") {
                            Fragment mapFrag = new MapFragment();
                            //Maybe getSupportFragmentManager()?
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fm.beginTransaction();
                            setHasOptionsMenu(true);
                            transaction.replace(R.id.fragment_container, mapFrag, "MAP_FRAG");
                            transaction.commit();
                        }
                    }
                };

                RegisterTask registerTask = new RegisterTask(uiMessageHandler, req, serverHost.getText().toString(), serverPort.getText().toString());

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);

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

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0) {
                    signIn.setEnabled(true);
                } else {
                    signIn.setEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0) {
                    signIn.setEnabled(true);
                } else {
                    signIn.setEnabled(false);
                }
            }
        });

        serverHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0) {
                    signIn.setEnabled(true);
                } else {
                    signIn.setEnabled(false);
                }
            }
        });

        serverPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0) {
                    signIn.setEnabled(true);
                } else {
                    signIn.setEnabled(false);
                }
            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0
                        && firstName.getText().toString().length() != 0
                        && lastName.getText().toString().length() != 0
                        && email.getText().toString().length() != 0
                        && gender.getCheckedRadioButtonId() != -1) {
                    register.setEnabled(true);
                } else {
                    register.setEnabled(false);
                }
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0
                        && firstName.getText().toString().length() != 0
                        && lastName.getText().toString().length() != 0
                        && email.getText().toString().length() != 0
                        && gender.getCheckedRadioButtonId() != -1) {
                    register.setEnabled(true);
                } else {
                    register.setEnabled(false);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userName.getText().toString().length() != 0
                        && password.getText().toString().length() != 0
                        && serverPort.getText().toString().length() != 0
                        && serverHost.getText().toString().length() != 0
                        && firstName.getText().toString().length() != 0
                        && lastName.getText().toString().length() != 0
                        && email.getText().toString().length() != 0
                        && gender.getCheckedRadioButtonId() != -1) {
                    register.setEnabled(true);
                } else {
                    register.setEnabled(false);
                }
            }
        });

        gender.setOnCheckedChangeListener((gender, checkedId) -> {
            if (userName.getText().toString().length() != 0
                    && password.getText().toString().length() != 0
                    && serverPort.getText().toString().length() != 0
                    && serverHost.getText().toString().length() != 0
                    && firstName.getText().toString().length() != 0
                    && lastName.getText().toString().length() != 0
                    && email.getText().toString().length() != 0
                    && gender.getCheckedRadioButtonId() != -1) {
                register.setEnabled(true);
            } else {
                register.setEnabled(false);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return true;
    }
}