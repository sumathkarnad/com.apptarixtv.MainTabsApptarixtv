package com.apptarixtv;

import java.util.ArrayList;

import com.apptarixtv.constant.ApptarixConstant;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.os.Bundle;

public class InviteFriendsActivity extends Activity {
	
	private Facebook mFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 mFacebook = new Facebook(ApptarixConstant.APP_ID);
		
	}
	
	public void inviteFriends(Activity activity, ArrayList<String> friendsIds){
	    // Safe programming
	    if(friendsIds == null || friendsIds.size() == 0)        
	        return;

	    Bundle parameters = new Bundle();

	    // Get the friend ids
	    String friendsIdsInFormat = "";
	    for(int i=0; i<friendsIds.size()-1; i++){
	        friendsIdsInFormat = friendsIdsInFormat + friendsIds.get(i) + ", ";
	    }
	    friendsIdsInFormat = friendsIdsInFormat + friendsIds.get(friendsIds.size()-1).toString();

	    parameters.putString("to", friendsIdsInFormat);
	    parameters.putString( "message", "Use my app!");

	    // Show dialog for invitation
	    mFacebook.dialog(activity, "apprequests", parameters, new Facebook.DialogListener() {
	        @Override
	        public void onComplete(Bundle values) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onFacebookError(FacebookError e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onError(DialogError e) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onCancel() {
	            // TODO Auto-generated method stub

	        }
	    });

	}

}
