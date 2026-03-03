package com.example.c37b.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.c37b.model.Ride
import kotlinx.coroutines.launch

val MotoBlue = Color(0xFF1A4480)

@Composable
fun LoginScreen(navController: NavController, viewModel: RideViewModel) {
    var email by remember { mutableStateOf("admin@gmail.com") }
    var password by remember { mutableStateOf("password") }
    var phoneNumber by remember { mutableStateOf("") }
    var bikeNumber by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }
    
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.DirectionsBike,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MotoBlue
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "BikeRReg",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MotoBlue
            )
        }
        Spacer(Modifier.height(48.dp))
        Text(
            text = if (isSignUp) "Create your account." else "Welcome to BikeRReg,\nwith your account.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Username or Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        if (isSignUp) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholder = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = bikeNumber,
                onValueChange = { bikeNumber = it },
                placeholder = { Text("Bike Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
        if (error.isNotEmpty()) {
            Text(error, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                if (isSignUp) {
                    viewModel.signUp(email, password, phoneNumber, bikeNumber) { err ->
                        if (err == null) {
                            Toast.makeText(context, "Account Registered Successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate("list") { popUpTo("login") { inclusive = true } }
                        } else {
                            error = err
                        }
                    }
                } else {
                    viewModel.login(email, password) { err ->
                        if (err == null) {
                            navController.navigate("list") { popUpTo("login") { inclusive = true } }
                        } else {
                            error = err
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MotoBlue)
        ) {
            Text(if (isSignUp) "Sign Up" else "Log In", fontSize = 18.sp, color = Color.White)
        }
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (isSignUp) "Already have an account? " else "Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = if (isSignUp) "Log In" else "Sign Up",
                color = MotoBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { 
                    isSignUp = !isSignUp 
                    error = ""
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideListScreen(viewModel: RideViewModel, navController: NavController) {
    val rides = viewModel.rides
    val userRole by viewModel.userRole.collectAsState()
    val currentUserEmail by viewModel.currentUser.collectAsState()
    
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredRides = if (searchQuery.isEmpty()) rides else rides.filter {
        it.title.contains(searchQuery, ignoreCase = true) || 
        it.destination.contains(searchQuery, ignoreCase = true) ||
        it.bikeNumber.contains(searchQuery, ignoreCase = true)
    }

    val myRides = filteredRides.filter { it.joinedUsers.contains(currentUserEmail) }
    val availableRides = filteredRides.filter { !it.joinedUsers.contains(currentUserEmail) }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search rides, destinations or bike #...", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.White) },
                            trailingIcon = {
                                IconButton(onClick = { 
                                    searchQuery = ""
                                    isSearchActive = false 
                                }) { Icon(Icons.Default.Close, null, tint = Color.White) }
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MotoBlue)
                )
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DirectionsBike, null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("BikeRReg", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) { Icon(Icons.Default.Search, "Search", tint = Color.White) }
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(Icons.Default.AccountCircle, "Profile", tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MotoBlue)
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DirectionsBike, null) },
                    label = { Text("Rides") },
                    selected = true,
                    onClick = { }
                )
                if (userRole != "Organizer") {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, null) },
                        label = { Text("My Rides") },
                        selected = false,
                        onClick = { navController.navigate("my_rides") }
                    )
                }
            }
        },
        floatingActionButton = {
            if (userRole == "Organizer") {
                FloatingActionButton(onClick = { navController.navigate("add_ride") }, containerColor = MotoBlue, contentColor = Color.White) {
                    Icon(Icons.Default.Add, "Add Ride")
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF2F2F2))) {
            if (availableRides.isNotEmpty() || (userRole == "Organizer" && filteredRides.isNotEmpty())) {
                item {
                    Text(
                        "Available Rides",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                items(if (userRole == "Organizer") filteredRides else availableRides) { ride ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        RideItem(ride, userRole, isJoined = ride.joinedUsers.contains(currentUserEmail), onClick = {
                            navController.navigate("details/${ride.rideId}")
                        })
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            if (userRole != "Organizer" && myRides.isNotEmpty()) {
                item {
                    Text(
                        "Upcoming Rides",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(myRides) { ride ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        RideItem(ride, userRole, isJoined = true, onClick = {
                            navController.navigate("details/${ride.rideId}")
                        })
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
            
            if (filteredRides.isEmpty() && searchQuery.isNotEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No rides found matching \"$searchQuery\"", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideItem(ride: Ride, userRole: String?, isJoined: Boolean = false, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = if (ride.imageUrl.isNotEmpty()) ride.imageUrl else "https://via.placeholder.com/150",
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(ride.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(ride.date, color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                if (isJoined && userRole != "Organizer") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Registered", color = Color(0xFF4CAF50), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                } else {
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MotoBlue),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(if (userRole == "Organizer") "Edit Ride" else "Register", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRidesOnlyScreen(viewModel: RideViewModel, navController: NavController) {
    val currentUserEmail by viewModel.currentUser.collectAsState()
    val myRides = viewModel.rides.filter { it.joinedUsers.contains(currentUserEmail) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Registered Rides") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (myRides.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("You haven't registered for any rides yet.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF2F2F2)).padding(16.dp)) {
                items(myRides) { ride ->
                    RideItem(ride, "User", isJoined = true, onClick = {
                        navController.navigate("details/${ride.rideId}")
                    })
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: RideViewModel, navController: NavController) {
    val userDetails by viewModel.userDetails.collectAsState()
    
    var isEditing by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var editedPhone by remember { mutableStateOf("") }
    
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (userDetails != null && !isEditing) {
                        IconButton(onClick = { 
                            firstName = userDetails?.firstName ?: ""
                            lastName = userDetails?.lastName ?: ""
                            editedPhone = userDetails?.phoneNumber ?: ""
                            isEditing = true 
                        }) {
                            Icon(Icons.Default.Edit, "Edit Profile")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(100.dp).clip(CircleShape).background(MotoBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(userDetails?.email?.take(1)?.uppercase() ?: "U", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(24.dp))
            
            Text(text = "Email", color = Color.Gray, fontSize = 14.sp)
            Text(text = userDetails?.email ?: "Guest", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            
            Spacer(Modifier.height(16.dp))
            
            if (isEditing) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedPhone,
                    onValueChange = { editedPhone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Button(onClick = {
                        viewModel.updateProfile(firstName, lastName, editedPhone) { err ->
                            if (err == null) {
                                isEditing = false
                                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }, modifier = Modifier.weight(1f)) { Text("Save") }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = { isEditing = false }, modifier = Modifier.weight(1f)) { Text("Cancel") }
                }
            } else {
                Text(text = "Name", color = Color.Gray, fontSize = 14.sp)
                Text(text = "${userDetails?.firstName ?: ""} ${userDetails?.lastName ?: ""}".ifBlank { "N/A" }, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                
                Spacer(Modifier.height(16.dp))
                
                Text(text = "Phone Number", color = Color.Gray, fontSize = 14.sp)
                Text(text = userDetails?.phoneNumber ?: "N/A", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                
                Spacer(Modifier.height(16.dp))
                
                Text(text = "Bike Number", color = Color.Gray, fontSize = 14.sp)
                Text(text = userDetails?.bikeNumber ?: "N/A", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login") { popUpTo(0) }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Log Out", fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideDetailsScreen(rideId: String, viewModel: RideViewModel, navController: NavController) {
    val ride = viewModel.rides.find { it.rideId == rideId } ?: return
    val currentUserEmail by viewModel.currentUser.collectAsState()
    val isJoined = ride.joinedUsers.contains(currentUserEmail)
    val userRole by viewModel.userRole.collectAsState()
    
    var showJoinDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userNote by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Ride Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (userRole == "Organizer") {
                        IconButton(onClick = { showDeleteDialog = true }) { 
                            Icon(Icons.Default.Delete, "Delete", tint = Color.Red) 
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            AsyncImage(
                model = if (ride.imageUrl.isNotEmpty()) ride.imageUrl else "https://via.placeholder.com/400x200",
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            LazyColumn(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                item {
                    Text(ride.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    DifficultyBadge(ride.difficulty)
                    Spacer(Modifier.height(16.dp))
                    DetailRow(Icons.Default.Place, "Destination", ride.destination)
                    DetailRow(Icons.Default.DateRange, "Date", ride.date)
                    DetailRow(Icons.Default.LocationOn, "Meetup", ride.meetupLocation)
                    DetailRow(Icons.Default.Info, "Bike Type", ride.bikeType)
                    DetailRow(Icons.Default.Pin, "Bike #", ride.bikeNumber)
                    DetailRow(Icons.Default.Person, "Riders", "${ride.joinedUsers.size} / ${ride.maxRiders}")
                    
                    Spacer(Modifier.height(24.dp))
                }

                if (userRole == "Organizer") {
                    item {
                        Button(
                            onClick = { 
                                navController.navigate("edit_ride/${ride.rideId}")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MotoBlue)
                        ) { Text("Edit Ride Details") }
                        
                        Spacer(Modifier.height(24.dp))
                        Text("Registered Rider Notes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(8.dp))
                    }
                    
                    if (ride.userNotes.isEmpty()) {
                        item { Text("No notes from riders yet.", color = Color.Gray, fontSize = 14.sp) }
                    } else {
                        items(ride.userNotes.toList()) { (email, note) ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(email.replace(",", "."), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MotoBlue)
                                    Text(note, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                } else {
                    item {
                        if (ride.status != "Completed") {
                            if (isJoined) {
                                val myNote = ride.userNotes[currentUserEmail?.replace(".", ",")]
                                if (myNote != null) {
                                    Text("Your Note:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text(myNote, fontSize = 14.sp, modifier = Modifier.padding(bottom = 16.dp))
                                }
                                Button(
                                    onClick = { viewModel.leaveRide(rideId, currentUserEmail!!) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) { Text("Cancel Ride") }
                            } else if (ride.status == "Open") {
                                OutlinedTextField(
                                    value = userNote,
                                    onValueChange = { userNote = it },
                                    label = { Text("Add a note (optional)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("e.g. Bringing a helmet") }
                                )
                                Spacer(Modifier.height(16.dp))
                                Button(
                                    onClick = { showJoinDialog = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MotoBlue)
                                ) { Text("Register for Ride") }
                            } else {
                                Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                                    Text("Ride is Full")
                                }
                            }
                        } else {
                            Text("This ride has already been completed.", modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.Gray)
                        }
                    }
                }
            }
        }

        if (showJoinDialog) {
            AlertDialog(
                onDismissRequest = { showJoinDialog = false },
                title = { Text("Confirm Registration") },
                text = { Text("Do you want to join this ride to ${ride.destination}?") },
                confirmButton = {
                    TextButton(onClick = {
                        val result = viewModel.joinRide(rideId, currentUserEmail!!, userNote)
                        if (result != "Success") {
                            scope.launch { snackbarHostState.showSnackbar(result) }
                        } else {
                            Toast.makeText(context, "Registered for Ride Successfully", Toast.LENGTH_SHORT).show()
                        }
                        showJoinDialog = false
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = { showJoinDialog = false }) { Text("Cancel") }
                }
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Ride") },
                text = { Text("Are you sure you want to permanently delete this ride? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteRide(rideId)
                            showDeleteDialog = false
                            Toast.makeText(context, "Ride Deleted Successfully", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) { Text("Delete") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: String) {
    val color = when (difficulty) {
        "Easy" -> Color(0xFF4CAF50)
        "Moderate" -> Color(0xFFFF9800)
        "Hard" -> Color(0xFFF44336)
        else -> Color.Gray
    }
    Surface(color = color, shape = RoundedCornerShape(16.dp)) {
        Text(difficulty, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 12.sp)
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text("$label: ", fontWeight = FontWeight.SemiBold)
        Text(value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRideScreen(viewModel: RideViewModel, rideId: String? = null, onBack: () -> Unit) {
    val rideToEdit = remember(rideId) { viewModel.rides.find { it.rideId == rideId } }

    var title by remember { mutableStateOf(rideToEdit?.title ?: "") }
    var destination by remember { mutableStateOf(rideToEdit?.destination ?: "") }
    var date by remember { mutableStateOf(rideToEdit?.date ?: "") }
    var meetup by remember { mutableStateOf(rideToEdit?.meetupLocation ?: "") }
    var bikeType by remember { mutableStateOf(rideToEdit?.bikeType ?: "") }
    var bikeNumber by remember { mutableStateOf(rideToEdit?.bikeNumber ?: "") }
    var maxRiders by remember { mutableStateOf(rideToEdit?.maxRiders?.toString() ?: "10") }
    var difficulty by remember { mutableStateOf(rideToEdit?.difficulty ?: "Easy") }
    var imageUrl by remember { mutableStateOf(rideToEdit?.imageUrl ?: "") }
    
    var expanded by remember { mutableStateOf(false) }
    val difficulties = listOf("Easy", "Moderate", "Hard")
    
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            if (uri != null) imageUrl = uri.toString()
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (rideId == null) "Add New Ride" else "Edit Ride") }, navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
            })
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp).background(MaterialTheme.colorScheme.background)) {
            item {
                Text("Ride Thumbnail", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                
                Box(
                    modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .clickable { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUrl.isNotEmpty()) {
                        Box {
                            AsyncImage(model = imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            IconButton(
                                onClick = { imageUrl = "" },
                                modifier = Modifier.align(Alignment.TopEnd).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                            }
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                            Text("Click to pick from gallery", color = Color.Gray)
                        }
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Or Paste Image URL here") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (imageUrl.isNotEmpty()) {
                            IconButton(onClick = { imageUrl = "" }) { Icon(Icons.Default.Clear, null) }
                        }
                    },
                    placeholder = { Text("https://example.com/bike.jpg") }
                )
                
                Spacer(Modifier.height(24.dp))
                
                OutlinedTextField(title, { title = it }, label = { Text("Ride Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(destination, { destination = it }, label = { Text("Destination") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(date, { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(meetup, { meetup = it }, label = { Text("Meetup Location") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(bikeType, { bikeType = it }, label = { Text("Bike Type") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(bikeNumber, { bikeNumber = it }, label = { Text("Bike Number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(maxRiders, { maxRiders = it }, label = { Text("Max Riders") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(Modifier.height(16.dp))
                
                Text("Difficulty", fontWeight = FontWeight.SemiBold)
                Box {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(difficulty) }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        difficulties.forEach { diff ->
                            DropdownMenuItem(text = { Text(diff) }, onClick = { difficulty = diff; expanded = false })
                        }
                    }
                }
                
                Spacer(Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        if (title.isNotEmpty() && destination.isNotEmpty() && date.isNotEmpty()) {
                            val ride = Ride(
                                rideId = rideId ?: "",
                                title = title,
                                destination = destination,
                                date = date,
                                meetupLocation = meetup,
                                bikeType = bikeType,
                                bikeNumber = bikeNumber,
                                maxRiders = maxRiders.toIntOrNull() ?: 10,
                                difficulty = difficulty,
                                imageUrl = imageUrl
                            )
                            if (rideId == null) {
                                viewModel.addRide(ride)
                                Toast.makeText(context, "Bike Registered Successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.updateRide(ride)
                                Toast.makeText(context, "Ride Updated Successfully", Toast.LENGTH_SHORT).show()
                            }
                            onBack()
                        }
                    }, 
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MotoBlue)
                ) { Text(if (rideId == null) "Create Ride" else "Update Ride") }
                
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}
