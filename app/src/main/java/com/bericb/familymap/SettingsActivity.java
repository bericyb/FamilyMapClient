package com.bericb.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private Switch lifeStory;
    private Switch familyTree;
    private Switch spouseSwitch;
    private Switch fatherSwitch;
    private Switch motherSwitch;
    private Switch maleSwitch;
    private Switch femaleSwitch;
    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle(R.string.settingsActivity);

        lifeStory = findViewById(R.id.lifeStorySwitch);
        familyTree = findViewById(R.id.familyTreeSwitch);
        spouseSwitch = findViewById(R.id.spouseSwitch);
        fatherSwitch = findViewById(R.id.fatherSwitch);
        motherSwitch = findViewById(R.id.motherSwitch);
        maleSwitch = findViewById(R.id.maleSwitch);
        femaleSwitch = findViewById(R.id.femaleSwitch);
        logout = findViewById(R.id.logout);

        DataCache data = DataCache.getInstance();

        Settings settings = data.getSettings();

        lifeStory.setChecked(settings.isLifeStory());
        familyTree.setChecked(settings.isFamilyTree());
        spouseSwitch.setChecked(settings.isSpouseLines());
        fatherSwitch.setChecked(settings.isFatherSide());
        motherSwitch.setChecked(settings.isMotherSide());
        maleSwitch.setChecked(settings.isMaleEvent());
        femaleSwitch.setChecked(settings.isFemaleEvent());

        lifeStory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeStory(isChecked);
            }
        });

        familyTree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyTree(isChecked);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLines(isChecked);
            }
        });

        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFatherSide(isChecked);
            }
        });

        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setMotherSide(isChecked);
            }
        });

        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setMaleEvent(isChecked);
            }
        });

        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFemaleEvent(isChecked);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().clear();
//                Fragment loginFragment = new LoginFragment();
//                FragmentManager fm = MainActivity.getSupportFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                transaction.add(R.id.fragment_container, loginFragment, "LOGIN_FRAG");
//                transaction.commit();
//                android.app.Fragment main = getParent().getFragmentManager().findFragmentByTag("MAP_FRAG");
//                Fragment fragment = getSupportFragmentManager().findFragmentByTag("MAP_FRAG");
//                if (fragment != null) {
//                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                }

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
        });

    }
}