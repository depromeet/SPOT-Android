package com.depromeet.designsystem

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.depromeet.designsystem.databinding.SpotTeamLabelBinding

class SpotTeamLabel @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private lateinit var binding: SpotTeamLabelBinding

    var teamType: String? = null
        set(value) {
            when (value) {
                "LG" -> {
                    setTeamColor(R.color.color_lg, R.color.color_lg_font)
                }
                "두산" -> {
                    setTeamColor(R.color.color_doosan, R.color.color_doosan_font)
                }
                "삼성" -> {
                    setTeamColor(R.color.color_samsung, R.color.color_samsung_font)
                }
                "KIA" -> {
                    setTeamColor(R.color.color_kia, R.color.color_kia_font)
                }
                "SSG" -> {
                    setTeamColor(R.color.color_ssg, R.color.color_ssg_font)
                }
                "히어로즈" -> {
                    setTeamColor(R.color.color_heroes, R.color.color_heroes_font)
                }
                "KT" -> {
                    setTeamColor(R.color.color_kt, R.color.color_kt_font)
                }
                "롯데" -> {
                    setTeamColor(R.color.color_lotte, R.color.color_lotte_font)
                }
                "한화" -> {
                    setTeamColor(R.color.color_hanwha, R.color.color_hanwha_font)
                }
                "NC" -> {
                    setTeamColor(R.color.color_nc, R.color.color_nc_font)
                }
            }
            binding.tvTeam.text = value
            field = value
        }

    init {
        initView()
    }

    private fun initView() {
        binding = SpotTeamLabelBinding.bind(
            inflate(
                context,
                R.layout.spot_team_label,
                this
            )
        )
    }

    private fun setTeamColor(@ColorRes backgroundColor: Int, @ColorRes fontColor: Int) {
        binding.tvTeam.backgroundTintList = ContextCompat.getColorStateList(context, backgroundColor)
        binding.tvTeam.setTextColor(ContextCompat.getColor(context, fontColor))
    }
}