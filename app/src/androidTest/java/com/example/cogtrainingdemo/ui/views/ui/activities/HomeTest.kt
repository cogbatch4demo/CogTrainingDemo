package com.example.cogtrainingdemo.ui.views.ui.activities


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.cogtrainingdemo.R
import com.example.cogtrainingdemo.ui.views.ui.adapters.EpisodeItemRecyclerViewAdapter
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(Home::class.java)

    @Before
    fun setup() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_dashboard), withContentDescription("Episodes"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())
        waitForViewToDisappear(R.id.progressbar, 2000L)
    }

    @Test
    fun homeTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.recyclerview_episodes),
                childAtPosition(
                    withId(R.id.linearLayout),
                    1
                )
            )
        )

        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        pressBack()
    }

    @Test
    fun check_is_searchview_displayed_on_app_launch() {
        onView(withId(R.id.searchView)).check(matches(isDisplayed()))
    }

    fun waitForViewToDisappear(viewId: Int, maxWaitingTimeMs: Long) {
        val endTime = System.currentTimeMillis() + maxWaitingTimeMs
        while (System.currentTimeMillis() <= endTime) {
            try {
                onView(allOf(withId(viewId), isDisplayed())).check(matches(not(doesNotExist())))
            } catch (ex: NoMatchingViewException) {
                return  // view has disappeared
            }
        }
        throw RuntimeException("timeout exceeded") // or whatever exception you want
    }

    fun waitForViewToAppear(viewId: Int, maxWaitingTimeMs: Long) {
        val endTime = System.currentTimeMillis() + maxWaitingTimeMs
        while (System.currentTimeMillis() <= endTime) {
            try {
                onView(withId(viewId)).check(matches(ViewMatchers.isDisplayed()))
                return
            } catch (ex: NoMatchingViewException) {
                // view has disappeared
            }
        }
        throw RuntimeException("timeout exceeded") // or whatever exception you want
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    /**
     * Recyclerview comes into view
     */

    @Test
    fun check_is_recyclerview_displayed_on_app_launch() {
        onView(withId(R.id.recyclerview_episodes)).check(matches(isDisplayed()))
    }

    /**
     * scrolls to the indicated position in the list
     */
    @Test
    fun scrollToItem_item_at_position() {
        onView(withId(R.id.recyclerview_episodes))
            .perform(
                RecyclerViewActions.scrollToPosition<EpisodeItemRecyclerViewAdapter.ViewHolder>(
                    ITEM_AT_POSITION
                )
            )
        onView(withId(R.id.recyclerview_episodes))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<EpisodeItemRecyclerViewAdapter.ViewHolder>(
                    ITEM_AT_POSITION,
                    click()
                )
            )
        waitForViewToAppear(R.id.espisode, 2000L)
        onView(withId(R.id.espisode)).check(matches(withText(containsString("" + (ITEM_AT_POSITION + 1)))))
        pressBack()
    }

    /**
     * First scroll to the position that needs to be matched and click on it.
     * performs a click event
     */
    @Test
    fun scrollToItemBelowFold_checkItsText() {
        onView(withId(R.id.recyclerview_episodes))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<EpisodeItemRecyclerViewAdapter.ViewHolder>(
                    ITEM_BELOW_THE_FOLD,
                    click()
                )
            )
        waitForViewToAppear(R.id.espisode, 2000L)
        onView(withId(R.id.espisode)).check(matches(withText(containsString("" + (ITEM_BELOW_THE_FOLD + 1)))))
        pressBack()
    }

    companion object {
        private const val ITEM_BELOW_THE_FOLD = 30
        private const val ITEM_AT_POSITION = 20
    }
}
