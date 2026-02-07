package com.example.calculadoraasvr

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var pantalla : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pantalla = findViewById<TextView>(R.id.pantalla)
    }

    public fun sum(view : View){
        if (pantalla.text.equals("ERROR: Invalid operation")){
            pantalla.text = ""
        }
        pantalla.text = pantalla.text.toString() + "+"
    }

    public fun sub(view : View){
        if (pantalla.text.equals("ERROR: Invalid operation")){
            pantalla.text = ""
        }
        pantalla.text = pantalla.text.toString() + "-"
    }

    public fun div(view : View){
        if (pantalla.text.equals("ERROR: Invalid operation")){
            pantalla.text = ""
        }
        pantalla.text = pantalla.text.toString() + "/"
    }

    public fun mult(view : View){
        if (pantalla.text.equals("ERROR: Invalid operation")){
            pantalla.text = ""
        }
        pantalla.text = pantalla.text.toString() + "*"
    }

    public fun addExpresion(view : View){
        if (pantalla.text.equals("ERROR: Invalid operation")){
            pantalla.text = ""
        }
        val id = resources.getResourceEntryName(view.id).toString()
        val expr = id[id.length-1]
        pantalla.text = pantalla.text.toString() + "${expr}"
    }

    public fun calculate(view : View){
        //Toast.makeText(this, "Calculation", Toast.LENGTH_SHORT).show()
        val currentCalculation = pantalla.text
        val regex = Regex("""^\d+(\.\d+)?([+\-*/]\d+(\.\d+)?)*$""")
        var resultText : String

        if (!regex.matches(currentCalculation.toString())) {
            //Toast.makeText(this, "Operation Invalid", Toast.LENGTH_SHORT).show()
            resultText = "ERROR: Invalid operation"
            pantalla.text = resultText
        }
        else{
            //Toast.makeText(this, "Operation Valid", Toast.LENGTH_SHORT).show()
            val tokenRegex = Regex("""(\d+(\.\d+)?)|[+\-*/]""")
            val tokens = tokenRegex.findAll(currentCalculation.toString()).map { it.value }
                .toList().toMutableList()

            var i = 0
            while (i < tokens.size) {
                if (tokens[i] == "*" || tokens[i] == "/") {
                    val left = tokens[i-1].toDouble()
                    val right = tokens[i+1].toDouble()

                    val result = if (tokens[i] == "*") left * right else left / right

                    tokens[i-1] = result.toString()
                    tokens.removeAt(i)      // operator
                    tokens.removeAt(i)      // right
                    i = 0
                } else i++
            }

            // Second pass: + and -
            var result = tokens[0].toDouble()
            i = 1

            while (i < tokens.size) {
                val op = tokens[i]
                val value = tokens[i+1].toDouble()

                result = if (op == "+") result + value else result - value
                i += 2
            }

            resultText = result.toString()
            pantalla.text = resultText
        }
    }
}