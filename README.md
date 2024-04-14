# WordSearchHVDR

__Purpose of Project:__ Unsuccessful looking for a complete word search puzzle for an Android device. This project will fill that void.

__OS Platform: Android:__ Build: SDK 30

__Language:__ Kotlin

__License:__ MIT License

__Version:__ 1.0

__Date:__ 4/12/2024

__Language:__ U.S. English (only)

__Category:__ Word Search Puzzle

### Features:

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

![start.png](/images/start.png)

---

#### Show Result button selected

![results.png](/images/results.png)

---

#### End of game

![finish.png](/images/finish.png)
