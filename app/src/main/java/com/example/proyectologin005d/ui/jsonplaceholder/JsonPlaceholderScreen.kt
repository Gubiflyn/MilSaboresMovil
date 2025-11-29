package com.example.proyectologin005d.ui.jsonplaceholder

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JsonPlaceholderScreen(
    viewModel: JsonPlaceholderViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ejemplo API externa") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // Zona de botones de prueba (GET / POST / PUT / DELETE)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Acciones de ejemplo con JSONPlaceholder:",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.loadPosts() },
                        enabled = !state.isLoading && !state.isActionRunning,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("GET (recargar)")
                    }
                    Button(
                        onClick = { viewModel.createDemoPost() },
                        enabled = !state.isLoading && !state.isActionRunning,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("POST demo")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.updateDemoPost(1) },
                        enabled = !state.isLoading && !state.isActionRunning,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("PUT demo (id 1)")
                    }
                    Button(
                        onClick = { viewModel.deleteDemoPost(1) },
                        enabled = !state.isLoading && !state.isActionRunning,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("DELETE demo (id 1)")
                    }
                }

                if (state.isActionRunning) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }

                state.actionMessage?.let { msg ->
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Nota: JSONPlaceholder es una API de pruebas. " +
                            "Los POST/PUT/DELETE funcionan pero NO guardan cambios reales, " +
                            "solo demuestran la comunicación.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Contenido principal: lista de posts
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    state.errorMessage != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error al cargar posts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = state.errorMessage ?: "")
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(state.posts) { post ->
                                JsonPostItem(post = post)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JsonPostItem(
    post: com.example.proyectologin005d.data.source.remote.JsonPlaceholderPostDto
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "#${post.id} · user ${post.userId}",
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
