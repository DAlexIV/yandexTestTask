package com.dalexiv.yandextest.musicbrowser;

import android.app.Application;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.dalexiv.yandextest.musicbrowser.ui.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by dalexiv on 4/24/16.
 */
@RunWith(AndroidJUnit4.class)
public class DetailedActivityTest extends ApplicationTestCase<Application> {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    public DetailedActivityTest() {
        super(Application.class);
    }

    @Test
    public void testContent() {
        onView(withId(R.id.recyclerPerfs))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.perfGenre))
                .check(matches(withText("pop, dance, electronics")));
        onView(withId(R.id.perfStats))
                .check(matches(withText("22 альбома · 81 песня")));
        onView(withId(R.id.perfDesc))
                .check(matches(withText(containsString("шведская певица"))));
    }
}
