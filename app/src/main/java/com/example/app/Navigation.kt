package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.app.roomDB.Pessoa
import com.example.app.roomDB.PessoaDataBase
import com.example.app.viewModel.PessoaViewModel
import com.example.app.viewModel.Repository

sealed class BottomNavScreens(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
) {
    data object Cadastro : BottomNavScreens(
        "cadastro",
        "Cadastro",
        Icons.Filled.Create,
        Icons.Outlined.Create,
        false,
        null
    )

    data object Editar : BottomNavScreens(
        "editar",
        "Editar",
        Icons.Filled.List,
        Icons.Outlined.List,
        false,
        null
    )
}

class Navigation : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PessoaDataBase::class.java,
            "pessoa.db"
        ).build()
    }

    private val viewModel by viewModels<PessoaViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PessoaViewModel(Repository(db)) as T
                }
            }
        }
    )


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                BottomNavScreens.Cadastro,
                BottomNavScreens.Editar,
            )
            var selectedItemIndex by rememberSaveable {
                mutableIntStateOf(0)
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary
            ) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        navController.navigate(item.route)
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            Modifier.padding(bottom = 10.dp)
                                        )
                                    },
                                    alwaysShowLabel = false,
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount != null) {
                                                    Badge {
                                                        Text(text = item.badgeCount.toString())
                                                    }
                                                } else if (item.hasNews) {
                                                    Badge()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    },
                    content = { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            NavHost(navController, startDestination = BottomNavScreens.Cadastro.route) {
                                composable(BottomNavScreens.Cadastro.route) {
                                    CadastroScreen(
                                        viewModel,
                                        this@Navigation
                                    )
                                }
                                composable(BottomNavScreens.Editar.route) { EditarScreen(this@Navigation) }
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun CadastroScreen(viewModel: PessoaViewModel, navigationActivity: Navigation) {
        var nome by remember {
            mutableStateOf("")
        }

        var telefone by remember {
            mutableStateOf("")
        }

        val pessoa = Pessoa(
            nome,
            telefone
        )


        Column(
            Modifier
                .background(Color.White)
        ) {
            Row(
                Modifier
                    .padding(20.dp)
            ) {

            }
            Row(
                Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                Text(
                    text = "App DataBase",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
            Row(
                Modifier
                    .padding(20.dp)
            ) {

            }
            Row(
                Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text(text = "Nome:") }
                )
            }
            Row(
                Modifier
                    .padding(20.dp)
            ) {

            }
            Row(
                Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                TextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text(text = "Telefone:") }
                )
            }
            Row(
                Modifier
                    .padding(20.dp)
            ) {

            }
            Row(
                Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                Button(
                    onClick = {
                        viewModel.upsertPessoa(pessoa)
                        nome = ""
                        telefone = ""
                    }
                ) {
                    Text(text = "Cadastrar")
                }
            }
        }
    }

    @Composable
    fun EditarScreen(navigationActivity: Navigation) {
        var pessoaList by remember {
            mutableStateOf(listOf<Pessoa>())
        }

        viewModel.getPessoa().observe(navigationActivity) {
            pessoaList = it
        }

        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                Arrangement.Center
            ) {
                Text(
                    text = "App DataBase",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                Arrangement.Center
            ) {
                Column(
                    Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "Nome",
                        fontWeight = FontWeight.Bold)
                }
                Column(
                    Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    Text(text = "Telefone",
                        fontWeight = FontWeight.Bold)
                }
            }
            Row (
                Modifier.padding(top = 16.dp)
            ){
                LazyColumn {
                    items(pessoaList) { pessoa ->
                        Row(
                            Modifier
                                .clickable {
                                    viewModel.deletePessoa(pessoa)
                                }
                                .fillMaxWidth()
                                .padding(bottom = 8.dp, top = 8.dp),
                            Arrangement.Center
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth(0.5f),
                                Arrangement.Center
                            ) {
                                Text(text = "${pessoa.nome}",
                                    fontSize = 16.sp)
                            }
                            Column(
                                Modifier
                                    .fillMaxWidth(0.5f),
                                Arrangement.Center
                            ) {
                                Text(
                                    text = "${pessoa.telefone}",
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Divider()
                    }
                }
            }

        }
    }

    @Composable
    fun TextStyleNav(text: String) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }

}