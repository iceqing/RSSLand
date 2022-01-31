package cc.iceq.rss.model

data class ArticleInfo(
    var id:Long,
    var title: String="",
                       var url:String="",
                       var createTime:String="",
                       var desc:String="",
                       var author: String=""
                       )
