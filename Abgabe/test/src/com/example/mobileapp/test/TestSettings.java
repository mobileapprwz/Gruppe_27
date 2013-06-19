package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;

public class TestSettings extends  ActivityInstrumentationTestCase2<StartActivity> {

	private Solo solo;
	private String username = "b";
	private String password = "b";

	public TestSettings() {
		super(StartActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	protected void tearDown () throws Exception {
		super.tearDown();
		
	}
	
	/*
	 * hier muss ich alle morgen noch testen ob die wirklich gehne
	Testcases
	check ob settings von überall aufrufbar
	change profile name check
	 	gibts den schon					done
	 	stimmt des passwort				done
	 	einen check obs geht			done
	 	einen wo nix geändert wurde		done
	 change pw
	 	check pw repetition				done
	 	check old pw richtig			done
	 	einmal testen ob es geht		done
	 
	 */
	
	public void Login() {
		solo.clickOnButton("Sign in");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, username);
		solo.enterText(1, password);
		solo.clickOnButton("Sign in");
		solo.waitForActivity("HomeActivity");
	}
		
	
	public void testSettingsChangePassword() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Password");
		solo.enterText(0, "b");
		solo.enterText(1, "c");
		solo.enterText(2, "c");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Password changes successfully...")); 
		solo.clickOnText("Password");
		solo.enterText(0, "c");
		solo.enterText(1, "b");
		solo.enterText(2, "b");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Password changes successfully..."));
	}
	
	
	public void testSettingsChangePasswordRepetitionWrong() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Password");
		solo.enterText(0, "b");
		solo.enterText(1, "c");
		solo.enterText(2, "b");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Password repetition doesn't match password."));
	}
	
	
	public void testSettingsChangePasswordOldPasswordWrong() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Password");
		solo.enterText(0, "bb");
		solo.enterText(1, "b");
		solo.enterText(2, "b");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Invalid password."));
	}
	
	
	public void testSettingsChangeUsername() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Name");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "bb");
		solo.enterText(1, "b");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Changed name successfully."));
		solo.clickOnText("Name");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.enterText(0, "b");
		solo.enterText(1, "b");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Changed name successfully."));
	}
	
	public void testSettingsChangeUsernameAlreadyExists() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Name");
		solo.clearEditText(0);
		solo.enterText(0, "Sandra");
		solo.enterText(1, "b");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("There is already an user with this name registrated..."));
	}
	
	public void testSettingsChangeUsernameNothingChanged() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Name");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Nothing to change."));
	}
	
	public void testSettingsChangeUsernameWrongPassword() {
		Login();
		solo.clickOnView(solo.getView(com.example.mobileapp.R.id.main_item));
		solo.clickOnText("Settings");
		solo.clickOnText("Profile");
		solo.clickOnText("Name");
		solo.clearEditText(0);
		solo.enterText(0, "bbb");
		solo.enterText(1, "wrongpass");
		solo.clickOnButton("OK");
		assertTrue(solo.searchText("Invalid password."));
	}

}
