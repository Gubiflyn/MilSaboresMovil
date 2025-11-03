package com.example.proyectologin005d.ui.history

import android.app.Application
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderItem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    val ctx = LocalContext.current
    val vm: HistoryViewModel =
        viewModel(factory = HistoryViewModel.factory(ctx.applicationContext as Application))
    val state by vm.uiState.collectAsState()

    val brown = Color(0xFF8B4513)
    val cream = Color(0xFFFFF5E1)
    val sdf = remember { SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Historial") },
                actions = {
                    IconButton(onClick = { vm.clearAllForCurrentUser() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar todo")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = brown,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(cream)
        ) {
            Column(Modifier.fillMaxSize()) {

                val aggText = state.aggregates.entries.joinToString(prefix="{", postfix="}") {
                    "${it.key}:(items=${it.value.itemsCount},total=${it.value.total})"
                }
                Text(
                    text = "user=${state.userEmail ?: "-"} | ord=${state.orders.size} | sel=${state.selectedOrderId ?: "-"} | agg=$aggText",
                    color = Color(0x99000000),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )

                AnimatedVisibility(visible = state.loading, enter = fadeIn(), exit = fadeOut()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = brown)
                    }
                }

                AnimatedVisibility(
                    visible = !state.loading && state.orders.isEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Aún no tienes compras registradas.", style = MaterialTheme.typography.titleMedium)
                        Text("Cuando completes una compra, aparecerá aquí.", color = Color(0x99000000))
                    }
                }

                AnimatedVisibility(
                    visible = state.orders.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.orders) { order ->
                            OrderCard(
                                order = order,
                                items = if (state.selectedOrderId == order.id) state.selectedItems else emptyList(),
                                sdf = sdf,
                                onExpand = { vm.loadItemsFor(order.id) },
                                onShare = {
                                    shareOrder(
                                        ctx = ctx,
                                        order = order,
                                        items = if (state.selectedOrderId == order.id) state.selectedItems else emptyList(),
                                        sdf = sdf
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    items: List<OrderItem>,
    sdf: SimpleDateFormat,
    onExpand: () -> Unit,
    onShare: () -> Unit
) {
    val brown = Color(0xFF8B4513)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Compra #${order.id}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = brown
                )
            )
            Text(
                "Fecha: ${sdf.format(Date(order.fechaMillis))}",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ítems: ${order.itemsCount}")
                Text("Total: CLP ${order.total}", fontWeight = FontWeight.Bold, color = brown)
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onExpand,
                border = BorderStroke(1.dp, brown),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = brown)
            ) { Text("Ver detalle") }

            AnimatedVisibility(
                visible = items.isNotEmpty(),
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFDDDDDD))
                    items.forEach {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${it.nombre} x${it.cantidad}")
                            Text("CLP ${it.precio * it.cantidad}")
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = onShare,
                            border = BorderStroke(1.dp, brown),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = brown)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Compartir boleta")
                        }
                    }
                }
            }
        }
    }
}

private fun shareOrder(
    ctx: android.content.Context,
    order: Order,
    items: List<OrderItem>,
    sdf: SimpleDateFormat
) {
    val total = if (items.isNotEmpty()) items.sumOf { it.precio * it.cantidad } else order.total
    val body = buildString {
        appendLine("📄 Boleta MilSabores")
        appendLine("Fecha: ${sdf.format(Date(order.fechaMillis))}")
        appendLine("Total: CLP $total")
        if (items.isNotEmpty()) {
            appendLine("-------- Detalle --------")
            items.forEach { i ->
                appendLine("• ${i.nombre} x${i.cantidad} @ ${i.precio} = ${i.precio * i.cantidad}")
            }
            appendLine("-------------------------")
        }
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Boleta MilSabores #${order.id}")
        putExtra(Intent.EXTRA_TEXT, body)
    }
    ctx.startActivity(Intent.createChooser(intent, "Compartir boleta"))
}
