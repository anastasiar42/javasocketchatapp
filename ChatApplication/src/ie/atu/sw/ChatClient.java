package ie.atu.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
	public static final int SERVER_PORT = 8080;

	public static void main(String[] args) {
		String hostname = args.length > 0 ? args[0] : "localhost";

		try (Socket socket = new Socket(hostname, SERVER_PORT)) {
			System.out.println("Connected to Chat Server on host " + hostname);

			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Writer out = new OutputStreamWriter(socket.getOutputStream());

			Thread serverListener = new Thread(() -> {
				try {
					String serverResponse;
					while ((serverResponse = serverInput.readLine()) != null) {
						System.out.println("Received message from server: " + serverResponse);
					}
				} catch (IOException e) {
					System.err.println("Connection to the server was lost. Please check your network connection.");

				}
			});
			serverListener.start();

			String userInputString;
			while ((userInputString = userInput.readLine()) != null) {
				out.write(userInputString + "\n");
				out.flush();
				if (userInputString.equals("\\q")) {
					break;
				}
			}

			// Stop the serverListener thread when user enters \q
			serverListener.interrupt();
			try {
				serverListener.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			System.out
					.println("Error: Could not connect to the server. Please check the server address and try again.");
		}
	}

}
