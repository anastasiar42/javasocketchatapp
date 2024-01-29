# Chat Application

@author Anastasiia Ryzhkova
@version Java 21

# Description

The Chat Application facilitates real-time communication between client and a server. Client and
server can exchange messages.

# To Run

Compile the application from bin directory:
jar -cf ChatApp.jar *
Server is run from console at .jar file directory:
java --enable-preview -cp.:./ChatApp.jar ie.atu.sw.Runner server
Client is run from console (separate from server terminal) at .jar file directory:
java- -enable-preview -cp.:./ChatApp.jar ie.atu.sw.Runner client localhost

# Process overview

```
● Initialise server
   ○ The main method in ChatServer class starts the server by creating a ServerSocket on
the specified port (PORT).
   ○ It creates the ServerInputThread to handle server-side input concurrently.
   ○ Run method of ServerInputThread class (nestedinChatServer) continuously reads
input from the server's console using System.in. and sends server messages to the
connected client.
● Client Connection
   ○ The main method in ChatServer accepts a client connection using server.accept()
● Exchange Messages Between Server and Client
   ○ In the ChatHandler class, the sendMessage method writes a message to the client's
outputstream, which is then sent to the client, the main method reads user input and
sends it to the server.
   ○ The serverListener Thread in ChatClient continuously listens for messages from the
server and prints them to the client's console.
```

# Considerations
```
1.In the ChatClient class, the server's hostname is obtained from the command-line arguments:
String hostname=args.length> 0 ?args[0]:"localhost";
So to connect to server on local host command **java--enable-preview-cp.:./ChatApp.jar
ie.atu.sw.Runner client localhost** shall be used
2.In the ChatClientclass, if the Socket constructor fails to establish a connection with the server, an IOException is caught, and the error message “Error: Could not connect to the server. Please check
the server address and try again.” is printed.
```

