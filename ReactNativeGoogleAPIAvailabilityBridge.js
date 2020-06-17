"use strict";

var ReactNative = require("react-native");
var Platform = ReactNative.Platform;
var GoogleAPIAvailability =
  ReactNative.NativeModules.ReactNativeGooglePlayServices;

class ReactNativeGoogleAPIAvailabilityBridge {
  constructor() {}

  checkGooglePlayServices(result) {
    return GoogleAPIAvailability.checkGooglePlayServices(result);
  }

  promptGooglePlayUpdate(allowCancel) {
    return GoogleAPIAvailability.promptGooglePlayUpdate(allowCancel);
  }

  openGooglePlayUpdate() {
    return GoogleAPIAvailability.openGooglePlayUpdate();
  }

  showServiceMissingDialog() {
    return GoogleAPIAvailability.showServiceMissingDialog();
  }

  showServiceDisabledDialog() {
    return GoogleAPIAvailability.showServiceDisabledDialog();
  }
}

module.exports = new ReactNativeGoogleAPIAvailabilityBridge();
