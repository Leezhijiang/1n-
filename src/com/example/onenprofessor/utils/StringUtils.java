package com.example.onenprofessor.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
	/**
	 * 
	 * ÅÐ¶Ï×Ö·û´®ÊÇ·ñÎª¿Õ
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s){

		if (s == null || s == "")  
			return true;
		else {
			return false;
		}

	}
	public static boolean isDigitalAndWord(String text){ 

		String strPattern = "[a-zA-Z0-9]+";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(text);
		return m.matches();


	}
}
