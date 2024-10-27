package com.mongmong.namo.data.datasource.friend

import android.util.Log
import com.mongmong.namo.data.dto.FriendBaseResponse
import com.mongmong.namo.data.dto.GetFriendListResponse
import com.mongmong.namo.data.dto.GetFriendListResult
import com.mongmong.namo.data.dto.GetFriendRequestResponse
import com.mongmong.namo.data.remote.FriendApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteFriendDataSource @Inject constructor(
    private val friendApiService: FriendApiService
) {
    /** 친구 정보 */
    // 친구 목록 조회
    suspend fun getFriends(): GetFriendListResponse {
        var friendResponse = GetFriendListResponse(result = GetFriendListResult())
        withContext(Dispatchers.IO) {
            runCatching {
                friendApiService.getFriendList(searchWord = null) //TODO: 페이지, 친구 검색
            }.onSuccess {
                Log.d("RemoteFriendDataSource", "getFriends Success $it")
                friendResponse = it
            }.onFailure {
                Log.d("RemoteFriendDataSource", "getFriends Failure $it")
            }
        }
        return friendResponse
    }

    /** 친구 요청 */
    // 친구 요청 목록 조회
    suspend fun getFriendRequests(): GetFriendRequestResponse {
        var friendResponse = GetFriendRequestResponse(result = emptyList())
        withContext(Dispatchers.IO) {
            runCatching {
                friendApiService.getFriendRequestList()
            }.onSuccess {
                Log.d("RemoteFriendDataSource", "getFriendRequests Success $it")
                friendResponse = it
            }.onFailure {
                Log.d("RemoteFriendDataSource", "getFriendRequests Failure $it")
            }
        }
        return friendResponse
    }

    // 친구 요청
    suspend fun postFriendRequest(
        nicknameTag: String
    ): FriendBaseResponse {
        var friendResponse = FriendBaseResponse()
        withContext(Dispatchers.IO) {
            runCatching {
                friendApiService.postFriendRequest(nicknameTag)
            }.onSuccess {
                Log.d("RemoteFriendDataSource", "postFriendRequest Success $it")
                friendResponse = it
            }.onFailure {
                Log.d("RemoteFriendDataSource", "postFriendRequest Failure $it")
            }
        }
        return friendResponse
    }

    // 친구 요청 수락
    suspend fun acceptFriendRequest(
        requestId: Long
    ): FriendBaseResponse {
        var friendResponse = FriendBaseResponse()
        withContext(Dispatchers.IO) {
            runCatching {
                friendApiService.acceptFriendRequest(requestId)
            }.onSuccess {
                Log.d("RemoteFriendDataSource", "acceptFriendRequest Success $it")
                friendResponse = it
            }.onFailure {
                Log.d("RemoteFriendDataSource", "acceptFriendRequest Failure $it")
            }
        }
        return friendResponse
    }

    // 친구 요청 거절
    suspend fun rejectFriendRequest(
        requestId: Long
    ): FriendBaseResponse {
        var friendResponse = FriendBaseResponse()
        withContext(Dispatchers.IO) {
            runCatching {
                friendApiService.rejectFriendRequest(requestId)
            }.onSuccess {
                Log.d("RemoteFriendDataSource", "rejectFriendRequest Success $it")
                friendResponse = it
            }.onFailure {
                Log.d("RemoteFriendDataSource", "rejectFriendRequest Failure $it")
            }
        }
        return friendResponse
    }
}