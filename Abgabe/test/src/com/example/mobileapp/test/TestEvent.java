package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestEvent extends ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	private String username = "test";
	private String password = "test";
	
	public TestEvent() {
		super(StartActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	protected void tearDown () throws Exception {
		super.tearDown();
		
	}
	
	public void Login() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, username);
		solo.enterText(1, password);
		solo.clickOnButton("Sign in");
		solo.waitForActivity("HomeActivity");
	}
	
	public void testEventLookAtUserList() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Baseball");
		solo.clickOnText("testevent");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Show Userlist");
		solo.searchText("test");
		solo.clickOnButton("Close");
	}
	
	public void testEventLookAtEvent() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Baseball");
		solo.clickOnText("testevent"); 
	}
	
	public void testEventSignInDeadlineExpired() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Baseball");
		solo.clickOnText("deadlineevent");
		solo.clickOnButton("Sign in");
		assertTrue(solo.searchText("Deadline expired."));
	}
	
	public void testEventSignInInFullEvent() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Baseball");
		solo.clickOnText("fullevent");
		solo.clickOnButton("Sign in");
		assertTrue(solo.searchText("Max user count reached."));
	}
	
	public void testEventSignInEvent() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Basketball");
		solo.clickOnText("SBasketball");
		solo.clickOnButton("Sign in");
		assertTrue(solo.searchText("Successfully registrated."));
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Home");
		solo.clickOnButton("My Events");
		solo.searchText("SBasketball");
	}
	
	public void testEventSignInEventAndOff() {
		Login();
		solo.clickOnButton("My Events");
		solo.clickOnText("Basketball");
		solo.clickOnText("SBasketball");
		solo.clickOnButton("Sign off");
		assertTrue(solo.searchText("Successfully signed off."));
	}
	
}
