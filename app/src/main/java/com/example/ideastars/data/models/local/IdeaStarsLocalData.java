package com.example.ideastars.data.models.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.example.ideastars.data.models.Favorite;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.Item;
import com.example.ideastars.data.models.Word;
import com.google.common.primitives.Longs;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class IdeaStarsLocalData implements IdeaStarsData {
    private static IdeaStarsLocalData INSTANCE;

    private IdeaStarsDbHelper mDbHelper;

    // Prevent direct instantiation.
    private IdeaStarsLocalData(@NonNull Context context) {
        checkNotNull(context);
        mDbHelper = new IdeaStarsDbHelper(context);
    }

    public static IdeaStarsLocalData getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new IdeaStarsLocalData(context);
        }
        return INSTANCE;
    }

    public void getIdeas(@NonNull LoadIdeasCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Ideas ideas = getIdeas(db);

        db.close();

        if (null != ideas) {
            callback.onIdeasLoaded(ideas);
        } else {
            callback.onDataNotAvailable();
        }
    }

    private Ideas getIdeas(SQLiteDatabase db) {
        Ideas ideas = new Ideas();
        String[] projection = {
                IdeaStarsLocalEntity.IDEA_COLUMN_ID,
                IdeaStarsLocalEntity.IDEA_COLUMN_NAME,
                IdeaStarsLocalEntity.IDEA_COLUMN_PRIORITY
        };

        Cursor c = db.query(
                IdeaStarsLocalEntity.IDEA_TABLE_NAME, projection, null, null, null, null, null);

        ArrayList<Idea> idea_arr = new ArrayList<Idea>();
        Idea addIdea;
        while (c != null && c.moveToNext()) {
            long idea_id = c.getLong(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.IDEA_COLUMN_ID));
            String idea_name = c.getString(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.IDEA_COLUMN_NAME));
            int idea_priority = c.getInt(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.IDEA_COLUMN_PRIORITY));

            addIdea = new Idea(idea_id, idea_name, idea_priority);
            idea_arr.add(addIdea);

            addIdea.setWords(getWords(db, addIdea));

            addIdea.setFavorites(getFavorites(db, addIdea));
        }

        if (c != null) {
            c.close();
        }

        ideas.setIdeas(idea_arr.toArray(new Idea[idea_arr.size()]));

        return ideas;
    }

    private Word[] getWords(SQLiteDatabase db, Idea idea) {
        String[] projection = {
                IdeaStarsLocalEntity.WORD_COLUMN_ID,
                IdeaStarsLocalEntity.WORD_COLUMN_NAME,
                IdeaStarsLocalEntity.WORD_COLUMN_COLOR,
                IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID,
                IdeaStarsLocalEntity.WORD_COLUMN_PRIORITY
        };

        String query = IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID + " = " + idea.getId();

        long word_idea_id = idea.getId();

        Cursor c = db.query(
                IdeaStarsLocalEntity.WORD_TABLE_NAME, projection, query, null, null, null, null);

        ArrayList<Word> word_arr = new ArrayList<Word>();
        Word addWord;
        while (c != null && c.moveToNext()) {
            long word_id = c.getLong(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.WORD_COLUMN_ID));
            String word_name = c.getString(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.WORD_COLUMN_NAME));
            int word_color = c.getInt(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.WORD_COLUMN_COLOR));
            int word_priority = c.getInt(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.WORD_COLUMN_PRIORITY));

            addWord = new Word(word_id, word_idea_id, word_name, word_color, word_priority);
            word_arr.add(addWord);

            addWord.setItems(getItems(db, addWord));
        }

        if (c != null) {
            c.close();
        }

        return word_arr.toArray(new Word[word_arr.size()]);
    }

    private Item[] getItems(SQLiteDatabase db, Word word) {
        String[] projection = {
                IdeaStarsLocalEntity.ITEM_COLUMN_ID,
                IdeaStarsLocalEntity.ITEM_COLUMN_NAME,
                IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID,
                IdeaStarsLocalEntity.ITEM_COLUMN_PRIORITY
        };

        long item_word_id = word.getId();

        String query = IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID + " = " + item_word_id;

        Cursor c = db.query(
                IdeaStarsLocalEntity.ITEM_TABLE_NAME, projection, query, null, null, null, null);

        ArrayList<Item> item_arr = new ArrayList<Item>();
        Item addItem;
        while (c != null && c.moveToNext()) {
            long item_id = c.getLong(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.ITEM_COLUMN_ID));
            String item_name = c.getString(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.ITEM_COLUMN_NAME));
            int item_priority = c.getInt(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.ITEM_COLUMN_PRIORITY));

            addItem = new Item(item_id, item_word_id, item_name, item_priority);
            item_arr.add(addItem);
        }

        if (c != null) {
            c.close();
        }

        return item_arr.toArray(new Item[item_arr.size()]);
    }

    private Favorite[] getFavorites(SQLiteDatabase db, Idea idea) {
        Word[] words = idea.getWords();
        ArrayList<Long> allItems = new ArrayList<Long>();
        for (Word word : words) {
            for (Item item : word.getItems()) {
                allItems.add(item.getId());
            }
        }

        String query = "SELECT " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID + ","
                + IdeaStarsLocalEntity.FAVORITE_TABLE_NAME + "." + IdeaStarsLocalEntity.FAVORITE_COLUMN_FAV + ","
                + " GROUP_CONCAT( " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID + " ) AS " + IdeaStarsLocalEntity.FAVORITE_ITEM_RENAME_ITEM_ID + " FROM "
                + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME
                + " LEFT JOIN " + IdeaStarsLocalEntity.FAVORITE_TABLE_NAME + " ON "
                + IdeaStarsLocalEntity.FAVORITE_TABLE_NAME + "." + IdeaStarsLocalEntity.FAVORITE_COLUMN_ID + " = "
                + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME + "." + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID
                + " WHERE "
                + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID + " IN( "
                + " SELECT " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID + " FROM "
                + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME + " WHERE "
                + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME + "." + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID + " IN( ? ) )"
                + " GROUP BY " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID;

        Cursor c = db.rawQuery(query, new String[]{Longs.join(",", Longs.toArray(allItems))});

        ArrayList<Favorite> fav_arr = new ArrayList<Favorite>();
        Favorite addFavorite;
        while (c != null && c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID));
            String item_ids_str = c.getString(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.FAVORITE_ITEM_RENAME_ITEM_ID));
            String[] item_ids_str_arr = item_ids_str.split(",");
            long[] item_ids = new long[item_ids_str_arr.length];
            for (int n = 0; n < item_ids.length; ++n) {
                item_ids[n] = Long.parseLong(item_ids_str_arr[n]);
            }
            int fav = c.getInt(c.getColumnIndexOrThrow(IdeaStarsLocalEntity.FAVORITE_COLUMN_FAV));

            addFavorite = new Favorite(id, item_ids, fav);
            fav_arr.add(addFavorite);
        }

        if (c != null) {
            c.close();
        }

        return fav_arr.toArray(new Favorite[fav_arr.size()]);
    }

    public void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(IdeaStarsLocalEntity.IDEA_TABLE_NAME, null, null);
        db.delete(IdeaStarsLocalEntity.WORD_TABLE_NAME, null, null);
        db.delete(IdeaStarsLocalEntity.ITEM_TABLE_NAME, null, null);
        db.delete(IdeaStarsLocalEntity.FAVORITE_TABLE_NAME, null, null);
        db.delete(IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME, null, null);

        db.close();
    }

    public void mergeIdeas(@NonNull Ideas ideas) {

    }

    public void saveIdeas(@NonNull Ideas ideas) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();

        try {
            Idea[] idea_arr = ideas.getIdeas();
            for (Idea idea : idea_arr) {
                saveIdea(db, idea);
                saveFavorites(db, idea);
                saveWords(db, idea);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        db.close();
    }


    public void saveIdea(@NonNull Idea idea) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();

        try {
            saveIdea(db, idea);
            saveFavorites(db, idea);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private void saveIdea(SQLiteDatabase db, @NonNull Idea idea) {
        long idea_id = idea.getId();
        String idea_name = idea.getName();
        int idea_priority = idea.getPriority();

        ContentValues ideaValues = new ContentValues();
        if (idea_id != -1) {
            ideaValues.put(IdeaStarsLocalEntity.IDEA_COLUMN_ID, idea_id); // the execution is different if _id is 2
        }
        ideaValues.put(IdeaStarsLocalEntity.IDEA_COLUMN_NAME, idea_name);
        ideaValues.put(IdeaStarsLocalEntity.IDEA_COLUMN_PRIORITY, idea_priority);

        if (idea_id != -1) {
            db.update(IdeaStarsLocalEntity.IDEA_TABLE_NAME, ideaValues, "_id=?", new String[]{String.valueOf(idea_id)});  // number 1 is the _id here, update to variable for your code
        } else {
            long ideaDbId = db.insert(IdeaStarsLocalEntity.IDEA_TABLE_NAME, null, ideaValues);
            idea.setId(ideaDbId);
        }
    }

    private void saveFavorites(SQLiteDatabase db, @NonNull Idea idea) {
        Favorite[] fav_arr = idea.getFavorites();

        ContentValues favValues = new ContentValues();
        ContentValues favItemValues = new ContentValues();

        for (Favorite fav : fav_arr) {
            favValues.put(IdeaStarsLocalEntity.FAVORITE_COLUMN_FAV, fav.getFavorite());
            if (-1 != fav.getId()) {
                db.update(IdeaStarsLocalEntity.FAVORITE_TABLE_NAME, favValues, "_id=?", new String[]{String.valueOf(fav.getId())});
            } else {
                long favId = db.insert(IdeaStarsLocalEntity.FAVORITE_TABLE_NAME, null, favValues);
                fav.setId(favId);
                long[] itemIds = fav.getItemIds();
                for (int n = 0; n < itemIds.length; ++n) {
                    favItemValues.clear();
                    favItemValues.put(IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID, favId);
                    favItemValues.put(IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID, itemIds[n]);
                    long favItemId = db.insert(IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME, null, favItemValues);
                }
            }
        }
    }

    private void saveWords(SQLiteDatabase db, Idea idea) {
        Word[] words = idea.getWords();
        for (Word word : words) {
            saveWord(db, word);
            saveItems(db, word);
        }
    }

    public void saveWord(@NonNull Word word) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();

        try {
            saveWord(db, word);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private void saveWord(SQLiteDatabase db, @NonNull Word word) {
        long word_id = word.getId();
        String word_name = word.getName();
        int word_color = word.getColor();
        int word_priority = word.getPriority();
        long word_idea_id = word.getIdeaId();

        ContentValues wordValues = new ContentValues();
        if (word_id != -1) {
            wordValues.put(IdeaStarsLocalEntity.WORD_COLUMN_ID, word_id); // the execution is different if _id is 2
        }
        wordValues.put(IdeaStarsLocalEntity.WORD_COLUMN_NAME, word_name);
        wordValues.put(IdeaStarsLocalEntity.WORD_COLUMN_COLOR, word_color);
        wordValues.put(IdeaStarsLocalEntity.WORD_COLUMN_PRIORITY, word_priority);
        wordValues.put(IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID, word_idea_id);

        if (word_id != -1) {
            db.update(IdeaStarsLocalEntity.WORD_TABLE_NAME, wordValues, "_id=?", new String[]{String.valueOf(word_id)});  // number 1 is the _id here, update to variable for your code
        } else {
            long wordDbId = db.insert(IdeaStarsLocalEntity.WORD_TABLE_NAME, null, wordValues);
            word.setId(wordDbId);
            word.setIdeaId(word_idea_id);
        }
    }

    private void saveItems(SQLiteDatabase db, Word word) {
        Item[] items = word.getItems();
        for (Item item : items) {
            saveItem(db, item);
        }
    }

    public void saveItem(@NonNull Item item) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransaction();

        try {
            saveItem(db, item);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    private void saveItem(SQLiteDatabase db, @NonNull Item item) {
        long item_id = item.getId();
        String item_name = item.getName();
        int item_priority = item.getPriority();
        long item_word_id = item.getWordId();

        ContentValues itemValues = new ContentValues();
        if (item_id != -1) {
            itemValues.put(IdeaStarsLocalEntity.ITEM_COLUMN_ID, item_id); // the execution is different if _id is 2
        }
        itemValues.put(IdeaStarsLocalEntity.ITEM_COLUMN_NAME, item_name);
        itemValues.put(IdeaStarsLocalEntity.ITEM_COLUMN_PRIORITY, item_priority);
        itemValues.put(IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID, item_word_id);

        if (item_id != -1) {
            db.update(IdeaStarsLocalEntity.ITEM_TABLE_NAME, itemValues, "_id=?", new String[]{String.valueOf(item_id)});  // number 1 is the _id here, update to variable for your code
        } else {
            long itemDbId = db.insert(IdeaStarsLocalEntity.ITEM_TABLE_NAME, null, itemValues);
            item.setId(itemDbId);
            item.setWordId(item_word_id);
        }
    }

    public void deleteIdeas(long[] ideaIds) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        {
            ArrayList<Long> favIds = new ArrayList<Long>();
            String selectFavSql = "SELECT DISTINCT " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID +
                    " FROM " + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID + " IN " +
                    "( SELECT " + IdeaStarsLocalEntity.ITEM_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID + " IN " +
                    "( SELECT " + IdeaStarsLocalEntity.WORD_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.WORD_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID + " IN (?) " +
                    ")" +
                    ")";
            Cursor cursor = db.rawQuery(selectFavSql, new String[]{Longs.join(",", ideaIds)});
            try {
                while (cursor.moveToNext()) {
                    favIds.add(cursor.getLong(cursor.getColumnIndex(IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID)));
                }
            } finally {
                cursor.close();
            }
            deleteFavorites(db, Longs.toArray(favIds));
        }

        {
            String deleteItemSql = "DELETE FROM " + IdeaStarsLocalEntity.ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID + " IN " +
                    "(SELECT " + IdeaStarsLocalEntity.WORD_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.WORD_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID + " IN (?) )";
            SQLiteStatement stmt = db.compileStatement(deleteItemSql);
            stmt.bindString(1, Longs.join(",", ideaIds));
            stmt.execute();
        }

        {
            String deleteWordSql = "DELETE FROM " + IdeaStarsLocalEntity.WORD_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID + " IN (?)";
            SQLiteStatement stmt = db.compileStatement(deleteWordSql);
            stmt.bindString(1, Longs.join(",", ideaIds));
            stmt.execute();
        }

        {
            String deleteIdeaSql = "DELETE FROM " + IdeaStarsLocalEntity.IDEA_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.IDEA_COLUMN_ID + " IN (?)";
            SQLiteStatement stmt = db.compileStatement(deleteIdeaSql);
            stmt.bindString(1, Longs.join(",", ideaIds));
            stmt.execute();
        }

        db.close();
    }

    public void deleteWords(long[] wordIds) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        {
            ArrayList<Long> favIds = new ArrayList<Long>();
            String selectFavSql = "SELECT DISTINCT " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID +
                    " FROM " + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID + " IN " +
                    "( SELECT " + IdeaStarsLocalEntity.ITEM_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID + " IN " +
                    "( SELECT " + IdeaStarsLocalEntity.WORD_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.WORD_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.WORD_COLUMN_ID + " IN (?) " +
                    ")" +
                    ")";
            Cursor cursor = db.rawQuery(selectFavSql, new String[]{Longs.join(",", wordIds)});
            try {
                while (cursor.moveToNext()) {
                    favIds.add(cursor.getLong(cursor.getColumnIndex(IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID)));
                }
            } finally {
                cursor.close();
            }
            deleteFavorites(db, Longs.toArray(favIds));
        }

        {
            String deleteItemSql = "DELETE FROM " + IdeaStarsLocalEntity.ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID + " IN " +
                    "(SELECT " + IdeaStarsLocalEntity.WORD_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.WORD_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID + " IN (?) )";
            SQLiteStatement stmt = db.compileStatement(deleteItemSql);
            stmt.bindString(1, Longs.join(",", wordIds));
            stmt.execute();
        }

        {
            String deleteWordSql = "DELETE FROM " + IdeaStarsLocalEntity.WORD_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.WORD_COLUMN_ID + " IN (?)";
            SQLiteStatement stmt = db.compileStatement(deleteWordSql);
            stmt.bindString(1, Longs.join(",", wordIds));
            stmt.execute();
        }


        db.close();
    }

    public void deleteItems(long[] itemIds) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        {
            ArrayList<Long> favIds = new ArrayList<Long>();
            String selectFavSql = "SELECT DISTINCT " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID +
                    " FROM " + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID + " IN " +
                        "( SELECT " + IdeaStarsLocalEntity.ITEM_COLUMN_ID + " FROM " + IdeaStarsLocalEntity.ITEM_TABLE_NAME +
                        " WHERE " + IdeaStarsLocalEntity.ITEM_COLUMN_ID + " IN (?) " +
                        ")";
            Cursor cursor = db.rawQuery(selectFavSql, new String[]{Longs.join(",", itemIds)});
            try {
                while (cursor.moveToNext()) {
                    favIds.add(cursor.getLong(cursor.getColumnIndex(IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID)));
                }
            } finally {
                cursor.close();
            }
            deleteFavorites( db, Longs.toArray(favIds));
        }

        {
            String deleteItemSql = "DELETE FROM " + IdeaStarsLocalEntity.ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.ITEM_COLUMN_ID + " IN (?)";
            SQLiteStatement stmt = db.compileStatement(deleteItemSql);
            stmt.bindString(1, Longs.join(",", itemIds));
            stmt.execute();
        }

        db.close();

    }

    public void deleteFavorites( @NonNull SQLiteDatabase db, long[] favIds )
    {
        {
            String deleteItemSql = "DELETE FROM " + IdeaStarsLocalEntity.FAVORITE_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.FAVORITE_COLUMN_ID + " IN (?)";
            SQLiteStatement stmt = db.compileStatement(deleteItemSql);
            stmt.bindString(1, Longs.join(",", favIds));
            stmt.execute();
        }

        {
            String deleteItemSql = "DELETE FROM " + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME +
                    " WHERE " + IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID + " IN (?)";
            SQLiteStatement stmt = db.compileStatement(deleteItemSql);
            stmt.bindString(1, Longs.join(",", favIds));
            stmt.execute();
        }
    }

}
