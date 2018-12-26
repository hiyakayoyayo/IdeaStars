package com.example.ideastars.data.models.document;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.utils.JsonUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class IdeaStarsDocumentData {
    private static IdeaStarsDocumentData INSTANCE;

    private Context mContext;

    // Prevent direct instantiation.
    private IdeaStarsDocumentData(@NonNull Context context) {
        mContext = checkNotNull(context);
    }

    public static IdeaStarsDocumentData getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new IdeaStarsDocumentData(context);
        }
        return INSTANCE;
    }

    public void getIdeas(@NonNull String inputStr, @NonNull final IdeaStarsData.LoadIdeasCallback callback)
    {
        Uri uri = Uri.parse(inputStr);

        JsonUtils.IJsonSerializerCallback<Ideas> serializerCallback = new JsonUtils.IJsonSerializerCallback<Ideas>(){
            @Override
            public void onSuccess( Ideas result )
            {
                callback.onIdeasLoaded(result);
            }
            @Override
            public void onFailure( Exception e )
            {
                callback.onDataNotAvailable();
            }
        };
        JsonUtils.JsonSerializer<Ideas> serializer = new JsonUtils.JsonSerializer<Ideas>( mContext, uri, serializerCallback );

        serializer.execute();
    }

    public void saveIdeas(@NonNull String outputStr, @NonNull Ideas ideas, @NonNull final IdeaStarsData.SaveIdeasCallback callback )
    {
        Uri uri = Uri.parse(outputStr);

        JsonUtils.IJsonDeserializerCallback deserializerCallback = new JsonUtils.IJsonDeserializerCallback() {
            @Override
            public void onSuccess() {
                callback.onSaved();
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailed();
            }
        };
        JsonUtils.JsonDeserializer deserializer = new JsonUtils.JsonDeserializer( mContext, uri, ideas, deserializerCallback );

        deserializer.execute();
    }

}
