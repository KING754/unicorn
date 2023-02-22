package com.game.client.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author KING
 * @date 2023/01/03
 */
public class TestMain {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String test = "0";
        byte[] bs = test.getBytes("UTF-8");
        byte[] bs3 = Arrays.copyOf(bs,64);
        System.out.println(Arrays.toString(bs3));

        byte[] bs2 = new byte[]{48,0,0,0,0};
        String str = new String(bs2,"UTF-8");
        System.out.println(str.trim());




    }
}
