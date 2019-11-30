package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class reminder extends AppCompatActivity {

//    ArrayList<titipan> arrReminder = new ArrayList<titipan>();
    ArrayList<ReminderClass> list_reminder = new ArrayList<>();
    RecyclerView rvReminder;
    ReminderDB db;
    ReminderAdapter adpt;
    EditText edJumlah, edNama;
    Button btnInsert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        setTitle("Reminder");
        rvReminder = findViewById(R.id.lvReminder);
        edJumlah = findViewById(R.id.editTextReminderEntryJumlah);
        edNama = findViewById(R.id.editTextReminderEntryNama);
        btnInsert = findViewById(R.id.buttonReminderInsertEntry);
        list_reminder = new ArrayList<>();

        db = Room.databaseBuilder(this, ReminderDB.class,"db_sdp").build();

        rvReminder.setHasFixedSize(true);
        rvReminder.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        adpt = new ReminderAdapter(this, list_reminder);
        rvReminder.setAdapter(adpt);
        
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edJumlah.getText().toString().equalsIgnoreCase("")&&!edNama.getText().toString().equalsIgnoreCase("")){
                    String nama = edNama.getText().toString();
                    Integer harga = Integer.parseInt(edJumlah.getText().toString());
                    ReminderClass temp = new ReminderClass(nama, harga, false);
                    insertEntry(temp);
                    edNama.setText("");
                    edJumlah.setText("");
                }
                else{
                    Toast.makeText(reminder.this, "Harap semua field diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getData();
    }

    public void removeEntryCheckBox(ReminderClass obj, boolean is_cb_checked){
        ReminderClass temp = obj;
        obj.setIs_checked(is_cb_checked);
        updateEntry(temp);
    }
    public void insertEntry(ReminderClass obj){
        new insertReminder().execute(obj);
    }
    public void getData(){
        new getAllReminder().execute();
    }
    public void removeEntry(ReminderClass obj) {
        new deleteReminder().execute(obj);
    }
    public void updateEntry(ReminderClass obj) {
        new updateReminder().execute(obj);
    }

    private class getAllReminder extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            list_reminder.clear();
            list_reminder.addAll(db.ReminderDAO().getAllReminder());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adpt.notifyDataSetChanged();
        }
    }
    private class insertReminder extends AsyncTask<ReminderClass,Void,Void>{

        @Override
        protected Void doInBackground(ReminderClass... reminderClasses) {
            db.ReminderDAO().addNewReminder(reminderClasses[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getData();
        }
    }
    private class deleteReminder extends AsyncTask<ReminderClass,Void,Void> {
        @Override
        protected Void doInBackground(ReminderClass... reminderClasses) {
            db.ReminderDAO().deleteReminder(reminderClasses[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getData();
        }
    }
    private class updateReminder extends AsyncTask<ReminderClass,Void,Void> {
        @Override
        protected Void doInBackground(ReminderClass... reminderClasses) {
            db.ReminderDAO().updateReminder(reminderClasses[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getData();
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            finish();
        }
        return true;
    }

}
