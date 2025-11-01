package com.example.proyectologin005d.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    vm: CartViewModel = viewModel(),
    onPaid: () -> Unit = {}
) {
    val items by vm.items.collectAsState()
    val totales by vm.totales.collectAsState()

    fun clp(n: Int) = n.coerceAtLeast(0).toString()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito") }) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal:")
                    Text("$${clp(totales.subtotal)}")
                }

                if (totales.descuento > 0) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Descuento", color = MaterialTheme.colorScheme.primary)
                        Text("-$${clp(totales.descuento)}", color = MaterialTheme.colorScheme.primary)
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", fontWeight = FontWeight.Bold)
                    Text("$${clp(totales.total)}", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        vm.clear()
                        onPaid()
                    },
                    enabled = items.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text("Pagar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío")
                }
            } else {
                items.forEach { it ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• ${it.nombre}  x${it.cantidad}")
                        Text("@$${clp(it.precio)}")
                    }
                }
            }
        }
    }
}
