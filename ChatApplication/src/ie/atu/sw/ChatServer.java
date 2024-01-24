package ie.atu.sw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	public final static int PORT = 8080;
	private static List<ChatHandler> clients = new ArrayList<>();

	// Create a separate thread to read input from the server terminal
	private static class ServerInputThread extends Thread {
		@Override
		public void run() {
			try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
				String serverMessage;
				while ((serverMessage = consoleReader.readLine()) != null) {
					sendServerMessage("Server: " + serverMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		// Start the server input thread
		ServerInputThread inputThread = new ServerInputThread();
		inputThread.start();

		// Start the server and handle client connections
		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Listening for connection on port " + PORT);
			while (true) {
				try {
					Socket connection = server.accept();
					System.out.println(
							"Client connected from " + connection.getInetAddress() + ":" + connection.getPort());

					ChatHandler handler = new ChatHandler(connection);
					clients.add(handler);
					handler.handle();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Couldn't start server");
		}
	}

	// Send server messages to client
	public static void sendServerMessage(String message) {
		synchronized (clients) {
			for (ChatHandler client : clients) {
				client.sendMessage(message);
			}
		}
	}

	private static class ChatHandler {
		private Socket connection;
		private BufferedReader in;
		private PrintWriter out;

		public ChatHandler(Socket connection) {
			this.connection = connection;
			try {
				this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				this.out = new PrintWriter(connection.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void handle() {
			try {
				while (true) {
					String message = in.readLine();
					if (message == null || message.equals("\\q")) {
						broadcast("User disconnected");
						break;
					}
					System.out.println("Message from " + connection.getInetAddress() + ": " + message);

					if (message.startsWith("Server:")) {
						System.out.println(
								"Server sent message to client: " + message.substring("Server:".length()).trim());
					} else {
						broadcast(message);
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					connection.close();
					clients.remove(this);
					broadcast("User disconnected");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void broadcast(String message) {
			synchronized (clients) {
				for (ChatHandler client : clients) {
					if (client != this) { // Exclude the current client
						client.sendMessage(message);
					}
				}
			}
		}

		private void sendMessage(String message) {
			out.println(message);
		}
	}
}
