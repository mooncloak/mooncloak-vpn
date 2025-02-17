import java.text.SimpleDateFormat
import java.util.*

/**
 * Retrieves a [String] representation of the current [Date] in an ISO-8601 format.
 */
fun nowTimestamp(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    return dateFormat.format(Date())
}
