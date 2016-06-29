package com.tch.test.tigase.mytigase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
	
	private static final Logger logger = Logger.getLogger(Test.class.getName());

	public static void main(String[] args) {
		/*if (logger.isLoggable(Level.FINEST)) {
			logger.log(Level.FINEST, "MyPlugin ================= 接收到packet：");
			logger.log(Level.FINEST, "MyPlugin ================= 接收到settings：");
			System.out.println("MyPlugin ================= 接收到packet：");
		}*/
		Long a = 1000L;
		Long b = 1000L;
		System.out.println(a == b);
	}
	
}
