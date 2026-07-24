package com.servicehub.cliente

import android.app.Application
import com.servicehub.cliente.data.AppGraph

/** Application entry point that wires the in-app dependency graph. */
class ServiceHubClienteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.initialize(this)
    }
}
