package ie.atu.sw;

public class Runner {
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
			startServer();
		} else if (args.length > 0 && args[0].equalsIgnoreCase("client")) {
			startClient(args);
		} else {
			System.out.println("Usage: java Runner server|client");
		}
	}

	private static void startServer() {
		System.out.println("Starting Chat Server...");
		ChatServer.main(new String[] {});
	}

	private static void startClient(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java Runner client <localhost>");
			return;
		}

		String hostname = args[1];
		System.out.println("Starting Chat Client...");
		ChatClient.main(new String[] { hostname });
	}
}
