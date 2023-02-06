package com.muhsanjaved.sqlite_database_practice;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muhsanjaved.sqlite_database_practice.data.PetContract.PetEntry;
import com.muhsanjaved.sqlite_database_practice.data.PetDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

   // private PetDbHelper mDbHelper;

    private static final int PET_LOADER = 0;
    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        });


      //  mDbHelper = new PetDbHelper(this);
        // displayDatabaseInfo();

        // find the listview which will be populated with the pet data
        ListView petListView =findViewById(R.id.list);

        //Find and set empty view on the listview, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this ,null);
        petListView.setAdapter(mCursorAdapter);

        //setup the item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent= new Intent(MainActivity.this, EditorActivity.class);
                // set id uri item
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);

                //setting the URI on the data field of the intent
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        //Kick off the loader
        getLoaderManager().initLoader(PET_LOADER,null,this);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */

    private void displayDatabaseInfo() {
//        // To access our database, we instantiate our subclass of SQLiteOpenHelper
//        // and pass the context, which is the current activity.
//        PetDbHelper mDbHelper = new PetDbHelper(this);

//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mPetDbHelper.getReadableDatabase();

//        // Perform this raw SQL query "SELECT * FROM pets"
//        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);

//        String[] projection = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT
//        };

//        Cursor cursor = db.query(
//                PetEntry.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        );

//        Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
        /*TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).


            displayView.setText("The pets table contains  " + cursor.getCount() + "pets\n\n");

            displayView.append(PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " +
                    PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " +
                    PetEntry.COLUMN_PET_WEIGHT + "\n"
            );

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));

            }


        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }*/

//
//        ListView petListView =findViewById(R.id.list);
//        // Setup Adapter Cursor
//        PetCursorAdapter adapter = new PetCursorAdapter(this,cursor);
//
//        //Attach the adapter
//        petListView.setAdapter(adapter);



    }

    private void insertPets() {

//        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "TOTO");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

//        long newRoID = db.insert(PetEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "new Ro Id   " + newUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets()
    {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
        Log.v("Catalog Activity" , rowsDeleted + " rows deleted from pet database");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                Toast.makeText(this, "Do nothing for now", Toast.LENGTH_SHORT).show();
                insertPets();
                break;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection ={
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}