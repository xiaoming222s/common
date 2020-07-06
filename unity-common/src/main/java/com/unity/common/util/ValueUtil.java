package com.unity.common.util;

public class ValueUtil<T> {
	private T _val;
	
	public ValueUtil(){}
	
	public ValueUtil(T o){
		_val = o;
	}
	
	public T get(){
		return _val;
	}
	
	public void set(T val){
		_val=val;
	}
}
