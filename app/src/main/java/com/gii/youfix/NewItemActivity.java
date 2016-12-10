package com.gii.youfix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gii.youfix.order.FirebaseContent;

import java.io.File;

public class NewItemActivity extends AppCompatActivity {

    FirebaseContent.OrderItem newOrderItem = new FirebaseContent.OrderItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder();
            }
        });

        ((Button)findViewById(R.id.form_button_photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YouFix.photoTakeTo = "NewItemActivity";
                takePhoto();
            }
        });

        ((Button)findViewById(R.id.form_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        if (!YouFix.sharedPref.getString("city", "").equals("")) {
            String savedCity = YouFix.sharedPref.getString("city", "");
            String[] cities = getResources().getStringArray(R.array.cities_array);
            for (int i = 0; i < cities.length; i++)
                if (cities[i].equals(savedCity))
                    ((Spinner)findViewById(R.id.form_city)).setSelection(i);
        }

        if (!YouFix.sharedPref.getString("phone","").equals(""))
            ((EditText)findViewById(R.id.form_phone)).setText(YouFix.sharedPref.getString("phone",""));
        if (!YouFix.sharedPref.getString("autoBrand","").equals(""))
            ((EditText)findViewById(R.id.form_auto)).setText(YouFix.sharedPref.getString("autoBrand",""));
        if (!YouFix.sharedPref.getString("year","").equals(""))
            ((EditText)findViewById(R.id.form_year)).setText(YouFix.sharedPref.getString("year",""));
        /* and address? not sure
        if (!YouFix.sharedPref.getString("address","").equals(""))
            address.setText(YouFix.sharedPref.getString("address",""));
            */

    }

    private void sendOrder() {
        String autoBrand = ((EditText)findViewById(R.id.form_auto)).getText().toString();
        String description = ((EditText)findViewById(R.id.form_description)).getText().toString();
        String phone = ((EditText)findViewById(R.id.form_phone)).getText().toString();
        String city = ((Spinner)findViewById(R.id.form_city)).getSelectedItem().toString();
        String year = ((EditText)findViewById(R.id.form_year)).getText().toString();

        YouFix.savePref("autoBrand",autoBrand);
        YouFix.savePref("year",year);

        newOrderItem.ownerId = YouFix.androidID;
        newOrderItem.autoBrand = autoBrand;
        newOrderItem.city = city;
        newOrderItem.year = year;
        newOrderItem.phone = phone;
        newOrderItem.content = autoBrand + ", " + year;
        String details = autoBrand + ", " + year + "\n\n";
        details = details + description;
        newOrderItem.details = details;
        if (!YouFix.sharedPref.getString("name","").equals(""))
            newOrderItem.ownerName = YouFix.sharedPref.getString("name","");


        YouFix.ref.child("youfix/orders/" + newOrderItem.id).setValue(newOrderItem);
        (new AlertDialog.Builder(this)).setMessage("Ваша заявка отправлена")
                //.setMessage(getContext().getString(R.string.enter_amount))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }).show();
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void takePhoto() {
        CameraAndPictures.bitmap = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final String id = CameraAndPictures.savePictureToFirebase();
            if (CameraAndPictures.bitmap != null) {
                ImageView newImage = new ImageView(this);
                newImage.setImageBitmap(CameraAndPictures.bitmap);
                ((LinearLayout)findViewById(R.id.photos_ll)).addView(newImage);
                newOrderItem.photos.add(id);
            }
            //TODO: here just add the id to the object of this window
        }
    }



}
