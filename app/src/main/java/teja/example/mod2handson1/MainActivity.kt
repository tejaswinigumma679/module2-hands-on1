package teja.example.mod2handson1

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayout = findViewById<LinearLayout>(R.id.formContainer)

        // Load JSON file
        val jsonConfig = resources.openRawResource(R.raw.form_config)
            .bufferedReader()
            .use { it.readText() }

        // Parse JSON
        val formConfig = Gson().fromJson(jsonConfig, FormConfig::class.java)

        // Create fields dynamically
        formConfig.fields.forEach { field ->
            when (field.type) {
                "text" -> createTextField(linearLayout, field.label, field.hint)
                "number" -> createNumberField(linearLayout, field.label, field.hint)
                "dropdown" -> createDropdownField(linearLayout, field.label, field.options ?: emptyList())
                "checkbox" -> createCheckboxField(linearLayout, field.label)
            }
        }

        val submitButton = Button(this).apply {
            text = "SUBMIT"
            setBackgroundColor(resources.getColor(android.R.color.darker_gray))
            setTextColor(resources.getColor(android.R.color.white))
            setPadding(20, 20, 20, 20)
            setOnClickListener {
                Toast.makeText(this@MainActivity, "Form Submitted!", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonParams.setMargins(0, 20, 0, 20)

        linearLayout.addView(submitButton, buttonParams)
    }

    private fun createTextField(parent: LinearLayout, label: String, hint: String?) {

        val textView = TextView(this).apply {
            text = label
            textSize = 14f
        }

        val editText = EditText(this).apply {
            this.hint = hint
            background = getDrawable(android.R.drawable.edit_text)
            setPadding(10, 10, 10, 10)
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 10, 0, 20)

        parent.addView(textView)
        parent.addView(editText, params)
    }

    private fun createNumberField(parent: LinearLayout, label: String, hint: String?) {

        val textView = TextView(this).apply {
            text = label
            textSize = 14f
        }

        val editText = EditText(this).apply {
            this.hint = hint
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 10, 0, 20)

        parent.addView(textView)
        parent.addView(editText, params)
    }

    private fun createDropdownField(parent: LinearLayout, label: String, options: List<String>) {

        val textView = TextView(this).apply {
            text = label
            textSize = 14f
        }

        val spinner = Spinner(this).apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                options
            )
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 10, 0, 20)

        parent.addView(textView)
        parent.addView(spinner, params)
    }

    private fun createCheckboxField(parent: LinearLayout, label: String) {
        val checkBox = CheckBox(this).apply {
            text = label
        }

        parent.addView(checkBox)
    }

    // Data classes
    data class FormConfig(
        @SerializedName("fields") val fields: List<FormField>
    )

    data class FormField(
        @SerializedName("type") val type: String,
        @SerializedName("label") val label: String,
        @SerializedName("hint") val hint: String? = null,
        @SerializedName("options") val options: List<String>? = null
    )
}