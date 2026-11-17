package com.skuvahncaptain.module.home.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.skuvahncaptain.R
import com.skuvahncaptain.base.view.BaseActivity
import com.skuvahncaptain.databinding.ActivityRoutesBinding
import com.skuvahncaptain.module.home.adapters.RoutesAdapter
import com.skuvahncaptain.module.home.dialog.DeleteConfirmationDialog
import com.skuvahncaptain.module.home.listeners.*
import com.skuvahncaptain.module.home.models.RouteResponseModel
import com.skuvahncaptain.module.home.states.RouteState
import com.skuvahncaptain.module.home.viewmodels.RoutesViewModel
import com.skuvahncaptain.utilities.CustomToast
import com.skuvahncaptain.utilities.PreferenceConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutesActivity : BaseActivity<ActivityRoutesBinding>(),
    RouteClickListener,
    RouteDeleteClickListener {

    private val viewModel: RoutesViewModel by viewModels()
    private lateinit var adapter: RoutesAdapter

    override fun getLayoutResourceId() = R.layout.activity_routes

    override fun initView() {
        super.initView()

        window.statusBarColor = getColor(R.color.light_blue)

        setupRecycler()
        attachClicks()
        observe()

        viewModel.myRoutes(pref.token, pref.userId)
    }

    private fun setupRecycler() {
        adapter = RoutesAdapter(arrayListOf())

        adapter.routeClickListener = this
        adapter.deleteRouteClickListener = this

        binding.rvAddresses.layoutManager = LinearLayoutManager(this)
        binding.rvAddresses.adapter = adapter
    }

    private fun attachClicks() {

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.btnAddAddress.setOnClickListener {
            addressLauncher.launch(Intent(this, RoutesAddActivity::class.java))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.myRoutes(pref.token, pref.userId)
        }
    }

    private fun observe() {

        viewModel.state.observe(this) {

            when (it) {

                is RouteState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }

                is RouteState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false

                    if (it.list.isEmpty()) {
                        binding.tvNoAddress.visibility = View.VISIBLE
                        binding.rvAddresses.visibility = View.GONE
                    } else {
                        binding.tvNoAddress.visibility = View.GONE
                        binding.rvAddresses.visibility = View.VISIBLE
                        adapter.updateData(it.list)
                    }
                }

                is RouteState.DeleteSuccess -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    CustomToast.showMessage(this, it.message)

                    // 🔥 Refresh list after delete
                    viewModel.myRoutes(pref.token, pref.userId)
                }

                is RouteState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    CustomToast.showMessage(this, it.message)
                }

                else -> {}
            }
        }
    }

    override fun onRouteClick(address: RouteResponseModel.Route) {}

    override fun onDeleteRouteClick(address: RouteResponseModel.Route) {

        val dialog = DeleteConfirmationDialog().apply {
            arguments = Bundle().apply {
                putSerializable(PreferenceConstants.address, address)
            }

            listener = object : DeleteConfirmationDialog.OnDeleteConfirmListener {
                override fun onDeleteConfirmed(
                    address: RouteResponseModel.Route?,
                    deleteUserId: String?
                ) {
                    address?.id?.let {
                        viewModel.deleteRoute(pref.token, it, pref.userId)
                    }
                }
            }
        }

        dialog.show(supportFragmentManager, "DeleteDialog")
    }

    private val addressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                viewModel.myRoutes(pref.token, pref.userId)
            }
        }
}