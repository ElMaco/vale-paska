package vale;

public class Testi {

	public static void main(String[] args) {
		
		Thread serverThread = new Thread() {
            public void run() {
                vpServer.main(null);
            }
        };
        
        Thread clientThread = new Thread() {
            public void run() {
                VpClientGui.main(null);
            }
        };
        
        
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clientThread.start();

	}

}
