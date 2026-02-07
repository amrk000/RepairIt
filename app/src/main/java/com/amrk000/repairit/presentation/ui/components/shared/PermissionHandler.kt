package com.amrk000.repairit.presentation.ui.components.shared

import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

@Composable
fun PermissionHandler(
    manifestPermission: String,
    onAllowed : () -> Unit = {},
    onDenied : () -> Unit = {}
): (onAllowedInvoker: () -> Unit) -> Unit {
    val TAG = "PermissionHandler"

    val context = LocalContext.current
    var pendingInvoker by remember { mutableStateOf<(() -> Unit)?>(null) }
    val permissionName = manifestPermission.split(".")
        .last()
        .lowercase()
        .replace("_"," ")

    val permissionRequest = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { result: Boolean ->
        if (result) {
            Log.i(TAG, "$permissionName permission allowed !")
            onAllowed()
            pendingInvoker?.invoke()
        } else {
            Toast.makeText(context, "$permissionName permission denied !", Toast.LENGTH_SHORT).show()
            Log.w(TAG, "$permissionName permission denied !")
            onDenied()
        }

    }

    return { onAllowedInvoker: () -> Unit ->
        try {


            if (ActivityCompat.checkSelfPermission(
                    context,
                    manifestPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                pendingInvoker = onAllowedInvoker
                permissionRequest.launch(manifestPermission)
            } else {
                onAllowed()
                onAllowedInvoker()
            }
        } catch (e : Exception){
            e.printStackTrace()
        }
    }
}