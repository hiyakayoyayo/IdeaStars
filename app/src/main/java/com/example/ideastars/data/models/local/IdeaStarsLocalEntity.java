package com.example.ideastars.data.models.local;

/**
 * Created by hiyakayoyayo on 2017/04/26.
 */

public class IdeaStarsLocalEntity {

// idea table
    public static final String IDEA_TABLE_NAME = "idea";

    public static final String IDEA_COLUMN_ID = "_id";
    public static final String IDEA_COLUMN_NAME = "_name";
    public static final String IDEA_COLUMN_PRIORITY = "_priority";

// word table
    public static final String WORD_TABLE_NAME = "word";

    public static final String WORD_COLUMN_ID = "_id";
    public static final String WORD_COLUMN_NAME = "_name";
    public static final String WORD_COLUMN_COLOR = "_color";
    public static final String WORD_COLUMN_IDEA_ID = "_idea_id";
    public static final String WORD_COLUMN_PRIORITY = "_priority";

// item table
    public static final String ITEM_TABLE_NAME = "item";

    public static final String ITEM_COLUMN_ID = "_id";
    public static final String ITEM_COLUMN_NAME = "_name";
    public static final String ITEM_COLUMN_WORD_ID = "_word_id";
    public static final String ITEM_COLUMN_PRIORITY = "_priority";

// favorite table
    public static final String FAVORITE_TABLE_NAME = "fav";

    public static final String FAVORITE_COLUMN_ID = "_id";
    public static final String FAVORITE_COLUMN_FAV = "_fav";

// favorite item table
    public static final String FAVORITE_ITEM_TABLE_NAME = "fav_item";

    public static final String FAVORITE_ITEM_COLUMN_ID = "_id";
    public static final String FAVORITE_ITEM_COLUMN_FAV_ID = "_fav_id";

    public static final String FAVORITE_ITEM_COLUMN_ITEM_ID = "_item_id";
    public static final String FAVORITE_ITEM_RENAME_ITEM_ID = "_items";


}
