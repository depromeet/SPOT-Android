package com.dpm.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.dpm.domain.preference.SharedPreference
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
    override var nickname: String
        get() = preferences.getString("nickname", "").orEmpty()
        set(value) {
            preferences.edit(commit = true) {
                putString("nickname", value)
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

    override var teamName: String
        get() = preferences.getString("teamName","").orEmpty()
        set(value) {
            preferences.edit(commit = true) {
                putString("teamName", value)
            }
        }
    override var teamId: Int
        get() = preferences.getInt("teamId", 0)
        set(value) {
            preferences.edit(commit = true) {
                putInt("teamId", value)
            }
        }
    override var levelTitle: String
        get() = preferences.getString("levelTitle", "").orEmpty()
        set(value) {
            preferences.edit(commit = true) {
                putString("levelTitle",value)
            }
        }

    override var profileImage: String
        get() = preferences.getString("profileImage","").orEmpty()
        set(value) {
            preferences.edit(commit = true) {
                putString("profileImage", value)
            }
        }
}
