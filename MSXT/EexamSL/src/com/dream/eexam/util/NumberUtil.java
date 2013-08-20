package com.dream.eexam.util;

public class NumberUtil {
	
	
	/**
	 * 保留0位小数
	 * @param c
	 * @return
	 */
    public static String formatData0Deci(double c) {
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0");
        return myformat.format(c);
    }
    
	/**
	 * 保留一位小数
	 * @param c
	 * @return
	 */
    public static String formatData1Deci(double c) {
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0.0");
        return myformat.format(c);
    }
    
    /**
	 * 保留两位小数
	 * @param c
	 * @return
	 */
    public static String formatData2Deci(double c) {
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0.00");
        return myformat.format(c);
    }

    /**
     * 保留四位小数
     * @param c
     * @return
     */
    public static String formatData4Deci(double c) {
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0.0000");
        return myformat.format(c);
    }
}
