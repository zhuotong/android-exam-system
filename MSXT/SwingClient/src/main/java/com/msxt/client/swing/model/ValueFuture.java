package com.msxt.client.swing.model;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ValueFuture<T> implements Future<T>{
	T value;
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public T get(){
		return value;
	}
	
	@Override
	public T get(long timeout, TimeUnit unit){
		return value;
	}
	
	public void set(T value){
		this.value = value;
	}
}
