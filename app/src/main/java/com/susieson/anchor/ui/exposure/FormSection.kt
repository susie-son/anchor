package com.susieson.anchor.ui.exposure

fun onClickClose(isEmpty: Boolean, onNavigateUp: () -> Unit, onShowDiscardDialog: () -> Unit) {
    if (isEmpty) {
        onNavigateUp()
    } else {
        onShowDiscardDialog()
    }
}
