package com.example.garbagedisposal;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GarbaseScanActivityIntegrationTests {

    @Rule
    public ActivityScenarioRule<GarbageScan> activity = new ActivityScenarioRule<>(GarbageScan.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.garbagedisposal", appContext.getPackageName());
    }

    @Test
    public void TestHeaderTextViewDisplayed(){
        onView(withText("Scan Your Garbage Items")).check(matches(isDisplayed()));
    }
    @Test
    public void Test_garbage_type_text_view_displayed(){
        onView(withText("Garbage Type : ")).check(matches(isDisplayed()));
    }

    @Test
    public void TestOnCreate_HowToDispose_button_disabled() {
        onView(withId(R.id.button4)).check(matches(isNotEnabled()));
    }

    @Test
    public void CameraOpenTest_on_scan_click(){
        onView(withId(R.id.button11)).perform(click());
    }
}