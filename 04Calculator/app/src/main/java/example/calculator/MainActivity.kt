package example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clearButtonClicked(view:View) {

    }

    fun buttonClicked(view:View) {
        when(view.id) {
            R.id.button0 -> numberButtonClicked("0")
            R.id.button1 -> numberButtonClicked("1")
            R.id.button2 -> numberButtonClicked("2")
            R.id.button3 -> numberButtonClicked("3")
            R.id.button4 -> numberButtonClicked("4")
            R.id.button5 -> numberButtonClicked("5")
            R.id.button6 -> numberButtonClicked("6")
            R.id.button7 -> numberButtonClicked("7")
            R.id.button8 -> numberButtonClicked("8")
            R.id.button9 -> numberButtonClicked("9")
        }

    }

    fun historyButtonClicked(view:View) {

    }

    fun resultButtonClicked(view:View) {

    }

    fun numberButtonClicked(num: String) {
        var expressionText = expressionTextView.text.toString().split(" ")

        if(expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this,"You cannot enter more than 15 digits",Toast.LENGTH_SHORT).show()
            return
        }else if(expressionText.last().isEmpty() && num == "0") {
            Toast.makeText(this, "You cannot enter 0 for the first digit number", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(num)
    }
}