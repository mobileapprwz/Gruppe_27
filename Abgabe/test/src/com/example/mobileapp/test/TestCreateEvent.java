package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestCreateEvent extends ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	private String username = "test";
	private String password = "test";
	
	public TestCreateEvent() {
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
	
	public void testCreateEventCheckTitle() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnImage(4);
		solo.clickOnText("Create new Event");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clickOnButton("Next");
		assertTrue(solo.searchText("Title is too short"));
	}
	
	public void testCreateEventDeleteEvent() {
		//event needs to be activated in db to work
		Login();
		solo.clickOnButton("My Events");
		solo.clickOnText("EventtoDelete");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Delete");
		assertTrue(solo.searchText("Event successfully deleted."));
	}
	
	public void testCreateEventCheckLocation() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnImage(4);
		solo.clickOnText("Create new Event");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "Test1");
		solo.clickOnButton("Next");
		assertTrue(solo.searchText("You must insert a location"));
	}
	
	public void testCreateEvent() {
		Login();
		solo.clickOnButton("Browse Categories");
		solo.clickOnImage(4);
		solo.clickOnText("Create new Event");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "Event1");
		solo.enterText(1, "Location1");
		solo.clickOnButton("Next");
		solo.clickOnButton("Submit");
	}
	
}
