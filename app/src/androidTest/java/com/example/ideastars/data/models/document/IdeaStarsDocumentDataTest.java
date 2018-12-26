package com.example.ideastars.data.models.document;

import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.Word;

import org.junit.After;
import org.junit.Before;
import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;

/**
 * Created by hiyakayoyayo on 2017/05/07.
 */
public class IdeaStarsDocumentDataTest {

    private Ideas mIdeas;
    private String mUri;

    @Before
    public void setUp() throws Exception {
        mIdeas = new Ideas();

        Idea[] testIdeas = new Idea[5];
        Word addWord;
        for (int i = 0; i < 5; ++i) {
            testIdeas[i] = new Idea();
            testIdeas[i].setName("Idea[" + i + "]");
            testIdeas[i].setPriority(i);
            for (int w = 0; w < 5; ++w) {
                addWord = testIdeas[i].AddWord("Idea[" + i + "] Word[" + w + "]", Color.WHITE,i*w+w);
                for (int item = 0; item < 10; ++item) {
                    addWord.AddItem("Idea[" + i + "] Word[" + w + "] + Item[" + item + "]",i*w*item+item);
                }
            }
        }

        mIdeas.setIdeas(testIdeas);

    }

    @After
    public void tearDown() throws Exception {

    }

    private Ideas mSavedIdeas;

    public void getIdeas() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        IdeaStarsDocumentData.getInstance(InstrumentationRegistry.getTargetContext()).getIdeas(mUri, new IdeaStarsData.LoadIdeasCallback() {
            @Override
            public void onIdeasLoaded(Ideas ideas) {
                mSavedIdeas = ideas;
                signal.countDown();
            }

            @Override
            public void onDataNotAvailable() {
            }
        });

        signal.await(1, TimeUnit.SECONDS);

        Assert.assertThat(mSavedIdeas, is(mIdeas));
    }

    public void saveIdeas() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        IdeaStarsDocumentData.getInstance(InstrumentationRegistry.getTargetContext()).saveIdeas(mUri, mIdeas, new IdeaStarsData.SaveIdeasCallback() {
            @Override
            public void onSaved() {
                Log.v("Log", "Save Success");
                signal.countDown();
            }

            @Override
            public void onFailed() {

            }
        });
        signal.await(1, TimeUnit.SECONDS);

        Assert.assertTrue("Success", true);
    }

    public void SaveLoadIdeas() throws Exception
    {
        saveIdeas();
        getIdeas();
    }
}