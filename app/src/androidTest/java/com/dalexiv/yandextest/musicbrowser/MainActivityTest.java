package com.dalexiv.yandextest.musicbrowser;

import android.app.Application;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.dalexiv.yandextest.musicbrowser.ui.activity.MainActivity;
import com.dalexiv.yandextest.musicbrowser.util.MyMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ApplicationTestCase<Application> {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    public MainActivityTest() {
        super(Application.class);
    }
    @Test
    public void testFetching() {
        onView(withId(R.id.recyclerPerfs)).check(ViewAssertions.matches(MyMatchers.withListSize(317)));
    }
}