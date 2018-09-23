"""
A simple Python script to send messages to the crawler over Bluetooth.
"""
from __future__ import print_function
import bluetooth
from pynput import keyboard
from pynput.keyboard import Key
import time




serverMACAddress = '98:d3:32:21:06:08'
port = 1
s = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
s.connect((serverMACAddress,port))



def on_release(key):
    if key == Key.esc:
    # Stop listener
        return False
    s.send(bytes(evaluate_key(key)))
    print('\rMoving...           ', end=''),
    time.sleep(2)
    print('\rWaiting for input...', end=''),
    #print(evaluate_key(key))
    #print('{0} released'.format(
    #    key))



def evaluate_key(key_pressed):
    if not isinstance(key_pressed, Key):
        key_pressed = key_pressed.char
    return {
        Key.up: 1,
        Key.down: 2,
        Key.left:  3,
        Key.right: 4,
        Key.space: 5,
        'd': 6,
        'r': 7,
        'l': 8
    }.get(key_pressed, -1)  


print('Hey, there! Use the arrow keys to navigate')
print('Press D for dancing')
print('Press R for free roaming')
print('Press L for the light\n\n')
print('Waiting for input')

# Collect events until released
with keyboard.Listener(
        on_release=on_release) as listener:
    listener.join()

s.close()

