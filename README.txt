My Final Project is a Multi-Threaded Chat Messenger, with both public and private messaging.
I built it with Sockets, threads, and Swing GUI. The program lets several clients connect to a server and chat together in a shared GC (Group Chat), and even send private messages to each other that I call "whispers".


A normal message will appear like this:
"Nick: Hello World!"

A whisper can be sent by typing it in this format:
/w username message

The recipient of the whisper will see: 
"[Whisper from Nick]: hey, this is Nick."

The sender will also see their whisper in the chat, just as confirmation that it went through:
"[Whisper to Nick]: hey, this is Nick."



The server can handle several simultaneous users. Each client is given its own thread thanks to ClientHandler.


When a client opens the GUI, they must first enter a username. Their username should not be empty, nor should it be identical to another username of a client in the chat. In both of these situations, the GUI will close. Also, the client handles a failed server connection, sudden disconnects, and users trying to send messages while disconnected. 

You can quit from the chat by either closing the GUI, or typing quit in the chat. 

This project was made for my Advanced Programming for IT class.