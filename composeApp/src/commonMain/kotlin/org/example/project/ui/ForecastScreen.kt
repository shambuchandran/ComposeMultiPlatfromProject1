package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cmpproject1.composeapp.generated.resources.Res
import cmpproject1.composeapp.generated.resources.ic_cloud
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import org.example.project.data.models.forcastmodel.ForecastData
import org.jetbrains.compose.resources.painterResource

@Composable
fun ForecastScreen(navController: NavController) {
    val factory = rememberLocationTrackerFactory(LocationTrackerAccuracy.Best)
    val locationTracker = remember { factory.createLocationTracker() }
    BindLocationTrackerEffect(locationTracker)
    val viewModel = remember { ForecastViewModel(locationTracker) }

    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getForecast()
    }
    Column(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF7FD4FF),
                    Color(0xFF4A90E2)
                )
            )
        ).systemBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
        when (val forecastState = state.value) {
            is ForecastState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("Loading...", color = Color.White)
                }

            }

            is ForecastState.Data -> {
                val dailyData = forecastState.dailyData
                val weeklyData = forecastState.weeklyData
                ForecastScreenContent(dailyData, weeklyData)
            }

            is ForecastState.Error -> {
                Text("Error Occured")

            }
        }

    }

}

@Composable
fun ColumnScope.ForecastScreenContent(
    dailyData: List<ForecastData>,
    weeklyData: List<ForecastData>
) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "Daily forecast",
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterStart),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            dailyData.get(0).dt_txt.split(" ")[0],
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
    LazyRow {
        items(dailyData) {
            ForecastRowItem(it)
        }
    }
    Spacer(modifier = Modifier.size(16.dp))
    LazyColumn {
        items(weeklyData) {
            ForecastColumnItem(it)
        }

    }
    Spacer(modifier = Modifier.size(16.dp))
}

@Composable
fun ForecastRowItem(data: ForecastData) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp).height(155.dp)
    ) {
        Text(data.main.temp.toString(), color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))
        Image(painter = painterResource(Res.drawable.ic_cloud), contentDescription = null)
        Spacer(modifier = Modifier.size(8.dp))
        Text(data.dt_txt.split(" ")[1].removeSuffix(":00"), color = Color.White)

    }
}

@Composable
fun ForecastColumnItem(data: ForecastData) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().height(60.dp).padding(8.dp)
    ) {
        Text(data.dt_txt.split(" ")[0].drop(5), color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))
        Image(painter = painterResource(Res.drawable.ic_cloud), contentDescription = null)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "${data.main.temp?.toInt()}C", color = Color.White)





    }
}