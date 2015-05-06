package com.example.andreas.mainview.documents;

import android.content.Context;

import com.example.andreas.mainview.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.*;
/**
 * Created by Andreas on 2015-05-05.
 */
public class Save {

    private DataInputStream goldReader;
    private DataInputStream itemReader;
    private DataInputStream partReader;

    private DataOutputStream goldWriter;
    private DataOutputStream itemWriter;
    private DataOutputStream partWriter;
    private Context context;


    public Save(Context context){

        this.context=context;



    }
    public void writeItem(String item){ //Kolla så den inte skriver över..
        try {


            //Något
            itemWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getItems(){

        ArrayList<String> itemlist=new ArrayList<String>();

        String item;
        try {
            item=itemReader.readLine();
            while(item!=null){
                itemlist.add(item);
                item=itemReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemlist;
    }
    public void writePart(int part){    //Dubbelkolla så den skriver över TANKEVERKSTAD!.
        try {
            partWriter = new DataOutputStream(
                    context.openFileOutput("partdoc.txt", Context.MODE_PRIVATE));

            partWriter.write(part);
            partWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public int getPart(){
        try {
            partReader = new DataInputStream(new DataInputStream(new BufferedInputStream(
                    context.openFileInput("partdoc.txt"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1; //if the document is empty it is part 1.
    }

    public void writeGold(int gold){    //Update Document with current amount of gold. .
        try {
            goldWriter = new DataOutputStream(
                    context.openFileOutput("golddoc.txt", Context.MODE_PRIVATE));
            goldWriter.writeInt(gold);
            goldWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public int getGold(){       //Get the saved amount gold.
        try {

            goldReader = new DataInputStream(new DataInputStream(new BufferedInputStream(
                    context.openFileInput("golddoc.txt"))));

            return goldReader.readInt();

        } catch (IOException e) {
            e.printStackTrace();
            return 0;           //if the document is empty.
        }

    }
}