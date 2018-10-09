package com.htdwps.bakingappudacityproject;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String RECIPE_ITEM = "Brownies";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTest = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkRecyclerViewExists() {
        onView(Matchers.anyOf(withId(R.id.rv_recipe_list)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void browniesInRecyclerViewList() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions
                .scrollToPosition(1));

        onView(withText(RECIPE_ITEM)).check(matches(isDisplayed()));

    }

}
