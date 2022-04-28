package example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.room.Room
import example.calculator.model.History
import org.w3c.dom.Text
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }

    val historyLayout: View by lazy {
        findViewById(R.id.historyLayout)
    }

    val historyLinearLayout: LinearLayout by lazy {
        findViewById(R.id.historyLinearLayout)
    }

    lateinit var db: AppDatabase

    private var hasOperator: Boolean = false
    private var isOperator: Boolean = false
    private var isResultButtonClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun clearButtonClicked(view:View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
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
            R.id.buttonDivider -> operatorButtonClicked("/")
            R.id.buttonMinus -> operatorButtonClicked("-")
            R.id.buttonPlus -> operatorButtonClicked("+")
            R.id.buttonModulo -> operatorButtonClicked("%")
            R.id.buttonMulti -> operatorButtonClicked("X")
        }

    }

    fun resultButtonClicked(view:View) {
        val expresstionTexts = expressionTextView.text.split(" ")

        if(expresstionTexts.isEmpty() || expresstionTexts.size == 1) {
            return
        }

        if(expresstionTexts.size != 3 && hasOperator) {
            Toast.makeText(this,"Expression is not completed",Toast.LENGTH_SHORT).show()
            return
        }

        if(expresstionTexts[0].isNumber().not() || expresstionTexts[2].isNumber().not()) {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        Thread(Runnable {
            db.historyDao().insertHistory(History(null,expressionText,resultText))
        }).start()

        expressionTextView.text = resultTextView.text
        resultTextView.text = ""
        isOperator = false
        hasOperator = false
        isResultButtonClicked = true
    }

    fun numberButtonClicked(num: String) {
        if(isOperator) {
            expressionTextView.append(" ")
        } else if(isResultButtonClicked) {
            expressionTextView.text = ""
            isResultButtonClicked = false
        }

        isOperator = false

        var expressionText = expressionTextView.text.toString().split(" ")

        if(expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this,"You cannot enter more than 15 digits",Toast.LENGTH_SHORT).show()
            return
        }else if(expressionText.last().isEmpty() && num == "0") {
            Toast.makeText(this, "You cannot enter 0 for the first digit number", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(num)
        resultTextView.text = calculateExpression()
    }

    fun operatorButtonClicked(operator: String) {
        if(expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                var text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "You can add operator only once", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                expressionTextView.append(" $operator")
            }
        }

        isResultButtonClicked = false

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTextView.text.length-1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTextView.text = ssb

        isOperator = true
        hasOperator = true
    }

    fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")

        if(hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if(expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when(op){
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "X" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(view:View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row,null,false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = it.result

                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()
    }

    fun closeHistoryButtonClicked(view: View) {
        historyLayout.isVisible = false
    }

    fun historyClearButtonClicked(view: View) {
        historyLinearLayout.removeAllViews()

        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()

    }
}

fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}