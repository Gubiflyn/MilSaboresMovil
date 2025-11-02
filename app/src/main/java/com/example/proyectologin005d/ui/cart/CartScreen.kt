package com.example.proyectologin005d.ui.cart

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.proyectologin005d.data.database.PastelDatabase
import com.example.proyectologin005d.data.repository.OrderRepository
import com.example.proyectologin005d.session.SessionManager

@Composable
fun CartScreen(
    vm: CartViewModel = viewModel(),
    onPaid: () -> Unit = {} // ← callback para navegar
) {
    val ctx = LocalContext.current
    // Repo único para esta pantalla (misma DB)
    val repo = remember {
        val app = ctx.applicationContext as Application
        val db = PastelDatabase.getInstance(app)
        OrderRepository(db.orderDao())
    }
    // Inyecta el repo en el VM una sola vez
    LaunchedEffect(Unit) { vm.setOrderRepository(repo) }

    val items by vm.items.collectAsState()
    val totales by vm.totales.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // DEBUG de usuario/beneficios
        Text(
            text = "user=${vm.debugUserName()}   50%=${vm.debugTiene50()}   10%=${vm.debugTiene10()}",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF555555)
        )
        Spacer(Modifier.height(8.dp))

        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { line ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${line.nombre}  x${line.cantidad}")
                        Text("$${line.precio}")
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Subtotal: $${totales.subtotal}")
        Text("Descuento: -$${totales.descuento}")
        Text("Total: $${totales.total}", fontWeight = FontWeight.Bold, color = Color(0xFF8B4513))
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val email = SessionManager.currentUser?.email
                // guarda y cuando termine, navega
                vm.placeOrder(email) { onPaid() }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pagar")
        }
    }
}
