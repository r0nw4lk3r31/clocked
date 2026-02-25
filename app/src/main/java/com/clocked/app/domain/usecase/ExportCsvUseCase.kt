package com.clocked.app.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.clocked.app.data.repository.ShiftRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class ExportCsvUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ShiftRepository,
) {
    suspend operator fun invoke(yearMonth: YearMonth): Uri = withContext(Dispatchers.IO) {
        val shifts = repository.getShiftsSnapshot(yearMonth)
        val monthLabel = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        val dayFmt = DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("nl"))

        val csv = buildString {
            appendLine("datum,dag,start,eind,nacht,type,afdeling,notitie")
            shifts.forEach { s ->
                val day = s.date.format(dayFmt).lowercase()
                val note = s.note.replace("\"", "\"\"") // escape quotes
                appendLine("${s.date},$day,${s.startTime},${s.endTime},${s.crossesMidnight},${s.shiftType.name},${s.ward},\"$note\"")
            }
        }

        val dir = File(context.cacheDir, "exports").also { it.mkdirs() }
        val file = File(dir, "shifts_$monthLabel.csv")
        file.writeText(csv, Charsets.UTF_8)

        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
}
