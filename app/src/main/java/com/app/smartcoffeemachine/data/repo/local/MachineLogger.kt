package com.app.smartcoffeemachine.data.repo.local

import com.app.smartcoffeemachine.domain.model.StateTransitionLog
import com.app.smartcoffeemachine.domain.repo.local.IMachineLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MachineLogger(
    private val logFile: File,
) : IMachineLogger {
    override suspend fun logTransition(log: StateTransitionLog) {
        withContext(Dispatchers.IO) {
            val formattedDate = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS",
                Locale.getDefault()
            ).format(Date(log.timestamp))

            val logEntry = buildString {

                append("[$formattedDate] ")

                append("${log.from} → ${log.to}")

                append(" | duration: ${log.durationMs}ms")

                append("\n")
            }
            logFile.appendText(logEntry)
        }
    }
}