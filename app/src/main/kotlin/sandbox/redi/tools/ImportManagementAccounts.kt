package sandbox.redi.tools

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import sandbox.redi.tools.feedback.Feedback
import sandbox.redi.tools.feedback.FeedbackToStdOut
import java.io.File
import java.util.*

private val Row.isEmpty: Boolean
    get() {
        val nonEmpty = firstOrNull { it != null && it.stringCellValue != null && it.stringCellValue.isNotBlank() }
        return nonEmpty == null
    }

class ImportManagementAccounts {

    fun process(request: Request) {
        request.started()
        val r = runCatching { request.import() }
        r.onFailure { request.failed(it) }
        r.onSuccess { request.completedOk() }
    }

    private fun Request.import() {
        WorkbookFactory.create(source).use { wb ->
            check(wb.numberOfSheets > 0) { "Workbook is empty" }
            check(wb.getSheetAt(0).physicalNumberOfRows > 1) { "Workbook sheet empty" }
            var totalImports = 0
            val sheet = wb.getSheetAt(0)
            val headers = sheet.getRow(0).withIndex().associate { (i, c) -> c.stringCellValue?.lowercase() to i }
            val accountHeader = accountHeader ?: DEFAULT_ACCOUNT_HEADER
            val developemntHeader = developmenHeader ?: DEFAULT_DEVELOPMENT_HEADER
            val accountColumn = headers[accountHeader]
            val developColumn = headers[developemntHeader]
            val seperator = accountNameSeperator ?: DEFAULT_ACCOUNT_NAME_SEPERATOR
            check(accountColumn != null && developColumn != null) {
                "Sheet 1 must have a header (first row) with both a $developemntHeader and a $accountHeader headers."
            }
            for (r in 1 until sheet.lastRowNum) {
                val row = sheet.getRow(r)
                if (row.isEmpty) continue
                val accountHolderNames = row.getCell(accountColumn)?.stringCellValue?.split(seperator)
                val developmentName = row.getCell(developColumn)?.stringCellValue
                if (developmentName.isNullOrEmpty() || accountHolderNames.isNullOrEmpty()) {
                    if (developmentName == null) warn { "No development names found at row ${r + 1}" }
                    if (accountHolderNames.isNullOrEmpty()) warn { "No person names found at row ${r + 1}" }
                    trace { "Skipped row ${r + 1}: ${row.joinToString { it.stringCellValue }}" }
                    continue
                }
                totalImports += importAccounts(developmentName, accountHolderNames)
                    .also { n -> trace { "importing $n accounts on row ${r + 1} into development \"$developmentName\"" } }
            }
            info { "Imported $totalImports total accounts from ${source.name}" }
        }
    }

    private fun Feedback.importAccounts(
        developmentName: String,
        accountHolderNames: List<String>,
    ): Int {
        trace { "Importing ${accountHolderNames.size} accounts into $developmentName" }
        return accountHolderNames.size
    }

    interface Request : Feedback {
        val source: File
        val accountHeader: String?
        val developmenHeader: String?
        val accountNameSeperator: Char?
    }

    companion object {
        const val DEFAULT_DEVELOPMENT_HEADER = "development"
        const val DEFAULT_ACCOUNT_HEADER = "account"
        const val DEFAULT_ACCOUNT_NAME_SEPERATOR = '/'
    }

    class SimpleRequest(
        override val source: File,
        private val failFast: Boolean = false,
        private val feedback: Feedback = FeedbackToStdOut(
            "Importing management accounts from ${source.name}"
        ) { failFast },
        override val accountHeader: String = DEFAULT_ACCOUNT_HEADER,
        override val developmenHeader: String = DEFAULT_DEVELOPMENT_HEADER,
        override val accountNameSeperator: Char = DEFAULT_ACCOUNT_NAME_SEPERATOR,
    ) : Feedback by feedback, Request {

        override fun equals(other: Any?): Boolean {
            return when {
                other === this -> true
                other == null -> false
                else -> (other as? SimpleRequest)?.source == source && this.feedback == feedback
            }
        }

        override fun hashCode(): Int = Objects.hash(source.name, feedback)
    }

}


