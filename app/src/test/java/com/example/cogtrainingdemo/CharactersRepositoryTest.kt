package com.example.cogtrainingdemo

import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.jupiter.api.Test


import retrofit2.Response

@ExperimentalCoroutinesApi
class CharactersRepositoryTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var charactersRemoteDataSource: CharactersRemoteDataSource
    private var SUT: CharactersRepository

    init {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)

        SUT = CharactersRepository.getInstance(charactersRemoteDataSource)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test - invoke all characters` () {
        runTest {
            val response = mockk<Response<List<CharactersItem>>>()
            coEvery { charactersRemoteDataSource.getAllCharacters() } returns response
            SUT.getAllCharacters().message
            coVerify { charactersRemoteDataSource.getAllCharacters() }
        }
    }
}
