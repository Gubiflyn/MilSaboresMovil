package com.example.proyectologin005d.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectologin005d.ui.cart.LineaCarrito
import androidx.compose.ui.graphics.Color


private val Brown = Color(0xFF8B4513)
private val Cream = Color(0xFFFFF5E1)
private val CardBg = Color(0xFFFFE6C7)
private val TextMain = Color(0xFF3B2A1A)
private val TextSub = Color(0xFF4B3621)

@Composable
fun CartScreen(
    vm: CartViewModel,
    onPaid: () -> Unit = {}
) {
    val items by vm.items.collectAsState()
    val totales by vm.totales.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío", color = TextMain)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { it ->
                    CartItemCard(
                        item = it,
                        onRemoveOne = { vm.remove(it.nombre) },
                        onAddOne = { vm.add(it.nombre, it.precio) }
                    )
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal:", color = TextSub)
                    Text("$${totales.subtotal}", color = TextMain)
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Descuento:", color = TextSub)
                    Text("- $${totales.descuento}", color = Brown, fontWeight = FontWeight.Bold)
                }
                Divider(Modifier.padding(vertical = 8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", color = TextMain, fontWeight = FontWeight.Bold)
                    Text("$${totales.total}", color = Brown, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                onPaid()
            },
            enabled = items.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Brown,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Text("Pagar", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CartItemCard(
    item: LineaCarrito,
    onRemoveOne: () -> Unit,
    onAddOne: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(item.nombre, color = TextMain, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text("x${item.cantidad}", color = TextSub)
            }
            Text("$${item.precio}", color = TextMain)
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onRemoveOne, contentPadding = PaddingValues(horizontal = 10.dp)) {
                Text("-")
            }
            Spacer(Modifier.width(6.dp))
            OutlinedButton(onClick = onAddOne, contentPadding = PaddingValues(horizontal = 10.dp)) {
                Text("+")
            }
        }
    }
}
