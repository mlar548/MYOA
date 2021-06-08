package com.gec.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5Util {

	public static Md5Hash getMd5Hash(String password, String salt) {

		return new Md5Hash(password, salt, 2);
	}
}
