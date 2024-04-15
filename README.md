# WordSearchHVDR

* [App Features](app-features)
* [App Configuration Options](app-configuration-options)
* [Project Import Tips](#project-import-tips)
* [Configure Run Environment](#configure-run-environment)
* [Readme File](#readme-file)

__Purpose of Project:__ Unsuccessful looking for a complete word search puzzle for an Android device. This project will fill that void.

__OS Platform: Android:__ Build: SDK 30

__Language:__ Kotlin

__License:__ MIT License

__Version:__ 1.0

__Date:__ 4/12/2024

__Language:__ U.S. English (only)

__Category:__ Word Search Puzzle

<div id="features"></div>

---

### App Features

* Word direction placement (HVDR):
	* Horizontal
	* Vertical
	* Diagonal Down
	* Diagonal Up
	* Reverse

* Word placement can be reversed for any direction.
* Word selection is performed by clicking the start of the word then clicking the end of the word. The selection can also be reversed (clicking the end of the word then clicking the start of the word).
* Selecting a word will produce a random color for the word selection.
* Game restart button will restart the game and reset any progress.
* Show result button will show the words by marking them with a red border.
* Support for night theme.
* No support for screen rotation.

---

### App Configuration Options
* Class `WordSearchValues` contains all configuration variables.

* Variable `gameboardDimension` - size of the gameboard (set to 12 x12).
	* The larger the value, the more likely the gameboard will not fit the resolution of the device.

* Variable `maxWordLength` - maximum length of the word from the repository to place on the gameboard (set to 10).
	* `maxWordLenght` must be less than or equal to gameboard dimension.
	* The smaller the length, the more likely the app will find a placement for the word.

* Variable `maxWords` - the number of words to place on the gameboard (set to 6).
	* Must be an even number between 2 and 10.
	* The larger the value, the more likely the app will not find a placement for the word on the gameboard.
	* The larger the value, the more likely the buttons will not fit the resolution of the device.

* Variable `maxPlacementAttempts` - number of attempts to find a word placement on the gameboard (set to 50).
	* If no replacement is found the word is excluded.
	* If this value is set too high may cause the app to crash.

* Variable `maxWordAttempts` - number of attempts to found a word from the repository that is equal to or less than `maxWordLength`.

---

#### Beginning screen of Word Search

![Game Start](/images/start.png)

---

#### Show Result button selected

![Show Word Results](/images/results.png)

---

#### End of game

![Game Finished](/images/finish.png)

---

### Project Import Tips

After cloning this project and opening it in Android Studio, you may receive the following notification.

![Gradle JDK Missing](/images/Gradle-JDK-Missing.png)

Select the link `Select the Gradle JDK location` or change the JDK in settings.

Replace the underscores with your user home directory. This path is dependent on your Android Studio and Gradle SDK installation.

![Gradle JDK Setting](/images/Gradle-JDK-Setting.png)

Ensure that a Gradle local.properties is created in the project root directory and that it contains the Gradle SDK directory.

![Local Properties Directory](/images/Local-Properties-Directory.png)

The contents of `local.properties` should be your Gradle SDK directory.

```
## This file is automatically generated by Android Studio.
# Do not modify this file -- YOUR CHANGES WILL BE ERASED!
#
# This file should *NOT* be checked into Version Control Systems,
# as it contains information specific to your local configuration.
#
# Location of the SDK. This is only used by Gradle.
# For customization when using a Version Control System, please read the
# header note.
sdk.dir=C\:\\Users\\wgw_\\AppData\\Local\\Android\\Sdk
```

---

### Configure Run Environment

To setup an Android run environment, select `Add Configuration...`.

![Run Configuration](/images/Run-Configuration.png)

Next select `Add new run configuration...` and select Android.

![Run Configuration Android](/images/Run-Configuration-Android.png)

Setup the following configuration.

![Run Configuration Setup](/images/Run-Configuration-Setup.png)

After the run environment is configured, ensure you have an emulator (Pixel 6 API 30) configured or your personal Android device is connected.

![Run Configuration Emulator](/images/Run-Configuration-Emulator.png)

---

### Readme File

Android Studio may not display the README.md file due to a bug, just select the editor (code) tab.
