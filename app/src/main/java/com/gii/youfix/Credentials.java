package com.gii.youfix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Credentials extends AppCompatActivity {

    Spinner city;
    EditText name;
    EditText phone;
    EditText address;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        city = (Spinner)findViewById(R.id.form_city2);
        name = (EditText)findViewById(R.id.form_name2);
        phone = (EditText)findViewById(R.id.form_phone2);
        address = (EditText)findViewById(R.id.form_address2);
        save = (Button)findViewById(R.id.form_save2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlles();
            }
        });
        if (!YouFix.sharedPref.getString("city", "").equals("")) {
            String savedCity = YouFix.sharedPref.getString("city", "");
            String[] cities = getResources().getStringArray(R.array.cities_array);
            for (int i = 0; i < cities.length; i++)
                if (cities[i].equals(savedCity))
                    city.setSelection(i);
        }

        if (!YouFix.sharedPref.getString("name","").equals(""))
            name.setText(YouFix.sharedPref.getString("name",""));
        if (!YouFix.sharedPref.getString("phone","").equals(""))
            phone.setText(YouFix.sharedPref.getString("phone",""));
        if (!YouFix.sharedPref.getString("address","").equals(""))
            address.setText(YouFix.sharedPref.getString("address",""));
    }

    private void saveAlles() {
        YouFix.savePref("city",city.getSelectedItem().toString());
        YouFix.savePref("name",name.getText().toString());
        YouFix.savePref("phone",phone.getText().toString());
        YouFix.savePref("address",address.getText().toString());
        finish();
    }
}
