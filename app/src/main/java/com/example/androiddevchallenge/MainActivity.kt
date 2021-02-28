/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val vm: DogViewModel = viewModel()
                MyApp(vm)
            }
        }
    }
}


// Start building your app here!
val sampleData = listOf(
    Dog("Sirius", "Mutt", 2),
    Dog("Barry", "Mutt", 2),
    Dog("Sasha", "Jack Russel", 10),
    Dog("Lexy", "Min Pin", 15),
)

class DogViewModel : ViewModel() {
    private val _state = MutableStateFlow(AppState(sampleData, null))
    val state: StateFlow<AppState>
        get() = _state

    fun dogSelected(dog: Dog?) {
        _state.value = state.value.copy(selected = dog)
    }

    data class AppState(val dogs: List<Dog>, val selected: Dog?)
}

@Composable
fun MyApp(viewModel: DogViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        val appState: DogViewModel.AppState by viewModel.state.collectAsState()
        val selected = appState.selected
        if (selected != null) {
            DogDetail(selected)
            BackHandler(onBack = { viewModel.dogSelected(null) })
        } else {
            DogList(appState.dogs, viewModel::dogSelected)
        }
    }
}

@Composable
fun DogList(dogs: List<Dog>, onItemClick: (Dog) -> Unit) {
    Column {
        for (dog in dogs) {
            DogItem(dog, onItemClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DogItem(dog: Dog, onClick: (Dog) -> Unit) {
    ListItem(
        trailing = { Text(dog.age.toString()) },
        text = { Text(dog.name) },
        icon = {},
        modifier = Modifier.clickable { onClick(dog) })
}

@Composable
fun DogDetail(dog: Dog) {
    Column(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.default_dog),
            contentDescription = null,
            modifier = Modifier.height(100.dp).fillMaxWidth().clip(shape = CircleShape)
        )
        Text(dog.name)
        Text(dog.breed)
        Text("Age: ${dog.age}")
    }
}

// Previews
@Preview("Dog Detail", widthDp = 360, heightDp = 640)
@Composable
fun DogDetailPreview() {
    MyTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            DogDetail(sampleData.first())
        }
    }
}

@Preview("Dog item")
@Composable
fun DogItemPreview() {
    Surface(color = MaterialTheme.colors.background) {
        DogItem(sampleData.first(), {})
    }
}
