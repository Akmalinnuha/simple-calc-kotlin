package com.akmalinnuha.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var edTXT : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edTXT = findViewById(R.id.edText)

        val button1 = findViewById<Button>(R.id.btn1)
        button1.setOnClickListener { addText("1") }
        val button2 = findViewById<Button>(R.id.btn2)
        button2.setOnClickListener { addText("2") }
        val button3 = findViewById<Button>(R.id.btn3)
        button3.setOnClickListener { addText("3") }
        val button4 = findViewById<Button>(R.id.btn4)
        button4.setOnClickListener { addText("4") }
        val button5 = findViewById<Button>(R.id.btn5)
        button5.setOnClickListener { addText("5") }
        val button6 = findViewById<Button>(R.id.btn6)
        button6.setOnClickListener { addText("6") }
        val button7 = findViewById<Button>(R.id.btn7)
        button7.setOnClickListener { addText("7") }
        val button8 = findViewById<Button>(R.id.btn8)
        button8.setOnClickListener { addText("8") }
        val button9 = findViewById<Button>(R.id.btn9)
        button9.setOnClickListener { addText("9") }
        val button0 = findViewById<Button>(R.id.btn0)
        button0.setOnClickListener { addText("0") }
        val btnTambah = findViewById<Button>(R.id.addition)
        val btnKurang = findViewById<Button>(R.id.subtraction)
        val btnKali = findViewById<Button>(R.id.multiplication)
        val btnBagi = findViewById<Button>(R.id.dividing)
        btnTambah.setOnClickListener(this)
        btnKurang.setOnClickListener(this)
        btnKali.setOnClickListener(this)
        btnBagi.setOnClickListener(this)

        val btnClear = findViewById<Button>(R.id.clear)
        btnClear.setOnClickListener { edTXT.text = "" }
        val btnCE = findViewById<Button>(R.id.CE)
        btnCE.setOnClickListener { edTXT.text = getText().subSequence(0,getLength()) }
        val btnEval = findViewById<Button>(R.id.eval)
        btnEval.setOnClickListener {
            val numbers = getNum(edTXT.text.toString())
            val operatian = getOperation(edTXT.text.toString())
            edTXT.text = calculate(numbers,operatian)
        }
    }

    private fun getLength(): Int {
        return edTXT.length()-1
    }

    private fun getText(): String {
        return edTXT.text.toString()
    }

    private fun addText(txt: String) {
        edTXT.append(txt)
    }

    @SuppressLint("SetTextI18n")
    private fun changeLastValue(edt: TextView, chara :String) {
        edt.text = getText().substring(0,getLength())+chara
    }

    override fun onClick(v: View?) {
        val chasm : String = edTXT.text.toString()
        when (v?.id) {
            R.id.addition -> {
                if (chasm.isEmpty()) {
                    edTXT.text = ""
                } else if ((chasm[getLength()] == '-') or (chasm[getLength()] == '*') or (chasm[getLength()] == '/')) {
                    changeLastValue(edTXT,"+")
                } else {
                    addText("+")
                }
            }
            R.id.subtraction -> {
                if (chasm.isEmpty()) {
                    edTXT.text = ""
                } else if ((chasm[getLength()] == '+') or (chasm[getLength()] == '*') or (chasm[getLength()] == '/')) {
                    changeLastValue(edTXT,"–")
                } else {
                    addText("–")
                }
            }
            R.id.multiplication -> {
                if (chasm.isEmpty()) {
                    edTXT.text = ""
                }  else if ((chasm[getLength()] == '+') or (chasm[getLength()] == '-') or (chasm[getLength()] == '/')) {
                    changeLastValue(edTXT,"*")
                } else {
                    addText("*")
                }
            }
            R.id.dividing -> {
                if (chasm.isEmpty()) {
                    edTXT.text = ""
                } else if ((chasm[getLength()] == '-') or (chasm[getLength()] == '*') or (chasm[getLength()] == '+')) {
                    changeLastValue(edTXT,"/")
                } else {
                    addText("/")
                }
            }
        }
    }
    private fun calculate(numbers: ArrayList<String>, operator: ArrayList<Char>): String {
        var flow = true
        while(flow) {
            if (checkPriority(operator)) {
                val lan: Int = getIndex(operator)
                if(operator.elementAt(lan) == '*') {
                    val a: Double = numbers.elementAt(lan).toDouble()
                    val b: Double = numbers.elementAt(lan+1).toDouble()
                    val result = a*b
                    numbers[lan] = result.toString()
                    numbers.removeAt(lan+1)
                    operator.removeAt(lan)
                } else {
                    val a: Double = numbers.elementAt(lan).toDouble()
                    val b: Double = numbers.elementAt(lan+1).toDouble()
                    val result: Double = a/b
                    numbers[lan] = result.toString()
                    numbers.removeAt(lan+1)
                    operator.removeAt(lan)
                }
            } else {
                flow = false
            }
        }
        while (operator.isNotEmpty()) {
            if (operator.elementAt(0) == '+') {
                val a: Double = numbers.elementAt(0).toDouble()
                val b: Double = numbers.elementAt(1).toDouble()
                val result: Double = a+b
                var res1: Int
                if (result % 1.0 == 0.0 ) {
                    res1 = result.toInt()
                    numbers[0] = res1.toString()
                    numbers.removeAt(1)
                    operator.removeAt(0)
                } else {
                    numbers[0] = result.toString()
                    numbers.removeAt(1)
                    operator.removeAt(0)
                }
            } else {
                val a: Double = numbers.elementAt(0).toDouble()
                val b: Double = numbers.elementAt(1).toDouble()
                val result: Double = a-b
                var res1: Int
                if (result % 1.0 == 0.0 ) {
                    res1 = result.toInt()
                    numbers[0] = res1.toString()
                    numbers.removeAt(1)
                    operator.removeAt(0)
                } else {
                    numbers[0] = result.toString()
                    numbers.removeAt(1)
                    operator.removeAt(0)
                }
            }
        }
        return numbers[0]
    }

    private fun getNum(operation: String): ArrayList<String> {
        return operation.split("+", "–", "*", "/") as ArrayList<String>
    }

    private fun getOperation(operation : String): ArrayList<Char> {
        val operators = ArrayList<Char>()
        for (i in 1..operation.length) {
            if(operation[i-1] == '+') {
                operators.add('+')
            } else if(operation[i-1] == '–') {
                operators.add('-')
            } else if(operation[i-1] == '*') {
                operators.add('*')
            } else if(operation[i-1] == '/') {
                operators.add('/')
            }
        }
        return operators
    }

    private fun checkPriority(operator: ArrayList<Char>): Boolean {
        var judgement = false
        for (i in operator) {
            if ((i == '*') or (i == '/')) {
                judgement = true
                break
            }
        }
        return judgement
    }

    private fun getIndex(operator: ArrayList<Char>): Int {
        var a = 0
        var count = 0
        for (i in operator) {
            if ((i == '*') or (i == '/')) {
                a = count
                break
            }
            count++
        }
        return a
    }
}