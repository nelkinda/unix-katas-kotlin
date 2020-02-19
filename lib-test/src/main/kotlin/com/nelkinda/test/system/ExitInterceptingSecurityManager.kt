package com.nelkinda.test.system

import java.security.Permission

class ExitInterceptingSecurityManager(private val originalSecurityManager: SecurityManager?) : SecurityManager() {
    var status: Int? = null
        private set

    override fun checkPermission(perm: Permission) {
        originalSecurityManager?.checkPermission(perm)
    }

    override fun checkPermission(perm: Permission, context: Any) {
        originalSecurityManager?.checkPermission(perm, context)
    }

    override fun checkExit(status: Int) {
        super.checkExit(status)
        this.status = status
        throw ExitException(status)
    }

}