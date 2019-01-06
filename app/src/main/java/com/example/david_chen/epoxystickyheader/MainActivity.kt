package com.example.david_chen.epoxystickyheader

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyController
import com.example.david_chen.epoxystickyheader.databinding.ActivityMainBinding
import com.example.david_chen.epoxystickyheader.databinding.EpoxyLayoutItemHeaderBinding
import com.kodmap.library.kmrecyclerviewstickyheader.KmHeaderItemDecoration
import com.kodmap.library.kmrecyclerviewstickyheader.KmStickyListener

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = binding.rvList1
        val controller = object : EpoxyController() {
            override fun buildModels() {
                (1..6).forEach { header ->
                    itemHeader {
                        id("header_$header")
                        title("Header No.$header")
                    }

                    (1..10).forEach {content ->
                        itemContent {
                            id("content_${header}_$content")
                            content("From h=${header}, content $content")
                        }
                    }
                }
            }
        }

        val deco: RecyclerView.ItemDecoration = object : KmHeaderItemDecoration(object : KmStickyListener{
            override fun isHeader(position: Int?): Boolean {
                val model = controller.adapter.getModelAtPosition(position!!)
                return model is ItemHeaderBindingModel_
            }

            override fun getHeaderLayout(p0: Int?): Int {
                return R.layout.epoxy_layout_item_header
            }

            override fun getHeaderPositionForItem(position: Int?): Int {
                var counter = position!!

                while (!isHeader(counter)) {
                    counter--
                }

                return counter
            }

            override fun bindHeaderData(view: View?, position: Int?) {
                val view = view ?: return
                val position = position ?: return

                val model = controller.adapter.getModelAtPosition(position) as ItemHeaderBindingModel_
                val binding = EpoxyLayoutItemHeaderBinding.bind(view)
                binding.title = model.title()
                binding.executePendingBindings()
            }
        }){}
        recyclerView.addItemDecoration(deco)

        recyclerView.setController(controller)
        recyclerView.requestModelBuild()
    }
}
