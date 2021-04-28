//package com.example.androidmtgcompanionkotlin
//
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.action.ViewActions.typeText
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.platform.app.InstrumentationRegistry
//import org.hamcrest.CoreMatchers.equalTo
//import org.hamcrest.CoreMatchers.`is` as Is
//import org.junit.Assert.*
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//
///**
// * Instrumented test, which will execute on an Android device.
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//@RunWith(AndroidJUnit4::class)
//class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.androidmtgcompanionkotlin", appContext.packageName)
//    }
//
//
//    @Rule
//    var calculatorTestRule = ActivityScenarioRule(MainActivity::class.java)
//
//
//    // user flow 1:
//    // enter something into the first edit text
//    // enter something into the second edit text
//    // press one of the buttons
//    // results is shown in the text view below
//
//    // user flow 1:
//    // enter something into the first edit text
//    // enter something into the second edit text
//    // press one of the buttons
//    // results is shown in the text view below
//    // test 1.
//    // user follows the user flow
//    @Test
//    fun validCard() {
//        assertThat(cardNameValidation("Counterspell")).is(equalTo(true));
//    }
//    @Test
//    fun invalidCard() {
//        assertThat(cardNameValidation("Counterspeel")).is(equalTo(false));
//    }
//    @Test
//    fun manaCostCheck() {
//        assertThat(manaCostValidation("Counterspell")).is(equalTo("{U}{U}"));
//    }
//    @Test
//    fun validFormatLegality() {
//        assertThat(formatValidation("Counterspell", "Vintage")).is(equalTo(true));
//    }
//    @Test
//    fun validFormatLegality() {
//        assertThat(formatValidation("Counterspell", "Standard")).is(equalTo(false));
//    }
//
//    @Test
//    fun userFlowwithMul() {
//        onView(withId(R.id.editText_one)).perform(typeText("5"))
//        onView(withId(R.id.editText_two)).perform(typeText("4"))
//        onView(withId(R.id.button_mul)).perform(click())
//        onView(withId(R.id.textView_result)).check(matches(withText("20.0")))
//    }
//
//    // test 3:
//    // user will enter 5, then click the button
//    // then text view should display error in computing.
//    @Test
//    fun incorrectUserFlowwithAdd() {
//        onView(withId(R.id.editText_one)).perform(typeText("4"))
//        onView(withId(R.id.button_add)).perform(click())
//        onView(withId(R.id.textView_result)).check(matches(withText("Error in computing.")))
//    }
//
//    // test 4:
//    // user will enter a non number in both edit text
//    // press a button
//    //
//
//    // test 4:
//    // user will enter a non number in both edit text
//    // press a button
//    //
//    @Test
//    fun incorrectUserInputwithAdd() {
//        onView(withId(R.id.editText_one)).perform(typeText("four"))
//        onView(withId(R.id.editText_two)).perform(typeText("five"))
//        onView(withId(R.id.button_mul)).perform(click())
//        onView(withId(R.id.textView_result)).check(matches(withText("Error in computing.")))
//    }
//
//}