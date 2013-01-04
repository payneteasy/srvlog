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
            DatabaseUtil.createDatabase(new String[]{"bash", "create_database.sh"}, ".");
        } catch (IOException e) {
            System.out.println("Can't create database ");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Can't create database ");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BufferedReader bufferedReader = null;
        try {
//             bufferedReader = new BufferedReader(new FileReader("../../../../../../../srvlog-sql/db/create_database.sh"));
            bufferedReader = new BufferedReader(new FileReader("./create_database.sh"));

            String inLine;
            while ((inLine = bufferedReader.readLine()) != null) {  // exclude newline
                System.out.println(inLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
