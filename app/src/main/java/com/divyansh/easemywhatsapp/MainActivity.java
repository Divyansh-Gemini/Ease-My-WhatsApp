package com.divyansh.easemywhatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText1, editText2;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.editTextNumber1);
        editText2 = findViewById(R.id.editTextText1);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);

        btn1.setOnClickListener(v -> {
            String hyperlink = processInput();

            if (hyperlink.equals(""))
                Toast.makeText(MainActivity.this, "Fill atleast 1 field", Toast.LENGTH_SHORT).show();
            else if (!(hyperlink.equals("") || hyperlink.equals(" "))) {
                // If valid hyperlink is generated from processInput(), then copying it
                android.content.ClipboardManager myClipboard;
                myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("hyperlink", hyperlink);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(MainActivity.this, "Link copied", Toast.LENGTH_SHORT).show();
            }
        });

        btn2.setOnClickListener(v -> {
            String hyperlink = processInput();

            if (hyperlink.equals("")) {
                // If both the fields are empty, then Opening WhatsApp.
                PackageManager pm = getPackageManager();
                Intent i = pm.getLaunchIntentForPackage("com.whatsapp");
                startActivity(i);
            }
            else if (hyperlink.equals(" "));
            else {
                // If valid hyperlink is generated from processInput(), then redirecting to it.
                Intent openWhatsApp = new Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink));
                startActivity(openWhatsApp);
            }
        });
    }

    // If both the fields are not empty & number is correct, then generating hyperlink.
    String processInput() {
        String number = editText1.getText().toString();
        String message = (editText2.getText().toString()).trim().replaceAll(" ", "+").replaceAll("\n", "%0a");
        String result;

        if(number.isEmpty() && message.isEmpty())
            result = "";
        else if (!(number.isEmpty()) && number.length() != 10) {
            editText1.setError("Enter a valid no.");
            result = " ";
        }
        else {
            number = number.length() == 10 ? "91" + number : "";

            result = "https://wa.me/" + number;

            if (!(message.isEmpty()))
                result += "?text=" + message;
        }
        return result;
    }
}