package com.muhsanjaved.sqlite_database_practice.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.muhsanjaved.sqlite_database_practice.data.PetContract.PetEntry;

public class PetProvider extends ContentProvider {

    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    // Database helper object
    private PetDbHelper mDbHelper;

    // Initialize the provider and the database helper object.

    private static final int PETS = 100;
    private static final int PET_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // static initializer
    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);
    }


    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.

        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    //Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {

            case PETS:
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PET_ID:

                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Query unknown uri " + uri);
        }

        // Notification URI update data
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    //Insert new data into the provider with the given ContentValues.
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        if (match == PETS) {
            return insertPet(uri, values);
        }
        throw new IllegalArgumentException("Inserting is not support for " + uri);

    }

    private Uri insertPet(Uri uri, ContentValues values) {

        //check the name is not null
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        //gender is valid or not
        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Requires valid gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Requires valid weight");
        }
        // No need to check the breed, any value is valid (including null).

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(PetEntry.TABLE_NAME, null, values);

        // If the Id is -1 then the insertion failed. log an error and return null
        if (id == -1) {
            Log.e(LOG_TAG, "failed to insert new row " + uri);
            return null;
        }

        // Notification URI update data
        getContext().getContentResolver().notifyChange(uri, null);
        //Once we know the Id of the new row in the table.
        // return the new URi withId appended to the end of it.

        return ContentUris.withAppendedId(uri, id);
    }

    //Updates the data at the given selection and selection arguments, with the new ContentValues.
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return update(uri, values, selection, selectionArgs);
            case PET_ID:
                //For
                //So
                //ar
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);


            default:
                throw new IllegalArgumentException("update is not Supported for this " + uri);
        }

    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // if
        // check
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet require name");
            }
        }
        //If
        //Check
        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("valid gender required");
            }
        }
        //if
        //check
        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }

        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //return database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);

        // Notification URI update data
        int rowUpdated = database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }

    // Delete the data at the given selection and selection arguments.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //GEt writeable db
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all row that match the selection and selection args
                //return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                rowsDeleted = database.delete(PetEntry.TABLE_NAME,selection, selectionArgs);
                break;

            case PET_ID:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
//               return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                 rowsDeleted = database.delete(PetEntry.TABLE_NAME,selection, selectionArgs);
                 break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    //Returns the MIME type of data for the content URI.
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + "with match" + match);
        }
    }
}
