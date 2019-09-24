package com.syd.community;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "/usr/local/bin/wkhtmltoimage --quality 75  https://www.nowcoder.com /Users/syd/Desktop/temp/3.png";
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
            System.out.println("ok.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
