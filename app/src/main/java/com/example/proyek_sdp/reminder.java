package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

public class reminder extends AppCompatActivity {

    ArrayList<titipan> arrReminder = new ArrayList<titipan>();
    ListView lvReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        setTitle("Reminder");
        lvReminder = findViewById(R.id.lvReminder);
        arrReminder.add(new titipan("Kong Guan",true));
        arrReminder.add(new titipan("Shampoo",false));
        arrReminder.add(new titipan("iphone X",true));
        arrReminder.add(new titipan("Payung Pelangi",false));


        lvAdapter adap = new lvAdapter(arrReminder,getApplicationContext());
        lvReminder.setAdapter(adap);

        adap.notifyDataSetChanged();

        lvReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(i);

                // Translate the selected item to DTO object.
                titipan itemDto = (titipan) itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.cbBarang);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.checked)
                {
                    itemCheckbox.setChecked(false);
                    itemDto.checked = false;
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.checked = true;
                }
            }
        });


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

    class titipan{
        boolean checked = false;
        String nama = "";

        public titipan(String nama,boolean checked) {
            this.nama = nama;
            this.checked = checked;
        }
    }

    class lvAdapter extends BaseAdapter{
        ArrayList<titipan> arrtitipan;

        Context ctx = null;

        public lvAdapter(ArrayList<titipan> arrtitipan, Context ctx) {
            this.arrtitipan = arrtitipan;
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            int ret = 0;
            if(arrtitipan!=null){
                ret = arrtitipan.size();
            }
            return ret;
        }

        @Override
        public Object getItem(int i) {
            return arrtitipan.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            listitemholder holder = null;
            if(view!=null){
                holder = (listitemholder) view.getTag();
            }
            else{
                view = View.inflate(ctx,R.layout.list_reminder_layout,null);
                CheckBox listitemcheckbox = (CheckBox)view.findViewById(R.id.cbBarang);
                holder = new listitemholder(view);
                holder.cb = listitemcheckbox;
                holder.cb.setTextColor(Color.BLACK);
                view.setTag(holder);
            }
            titipan titip = arrtitipan.get(i);
            holder.cb.setChecked(titip.checked);
            holder.cb.setText(titip.nama);


            return view;
        }
    }

    class listitemholder extends RecyclerView.ViewHolder{

        CheckBox cb;
        public listitemholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
