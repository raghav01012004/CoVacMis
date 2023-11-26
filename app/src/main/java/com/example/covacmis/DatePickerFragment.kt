package com.example.covacmis
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: View? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = inflater.inflate(R.layout.dialog_date_picker, container, false)
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDateEditText: EditText = binding.findViewById(R.id.selectedDateEditText)
        val datePickerButton: FloatingActionButton = binding.findViewById(R.id.datePickerButton)
        val orderButton: Button = binding.findViewById(R.id.orderButton)

        datePickerButton.setOnClickListener {
            showDatePicker()
        }

        orderButton.setOnClickListener {
            val selectedDate = selectedDateEditText.text.toString()

            if (selectedDate.isNotEmpty()) {
                val brandName = arguments?.getString("brandName") ?: ""
                val vaccine = arguments?.getString("vaccine") ?: ""
                val username = arguments?.getString("username") ?: ""
                val companyName = arguments?.getString("companyName") ?: ""
                val latitude = arguments?.getString("latitude")?:""
                val longitude = arguments?.getString("longitude")?:""
                val address = arguments?.getString("address")?:""

                val intent = Intent(requireContext(), HospitalList::class.java)
                intent.putExtra("brandName", brandName)
                intent.putExtra("vaccine", vaccine)
                intent.putExtra("username", username)
                intent.putExtra("companyName", companyName)
                intent.putExtra("selectedDate", selectedDate)
                intent.putExtra("latitude",latitude)
                intent.putExtra("longitude",longitude)
                intent.putExtra("address",address)
                startActivity(intent)

                dismiss()
            } else {
                // Handle case when no date is selected
            }
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        val selectedDateEditText: EditText = binding.findViewById(R.id.selectedDateEditText)
        selectedDateEditText.setText(formattedDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
