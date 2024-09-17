/*
 * Designed and developed by 2024 tecruz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.visitedcountries.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.visitedcountries.designsystem.theme.VisitedCountriesTheme

@Composable
fun VisitedCountriesAutoCompleteTextField(
  title: String,
  hintText: String,
  entries: List<String>,
  onEntrySelected: (String) -> Unit,
) {
  var itemElement by remember { mutableStateOf("") }
  val heightTextFields by remember { mutableStateOf(55.dp) }
  var textFieldSize by remember { mutableStateOf(Size.Zero) }
  var expanded by remember { mutableStateOf(false) }
  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier = Modifier
      .padding(12.dp)
      .fillMaxWidth()
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          expanded = false
        },
      ),
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleSmall,
      color = Color.Black,
      modifier = Modifier.padding(bottom = 5.dp),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .height(heightTextFields)
          .border(
            width = 1.5.dp,
            color = Color.Black,
            shape = RoundedCornerShape(8.dp),
          )
          .onGloballyPositioned { coordinates ->
            textFieldSize = coordinates.size.toSize()
          },
        value = itemElement,
        onValueChange = {
          itemElement = it
          expanded = true
        },
        // Perform action when the TextField is clicked
        interactionSource = remember { MutableInteractionSource() }
          .also { interactionSource ->
            LaunchedEffect(interactionSource) {
              interactionSource.interactions.collect { interaction ->
                if (interaction is PressInteraction.Release) {
                  expanded = !expanded
                }
              }
            }
          },
        placeholder = { Text(hintText) },
        colors = TextFieldDefaults.colors(
          focusedContainerColor = Color.Transparent,
          unfocusedContainerColor = Color.Transparent,
          disabledContainerColor = Color.Transparent,
          cursorColor = Color.Black,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(
          color = Color.Black,
          fontSize = 16.sp,
        ),
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Done,
        ),
        singleLine = true,
        trailingIcon = {
          Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Rounded.KeyboardArrowDown,
            contentDescription = "arrow",
            tint = Color.Black,
          )
        },
      )

      AnimatedVisibility(visible = expanded) {
        Card(
          modifier = Modifier
            .width(textFieldSize.width.dp),
          shape = RoundedCornerShape(8.dp),
        ) {
          LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp),
          ) {
            if (itemElement.isNotEmpty()) {
              items(
                entries.filter {
                  it.lowercase().contains(itemElement.lowercase())
                }.sorted(),
              ) {
                ItemElement(title = it) { title ->
                  itemElement = title
                  expanded = false
                  onEntrySelected(itemElement)
                }
              }
            } else {
              items(
                entries.sorted(),
              ) {
                ItemElement(title = it) { title ->
                  itemElement = title
                  expanded = false
                  onEntrySelected(itemElement)
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ItemElement(title: String, onSelect: (String) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable {
        onSelect(title)
      }
      .padding(vertical = 12.dp, horizontal = 15.dp),
  ) {
    Text(text = title, fontSize = 16.sp)
  }
}

@Preview
@Composable
private fun VisitedCountriesAutoCompleteTextFieldPreview() {
  VisitedCountriesTheme {
    Box(modifier = Modifier.fillMaxSize()) {
      VisitedCountriesAutoCompleteTextField(
        title = "Countries",
        hintText = "Search Country",
        entries = listOf("Country A", "Country B", "Country C"),
        onEntrySelected = {},
      )
    }
  }
}
