package com.tealium.example

import DynamicVar
import TransactionProperties
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var listRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listRecyclerView = findViewById(R.id.listRecyclerView)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = ListAdapter()

        supportActionBar?.title = "Tealium Contentsquare Demo"
    }

    override fun onResume() {
        super.onResume()
        TealiumHelper.trackView("screen_title", mapOf("screen" to "home"))
    }

    private inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val nameTextView = itemView.findViewById(R.id.nameTextView) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (adapterPosition) {
                0 -> startActivity(Intent(this@MainActivity, ScreenViewActivity::class.java))
                1 -> sendTransaction()
                2 -> sendDynamicVar()
                3 -> startActivity(Intent(this@MainActivity, MiscellaneousActivity::class.java))
            }
        }

        private fun sendTransaction() {
            TealiumHelper.trackEvent(
                TransactionProperties.TRANSACTION, mapOf(
                    "order_total" to 10.99,
                    "order_currency" to "usd",
                    "order_id" to 12345
                )
            )
        }

        private fun sendDynamicVar() {
            TealiumHelper.trackEvent(
                DynamicVar.DYNAMIC_VAR, mapOf(
                    DynamicVar.DYNAMIC_VAR to mapOf(
                        "key1" to "value1",
                        "key2" to 2
                    )
                )
            )
        }
    }

    private inner class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {
        val names = listOf(
            "Screen Views",
            "Transactions",
            "Dynamic Variables",
            "Miscellaneous"
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_view_holder, parent, false)
            return ListViewHolder(view)
        }

        override fun getItemCount(): Int = names.size

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            holder.nameTextView.text = names[position]
        }
    }
}
