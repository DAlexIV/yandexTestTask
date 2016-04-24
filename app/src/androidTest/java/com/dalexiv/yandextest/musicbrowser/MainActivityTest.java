package com.dalexiv.yandextest.musicbrowser;

import android.app.Application;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.dalexiv.yandextest.musicbrowser.ui.activity.MainActivity;
import com.dalexiv.yandextest.musicbrowser.util.MyMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.dalexiv.yandextest.musicbrowser.util.OrientationChangeAction.orientationLandscape;

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
        onView(withId(R.id.recyclerPerfs)).check(matches(MyMatchers.withListSize(317)));
    }

    @Test
    public void testRotation() {
        onView(isRoot()).perform(orientationLandscape());
        testFetching();
    }

    @Test
    public void testContent() {
        onView(withId(R.id.recyclerPerfs))
                .check(matches(hasDescendant(withText("Tove Lo"))));
        onView(withId(R.id.recyclerPerfs))
                .check(matches(hasDescendant(withText("pop, dance, electronics"))));
        onView(withId(R.id.recyclerPerfs))
                .check(matches(hasDescendant(withText("22 альбома, 81 песня"))));
    }
}