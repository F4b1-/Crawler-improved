# Crawler-improved
Improved version of Sunfounder's Quadruped Crawling Robot.

![Crawler](https://github.com/F4b1-/Crawler-improved/blob/master/crawler.jpg)

Upgrades
------

After building the Crawling Robot I have made a few upgrades:

Bluetooth Support
------

The robot can now be controlled via Bluetooth. In order for this to work properly it was necessary to use a small Power Distribution Board.

Android App
------
You can now use the Android App available from this repo to control the robot.
![App](https://github.com/F4b1-/Crawler-improved/blob/master/screenshot_app.png)

Python script
------
You can also control the robot from your Computer using Bluetooth. Simply run the  python script.
You will nedd to install the following dependencies:
```
pip install pybluez
pip install pynput
```

Free Roam Mode
------
Making use of one of the many inexpensive ultrasound sensors a free roam/object avoidance mode was implemented. 
