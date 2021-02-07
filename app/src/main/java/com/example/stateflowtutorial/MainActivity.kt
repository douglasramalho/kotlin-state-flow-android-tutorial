package com.example.stateflowtutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.example.stateflowtutorial.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private var uiStateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGetSomething.setOnClickListener {
            viewModel.getSomething()
        }
    }

    override fun onStart() {
        super.onStart()

        uiStateJob = lifecycleScope.launchWhenResumed {
            viewModel.uiStateFlow.collect { uiState ->
                when (uiState) {
                    is MainViewModel.UiState.Success -> {
                        binding.flipperFlow.displayedChild = CHILD_VIEW_CONTENT
                        binding.progressFlow.isVisible = false
                        binding.textHelloFlow.text = getString(R.string.hello_flow)
                    }
                    is MainViewModel.UiState.Error -> {
                        binding.flipperFlow.displayedChild = CHILD_VIEW_CONTENT
                        binding.progressFlow.isVisible = false
                        binding.textHelloFlow.text = getString(R.string.error)
                    }
                    is MainViewModel.UiState.Loading -> {
                        binding.flipperFlow.displayedChild = CHILD_VIEW_LOADING
                        binding.progressFlow.isVisible = true
                    }
                    is MainViewModel.UiState.Initial -> binding.progressFlow.isVisible = false
                }
            }
        }
    }

    override fun onStop() {
        uiStateJob?.cancel()
        super.onStop()
    }

    companion object {
        private const val CHILD_VIEW_LOADING = 0
        private const val CHILD_VIEW_CONTENT = 1
    }
}