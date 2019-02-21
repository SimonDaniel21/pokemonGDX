package net.simondaniel.aux;

import java.util.Random;

public class MyRandom {

	public static Random random;
	
	static {
		random = new Random(System.currentTimeMillis());
	}
}
