package com.example.ideastars;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.ideastars.data.models.IdeaStarsRepository;
import com.example.ideastars.data.models.local.IdeaStarsLocalData;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {
    public static IdeaStarsRepository provideIdeaStarsRepository(@NonNull Context context)
    {
        checkNotNull(context);
        return IdeaStarsRepository.getInstance( IdeaStarsLocalData.getInstance(context) );
    }
}
