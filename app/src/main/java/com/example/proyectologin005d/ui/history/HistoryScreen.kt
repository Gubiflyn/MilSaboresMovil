package com.example.proyectologin005d.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectologin005d.ui.home.HomeBottomBar

@Composable
fun HistoryScreen(
    navController: NavController,
    vm: HistoryViewModel
) {
    val ui by vm.ui.collectAsState()
    val Brown = Color(0xFF8B4513)
    val Cream = Color(0xFFFFF5E1)
    val CardBg = Color(0xFFFFE6C7)
    val TextMain = Color(0xFF3B2A1A)

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            HomeBottomBar(
                current = "history",
                onHome = { navController.navigate("home") },
                onSearch = { navController.navigate("catalog") },
                onHistory = { /* ya estás en historial */ },
                onProfile = { navController.navigate("profile") },
                brown = Brown
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Mi historial de compras", style = MaterialTheme.typography.titleMedium, color = TextMain)
            Spacer(Modifier.height(8.dp))

            when {
                ui.userEmail == null -> {
                    Text("Sesión de invitado: no hay historial.", color = TextMain)
                }
                ui.loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                ui.orders.isEmpty() -> {
                    Text("Aún no tienes compras registradas.", color = TextMain)
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(ui.orders, key = { it.id }) { o ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CardBg),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { vm.selectOrder(o.id) }
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("Orden #${o.id}", color = TextMain)
                                    Spacer(Modifier.height(4.dp))
                                    Text("Fecha: ${formatFecha(o.fechaMillis)}", color = TextMain)
                                    Text("Ítems: ${o.itemsCount}  •  Total: $${o.total}", color = TextMain)
                                }
                            }
                        }

                        if (ui.selectedOrderId != null && ui.selectedItems.isNotEmpty()) {
                            item { Spacer(Modifier.height(8.dp)) }
                            item { Text("Detalle de la orden #${ui.selectedOrderId}", color = TextMain) }
                            items(ui.selectedItems, key = { it.id }) { itx ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(Modifier.padding(12.dp)) {
                                        Column(Modifier.weight(1f)) {
                                            Text(itx.nombre, color = TextMain)
                                            Text("x${itx.cantidad} • $${itx.precio}", color = TextMain)
                                        }
                                        Text("$${itx.subtotal}", color = TextMain)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatFecha(millis: Long): String {
    val cal = java.util.Calendar.getInstance().apply { timeInMillis = millis }
    val d = cal.get(java.util.Calendar.DAY_OF_MONTH)
    val m = cal.get(java.util.Calendar.MONTH) + 1
    val y = cal.get(java.util.Calendar.YEAR)
    val h = cal.get(java.util.Calendar.HOUR_OF_DAY)
    val mi = cal.get(java.util.Calendar.MINUTE)
    return "%02d-%02d-%04d %02d:%02d".format(d, m, y, h, mi)
}
