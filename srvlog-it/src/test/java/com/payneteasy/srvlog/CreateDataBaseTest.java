package com.payneteasy.srvlog;

import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

/**
 * Date: 04.01.13
 */
public class CreateDataBaseTest {

    @Test
    @Ignore
    public void createDatabaseTest(){
        try {
            DatabaseUtil.createDatabase(new String[]{"bash", "./create_database.sh"});
        } catch (IOException e) {
            System.out.println("Can't create database ");

        } catch (InterruptedException e) {
            System.out.println("Can't create database ");
            e.printStackTrace();
        }
    }
}
