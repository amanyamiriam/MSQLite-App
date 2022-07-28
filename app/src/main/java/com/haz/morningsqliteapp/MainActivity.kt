package com.haz.morningsqliteapp

import android.content.Context
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var editTextName: EditText? = null
    var editTextEmail: EditText? = null
    var editTextNumber: EditText? = null
    var buttonSave: Button? = null
    var buttonView: Button? = null
    var buttonDelete: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextName = findViewById(R.id.mEdtName)
        editTextEmail = findViewById(R.id.mEdtEmail)
        editTextNumber = findViewById(R.id.mEdtNumber)
        buttonSave = findViewById(R.id.mBtnSave)
        buttonView = findViewById(R.id.mBtnView)
        buttonDelete = findViewById(R.id.mBtnDelete)
        var db = openOrCreateDatabase("votersDB", Context.MODE_PRIVATE, null)
        //create a table called user inside the votersDB
        db.execSQL("CREATE TABLE IF NOT EXISTS user(jina VARCHAR,arafa VARCHAR, kitambulisho VARCHAR)")
        //Set a listener to button  save to implement the saving
        buttonSave!!.setOnClickListener {
            //Receive the data from the user
            var userName = editTextName!!.text.toString()
            var userEmail = editTextEmail!!.text.toString()
            var userNumber = editTextNumber!!.text.toString()
            //Check if the user is submitting empty fields
            if (userName.isEmpty() || userEmail.isEmpty() || userNumber.isEmpty()) {
                displayMessage("Empty FIELDS!!", "Please fill all inputs!!")
            } else {
                //proceed to save the data
                db.execSQL("INSERT INTO users VALUES('" + userName + "','" + userEmail + "','" + userNumber + "'")
                displayMessage("SUCCESS!!!", "Data saved successfull!!")
                clear()
            }

        }
        buttonView!!.setOnClickListener {
            //use cursor to select all the data from the database
            var cursor = db.rawQuery("SELECT *FROM users", null)
            //check if there is no record in the db
            if (cursor.count == 0) {
                displayMessage("NO DATA!!!", "Sorry, the db is empty!!")
            } else {
                //Use a string buffer to append all the records for display
                var buffer = StringBuffer()
                //use a loop to display data per row
                while (cursor.moveToNext()) {
                    buffer.append(cursor.getString(0) + "\n")//column 0 is for name
                    buffer.append(cursor.getString(1) + "\n")//column 1 is for email
                    buffer.append(cursor.getString(2) + "\n")//column 2 is for number
                }
                displayMessage("DB RECORDS", buffer.toString())
            }
        }

        buttonDelete!!.setOnClickListener {
            //receive the number from the user
            var number = editTextNumber!!.text.toString().trim()
            //Check if the user is submitting an empty field
            if (number.isEmpty()) {
                displayMessage("EMPTY FIELD!!!", "Please enter Number!!!")
            } else {
                //Use cursor to select user with provided ID
                var cursor =
                    db.rawQuery("SELECT * FROM users WHERE kitambulisho='" + number + "'",null)
                //Check if there is a user with the provided id
                if (cursor.count == 0){
                    displayMessage("NO RECORD!!!", "Sorry,no user found!!!")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+number+"'")
                    displayMessage("SUCCESS!!!","Record deleted successfull")
                    clear()
                }

            }
        }
    }

     fun displayMessage(kichwa: String, ujumbe: String) {
         var  alertDialog = AlertDialog.Builder(applicationContext)
         alertDialog.setTitle(kichwa)
         alertDialog.setMessage(ujumbe)
         alertDialog.create().show()

     }
    fun clear(){
        editTextName!!.setText("")
        editTextEmail!!.setText("")
        editTextNumber!!.setText("")
    }

}