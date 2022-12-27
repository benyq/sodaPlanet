package com.benyq.sodaplanet.music

/**
 *
 * @author benyq
 * @date 2022/12/1
 * @email 1520063035@qq.com
 * music数据中心
 */
class DataCenter {

    private val musicList =
        listOf(
            "http://m8.music.126.net/20221207150351/9a292aa7976551bd37d9fe72a7c2b24b/ymusic/0fd6/4f65/43ed/a8772889f38dfcb91c04da915b301617.mp3",
            "asset:///石川由依 - 13の冬.mp3",
            "http://81.69.26.237/music.mp3",
            "http://81.69.26.237/music2.flac"
        )

    private var index = 0

    val current = musicList[index]

    val next: String
        get() {
            index = (index + 1) % musicList.size
            return musicList[index]
        }

    val previous: String
        get() {
            index = (index + musicList.size - 1) % musicList.size
            return musicList[index]
        }
}