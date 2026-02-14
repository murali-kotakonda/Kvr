package com.kvr.invoice.util;

public class NumberToWordsUtil {
    private static final String[] UNITS = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    private static final String[] TEENS = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private static final String[] TENS = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    
    public static String convert(Double amount) {
        if (amount == null || amount == 0) return "Zero Rupees Only";
        
        long rupees = amount.longValue();
        int paise = (int) Math.round((amount - rupees) * 100);
        
        StringBuilder words = new StringBuilder();
        words.append(convertToWords(rupees)).append(" Rupees");
        
        if (paise > 0) {
            words.append(" and ").append(convertToWords(paise)).append(" Paise");
        }
        words.append(" Only");
        
        return words.toString();
    }
    
    private static String convertToWords(long number) {
        if (number == 0) return "Zero";
        
        if (number < 10) return UNITS[(int) number];
        if (number < 20) return TEENS[(int) number - 10];
        if (number < 100) return TENS[(int) number / 10] + (number % 10 > 0 ? " " + UNITS[(int) number % 10] : "");
        if (number < 1000) return UNITS[(int) number / 100] + " Hundred" + (number % 100 > 0 ? " " + convertToWords(number % 100) : "");
        if (number < 100000) return convertToWords(number / 1000) + " Thousand" + (number % 1000 > 0 ? " " + convertToWords(number % 1000) : "");
        if (number < 10000000) return convertToWords(number / 100000) + " Lakh" + (number % 100000 > 0 ? " " + convertToWords(number % 100000) : "");
        
        return convertToWords(number / 10000000) + " Crore" + (number % 10000000 > 0 ? " " + convertToWords(number % 10000000) : "");
    }
}
