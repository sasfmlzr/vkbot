package com.newapi.apivk.architecture.task

interface Task <in INPUT, out OUTPUT>{
    suspend fun execute(input: INPUT) : OUTPUT
}
