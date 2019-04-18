package com.android.wassally.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.wassally.R;

public class NewOrderActivity extends AppCompatActivity {

    private EditText mToPersonEditText;
    private EditText mFromPlaceEditText;
    private EditText mToPlaceEditText;
    private EditText mWeightEditText;
    private EditText mOfferEditText;
    private EditText mDurationEditText;
    private EditText mNoteEditTet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        setTitle("New Order");

        mToPersonEditText =findViewById(R.id.recipient_name_et);
        mFromPlaceEditText = findViewById(R.id.picking_location_et);
        mToPlaceEditText = findViewById(R.id.recipient_location_et);
        mWeightEditText = findViewById(R.id.package_weight_et);
        mOfferEditText = findViewById(R.id.package_cost_et);
        mDurationEditText = findViewById(R.id.duration_et);
        mNoteEditTet = findViewById(R.id.client_note_et);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_order_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_send_new_order:
                createNewOrder();

        }
    return true;
    }

    private void createNewOrder(){
        String toPerson = mToPersonEditText.getText().toString().trim();
        String toPlace = mToPlaceEditText.getText().toString().trim();
        String fromPlace = mFromPlaceEditText.getText().toString().trim();
        String note = mNoteEditTet.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString();
        String offerString = mOfferEditText.getText().toString();
        String durationString = mDurationEditText.getText().toString();

        if (TextUtils.isEmpty(toPerson)||TextUtils.isEmpty(toPlace)||TextUtils.isEmpty(fromPlace)
                ||TextUtils.isEmpty(weightString)||TextUtils.isEmpty(offerString)|| TextUtils.isEmpty(durationString)){
            Toast.makeText(this,"Please fullFill all fields ",Toast.LENGTH_SHORT).show();

        }else {
            int weight = Integer.getInteger(weightString,0);
            int offer = Integer.getInteger(offerString,0);
            int duration = Integer.getInteger(durationString,0);

            Toast.makeText(this,"successfully created new order ",Toast.LENGTH_SHORT).show();

        }

    }

}
