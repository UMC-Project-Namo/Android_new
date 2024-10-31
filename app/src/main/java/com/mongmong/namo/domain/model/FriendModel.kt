package com.mongmong.namo.domain.model

import java.io.Serializable

data class Friend(
    val userid: Long,
    val profileUrl: String?,
    val nickname: String,
    val tag: String,
    val introduction: String,
    val name: String,
    val birth: String,
    val isFavorite: Boolean, // 즐겨찾기 여부
    val favoriteColorId: Int,
): Serializable {
    fun convertToFriendInfo(): FriendInfo {
        return FriendInfo(
            profileUrl = this.profileUrl,
            nickname = this.nickname,
            tag = this.tag,
            name = this.name,
            introduction = this.introduction,
            birth = this.birth,
            favoriteColorId = this.favoriteColorId
        )
    }
}


data class FriendRequest(
    var userId: Long = 0L,
    var friendRequestId: Long = 0L,
    var profileUrl: String? = "",
    val nickname: String = "",
    val tag: String = "",
    var introduction: String = "",
    val name: String,
    var birth: String = "",
    var favoriteColorId: Int = 0
) {
    fun convertToFriendInfo(): FriendInfo {
        return FriendInfo(
            profileUrl = this.profileUrl,
            nickname = this.nickname,
            tag = this.tag,
            introduction = this.introduction,
            name = this.nickname,
            birth = this.birth,
            favoriteColorId = this.favoriteColorId
        )
    }
}

data class FriendInfo(
    var profileUrl: String? = "",
    val nickname: String = "",
    val tag: String = "",
    var introduction: String = "",
    val name: String,
    var birth: String = "",
    var favoriteColorId: Int = 0
)