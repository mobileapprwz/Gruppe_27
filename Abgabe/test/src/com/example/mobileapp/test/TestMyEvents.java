package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestMyEvents extends ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	
	public TestMyEvents() {
		super(StartActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	protected void tearDown () throws Exception {
		super.tearDown();
		
	}
	

	public void Login(String username, String password) {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, username);
		solo.enterText(1, password);
		solo.clickOnButton("Sign in");
		solo.waitForActivity("HomeActivity");
	}


	
	public void testMyEventsEditEventNoTitle() {
		Login("test","test");
		solo.clickOnButton("My Events");
		solo.clickOnText("testevent");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Edit");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clickOnButton("Next");
		assertTrue(solo.searchText("Title is too short"));
	}
	
	public void testMyEventsEditEvent() {
		Login("test","test");
		solo.clickOnButton("My Events");
		solo.clickOnText("testevent");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Edit");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "testevent2");
		solo.enterText(1, "Location");
		solo.clickOnButton("Next");
		solo.clickOnButton("Submit");  
		solo.searchText("testevent2");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Edit");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "testevent");
		solo.enterText(1, "Location");
		solo.clickOnButton("Next");
		solo.clickOnButton("Save");
		solo.searchText("testevent");
	}
	
	
	public void testMyEventsEditEventNoLocation() {
		Login("test","test");
		solo.clickOnButton("My Events");
		solo.clickOnText("testevent");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Edit");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "Test1");
		solo.clickOnButton("Next");
		assertTrue(solo.searchText("You must insert a location"));
	}
	
	public void testMyEventsCheckUserList() {
		Login("test","test");
		solo.clickOnButton("My Events");
		solo.clickOnText("Party");
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Show Userlist");
		solo.searchText("Hans");
	}
	
	public void testMyEventsSignOffOfEvent() {
		Login("test","test");
		solo.clickOnButton("My Events");
		solo.clickOnText("Party");
		solo.clickOnButton("Sign off");
		assertTrue(solo.searchText("Successfully signed off."));
		solo.clickOnButton("Sign in");
		assertTrue(solo.searchText("Successfully registrated."));
	}
	
	public void testMyEventsCheckEvents() {
		Login("test","test");
		solo.clickOnButton("My Events");
		solo.clickOnText("Party");
	}
	
	public void testMyEventsCheckWhenEmpty() {
		Login("testnoEvents","a");
		solo.clickOnButton("My Events");
		assertTrue(solo.searchText("No events in this category."));
	}
	
	public void testMyEventsCheckWhenFull() {
		Login("test","test");
		solo.clickOnButton("My Events");
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName()); 
	}
	
}
