package com.craftstone.cloudcraft;

import java.util.Scanner;

public class ConsoleScanner extends Thread {
	public void run() {
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				if (scanner.nextLine().equals("stop")) {
					CloudCraftServer.TERMINATE = true;
					ServerDeamon.EXIT = true;
				}
			}
		} finally {
			scanner.close();
		}
	}
}
