package com.mikyegresl.valostat.features.weapon

import com.mikyegresl.valostat.common.state.BaseState
import com.mikyegresl.valostat.base.model.weapon.WeaponDto

sealed class WeaponsScreenState: BaseState {

    object WeaponsScreenLoadingState : WeaponsScreenState()

    class WeaponsScreenErrorState(val t: Throwable) : WeaponsScreenState()

    data class WeaponsScreenDataState(
        val weapons: List<WeaponDto> = emptyList()
    ) : WeaponsScreenState()
}
