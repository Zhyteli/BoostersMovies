package com.boosterstestmovis

import androidx.test.platform.app.InstrumentationRegistry
import com.boosterstestmovis.data.api.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets

@RunWith(JUnit4::class)
class MovieDBServiceTest {

    lateinit var service: ApiService

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            ))
            .build()
            .create(ApiService::class.java)
    }

    @After
    @Throws(IOException::class)
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetMovie() {
        runBlocking {
            enqueueResponse("api-response/movie_list_response.json")
            val response = service.getMovieResponse(
                language = "en",
                sort = "release_date.desc",
                minVoteCountValue = "100",
                page = "1"
            )

            val request: RecordedRequest = mockWebServer.takeRequest()
            assertEquals("/movie?language=en&sort_by=release_date.desc&vote_count.gte=100&vote_average.gte=7&page=1", request.path)
            assertNotNull(response)
            assertTrue(response.isSuccessful)
            response.body()?.let {
                assertEquals(20, it.movies?.size)
                assertEquals("269149", it.movies?.get(1)?.id)
            }
        }
    }

    @Throws(IOException::class)
    private fun enqueueResponse(fileName: String, headers: Map<String, String>? = null) {
        val context = InstrumentationRegistry.getInstrumentation().context
        val inputStream = context.assets.open(fileName)
        if (inputStream == null) {
            fail("File not found: $fileName in assets directory")
        } else {
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            headers?.forEach { (key, value) ->
                mockResponse.addHeader(key, value)
            }
            mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
        }
    }
}