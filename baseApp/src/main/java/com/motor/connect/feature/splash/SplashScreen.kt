package com.motor.connect.feature.splash

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.feature.area.R
import com.motor.connect.feature.data.MainActivity
import io.reactivex.annotations.NonNull
import java.util.*

class SplashScreen : AppCompatActivity() {

    private val multiPERMISSIONS = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_view)

        val handler = Handler()
        handler.postDelayed({
            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                permissionCheck()

            } else {
                // Pre-Marshmallow
                openMainScreen()
            }
        }, 1000)
    }

    //-************************ permission check **************************

    private fun permissionCheck() {
        val permissionsNeeded = ArrayList<String>()

        val permissionsList = ArrayList<String>()
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)

        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add(Manifest.permission.SEND_SMS)

        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add(Manifest.permission.READ_SMS)

        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
            permissionsNeeded.add(Manifest.permission.RECEIVE_SMS)


        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {
                // Need Rationale
                var message = "You need to grant access to " + permissionsNeeded[0]
                for (i in 1 until permissionsNeeded.size)
                    message = message + ", " + permissionsNeeded[i]
                showMessageOKCancel(message,
                        DialogInterface.OnClickListener { dialog, which ->
                            if (Build.VERSION.SDK_INT >= 23) {
                                // Marshmallow+
                                requestPermissions(permissionsList.toTypedArray(),
                                        multiPERMISSIONS)
                            }
                        })
                return
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toTypedArray(),
                        multiPERMISSIONS)
            }
            return
        } else {
            openMainScreen()
        }
    }

    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {

        val cond: Boolean?
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission)
            }
            cond = true
        } else {
            // Pre-Marshmallow
            cond = true
        }
        return cond
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@SplashScreen)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {

        //Checking the request code of our request
        if (requestCode == 23) {
            //If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Permission Needed To Run The App", Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == multiPERMISSIONS) {
            val perms = HashMap<String, Int>()
            // Initial
            perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.SEND_SMS] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.READ_SMS] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.RECEIVE_SMS] = PackageManager.PERMISSION_GRANTED

            // Fill with results
            for (i in permissions.indices)
                perms[permissions[i]] = grantResults[i]
            // Check for ACCESS_FINE_LOCATION
            if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.SEND_SMS] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.READ_SMS] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.RECEIVE_SMS] == PackageManager.PERMISSION_GRANTED) {

                // All Permissions Granted
                openMainScreen()

            } else {
                // Permission Denied
                Toast.makeText(this@SplashScreen, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show()

                val handler = Handler()
                handler.postDelayed({
                    //Do something after 100
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, multiPERMISSIONS)
                    finish()
                }, 3000)
            }
        }
    }

    private fun openMainScreen() {
        MainActivity.show(this@SplashScreen)
        this@SplashScreen.finish()
    }
}
