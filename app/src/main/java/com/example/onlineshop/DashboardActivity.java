package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ArrayList<Item> listItem;
    private TextView textTotal;
    private int Total = 0;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String SESSION = "session";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setDataItem();
        buildRecyclerView();

        textTotal = (TextView) findViewById(R.id.textTotal);
    }

    public void setDataItem(){
        listItem = new ArrayList<>();
        listItem.add(new Item("Gazelle High - Merah",R.drawable.gazellemerah, 450000, String.valueOf(getText(R.string.detailProduk1))));
        listItem.add(new Item("Gazelle High - Biru",R.drawable.gazellebiru, 450000, String.valueOf(getText(R.string.detailProduk2))));
        listItem.add(new Item("Gazelle High - Hitam",R.drawable.gazellehitam, 450000, String.valueOf(getText(R.string.detailProduk3))));
        listItem.add(new Item("Air Jordan 1",R.drawable.jordan, 2000000, String.valueOf(getText(R.string.detailProduk4))));
        listItem.add(new Item("Air Jordan Chicago",R.drawable.chicago, 2400000, String.valueOf(getText(R.string.detailProduk5))));
        listItem.add(new Item("Gazelle High - Mocca", R.drawable.gazellemocca,450000, String.valueOf(getText(R.string.detailProduk6))));
    }

    public void buildRecyclerView(){
        mRecyclerView   = findViewById(R.id.recyclerview_id);
        mLayoutManager  = new GridLayoutManager(this, 2);
        mAdapter        = new RecyclerViewAdapter(this, listItem);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onTitleClick(int position) {
                Intent moveDetail = new Intent(DashboardActivity.this, DetailActivity.class);

                moveDetail.putExtra("imageItem", listItem.get(position).getImageItem());
                moveDetail.putExtra("titleItem", listItem.get(position).getTitleItem());
                moveDetail.putExtra("priceItem", listItem.get(position).getPriceItem());
                moveDetail.putExtra("deskripsiItem", listItem.get(position).getDeskripsiItem());

                startActivity(moveDetail);
            }

            @Override
            public void onImageClick(int position) {
                Total = Total + listItem.get(position).getPriceItem();
                textTotal.setText("Total : " + String.valueOf(formatIDR(Total)));
            }
        });
    }

    public String formatIDR(int total){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(total);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuPembayaran:
                Intent moveData = new Intent(this, PaymentActivity.class);
                moveData.putExtra("totalPrice", Total);
                startActivity(moveData);
                break;
            case R.id.menuCallCenter:
                String TelpNo = "082358215937";
                Intent telpIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + TelpNo));
                startActivity(telpIntent);
                break;
            case R.id.menuSMSCenter:
                String phoneNo = "082358215937";
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
                startActivity(smsIntent);
                break;
            case R.id.menuLokasi:
                String location = "Semarang";
                Uri uri = Uri.parse("geo:0,0?q="+ location);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;
            case R.id.menuUpdate:
                pref  = getSharedPreferences(SESSION, MODE_PRIVATE);
                Intent moveUpdate = new Intent(this,UpdateActivity.class);
                moveUpdate.putExtra("username", pref.getString("username", "notfound"));
                moveUpdate.putExtra("password", pref.getString("password", "notfound"));
                startActivity(moveUpdate);
                break;
            case R.id.menuLogout:
                moveIntent(this, MainActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveIntent(Context context, Class nameClass){
        Intent move = new Intent(context, nameClass);
        startActivity(move);
    }

    public void moveDataIntent(View view) {
        Intent moveData = new Intent(this, PaymentActivity.class);
        moveData.putExtra("totalPrice", Total);
        startActivity(moveData);
    }
}