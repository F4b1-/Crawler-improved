#include <SoftwareSerial.h>
#include <stdlib.h>
SoftwareSerial mySerial(12, 11); // RX, TX
#include <Adafruit_NeoPixel.h>

Adafruit_NeoPixel pixels = Adafruit_NeoPixel(1, 9, NEO_GRB + NEO_KHZ800);

int btData;
String inString;

// int trigPin = 10;    //Trig - green Jumper
// int echoPin = 13;    //Echo - yellow Jumper


void setupBluetooth() {
  // The HC-06 defaults to 9600 according to the datasheet
  mySerial.begin(9600);

/*  pixels.begin(); // This initializes the NeoPixel library.
  pixels.setPixelColor(0, pixels.Color(0,0,0));
  pixels.show(); // This sends the updated pixel color to the hardware.*/

 
}

void readBluetooth(int& order) {
  mySerial.flush();
  if (mySerial.available()){
    btData = mySerial.read();
    if (isDigit(btData)) {
      // convert the incoming byte to a char and add it to the string:
      inString += (char)btData;
    }
      order = inString.toInt();
      inString = "";
  
  }
}

void writeBluetooth(const char* message) {
    mySerial.write(message);
    mySerial.write(3);
}

void changeLight(boolean switchOn) {
  if(switchOn) {
     pixels.setPixelColor(0, pixels.Color(255,0,0));
     pixels.show(); // This sends the updated pixel color to the hardware.
  } 
  else {
     pixels.setPixelColor(0, pixels.Color(0,0,0));
     pixels.show(); // This sends the updated pixel color to the hardware.
  }

}


long getSonarDistance()
{

 
 
  // Read the signal from the sensor: a HIGH pulse whose
  // duration is the time (in microseconds) from the sending
  // of the ping to the reception of its echo off of an object.
  pinMode(echoPin, INPUT);
  duration = pulseIn(echoPin, HIGH);
 
  // convert the time into a distance
  cm = (duration/2) / 29.1;

  return cm;

}


