package com.dpm.presentation.seatreview.sample
object LevelUpManager {
    private var levelUpListener: (() -> Unit)? = null
    private var levelUpListener2: (() -> Unit)? = null

    fun setOnLevelUpListener(listener: () -> Unit) {
        levelUpListener = listener
    }

    fun triggerLevelUp() {
        levelUpListener?.invoke()
    }

    fun setOnLevelUpListener2(listener: () -> Unit) {
        levelUpListener2 = listener
    }

    fun triggerLevelUp2() {
        levelUpListener2?.invoke()
    }
}
