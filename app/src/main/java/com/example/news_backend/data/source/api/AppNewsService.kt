package com.example.news_backend.data.source.api

import com.example.news_backend.data.models.BanTin
import com.example.news_backend.data.models.Football
import com.example.news_backend.data.models.NewsResponse
import com.example.news_backend.network.ApiResponse
import com.example.news_backend.network.AuthenticationResponse
import com.example.news_backend.network.IntrospectResponse
import com.example.news_backend.network.UserResponse
import com.example.news_backend.network.request.ChangePassword
import com.example.news_backend.network.request.ChangePasswordRequest
import com.example.news_backend.network.request.FoolBallRequest
import com.example.news_backend.network.request.FootballDto
import com.example.news_backend.network.request.IntrospectRequest
import com.example.news_backend.network.request.LoginRequest
import com.example.news_backend.network.request.PostNewsLetterRequest
import com.example.news_backend.network.request.SavePosRequest
import com.example.news_backend.network.request.SignupRequest
import com.example.news_backend.network.request.UpdateUserRequest
import com.example.news_backend.network.response.PostNewsLetterResponse
import com.example.news_backend.network.response.SavePosResponse
import com.example.news_backend.network.response.UserResponseCoppy
import com.example.news_backend.network.response.UserUpdateRoleRequest
import com.example.news_backend.network.response.WeatherResponse
import com.example.news_backend.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AppNewsService {
    /**
     * bản tin
     * **/
    @GET("/api/news/category/{category}")
    fun getBanTin(
        @Path("category") category: String?
    ): Call<ApiResponse<List<BanTin>>>

    @GET("/api/news/full")
    fun getFullBanTin(): Call<ApiResponse<List<BanTin>>>


    /**
     * bóng đá:
     * **/
//        @Headers(
//        "x-rapidapi-host: free-football-soccer-videos.p.rapidapi.com",
//        "x-rapidapi-key: 4877c410b9mshe7fe9517ac14094p1cd13ejsn6a566299c797"
//    )
    @GET("/api/football")
    fun getFootballData(): Call<ApiResponse<List<Football>>>

    @POST("/api/football")
    fun postFootball(
        @Body football: FoolBallRequest
    ): Call<FootballDto>

    /**
     * Thời tiết
     * **/
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponse>


    /**
     * Tài khoản
     * **/
    @POST("/api/auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<ApiResponse<AuthenticationResponse>>

    @POST("/api/auth/signup")
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<ApiResponse<UserResponse>>

    @POST("/api/auth/introspect")
    fun authenticate(
        @Body request: IntrospectRequest
    ): Call<ApiResponse<IntrospectResponse>>


    @Headers(
//        "Authorizationt: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJob2FuZ3RpZW4yazMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3MTYwMTQ1MjgsImlhdCI6MTcxMzQyMjUyOCwic2NvcGUiOiJBRE1JTiJ9.47Cz77JJuod1aSLA2bHYL2TwFSuUxVcJg5XbmmJBagT1ntHMkftpLpL84LFTkI3XTBece7urIjFGmlxmJgKiKg"
          "Authorizationt: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJob2FuZ3RpZW4yazMuY29tIiwic3ViIjoicXVvY2Jhb2R0cjcwIiwiZXhwIjoxNzQ3ODE1NzE5LCJpYXQiOjE3NDUyMjM3MTksInNjb3BlIjoiQURNSU4ifQ.pWkwdCD6vOXxvS_hWG23qkyl6EgIBqdOqB2_vqHFnRGJCZyHiuojPt8ZwRjqIAot8BtxOpN897QqDo66-VGq7A"
    )

    @GET("/api/user/my-info")
    fun myinfo(): Call<ApiResponse<UserResponse>>

    @DELETE("/api/auth/delete/{id}")
    fun delete(
        @Path("id") id: Long
    ): Call<String>


    // ,  @Header("Authorization") authorization: String
    @PUT("/api/user/{id}")
    fun updateUser(
        @Path("id") id: Long, @Body update: UpdateUserRequest, @Header("Authorization") authorization: String
    ): Call<ApiResponse<UserResponse>>

    @PUT("/api/auth/changePassword")
    fun changePassword(
        @Body request: ChangePasswordRequest?
    ): Call<ApiResponse<UserResponse>>

    @PUT("api/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePassword
    ): Response<ResponseBody>

    /**
     *
     * NewsAPI
     * **/
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "in",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>


    /**
     * PostNews
     * **/
    @POST("/api/news")
    fun postNews(
        @Body postNewsLetterRequest: PostNewsLetterRequest
    ): Call<PostNewsLetterResponse>


    /**
     * Save News
     * **/
    @POST("/api/post")
    fun postNewsSave(
        @Body savePost: SavePosRequest
    ): Call<SavePosResponse>

    @GET("/api/post")
    fun getAllNewsSave(): Call<List<SavePosResponse>>

    @GET("/api/post/all/{userId}")
    fun getAllNewsSaveByUserId(
        @Path("userId") userId: Long
    ): Call<List<SavePosResponse>>

    @DELETE("/api/post/all")
    fun deleteAllPostNews(): Call<Void>

    @GET("/api/user")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): UserResponseCoppy

    @PUT("/api/user/role/{id}")
    suspend fun updateUserRole(
        @Path("id") userId: Long,
        @Body request: UserUpdateRoleRequest,
        @Header("Authorization") token: String
    ): Response<Unit>
}