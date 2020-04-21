package pharamacy.eg.sala.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "order.db";
    public static final String TABLE_NAME = "order_table";

    public static final String COL_ID = "ID";
    public static final String COL_NAMEPRODUCT = "NAMEPRODUCT";
    public static final String COL_NAMECOMPANY = "NAMECOMPANY";
    public static final String COL_PRICE = "PRICE";
    public static final String COL_DICOUNT = "DICOUNT";
    public static final String COL_NUMBEROFFICESID = "NUMBEROFFICESID";
    public static final String COL_NUMBEROFFICES = "NUMBEROFFICES";
    public static final String COL_NUMBERPHARMACY = "NUMBERPHARMACY";

    public static final String COL_DATE = "DATE";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAMEPRODUCT TEXT,NAMECOMPANY TEXT,PRICE DOUBLE,DICOUNT DOUBLE,NUMBEROFFICES TEXT,NUMBEROFFICESID TEXT,NUMBERPHARMACY TEXT,DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);


    }
// todo اضيف هنا الحجات اللي نقصاة رقم الصيدلية واسم المكتب
    public boolean insertData(String nameProduct,String nameCompany, double price, double discount, String numberOffice,String numberOfficeId,String numberPharmacy, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMEPRODUCT, nameProduct);
        contentValues.put(COL_NAMECOMPANY, nameCompany);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_DICOUNT, discount);
        contentValues.put(COL_NUMBEROFFICES, numberOffice);
        contentValues.put(COL_NUMBEROFFICESID, numberOfficeId);
        contentValues.put(COL_NUMBERPHARMACY, numberPharmacy);
        contentValues.put(COL_DATE, date);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //to check whether data is inserted in data base
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);

        return res;
    }
}
