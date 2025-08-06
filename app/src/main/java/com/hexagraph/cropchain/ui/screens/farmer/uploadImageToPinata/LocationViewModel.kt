package com.hexagraph.cropchain.ui.screens.farmer.uploadImageToPinata

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

enum class LocationPermissionState {
    NOT_REQUESTED,
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED
}

@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {

    private val _permissionState = mutableStateOf(LocationPermissionState.NOT_REQUESTED)
    val permissionState: State<LocationPermissionState> = _permissionState

    private val _currentLocation = mutableStateOf<LocationData?>(null)
    val currentLocation: State<LocationData?> = _currentLocation

    private val _isLoadingLocation = mutableStateOf(false)
    val isLoadingLocation: State<Boolean> = _isLoadingLocation

    private val _locationError = mutableStateOf("")
    val locationError: State<String> = _locationError

    private val _customAddress = mutableStateOf("")
    val customAddress: State<String> = _customAddress

    private val _isEditingAddress = mutableStateOf(false)
    val isEditingAddress: State<Boolean> = _isEditingAddress

    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun initializeLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        checkLocationPermission(context)
    }

    fun checkLocationPermission(context: Context) {
        _permissionState.value = when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> LocationPermissionState.GRANTED

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> LocationPermissionState.GRANTED

            else -> LocationPermissionState.DENIED
        }
    }

    fun requestLocationPermission(
        context: Context,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        when (_permissionState.value) {
            LocationPermissionState.NOT_REQUESTED, LocationPermissionState.DENIED -> {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            LocationPermissionState.PERMANENTLY_DENIED -> {
                // Handle permanently denied case - show dialog to go to settings
                _locationError.value = "Location permission permanently denied. Please enable it in settings."
            }
            LocationPermissionState.GRANTED -> {
                getCurrentLocation(context)
            }
        }
    }

    fun handlePermissionResult(
        context: Context,
        permissions: Map<String, Boolean>
    ) {
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        when {
            fineLocationGranted || coarseLocationGranted -> {
                _permissionState.value = LocationPermissionState.GRANTED
                getCurrentLocation(context)
            }
            else -> {
                _permissionState.value = LocationPermissionState.PERMANENTLY_DENIED
                _locationError.value = "Location permission is required to fetch your current location"
            }
        }
    }

    fun getCurrentLocation(context: Context) {
        if (_permissionState.value != LocationPermissionState.GRANTED) {
            _locationError.value = "Location permission not granted"
            return
        }

        _isLoadingLocation.value = true
        _locationError.value = ""

        viewModelScope.launch {
            try {
                val cancellationTokenSource = CancellationTokenSource()

                val location = fusedLocationClient?.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                )?.await()

                if (location != null) {
                    val address = getAddressFromLocation(context, location)
                    _currentLocation.value = LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        address = address
                    )
                    _customAddress.value = address
                } else {
                    _locationError.value = "Unable to get current location"
                }
            } catch (e: Exception) {
                _locationError.value = "Error getting location: ${e.message}"
            } finally {
                _isLoadingLocation.value = false
            }
        }
    }

    private suspend fun getAddressFromLocation(context: Context, location: Location): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                buildString {
                    if (address.getAddressLine(0) != null) {
                        append(address.getAddressLine(0))
                    } else {
                        if (address.locality != null) append("${address.locality}, ")
                        if (address.adminArea != null) append("${address.adminArea}, ")
                        if (address.countryName != null) append(address.countryName)
                    }
                }
            } else {
                "Lat: ${location.latitude}, Lng: ${location.longitude}"
            }
        } catch (e: Exception) {
            "Lat: ${location.latitude}, Lng: ${location.longitude}"
        }
    }

    fun updateCustomAddress(address: String) {
        _customAddress.value = address

    }

    fun startEditingAddress() {
        _isEditingAddress.value = true
    }

    fun stopEditingAddress() {
        _isEditingAddress.value = false
        // Update the current location with custom address
        _currentLocation.value?.let { current ->
            _currentLocation.value = current.copy(address = _customAddress.value)
        }
    }

    fun clearLocationError() {
        _locationError.value = ""
    }

    fun getLocationString(): String {
        return _currentLocation.value?.address ?: _customAddress.value.ifEmpty { "No location selected" }
    }
}
