package com.example.comp;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

public class MainFragment extends Fragment{

	
	private static final String TAG = "MainFragment";
	String username;
	Bundle bundle;
	private UiLifecycleHelper uiHelper;
	private TextView userInfoTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@SuppressWarnings("deprecation")
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {

	    if (state.isOpened()) {
	    	final Intent intent = new Intent(this.getActivity(), MenuActivity.class);
	    	
	    	// make request to the /me API
	          Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            // callback after Graph API response with user object
	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	            	
	            	// Request user data and show the results
	                        if (user != null) {
	                        	
	                            // Display the parsed user info
	                            //userInfoTextView.setText(buildUserInfoDisplay(user));
	                        	intent.putExtra("user", user.getName());
	                        	startActivity(intent);
	                        	getActivity().finish();
	                        }
	            }
	          });
        	 // startActivity(intent);
        	  //this.getActivity().finish();
	    	
	    	//super.getActivity().onBackPressed();
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	    	userInfoTextView.setVisibility(View.INVISIBLE);
	        Log.i(TAG, "Logged out...");
	    }
	}

	
	/*private String buildUserInfoDisplay(GraphUser user) {
	    StringBuilder userInfo = new StringBuilder("");

	    // Example: typed access (name)
	    // - no special permissions required
	    userInfo.append(String.format("Name: %s\n\n", 
	        user.getName()));

	    // Example: typed access (birthday)
	    // - requires user_birthday permission
	    userInfo.append(String.format("Birthday: %s\n\n", 
	        user.getBirthday()));

	    // Example: partially typed access, to location field,
	    // name key (location)
	    // - requires user_location permission
	    userInfo.append(String.format("Location: %s\n\n", 
	        user.getLocation().getProperty("name")));

	    return userInfo.toString();
	}*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_main, container, false);
	    
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    
	    return view;
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
}
