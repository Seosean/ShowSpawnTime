# ShowSpawnTime  

## Latest Version
[**1.14.4 Download**](https://github.com/Seosean/ShowSpawnTime/releases/download/sst/ShowSpawnTime-1.14.4.jar)
*(Absolutely same as 1.15 BETA 1)*
****
## Updates Logs
### Update 1.14.4 2023/8/28  
· Fixed a rare bug that you probably get kicked by server when a round ends.  
· Now when a wave passed, it will become darker instead of reserving its color.  
· Added Simplified Chinese and Traditional Chinese language supports.  
· Optimized lots of codes  

## Features
### Show Waves Intervals
![image](https://github.com/Seosean/ShowSpawnTime/assets/88036696/2b1458da-346c-4347-a4f0-b7ca5f751c04)

At the lower right corner, the wave information of the round will be displayed.  
When a certain wave is reached, there will be a sound of note triggerred,  
the yellow highlight will transfer to next wave (in a delay if in DE/BB, which can be cancelled in config),  
and the passed wave text will become darker gray.

### Powerup Alert (UPCOMING)
![UDE9GPF@XV0 LWY9S0$B9)M](https://github.com/Seosean/ShowSpawnTime/assets/88036696/ae500dfe-951f-4e14-b03f-ebee293aa8a9)

When a powerup spawned, the count down of powerup will instantly display in the screen.  
When they are nearly expiring in 10 seconds, they will blink for alert.  
If in a game you used to be in r2/r3/..., and met the dropping of insta or max,  
the mod will remember the pattern of ins/max/ss, then remind you in the next ins/max/ss round with "Insta Kill - Round". (Max Ammo and Shopping Spree as well.)
If you find that the pattern is incorrect, it happened when you restart Minecraft, or leave the game when it's in r2/r3/... and missed the first ins/max/ss.
**Note: I have already marked almost all special moments when a powerup drops, like when the last zombie died and the powerup drops in next round.**  
**In normal case, it should get patterns correctly. If you actually get a incorrect pattern, you can use "/sst ins 3", "/sst max 2" or "/sst ss 5" and so on to recorrect it.**  

### Game Time Recorder
![image](https://github.com/Seosean/ShowSpawnTime/assets/88036696/411b4903-1e00-4223-8460-ec0a6a2331ab)

After each round, mod will catch the game time from the scoreboard until the end of the round and send it to the chat.  
You may copy the text if you click the text in the chat.   
If you think its annoying to show the time in every round, you can switch the record mode in config.  
There are 4 modes for you: Tenfold, Quintuple, All, Off.

### Redundant Time Recorder
![image](https://github.com/Seosean/ShowSpawnTime/assets/88036696/33617cc8-5c66-4a4a-a482-a3ab9ab63646)

After each round, mod will record the time of the round, and caculate the redundant time from the final wave to the end when you clear up all zombies.
Note: A much more accurate timer as showcase is coming soon, it will be update in 1.15 ver.

### Lightning Rod Queue
![D8QOCILJS(E2EFEYH)A{~B0](https://github.com/Seosean/ShowSpawnTime/assets/88036696/9155d5a1-d1e6-4c91-aed1-54fb5ba19a00)
  
After using the lightning rod, a progress bar will be displayed in the middle of the screen, displayed by four extinguished lights.  
Each lightning strike will increase the progress bar by one and highlight one extinguished light.  
If there is no lightning strike within five seconds, all lights will turn off.  
If no lightning strike occurs within five seconds after extinguishing, the progress bar disappears.  
Note: Due to a judgment issue, the boss generation will result in a progress bar being displayed that goes out, which does not affect anything.

### Boss Alert (AA)


Besides the wave intervals feature, the text not only has a combination of gray and yellow.  
But in AA, waves with the old one will turn into a combination of dark green and green,  
waves with giants will turn into a combination of deep blue purple and sky blue,  
waves with both will turn into a combination of dark red and red,  
in order to alert the upcoming the old one and giants.
