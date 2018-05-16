package com.fifthgen.labelprinter.service

import android.app.IntentService
import android.content.Intent
import kotlin.reflect.KClass

class FetchDataService : IntentService(KClass::class.qualifiedName) {

    override fun onHandleIntent(intent: Intent?) {
        
    }
}