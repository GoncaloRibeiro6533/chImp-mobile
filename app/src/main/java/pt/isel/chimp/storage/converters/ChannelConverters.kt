package pt.isel.chimp.storage.converters

import androidx.room.TypeConverter
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.UserEntity

class Converters {

    @TypeConverter
    fun fromUserEntityToUser(value: UserEntity): User {
        return User(value.id, value.username, value.email)
    }

    @TypeConverter
    fun fromUserToUserEntity(value: User): UserEntity {
        return UserEntity(value.id, value.username, value.email)
    }


    @TypeConverter
    fun fromVarcharToVisibility(value: String): Visibility {
        return Visibility.valueOf(value)
    }

    @TypeConverter
    fun fromVisibilityToVarchar(value: Visibility): String {
        return value.name
    }


}