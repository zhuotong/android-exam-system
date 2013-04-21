package com.msxt.client.i18n;

import java.util.ResourceBundle;

public class ResourceBundleHandler {
	public static final ResourceBundle message = ResourceBundle.getBundle("ClientBase");
	
	public static String getString(String key) {
        return message.getString(key);
    }
}
