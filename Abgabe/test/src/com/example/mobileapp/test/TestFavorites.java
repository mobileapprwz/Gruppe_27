package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestFavorites extends ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	private String username = "test";
	private String password = "test";
	
	public TestFavorites() {
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
	
	public void testFavoritesCheckNewFavorites() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Favorites");
		solo.clickOnCheckBox(2);
		solo.clickOnCheckBox(3);
		solo.clickOnButton("Update");
		assertTrue(solo.searchText("Favorites changed successfully."));
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Home");
	}
	
	public void testFavoritesUncheckFavorites() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Favorites");
		solo.clickOnCheckBox(3);
		solo.clickOnCheckBox(4);
		solo.clickOnButton("Update");
		assertTrue(solo.searchText("Favorites changed successfully."));
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Home");
	}
	
	public void testFavoritesFindCategory() {
		Login();
		solo.clickOnImage(1);
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName());
		solo.waitForActivity("HomeActivity");
	}
	
}

