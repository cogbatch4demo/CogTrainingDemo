package com.example.cogtrainingdemo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cogtrainingdemo.ui.main.CharactersAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get: Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)


    /**
     * MainActivity comes into view
     */
    @Test
    fun check_is_activity_in_view() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    /**
     * Recyclerview comes into view
     */

    @Test
    fun check_is_recyclerview_displayed_on_app_launch() {
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()))
    }

    /**
     * scrolls to the indicated position in the list
     */
    @Test
    fun scrollToItem_item_at_position() {
        onView(withId(R.id.recyclerview))
            .perform(
                RecyclerViewActions.scrollToPosition<CharactersAdapter.ViewHolder>(
                    ITEM_AT_POSITION
                )
            )
    }

    /**
     * First scroll to the position that needs to be matched and click on it.
     * performs a click event
     */
    @Test
    fun scrollToItemBelowFold_checkItsText() {

        onView(withId(R.id.recyclerview))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CharactersAdapter.ViewHolder>(
                    ITEM_BELOW_THE_FOLD,
                    click()
                )
            )
    }

    @Test(expected = PerformException::class)
    fun itemWithText_doesNotExist() {
        onView(withId(R.id.recyclerview))
            .perform(
                RecyclerViewActions.scrollTo<CharactersAdapter.ViewHolder>(
                    hasDescendant(withText("not in the list"))
                )
            )
    }

    companion object {
        private const val ITEM_BELOW_THE_FOLD = 30
        private const val ITEM_AT_POSITION = 20
    }

}

