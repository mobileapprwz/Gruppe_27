package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;



public class TestLogin extends ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	
	public TestLogin() {
		super(StartActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	protected void tearDown () throws Exception {
		super.tearDown();
		
	}
	
	
	public void testLoginWrongUser() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "not in db");
		solo.enterText(1, "pass");
		solo.clickOnButton("Sign in");
		assertTrue(solo.searchText("User not found."));
	}
	
	public void testLoginWrongPassword() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "b");
		solo.enterText(1, "c");
		solo.clickOnButton("Sign in");
		assertTrue(solo.searchText("Invalid password."));
	}
	
	public void testLoginCancel() {
		solo.clickOnButton("Sign in");
		solo.clickOnButton("Cancel");
		solo.waitForActivity("StartActivity");
	}
	
	public void testLogin() {
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName());
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "b");
		solo.enterText(1, "b");
		solo.clickOnButton("Sign in");
		solo.waitForText("Favorites");
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName()); //should be HomeActivity
		solo.waitForActivity("HomeActivity");
	}
	
	
	public void testLoginAndLogout() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "b");
		solo.enterText(1, "b");
		solo.clickOnButton("Sign in");
		solo.waitForActivity("HomeActivity");
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName()); //should be HomeActivity
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Logout");
		solo.waitForActivity("StartActivity");
	}
	
	public void testLoginAndLogoutFromSettings() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "b");
		solo.enterText(1, "b");
		solo.clickOnButton("Sign in");
		solo.waitForActivity("HomeActivity");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName()); //should be HomeActivity
		//solo.waitForActivity("HomeActivity"); need to check from log what it is 
		//hier eventuell noch auf ein event klicken und dort ausloggen
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Logout");
		solo.waitForActivity("StartActivity");
	}
	
	public void testLoginAndLogoutFromEvent() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "b");
		solo.enterText(1, "b");
		solo.clickOnButton("Sign in");
		solo.waitForActivity("HomeActivity");
		solo.clickOnButton("Browse Categories");
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName()); 
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Logout");
		solo.waitForActivity("StartActivity");
	}
	

	
}
