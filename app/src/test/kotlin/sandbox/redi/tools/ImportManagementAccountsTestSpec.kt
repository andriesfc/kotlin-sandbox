package sandbox.redi.tools

import io.kotest.core.spec.style.FunSpec
import sandbox.framework.utils.toFileOrNull
import sandbox.redi.tools.ImportManagementAccounts.SimpleRequest
import java.io.File

class ImportManagementAccountsTestSpec : FunSpec({
    test("process") {
        val request = SimpleRequest(fileRes(), failFast = true)
        ImportManagementAccounts().process(request)
    }
})


private fun fileRes(): File {

    val res = "import-management-accounts.xlsx"

    val resourceUrl = ImportManagementAccountsTestSpec::class.java.getResource(res)
        ?: throw IllegalStateException("Resource not found: $res")

    return resourceUrl.toFileOrNull()
        ?: throw IllegalStateException("Resource not available locally $resourceUrl")
}




