package com.example.ideastars.data.models.local;

import android.graphics.Color;
import android.support.test.InstrumentationRegistry;

import com.example.ideastars.data.models.Favorite;
import com.example.ideastars.data.models.Idea;
import com.example.ideastars.data.models.IdeaStarsData;
import com.example.ideastars.data.models.Ideas;
import com.example.ideastars.data.models.Item;
import com.example.ideastars.data.models.Word;
import com.example.ideastars.Injection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by hiyakayoyayo on 2017/05/08.
 */
public class IdeaStarsRepositoryTest {
    private Ideas mIdeas;
    private Ideas mLoadIdeas;

    public class Hoge {
        private int value;
        private String str;

        public Hoge(int value,String str) {
            this.value = value;
            this.str = str;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @Before
    public void setUp() throws Exception {
        mIdeas = new Ideas();

        Idea[] testIdeas = new Idea[3];
        Word addWord;
        ArrayList<Favorite> favorites = new ArrayList<Favorite>();
        for (int i = 0; i < testIdeas.length; ++i) {
            testIdeas[i] = new Idea();
            testIdeas[i].setName("Idea[" + i + "]");
            testIdeas[i].setPriority(i);
            for (int w = 0; w < 2; ++w) {
                addWord = testIdeas[i].AddWord("Idea[" + i + "] Word[" + w + "]", Color.WHITE,i*w+w);
                for (int item = 0; item < 2; ++item) {
                    addWord.AddItem("Idea[" + i + "] Word[" + w + "] Item[" + item + "]",i*w*item+item);
                }
            }
            favorites.add( new Favorite(-1, new long[]{ 0, 2 }, 3 ) );
            favorites.add( new Favorite(-1, new long[]{ 1, 3 }, 2 ) );
            favorites.add( new Favorite(-1, new long[]{ 0, 3 }, 4 ) );
            testIdeas[i].setFavorites( favorites.toArray(new Favorite[favorites.size()]) );
        }

        mIdeas.setIdeas(testIdeas);

        Injection.provideIdeaStarsRepository(InstrumentationRegistry.getTargetContext()).deleteAll();
    }

    @After
    public void tearDown() throws Exception {

    }

    public void TestSave() throws Exception
    {
        IdeaStarsLocalData.getInstance(InstrumentationRegistry.getTargetContext()).saveIdeas(mIdeas);
    }

    public void TestLoad() throws Exception
    {
        final CountDownLatch signal = new CountDownLatch(1);

        IdeaStarsLocalData.getInstance(InstrumentationRegistry.getTargetContext()).getIdeas(new IdeaStarsData.LoadIdeasCallback() {
            @Override
            public void onIdeasLoaded(Ideas ideas) {
                mLoadIdeas = ideas;
                signal.countDown();
            }

            @Override
            public void onDataNotAvailable() {
                Assert.fail();
            }
        });
        signal.await(1, TimeUnit.SECONDS);

        assertThat(mLoadIdeas, notNullValue());
    }

    public void TestDeleteItem() throws Exception
    {
        IdeaStarsLocalData.getInstance(InstrumentationRegistry.getTargetContext()).deleteItems(new long[]{6});
    }

    public void TestDeleteWord() throws Exception
    {
        IdeaStarsLocalData.getInstance(InstrumentationRegistry.getTargetContext()).deleteWords(new long[]{4});
    }

    public void TestDeleteIdea() throws Exception
    {
        IdeaStarsLocalData.getInstance(InstrumentationRegistry.getTargetContext()).deleteIdeas(new long[]{1});
    }

    @Test
    public void Test() throws Exception
    {
        TestSave();
//        TestDeleteItem();
//        TestDeleteWord();
//        TestDeleteIdea();
//        TestLoad();
    }
}