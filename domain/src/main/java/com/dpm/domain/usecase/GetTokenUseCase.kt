package com.dpm.domain.usecase

import com.dpm.domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<String> {
        return authRepository.getToken()
    }
}