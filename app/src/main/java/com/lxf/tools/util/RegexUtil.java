package com.lxf.tools.util;


import java.util.regex.Pattern;

public class RegexUtil {
    public static final String URL = "(((http|ftp|https)://)?)([a-zA-Z0-9.-])(:[0-9]{1,4})/[a-zA-Z0-9&%./-~-]*";

    public static boolean isMatch(String regex,String message){
        return Pattern.matches(regex,message);
    }
}
