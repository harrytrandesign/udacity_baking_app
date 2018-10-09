package com.htdwps.bakingappudacityproject;

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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTest = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkRecyclerViewExists() {
        onView(Matchers.anyOf(withId(R.id.rv_recipe_list)))
                .check(matches(isDisplayed()));
    }

//    @Test
//    public void browniesInRecyclerViewList() {
//        onView(withId(R.id.rv_recipe_list))
//                .perform(RecyclerViewActions
//                .scrollToPosition(1));
//
//        onView(withText("Brownies")).check(matches(isDisplayed()));
//    }

}
