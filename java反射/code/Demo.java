package org.example;
import java.lang.reflect.Method;


public class Demo {
    public static void main(String[] args) throws  Exception {
        Runtime r = Runtime.getRuntime();
        Class c = Runtime.class;
        Method execMethod = c.getMethod("exec", String.class);
        execMethod.invoke(r, "calc");
    }
}

