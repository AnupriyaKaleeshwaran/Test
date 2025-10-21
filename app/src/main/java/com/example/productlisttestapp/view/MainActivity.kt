package com.example.productlisttestapp.view

import OrderViewModel
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productlisttestapp.R
import com.example.productlisttestapp.adapter.ItemAdapter
import com.example.productlisttestapp.adapter.PersonAdapter
import com.example.productlisttestapp.databinding.ActivityMainBinding
import com.example.productlisttestapp.model.ApiResponse
import com.example.productlisttestapp.model.OrderItem
import com.example.productlisttestapp.model.Person
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var items: List<OrderItem>
    private lateinit var persons: MutableList<Person>
    private lateinit var personAdapter: PersonAdapter
    private lateinit var viewModel: OrderViewModel
    var totalBill = 0.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        viewModel.orderResponse.observe(this) { apiResponse ->
            apiResponse?.let { setupUI(it) }
        }
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        viewModel.fetchOrder(86)


        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Evenly"))
        tabLayout.addTab(tabLayout.newTab().setText("Item"))
        tabLayout.addTab(tabLayout.newTab().setText("Shares"))
        tabLayout.addTab(tabLayout.newTab().setText("%"))
        tabLayout.addTab(tabLayout.newTab().setText("Manual"))
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.view?.setPadding(0, 0, 0, 0)
            val textView = tab?.view?.getChildAt(0) as? TextView
            textView?.textSize = 8f
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val totalBill = totalBill
                when (tab?.position) {
                    0 -> splitEvenly(totalBill)
                    1 -> splitByItem(totalBill)
                    2 -> splitByShares(totalBill)
                    3 -> splitByPercentage(totalBill)
                    4 -> manualAmount(totalBill)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupUI(apiResponse: ApiResponse) {
        val orderMessage = apiResponse.message.firstOrNull() ?: return
        items = orderMessage.item
        totalBill = orderMessage.order.total.toDouble()
        val seats = orderMessage.order.tableSeat

        binding.tvTotal.text = "£ ${String.format("%.2f", totalBill)}"

        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = ItemAdapter(items)

        persons = mutableListOf()

        for (i in 1..seats) {
            persons.add(Person("Person $i"))
        }

        personAdapter = PersonAdapter(persons)
        binding.rvPersons.layoutManager = LinearLayoutManager(this)
        binding.rvPersons.adapter = personAdapter

        splitEvenly(totalBill)
    }

    private fun splitEvenly(total: Double) {
        val share = total / persons.size
        persons.forEach { it.amount = share }
        personAdapter.notifyDataSetChanged()
    }

    private fun splitByItem(total: Double) {

        val itemNames = items.map { it.item_name }.toTypedArray()
        var currentPersonIndex = 0


        fun showSelectionDialog(person: Person) {
            val checkedItems = BooleanArray(items.size) { false }
            person.selectedItems.clear()
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Select items for ${person.name}")
            builder.setMultiChoiceItems(itemNames, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    person.selectedItems.add(items[which])
                } else {
                    person.selectedItems.remove(items[which])
                }
            }

            builder.setPositiveButton("OK") { _, _ ->
                var selectedTotal = 0.0
                person.selectedItems.forEach { item ->
                    selectedTotal += item.qty * item.price
                }
                person.amount = String.format("%.2f", selectedTotal).toDouble()
                personAdapter.notifyDataSetChanged()
                currentPersonIndex++
                if (currentPersonIndex < persons.size) {
                    showSelectionDialog(persons[currentPersonIndex])
                }
            }

            builder.setNegativeButton("Cancel", null)
            builder.show()
        }

        if (persons.isNotEmpty()) {
            showSelectionDialog(persons[currentPersonIndex])
        }


    }


    private fun splitByShares(total: Double) {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_manual_amount)
        dialog.setCancelable(true)
        val llContainer =
            dialog.findViewById<LinearLayout>(R.id.llManualContainer)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val tvError = dialog.findViewById<TextView>(R.id.tvError)

        val editTexts = mutableListOf<EditText>()
        val tvHeading = dialog.findViewById<TextView>(R.id.tvHeading)
        tvHeading.setText("Split by Manual")

        persons.forEach { person ->
            val et = EditText(this)
            et.hint = "${person.name}'s Share"
            et.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            et.setText(person.shares.toString())
            llContainer.addView(et)
            editTexts.add(et)
        }

        btnSave.setOnClickListener {
            var totalShares = 0.0
            val sharesList = mutableListOf<Double>()
            editTexts.forEach { et ->
                val share =
                    et.text.toString().toDoubleOrNull() ?: 0.0
                sharesList.add(share)
                totalShares += share
            }
            if (totalShares == 0.0) {
                tvError.text = "Please enter valid shares for each person"
                tvError.visibility = View.VISIBLE
            } else {
                persons.forEachIndexed { index, person ->
                    person.shares = sharesList[index]
                }
                persons.forEach { person ->
                    person.amount =
                        String.format("%.2f", total * (person.shares / totalShares)).toDouble()
                }
                personAdapter.notifyDataSetChanged()

                dialog.dismiss()
            }
        }

        dialog.show()
    }


    private fun splitByPercentage(total: Double) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_manual_amount)
        dialog.setCancelable(true)

        val llContainer = dialog.findViewById<LinearLayout>(R.id.llManualContainer)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val tvError = dialog.findViewById<TextView>(R.id.tvError)
        val editTexts = mutableListOf<EditText>()
        val tvHeading = dialog.findViewById<TextView>(R.id.tvHeading)
        tvHeading.setText("Split by Percentage")
        persons.forEach { person ->
            val et = EditText(this)
            et.hint = person.name
            et.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            et.setText(String.format("%.2f", person.percentage))
            llContainer.addView(et)
            editTexts.add(et)
        }

        btnSave.setOnClickListener {
            var sum = 0.0
            val percentages = mutableListOf<Double>()
            editTexts.forEach { et ->
                val value = et.text.toString().toDoubleOrNull() ?: 0.0
                percentages.add(value)
                sum += value
            }

            if (sum != 100.0) {
                tvError.text =
                    "Total percentage must be 100%. Currently: ${String.format("%.2f", sum)}%"
                tvError.visibility = View.VISIBLE
            } else {
                persons.forEachIndexed { index, person ->
                    person.percentage = percentages[index]
                    person.amount =
                        total * (percentages[index] / 100)
                }
                personAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialog.show()
    }


    private fun manualAmount(total: Double) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_manual_amount)
        dialog.setCancelable(true)

        val llContainer = dialog.findViewById<LinearLayout>(R.id.llManualContainer)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val tvError = dialog.findViewById<TextView>(R.id.tvError)

        val editTexts = mutableListOf<EditText>()
        val tvHeading = dialog.findViewById<TextView>(R.id.tvHeading)
        tvHeading.setText("Split by share")
        persons.forEach { person ->
            val et = EditText(this)
            et.hint = person.name
            et.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            et.setText(String.format("%.2f", person.amount))
            llContainer.addView(et)
            editTexts.add(et)
        }

        btnSave.setOnClickListener {
            var sum = 0.0
            val amounts = mutableListOf<Double>()

            editTexts.forEach { et ->
                val value = et.text.toString().toDoubleOrNull() ?: 0.0
                amounts.add(value)
                sum += value
            }

            if (sum != total) {
                tvError.text = "Total assigned (£${
                    String.format(
                        "%.2f",
                        sum
                    )
                }) does not match total bill (£${String.format("%.2f", total)})"
                tvError.visibility = View.VISIBLE
            } else {
                persons.forEachIndexed { index, person ->
                    person.amount = amounts[index]
                }
                personAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

}

