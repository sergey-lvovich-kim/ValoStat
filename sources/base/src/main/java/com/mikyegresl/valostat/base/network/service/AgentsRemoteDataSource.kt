package com.mikyegresl.valostat.base.network.service

import com.google.gson.JsonElement

interface AgentsRemoteDataSource {

    suspend fun getAgents(): JsonElement
}