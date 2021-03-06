package com.topjohnwu.magisk.data.network

import com.topjohnwu.magisk.core.Const
import com.topjohnwu.magisk.core.model.UpdateInfo
import com.topjohnwu.magisk.core.tasks.GithubRepoInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

private const val REVISION = "revision"
private const val MODULE = "module"
private const val FILE = "file"
private const val IF_NONE_MATCH = "If-None-Match"

private const val MAGISK_FILES = "topjohnwu/magisk_files"
private const val MAGISK_MASTER = "topjohnwu/Magisk/master"
private const val MAGISK_MODULES = "Magisk-Modules-Repo"

interface GithubPageServices {

    @GET("stable.json")
    suspend fun fetchStableUpdate(): UpdateInfo

    @GET("beta.json")
    suspend fun fetchBetaUpdate(): UpdateInfo
}

interface JSDelivrServices {

    @GET("$MAGISK_FILES@{$REVISION}/snet")
    @Streaming
    suspend fun fetchSafetynet(@Path(REVISION) revision: String = Const.SNET_REVISION): ResponseBody

    @GET("$MAGISK_FILES@{$REVISION}/bootctl")
    @Streaming
    suspend fun fetchBootctl(@Path(REVISION) revision: String = Const.BOOTCTL_REVISION): ResponseBody
}

interface GithubRawServices {

    @GET("$MAGISK_FILES/canary/debug.json")
    suspend fun fetchCanaryUpdate(): UpdateInfo

    @GET
    suspend fun fetchCustomUpdate(@Url url: String): UpdateInfo

    @GET("$MAGISK_MASTER/scripts/module_installer.sh")
    @Streaming
    suspend fun fetchInstaller(): ResponseBody

    @GET("$MAGISK_MODULES/{$MODULE}/master/{$FILE}")
    suspend fun fetchModuleFile(@Path(MODULE) id: String, @Path(FILE) file: String): String

    /**
     * This method shall be used exclusively for fetching files from urls from previous requests.
     * Him, who uses it in a wrong way, shall die in an eternal flame.
     * */
    @GET
    @Streaming
    suspend fun fetchFile(@Url url: String): ResponseBody

    @GET
    suspend fun fetchString(@Url url: String): String

}

interface GithubApiServices {

    @GET("users/${MAGISK_MODULES}/repos")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun fetchRepos(
        @Query("page") page: Int,
        @Header(IF_NONE_MATCH) etag: String,
        @Query("sort") sort: String = "pushed",
        @Query("per_page") count: Int = 100
    ): Response<List<GithubRepoInfo>>
}

