package com.benyq.sodaplanet.base

import android.app.Application
import androidx.room.Room
import com.benyq.sodaplanet.base.room.SodaPlanetRoomDB
import com.benyq.sodaplanet.base.room.sodaPlanetDB
import com.orhanobut.logger.*


/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 * 用于初始化 module
 */
object CommonModuleInit {

    private const val ModuleTransaction = "com.benyq.sodaplanet.transaction.TransactionInit"

    private var initModuleNames = arrayOf(ModuleTransaction)


    //这个方法包含 最基础的初始化方法，以及其他module的初始化
    fun onInit(app: Application) {
        initLog()
        sodaPlanetDB = Room.databaseBuilder(
            app, SodaPlanetRoomDB::class.java, "soda_planet_db"
        ).build()

        //组件初始化
        initModuleNames.forEach {
            try {
                val clazz = Class.forName(it)
                val init = clazz.newInstance() as IModuleInit
                init.onInitAhead(app)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }

    private fun initLog() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
            .methodCount(5) // (Optional) How many method line to show. Default 2
            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
            .logStrategy(LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("sodaPlanet") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}


interface IModuleInit {
    /**
     * 需要优先初始化的
     */
    fun onInitAhead(app: Application)

}