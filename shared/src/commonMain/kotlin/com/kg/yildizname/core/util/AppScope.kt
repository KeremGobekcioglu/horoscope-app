package com.kg.yildizname.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

// Fire-and-forget scope for work that must outlive the screen/ViewModel that
// triggered it — e.g. finishing a data wipe after navigating away has already
// cleared that ViewModel. Mirrors the existing tokenRefreshScope pattern in
// iosMain's TokenRefresh.kt, but shared since this is used from commonMain.
val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
