# Beat-Box-Program
A beat box synthesizer created in Java using Multi-threading, MIDI, I/O, and Networking libraries in order to provide the user with a chat room to share beats they've produced, save beats (serialization), and more functionality. (THIS PROGRAM DOES NOT INCLUDE ALL SOUND FILES ON MAC, AS MAC IS LACKING THE REQUIRED MIDI FILES IT WILL STILL PLAY, BUT WITH LIMITED BEATS).


**How To Run the Program:**

1.) Clone/Download the repo to a project directory.

2.) Navigate to where you downloaded the program and go to the directory: BeatBoxProgram/out/production

3.) Open two command lines, one for the server and one for the program, we will be connecting to your own computer as a\
    server (local host).
    
4.) In the first command line, run the command: % java MusicServer\
    after you hit enter the server will be up and running (leave this command line alone now)
    
5.) In the second command line, navigate to the BeatBoxProgram/out/production again, and this time issue the command:\
    % java BeatBox userName\
    **NOTE: We are taking a command line argument here for your preferred username, so just enter a name after BeatBox**
    
6.) The application should open in a window and you will be able to produce beats, save beats, send beats, and more.\
    To send a message to other users, just create a beat, and type a message in the upper textfield, and hit sendIt.

7.) To load another user's beat, simply click on their message in the chat box!

