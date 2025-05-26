package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class veritabani extends SQLiteOpenHelper {

    private static final String VERITABANI_ADI = "kisiler.db";
    private static final int SURUM = 1;

    public veritabani(Context context) {
        super(context, VERITABANI_ADI, null, SURUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE kisiler (id INTEGER PRIMARY KEY AUTOINCREMENT, ad TEXT, soyad TEXT, yas INTEGER, sehir TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kisiler");
        onCreate(db);
    }

    public void kayitEkle(String ad, String soyad, int yas, String sehir) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put("ad", ad);
        degerler.put("soyad", soyad);
        degerler.put("yas", yas);
        degerler.put("sehir", sehir);
        db.insert("kisiler", null, degerler);
        db.close();
    }

    public Cursor kayitlariGetir() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM kisiler", null);
    }

    public void kayitSil(String ad) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("kisiler", "ad=?", new String[]{ad});
        db.close();
    }

    public void kayitGuncelle(String ad, String yeniSoyad, int yeniYas, String yeniSehir) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues guncelDegerler = new ContentValues();
        guncelDegerler.put("soyad", yeniSoyad);
        guncelDegerler.put("yas", yeniYas);
        guncelDegerler.put("sehir", yeniSehir);
        db.update("kisiler", guncelDegerler, "ad=?", new String[]{ad});
        db.close();
    }
}
