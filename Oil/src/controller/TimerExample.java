package controller;

import java.util.Timer;
import java.util.TimerTask;

public class TimerExample {
	static int i = 0;
	static Timer timer = new Timer();

	public static void main(String[] args) {
		String qq = "a12312312M";
		System.out.println(qq.replaceAll("[^\\.0123456789]",""));

	}
}