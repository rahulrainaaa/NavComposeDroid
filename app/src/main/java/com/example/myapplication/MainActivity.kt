package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val counter: MutableIntState = mutableIntStateOf(0)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000)
                counter.intValue = counter.intValue + 1
            }
        }

    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val (counter, setCounter) = mainViewModel.counter
    val onFabClick = { setCounter(0) }

    Scaffold(
        topBar = { MyAppTopBar(titleText = "Timer $counter") },
        bottomBar = { MyAppBottomBar() },
        floatingActionButton = { MyAppFAB(onFabClick = onFabClick) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NavigationScreens()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppTopBar(titleText: String) {
    TopAppBar(colors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        Text(text = titleText)
    })
}

@Composable
fun MyAppBottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Bottom app bar",
        )
    }
}

@Composable
fun MyAppFAB(onFabClick: (() -> Unit)? = null) {
    onFabClick?.let {
        FloatingActionButton(onClick = { onFabClick() }) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset")
        }
    }
}

@Composable
fun NavigationScreens() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {

        Text(
            text = "Top text",
            modifier = Modifier
        )

        Text(
            text = "Bottom text",
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}
