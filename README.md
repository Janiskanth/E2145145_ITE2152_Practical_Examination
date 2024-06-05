**Overview**

The Weather Application is an Android app that provides real-time weather updates based on the user's current location. The app fetches weather details such as temperature, humidity, wind speed, and more using the OpenWeatherMap API. It also displays the current date, time, and the user's city name.

**Features**

Fetches and displays real-time weather information based on the user's location.
Displays the current date and time.
Shows detailed weather information including temperature, feels-like temperature, humidity, wind speed, cloudiness, and atmospheric pressure.
Uses the OpenWeatherMap API for weather data.
Displays city name based on geographical coordinates.
Handles location permissions and GPS status.

**Permissions**

The app requires the following permissions:

ACCESS_FINE_LOCATION: To access the user's precise location.
ACCESS_CROSS_LOCATION: To access the user's precise location.
INTERNET: To make network requests to the OpenWeatherMap API.

**Additional Features**

Error Handling:

Displays Toast messages and logs errors for debugging.
Handles JSON parsing errors and incomplete data scenarios.

Location Handling:

Checks for location permissions and requests updates only if GPS is enabled.
Prompts the user to enable GPS if it is turned off.

**Setup Instructions**

Clone the Repository:
git clone https://github.com/Janiskanth/E2145145_ITE2152_Practical_Examination.git
cd WeatherAPP

Open the Project in Android Studio:
Start Android Studio and open the project from the cloned repository.

Add OpenWeatherMap API Key:
Replace the placeholder appid in MainActivity.java with your OpenWeatherMap API key: private final String appid = "YOUR_API_KEY_HERE";

Add Dependencies
Add the Volley library to your build.gradle file: dependencies {
    implementation 'com.android.volley:volley:1.2.1'
}

Run the App:
Connect your Android device or start an emulator.
Build and run the project on your device/emulator.

**Usage Instraction**
Open the app and click the "Show Weather" button.
The app will request location permissions if not already granted.
If GPS is enabled, the app will fetch and display the current weather details.

# E2145145_ITE2152_Practical_Examination
ITE 2152 - Mobile Application Development -Practical Exam (E2145145)
