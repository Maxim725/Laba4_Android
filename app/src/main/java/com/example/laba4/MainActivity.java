package com.example.laba4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private static final int CALL_PHONE_PERMISSION_CODE = 100;
    private static final int READ_CONTACTS_PERMISSION_CODE = 101;
    private static final int PICK_CONTACT = 102;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.stackTrace);
        textView.append("onCreate\n");

        Button phoneButton = findViewById(R.id.phoneButton);
        phoneButton.setOnClickListener(view ->
                CheckCallPhonePermission(Manifest.permission.CALL_PHONE,
                        CALL_PHONE_PERMISSION_CODE));

        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(view ->
                CheckReadContactsPermission(Manifest.permission.READ_CONTACTS,
                        READ_CONTACTS_PERMISSION_CODE));

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view ->
                ClearText());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        textView.append("onStart\n");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        textView.append("onResume\n");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        textView.append("onPause\n");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        textView.append("onStop\n");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        textView.append("onRestart\n");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        textView.append("onDestroy\n");
    }

    private void ClearText()
    {
        textView.setText("");
    }

    public void CheckCallPhonePermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission }, requestCode);
        }
        else
            CallPhone();
    }

    public void CheckReadContactsPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission }, requestCode);
        }
        else
            ReedContacts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        if (requestCode == CALL_PHONE_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "Разрешение предоставлено (CALL_PHONE)",
                        Toast.LENGTH_SHORT).show();
                CallPhone();
            }
            else
                Toast.makeText(MainActivity.this, "Разрешение отклонено (CALL_PHONE)",
                        Toast.LENGTH_SHORT).show();
        }

        if (requestCode == READ_CONTACTS_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "Разрешение предоставлено (READ_CONTACTS)",
                        Toast.LENGTH_SHORT).show();
                ReedContacts();
            }
            else
                Toast.makeText(MainActivity.this, "Разрешение отклонено (READ_CONTACTS)",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void CallPhone()
    {
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                "11111199911119991"));
        startActivity(dialIntent);
    }

    private void ReedContacts()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode)
        {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id =
                                c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        @SuppressLint("Range") String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();

                            @SuppressLint("Range")
                            String phn_no = phones.getString(phones.getColumnIndex("data1"));

                            @SuppressLint("Range")
                            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));

                            Toast.makeText(this, "contact info : " + phn_no + "\n" + name, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }
    }
}