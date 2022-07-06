package com.randhir.tipapp

import android.app.assist.AssistContent
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.randhir.tipapp.components.InputField
import com.randhir.tipapp.ui.theme.TipAppTheme
import com.randhir.tipapp.util.calTotalPerPerson
import com.randhir.tipapp.util.calTotalTip
import com.randhir.tipapp.widgets.RoundButton



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
                
            MyApp {
                    MainContent()
            }


        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){

    TipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }

}


//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 133.0){
    
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(20.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
        , color = Color(0xFFE9D7F7)) {

        Column(modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                    val total = "%.2f".format(totalPerPerson)

                     Text(text = "Total Per Person",
                        style = MaterialTheme.typography.h4)
                     Text(text = "$$total", // "$$" - Append
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.ExtraBold)
        }
        
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){



        Column {

            //TopHeader()

            BillForm{billAmount ->
                Log.d("Amount", "MainContent: $billAmount")

            }
        }





}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier,
            onValChange: (String) -> Unit = {}){

    val totalBillState = remember {
        mutableStateOf("") }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    // Using experimental API
    val keyboardController = LocalSoftwareKeyboardController.current

    // Slider State
    val sliderState = remember { mutableStateOf(0f)}

    val tipP = (sliderState.value*100).toInt()

    val splitByState = remember { mutableStateOf(1)}

    val tipAmountState = remember { mutableStateOf(0.0)}

    val totalPerPersonState = remember { mutableStateOf(0.0)}

    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        , shape = RoundedCornerShape(corner = CornerSize(8.dp))
        , border = BorderStroke(width = 1.dp , color = Color.LightGray)) {

        Column(modifier = Modifier.padding(6.dp),
               verticalArrangement = Arrangement.Top,
               horizontalAlignment = Alignment.Start ) {

            InputField(valueState = totalBillState,
                labelId = "Enter Your Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if(!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                })

            // If you enter a valid value in text field it will show the content
             if(validState){
                
                Row(modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Start) {
                    
                    Text(text = "Split",
                         modifier = Modifier.align(alignment =
                                    Alignment.CenterVertically))

                    Spacer(modifier = Modifier.width(120.dp))
                    
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End) {

                        RoundButton(imageVector = Icons.Default.Remove,
                                    onClick = {
                                        splitByState.value =
                                            if(splitByState.value > 1) splitByState.value-1  else 1
                                        totalPerPersonState.value = calTotalPerPerson(
                                            totalBill = totalBillState.value.toDouble(), splitBy = splitByState.value , tipP = tipP)
                                    })

                        Text(text = "${splitByState.value}" ,
                             modifier = Modifier
                                 .padding(start = 9.dp, end = 9.dp)
                                 .align(alignment = Alignment.CenterVertically))

                        RoundButton(imageVector = Icons.Default.Add,
                            onClick = {
                                splitByState.value++
                                totalPerPersonState.value = calTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(), splitBy = splitByState.value , tipP = tipP)
                            })

                    }
                    
                }
            
            //Tip Row
            Row(modifier = Modifier.padding(horizontal = 5.dp
                        , vertical = 12.dp)) {
                
                Text(text = "Tip", modifier = Modifier
                    .align(alignment = Alignment.CenterVertically))
                
                Spacer(modifier = Modifier.width(200.dp))
                
                Text(text = "$ ${tipAmountState.value}", modifier = Modifier
                    .align(alignment = Alignment.CenterVertically))
                
            }

            Column(verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "$tipP %", modifier = Modifier.padding(top = 4.dp))

                Spacer(modifier = Modifier.height(12.dp))

                //Slider
                Slider(value = sliderState.value ,
                      onValueChange = {
                      newVal -> sliderState.value = newVal
                      tipAmountState.value = calTotalTip(totalBill = totalBillState.value.toDouble()
                          ,tipP = tipP)
                      totalPerPersonState.value = calTotalPerPerson(
                          totalBill = totalBillState.value.toDouble(), splitBy = splitByState.value , tipP = tipP)},
                      modifier = Modifier.padding(start = 14.dp , end = 14.dp),
                      steps = 5,
                      onValueChangeFinished = {})

                }
             // else it will be an empty box
            }else{
                Box() {}
            }

        }

    }

}



//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipAppTheme {
            MyApp {
                Text(text = "Hello")
            }
    }
}