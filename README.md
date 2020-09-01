Project based on JetBrains IntelliJ IDEA

#### Appium
Appium client should be started before implement testing process.
Please go to website https://appium.io/ and download Appium framework.
I have used 1.8.1 version.

Appium needs to be setup properly:
After install we need to set up IP address we will use during testing:
0.0.0.0:4723 for the real device
127.0.0.1:4723 for the emulator

Appium also need to set 2 environment variables for proper work. Click on "Edit Configuration" and input right paths to:
    ANDROID_HOME
    NODE_HOME


#### SDK manager
Also, ADB tool could react in a strange way, so it could be necessary to upgrade the Android SDK.
open Command Prompt and input: "sdkmanager --update"
or install Android Studio from scratch.

I have made this work on Microsft Windows, but all this setup is quite similar with Linux or Mac.

Also we need to check environment variables on local computer: open Control panel, click on "Edit environment variables for your account" and set right path to:
    ANDROID_HOME
    ANDROID_SDK_ROOT
    ANDROID_PLATFORM_TOOLS
    APPIUM_HOME
    APPIUM_BINARY_PATH
    JAVA_HOME



#### Testing process
Appium allows us to work with 3 different path to application
1. it could be local path, by example:
   capabilities.setCapability("app", "/home/naveen/IdeaProjects/QberLoginPage/app/Qber_Customer_V1.6.0.apk");
2. Remote stored app
   capabilities.setCapability(MobileCapabilityType.APP, "https://github.com/afollestad/material-dialogs/raw/master/sample/sample.apk");
3. Build-in app like Calculator


Also we need to setup proper Device name for testing process:
DesiredCapabilities cap = new DesiredCapabilities();
it could be emulator:
capabilities.setCapability("deviceName", "emulator-5554");
or real device:
capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Lenovo TB3-850M");

and proper platform device version:
cap.setCapability("platformVersion", "6.0");


#### UI Automator Viewer
Before work with UI testing we need to open 1 useful tool:
UI Automator Viewer - tool from Android SDK that helps to identify UI items and make reference on them during testing.
It allows us to get item by its ID, class, text or content description.


#### Run The Script And Automate The App

Run the script as a Java application or TestNG application from IntelliJ IDEA or Eclipse editor.
After execution, you can see that the Appium files logs for all actions performed against the mobile device.


My build-in testing applications

#### I. "Google Play" - application repository

0. testNetworkConnection() - check network connection as initial stage.

1. First of all, we check connection to the application with help of method testSessionEstablished()

2. testGooglePlayFeatures() method test the access of Settings and modify some of them.

3. testSearchInputAndReturnBack() - search by query, find search results, return back and visit one of the chapters.

4. testSearchWrongInput() - input different wrong chars in the search to test this behavior.

5. testToInstallAppFromGooglePlayStore() - search by particular application, visit app detail page and try to install.


#### II. "Calculator" - obvious standard test
1. testSessionEstablished() - we check the session of started application

2. testCalculateSum() - calculate the sum

3. testCalculatePowNegative() - test the unreachable method

4. testDivideByZero() - test division by zero

5. testWrongInput() - test of nonce input and what will happens

6. testCalculateWithPi() - test for PI const


#### III. Selenium web page test
0. Before test we need to be sure that Gecko Driver is installed

1. Firefox based test - open the google, make search, click on first link, return back, click to another one.

Conclusion.

It could be a lot more testing scenarios, depends on the necessary obstacles.
