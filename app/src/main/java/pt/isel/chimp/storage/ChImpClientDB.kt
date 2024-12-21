package pt.isel.chimp.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.isel.chimp.storage.daos.ChannelDao
import pt.isel.chimp.storage.daos.MessageDao
import pt.isel.chimp.storage.daos.UserDao
import pt.isel.chimp.storage.entities.ChannelEntity
import pt.isel.chimp.storage.entities.MessageEntity
import pt.isel.chimp.storage.entities.UserEntity
import pt.isel.chimp.storage.entities.UserInChannel

@Database(entities = [ChannelEntity::class, UserEntity::class, UserInChannel::class, MessageEntity::class], version =7)
abstract class ChImpClientDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun channelDao(): ChannelDao
    abstract fun messageDao(): MessageDao
   /* abstract fun channelUserDao(): ChannelUserDao
    abstract fun channelMessageDao(): ChannelMessageDao
    abstract fun channelUserMessageDao(): ChannelUserMessageDao*/
}