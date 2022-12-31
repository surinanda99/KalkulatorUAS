package com.example.kalkualtoruas;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Data> mExampleList;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RadioGroup Menu;
    RadioButton tambahData, kurangData, kaliData, dataBagi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        buildRecyclerView();
        setInsertButton();

        Button buttonHapus = findViewById(R.id.btnhapus);
        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        Menu = findViewById(R.id.Menu);
        tambahData = findViewById(R.id.tambah);
        kurangData = findViewById(R.id.kurang);
        kaliData = findViewById(R.id.kali);
        dataBagi = findViewById(R.id.bagi);

        Menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (tambahData.isChecked()) {
                    Toast.makeText(MainActivity.this, "Tambah", Toast.LENGTH_SHORT).show();
                } else if (kurangData.isChecked()) {
                    Toast.makeText(MainActivity.this, "Kurang", Toast.LENGTH_SHORT).show();
                } else if (kaliData.isChecked()) {
                    Toast.makeText(MainActivity.this, "Kali", Toast.LENGTH_SHORT).show();
                } else if (dataBagi.isChecked()) {
                    Toast.makeText(MainActivity.this, "Bagi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mExampleList);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Data>>() {}.getType();
        mExampleList = gson.fromJson(json, type);

        if (mExampleList == null) {
            mExampleList = new ArrayList<>();
        }
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setInsertButton() {
        Button buttonInsert = findViewById(R.id.btntambah);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText line1 = findViewById(R.id.text1);
                EditText line2 = findViewById(R.id.text2);
                insertItem(line1.getText().toString(), line2.getText().toString());
                saveData();
            }
        });
    }

    private void insertItem(String line1, String line2) {
        Double hasil = Double.valueOf(0);
        if (tambahData.isChecked()) {
            hasil = Double.parseDouble(line1) + Double.parseDouble(line2);
        } else if (kurangData.isChecked()) {
            hasil = Double.parseDouble(line1) - Double.parseDouble(line2);
        } else if (kaliData.isChecked()) {
            hasil = Double.parseDouble(line1) * Double.parseDouble(line2);
        } else if (dataBagi.isChecked()) {
            hasil = Double.parseDouble(line1) / Double.parseDouble(line2);
        }

        // memunculkan operasi yang dipilih dari operasiGroup ke dalam hasil
        String operasi = "";
        if (tambahData.isChecked()) {
            operasi = "+";
        } else if (kurangData.isChecked()) {
            operasi = "-";
        } else if (kaliData.isChecked()) {
            operasi = "x";
        } else if (dataBagi.isChecked()) {
            operasi = ":";
        }

        mExampleList.add(new Data(line1, operasi, line2, String.valueOf(hasil)));
        mAdapter.notifyItemInserted(mExampleList.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mExampleList.clear();
        mAdapter.notifyDataSetChanged();
    }

}