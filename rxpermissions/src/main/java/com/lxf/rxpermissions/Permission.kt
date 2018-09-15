package com.lxf.rxpermissions



data class Permission(
        val name: String,
        val granted: Boolean,
        val showShowRequestPermissionRational: Boolean = false
)