package com.miaosha.Utils;

import java.io.Serializable;

public class UUIDUtils implements Serializable {
    public static String generatorUUID(){
        return java.util.UUID.randomUUID().toString().replaceAll("-","");
    }

    public static void main(String[] args) {
       String UUID = generatorUUID();
        System.out.println(UUID);
    }
}
