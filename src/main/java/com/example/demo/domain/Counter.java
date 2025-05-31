package com.example.demo.domain;

public class Counter {
	private static long i = 0;

	public static long resetCounter() {
		i = 0;
		return i;
	}

	public static long getAndIncrement() {
		return i++;
	}
}
