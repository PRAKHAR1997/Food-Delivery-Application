package com.project.gaurs.tadqa.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.project.gaurs.tadqa.R;
import com.project.gaurs.tadqa.Unnecessary.DatabaseEntry;

public class ThankYouPage extends AppCompatActivity {

    Button order;
    DatabaseEntry databaseEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_page);
        order=(Button)findViewById(R.id.button16);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(ThankYouPage.this);
                SharedPreferences getData = getSharedPreferences("SHIPPING_ADDRESS", Context.MODE_PRIVATE);
                String name1 = getData.getString("firstname", "");
                String name2 = getData.getString("lastname", "");
                String name = name1.toUpperCase()+" "+name2.toUpperCase();
                String add1 = getData.getString("address","");
                String add2 = getData.getString("city","");
                String add3 = getData.getString("zip" , "");
                String add = add1.toUpperCase()+", "+add2.toUpperCase()+", "+add3;
                SharedPreferences getData1= getSharedPreferences("TIME", Context.MODE_PRIVATE);
                String time = getData1.getString("time","");
                Log.e("Time Value", time);
                databaseEntry.addToOrderTable(name, add, time);
                databaseEntry.deleteAll();
                Intent intent = new Intent(ThankYouPage.this, MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
