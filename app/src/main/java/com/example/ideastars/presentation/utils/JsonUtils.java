package com.example.ideastars.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hiyakayoyayo on 2017/05/04.
 */

public class JsonUtils {
    public interface IJsonSerializerCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public static class JsonSerializer<T> extends AsyncTask<Void,Void, T> {
        Context mContext;
        Uri mUri;
        IJsonSerializerCallback<T> mCallback;
        Exception mException;

        public JsonSerializer(Context context, Uri uri, IJsonSerializerCallback<T> callback ) {
            mContext = context;
            mUri = uri;
            mCallback = callback;
        }

        @Override
        protected T doInBackground(Void... params) {
            T ret = null;
            // get class JsonSerializer<T>
            Type thisType = this.getClass().getGenericSuperclass();
            // get class T
            Type classType = ((ParameterizedType)thisType).getActualTypeArguments()[0];
            Class<T> entityClass = (Class<T>)classType;

            StringBuilder str = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getContentResolver().openInputStream(mUri)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
                ObjectMapper mapper = new ObjectMapper();
                ret = mapper.readValue(str.toString(), entityClass );
            } catch (IOException e) {
                mException = e;
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(T t) {
            if( null != mCallback ) {
                if( null == mException ) {
                    mCallback.onSuccess(t);
                } else {
                    mCallback.onFailure(mException);
                }
            }
        }
    }

    public interface IJsonDeserializerCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public static class JsonDeserializer extends AsyncTask<Void,Void,Void> {
        Context mContext;
        Uri mUri;
        Object mWriteObject;
        IJsonDeserializerCallback mCallback;
        Exception mException;

        public JsonDeserializer(Context context, Uri uri, Object obj, IJsonDeserializerCallback callback ) {
            mContext = context;
            mUri = uri;
            mWriteObject = obj;
            mCallback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String json = null;

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(mContext.getContentResolver().openOutputStream(mUri)))){
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.writeValueAsString(mWriteObject);
                writer.write(json);
            } catch( IOException e) {
                mException = e;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            if( null != mCallback ) {
                if( null == mException ) {
                    mCallback.onSuccess();
                } else {
                    mCallback.onFailure(mException);
                }
            }
        }
    }

}
