package com.depromeet.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.depromeet.domain.preference.SharedPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSharedPreference @Inject constructor(
    private val preferences: SharedPreferences,
) : SharedPreference {
    override var token: String
        get() = preferences.getString("token", "").orEmpty()
        set(value) {
            preferences.edit(commit = true) {
                putString("token", value)
            }
        }
    override var refreshToken: String
        get() = preferences.getString("refresh_token", "").orEmpty()
        set(value) {
            preferences.edit(commit = true) {
                putString("refresh_token", value)
            }
        }

    override fun clear() {
        preferences.edit(commit = true) {
            clear()
        }
    }

    override var level: Int
        get() = preferences.getInt("level", 0)
        set(value) {
            preferences.edit(commit = true) {
                putInt("level", value)
            }
        }
}
