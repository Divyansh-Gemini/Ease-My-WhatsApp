package dev.divyanshgemini.easemywhatsapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.divyanshgemini.easemywhatsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCopy.setOnClickListener {
            val hyperlink = processInput()

            if (hyperlink.isEmpty())
                Toast.makeText(this@MainActivity, "Fill at least 1 field", Toast.LENGTH_SHORT).show()
            else if (hyperlink != " ") {
                // If valid hyperlink is generated from processInput(), then copying it
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("hyperlink", hyperlink)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(this@MainActivity, "Link copied", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnOpen.setOnClickListener {
            val hyperlink = processInput()

            // If both the fields are empty, then Opening WhatsApp.
            if (hyperlink.isEmpty())
                startActivity(packageManager.getLaunchIntentForPackage("com.whatsapp"))

            else if (hyperlink == " ");

            // If valid hyperlink is generated from processInput(), then redirecting to it.
            else startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(hyperlink)))
        }
    }

    // If both the fields are not empty & number is correct, then generating hyperlink.
    private fun processInput(): String {
        var mobile = binding.editTextMobile.text.toString().trim()
        val message = binding.editTextMessage.text.toString().trim().replace(" ", "+").replace("\n", "%0a")
        var result: String

        if (mobile.isEmpty() && message.isEmpty())
            result = ""
        else if (mobile.isNotEmpty() && mobile.length != 10) {
            binding.editTextMobile.error = "Enter a valid no."
            result = " "
        }
        else {
            mobile = if (mobile.length == 10) "91$mobile" else ""

            result = "https://wa.me/$mobile"

            if (message.isNotEmpty()) result += "?text=$message"
        }

        return result
    }
}