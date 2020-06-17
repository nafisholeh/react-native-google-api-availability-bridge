package com.ivanwu.googleapiavailabilitybridge;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class ReactNativeGooglePlayServicesModule extends ReactContextBaseJavaModule {
	public final ReactApplicationContext reactContext;

	public ReactNativeGooglePlayServicesModule(ReactApplicationContext reactContext) {
		super(reactContext);
		this.reactContext = reactContext;
	}

	@Override
	public String getName() {
		return "ReactNativeGooglePlayServices";
	}

	@ReactMethod
	public void checkGooglePlayServices(Callback result) {
		final Callback onResult = result;

		try {
			onResult.invoke(this.checkGooglePlayServicesHelper());
		} catch(Exception e) {}
	}

	@ReactMethod
	public void promptGooglePlayUpdate(boolean allowCancel) {
		showErrorDialog(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, allowCancel);
	}

	private void showErrorDialog(int errorCode, boolean allowCancel) {
		Dialog dialog = GoogleApiAvailability
			.getInstance()
			.getErrorDialog(
				this.getCurrentActivity(),
				errorCode,
				0
			);

		// Quit app if not user does not update play services
		if(allowCancel) {
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {
					reactContext.getCurrentActivity().finish();
				}
			});
		}
		
		dialog.show();
	}

	@ReactMethod
	public void openGooglePlayUpdate() {
		openPlayStore();
	}

	private void promptServicePermissionResolution(int status) {
		try {
			status.startResolutionForResult(reactContext.getCurrentActivity(), ConnectionResult.SERVICE_MISSING_PERMISSION)
		} catch (Exception error) {
			throw error;
		}
	}

	private String checkGooglePlayServicesHelper() {
		final int googlePlayServicesCheck = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.getCurrentActivity());
		switch (googlePlayServicesCheck) {
			// case ConnectionResult.SUCCESS:
			// 	return "success";
			// case ConnectionResult.SERVICE_DISABLED:
			// 	return "disabled";
			// case ConnectionResult.SERVICE_INVALID:
			// 	return "invalid";
			// case ConnectionResult.SERVICE_MISSING:
			// 	return "missing";
			// case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			// 	return "update";
			// case ConnectionResult.SERVICE_UPDATING:
			// 	return "updating";
			// case ConnectionResult.SERVICE_MISSING_PERMISSION:
			default:
				promptServicePermissionResolution(googlePlayServicesCheck);
				return "permission_missing";
		}
		return "failure";
	}

	// Opens Google Play Services with Play Store app
	private void openPlayStore() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=com.google.android.gms"));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		reactContext.startActivity(intent);
	}
}
