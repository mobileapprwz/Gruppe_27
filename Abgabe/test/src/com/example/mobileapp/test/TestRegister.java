package com.example.mobileapp.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mobileapp.StartActivity;
import com.jayway.android.robotium.solo.Solo;


public class TestRegister extends ActivityInstrumentationTestCase2<StartActivity> {

	
	private Solo solo;
	
	public TestRegister() {
		super(StartActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	
	
	
	public void testRegister() {
		//Test only works when user is deleted from Database before
		solo.clickOnButton("Register");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clearEditText(2);
		solo.clearEditText(3);
		solo.enterText(0, "Testuser");
		solo.enterText(1, "example@provider.net");
		solo.enterText(2, "Testuser");
		solo.enterText(3, "Testuser");
		solo.clickOnCheckBox(0);
		solo.clickOnButton("Close");
		solo.clickOnButton("Submit");
		solo.waitForActivity("StartActivity");
	}
	
	public void testRegisterWithoutAcceptedTerms() {
		solo.clickOnButton("Register");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clearEditText(2);
		solo.clearEditText(3);
		solo.enterText(0, "Testuser");
		solo.enterText(1, "example@provider.net");
		solo.enterText(2, "Testuser");
		solo.enterText(3, "Testuser");
		solo.clickOnCheckBox(0);
		solo.clickOnButton("Close");
		solo.clickOnButton("Submit");
		assertTrue(solo.searchText("Please accept our Business Terms."));
	}

    public void testRegisterWithNoPassword() {
		solo.clickOnButton("Register");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clearEditText(2);
		solo.clearEditText(3);
		solo.enterText(0, "Testuser");
		solo.enterText(1, "example@provider.net");
		solo.clickOnCheckBox(0);
		solo.clickOnButton("Close");
		solo.clickOnButton("Submit");
		assertTrue(solo.searchText("Please insert a password"));
	}

   
	public void testRegisterWithNoUser() {
		solo.clickOnButton("Register");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clearEditText(2);
		solo.clearEditText(3);
		solo.clickOnEditText(1);
		solo.enterText(1, "example@provider.net");
		solo.clickOnEditText(2);
		solo.enterText(2, "Testuser");
		solo.clickOnEditText(3);
		solo.enterText(3, "Testuser");
		solo.clickOnCheckBox(0);
		solo.clickOnButton("Close");
		solo.clickOnButton("Submit");
		assertTrue(solo.searchText("Please insert an username"));
	}
	
	public void testRegisterWithNoEmail() {
		solo.clickOnButton("Register");
		solo.clearEditText(0);
		solo.enterText(0, "Testuser");
		solo.clearEditText(1);
		solo.clickOnButton("Submit");
		assertTrue(solo.searchText("Please insert an email"));
	}
	
	public void testRegisterWithWrongPasswordRepetition() {
		solo.clickOnButton("Register");
		solo.clearEditText(0);
		solo.clearEditText(1);
		solo.clearEditText(2);
		solo.clearEditText(3);
		solo.enterText(0, "Testuser");
		solo.enterText(1, "example@provider.net");
		solo.enterText(2, "Testuser");
		solo.enterText(3, "Testuser2");
		solo.clickOnButton("Submit");
		assertTrue(solo.searchText("Password repetition doesn't match password."));
	}
	
	public void testRegisterCancel() {
		solo.clickOnButton("Register");
		solo.clickOnButton("Cancel");
		Log.i("TestLogin",solo.getCurrentActivity().getLocalClassName()); //should be StartActivity
		
	}
	

	public void testRegisterWithExistingUsername() {
		solo.clickOnButton("Register");
		solo.clickOnEditText(0);
		solo.enterText(0, "Sandra");
		solo.clickOnEditText(1);
		solo.enterText(1, "example@provider.net");
		solo.clickOnEditText(2);
		solo.enterText(2, "Testuser");
		solo.clickOnEditText(3);
		solo.enterText(3, "Testuser");
		solo.clickOnCheckBox(0);
		solo.clickOnButton("Close");
		solo.clickOnButton("Submit");
		solo.waitForText("There is already an user with this name registrated");
		assertTrue(solo.searchText("There is already an user with this name registrated"));
	}
	
	
	
}
