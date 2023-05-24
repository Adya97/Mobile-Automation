package utilities;


import base.BaseTest;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ReadConfig {

    static Properties pro;

    public ReadConfig()
    {
        File src = new File("C:\\Users\\ViNUS\\IdeaProjects\\MobileAutomationFramework\\resouces\\global.properties");

        try {
            FileInputStream fis = new FileInputStream(src);
            pro = new Properties();
            pro.load(fis);
        } catch (Exception e) {
            System.out.println("Exception is " + e.getMessage());
        }
    }
    public static String getMobilePlatformName(){
        return System.getenv("platform");
    }
}