package com.muhsanjaved.sqlite_database_practice.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class PetContract {

    private PetContract(){}

    //content://com.muhsanjaved.sqlite_database_practice
    public static final String CONTENT_AUTHORITY = "com.muhsanjaved.sqlite_database_practice";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";

    public static final class PetEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

        // THe Mine type of the {@link #Content_Uri} for a list of pets
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +"/"+ CONTENT_AUTHORITY + "/"+ PATH_PETS;

        // THe Mine type of the {@link #Content_Uri} for a single pets
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.ANY_CURSOR_ITEM_TYPE +"/"+ CONTENT_AUTHORITY + "/"+ PATH_PETS;

        public  static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";


        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static boolean isValidGender(int gender){
            if (gender == GENDER_UNKNOWN || gender== GENDER_MALE || gender == GENDER_FEMALE){
                return true;
            }
            return false;
        }
    }
}
