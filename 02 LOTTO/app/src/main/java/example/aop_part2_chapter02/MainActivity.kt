package example.aop_part2_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.w3c.dom.Text
import java.util.*

class MainActivity : AppCompatActivity() {
    private val numberTextViewList: List<TextView> by lazy {
        listOf(
            findViewById(R.id.textView1),
            findViewById(R.id.textView2),
            findViewById(R.id.textView3),
            findViewById(R.id.textView4),
            findViewById(R.id.textView5),
            findViewById(R.id.textView6))
    }

    private val pickNumberSet = hashSetOf<Int>()

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val runButton: Button by lazy {
        findViewById(R.id.runButton)
    }

    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }

    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }

    private var didRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNumberPicker()
        initAddButton()
        initClearButton()
        initRunButton()
    }

    private fun initNumberPicker() {
        numberPicker.maxValue = 45
        numberPicker.minValue = 1
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            didRun = true
            val list = getRandomNumber()

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number,textView)
            }
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            val number = numberPicker.value
            val textView = numberTextViewList[pickNumberSet.size]

            if(didRun) {
                Toast.makeText(this, "You should clear and retry", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if(pickNumberSet.contains(number)) {
                Toast.makeText(this,"$number is already added", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(pickNumberSet.size >= 5) {
                Toast.makeText(this,"number can be added up to 6", Toast.LENGTH_SHORT).show()
            }

            textView.isVisible = true
            textView.text = number.toString()
            setNumberBackground(number, textView)

            pickNumberSet.add(number)
        }
    }

    private fun setNumberBackground(number: Int, textView: TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            didRun = false

            pickNumberSet.clear()
            for(item in numberTextViewList) {
                item.isVisible = false
            }
        }
    }

    private fun getRandomNumber(): List<Int> {
        var randomNumber = 0

        val numberList = mutableListOf<Int>()
            .apply {
                while(this.size < 6) {
                    randomNumber = Random().nextInt(45) + 1
                    if(pickNumberSet.contains(randomNumber)) {
                        continue
                    }
                    this.add(randomNumber)
                }
            }

        val newList = pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size)

        return newList.sorted()
    }
}