package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestBrowseEvent extends ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	private String username = "b";
	private String password = "b";
	
	public TestBrowseEvent() {
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
	
	public void testBrowseEventFindSubcategories() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Baseball");
	}
	
	public void testBrowseEventFindEvents() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnText("Baseball");
		solo.clickOnText("testevent");
		solo.searchText("testevent");
		
	}
	
	public void testBrowseEventTestSwitch() { 
		Login();
		solo.clickOnButton("Browse Categories");
		solo.searchText("Football");
		solo.scrollToSide(0);
		solo.searchText("Cars");
	}
	
	
}
