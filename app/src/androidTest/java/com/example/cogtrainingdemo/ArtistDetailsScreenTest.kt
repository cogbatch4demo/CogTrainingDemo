package com.example.cogtrainingdemo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cogtrainingdemo.ui.views.ui.activities.DetailsScreen

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ArtistDetailsScreenTest {

    @get: Rule
    val scenarioRule = ActivityScenarioRule(DetailsScreen::class.java)

    /**
     * DetailsScreen comes into view
     */
    @Test
    fun check_is_homeFragment_in_view() {
        onView(withId(R.id.detailscreen)).check(matches(isDisplayed()))
    }

    /**
     * Test Artists info are displayed
     */
    @Test
    fun artist_image_is_displayed() {
        onView(withId(R.id.item_image)).check((matches(isDisplayed())))
        onView(withId(R.id.title)).check((matches(isDisplayed())))
        onView(withId(R.id.dob)).check((matches(isDisplayed())))
        onView(withId(R.id.potrayed_by)).check((matches(isDisplayed())))
    }

}
