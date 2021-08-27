//package com.gw.slbdc.model
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.gw.slbdc.test.RoomTestBean
//import com.gw.slbdc.test.RoomTestJavaBean
//import com.gw.slbdc.test.TestDao
//
//@Database(version = 0, entities = [
//    RoomTestBean::class, RoomTestJavaBean::class
//])
//abstract class LocalDatabase: RoomDatabase() {
//    abstract fun testDao(): TestDao
//}