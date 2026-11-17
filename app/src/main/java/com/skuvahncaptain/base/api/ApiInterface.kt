package com.skuvahncaptain.base.api

import com.skuvahncaptain.module.auth.login.BaseResponse
import com.skuvahncaptain.module.auth.login.ResendOtpRequest
import com.skuvahncaptain.module.auth.login.SendOtpRequest
import com.skuvahncaptain.module.auth.login.SendOtpResponse
import com.skuvahncaptain.module.auth.login.VerifyOtpRequest
import com.skuvahncaptain.module.auth.login.VerifyOtpResponse
import com.skuvahncaptain.module.home.models.RouteAddRequest
import com.skuvahncaptain.module.home.models.RouteAddResponse
import com.skuvahncaptain.module.home.models.RouteDeleteResponse
import com.skuvahncaptain.module.home.models.RouteResponseModel
import com.skuvahncaptain.module.home.models.BookingsResponseModel
import com.skuvahncaptain.module.home.models.HomeResponseModel
import com.skuvahncaptain.module.home.models.SchoolResponseModel
import com.skuvahncaptain.module.home.models.UpdateBookingResponse
import com.skuvahncaptain.module.profile.DeleteUserRequest
import com.skuvahncaptain.module.profile.DeleteUserResponse
import com.skuvahncaptain.module.profile.SaveCaptainRequest
import com.skuvahncaptain.module.profile.SaveCaptainResponse
import com.skuvahncaptain.utilities.EndPoints
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST(EndPoints.sendOtp)
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): BaseResponse<SendOtpResponse>


    @POST(EndPoints.verifyOtp)
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): BaseResponse<VerifyOtpResponse>

    @POST(EndPoints.resendOtp)
    suspend fun resendOtp(
        @Body request: ResendOtpRequest
    ): BaseResponse<Boolean>

    @POST(EndPoints.saveCaptain)
    suspend fun saveCaptain(
        @Header("Authorization") token: String,
        @Body request: SaveCaptainRequest
    ): BaseResponse<SaveCaptainResponse>

    @GET(EndPoints.myRoutes)
    suspend fun myRoutes(
        @Header("Authorization") token: String,
        @Query("captainId") captainId: String
    ): Response<RouteResponseModel>

    @DELETE(EndPoints.deleteRoute)
    suspend fun deleteRoute(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("userId") userId: String
    ): Response<RouteDeleteResponse>

    @PUT(EndPoints.updateBooking)
    suspend fun updateBooking(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("userId") userId: String,
        @Query("status") status: Int
    ): Response<UpdateBookingResponse>

    @POST(EndPoints.routes)
    suspend fun addRoute(
        @Header("Authorization") token: String,
        @Body body: RouteAddRequest
    ): Response<RouteAddResponse>

    @GET(EndPoints.bookings)
    suspend fun getBookings(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): Response<BookingsResponseModel>

    @GET(EndPoints.home)
    suspend fun getHome(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): Response<HomeResponseModel>

    @GET(EndPoints.schools)
    suspend fun getSchools(
        @Header("Authorization") token: String,
    ): Response<SchoolResponseModel>

    @POST(EndPoints.deleteUser)
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Body body: DeleteUserRequest
    ): Response<DeleteUserResponse>
}