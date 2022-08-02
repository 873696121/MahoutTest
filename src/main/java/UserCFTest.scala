import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.{CityBlockSimilarity, EuclideanDistanceSimilarity, LogLikelihoodSimilarity, PearsonCorrelationSimilarity, SpearmanCorrelationSimilarity, TanimotoCoefficientSimilarity, UncenteredCosineSimilarity}
import org.apache.mahout.cf.taste.recommender.RecommendedItem

import java.{io, util}

object UserCFTest {
  def main(args: Array[String]): Unit = {
    val filePath = "/Users/bytedance/temp/output.txt"
    val file = new io.File(filePath)
    val dataModel = new FileDataModel(file)

    //余弦相似度
    val cosineSimilarity = new UncenteredCosineSimilarity(dataModel)
    //欧几里得相似度
    val euclideanSimilarity = new EuclideanDistanceSimilarity(dataModel)
    //皮尔森相似度
    val pearsonCorrelationSimilarity = new PearsonCorrelationSimilarity(dataModel)
    // 基于Manhattan距离相似度
    val cityBlockSimilarity = new CityBlockSimilarity(dataModel)
    // 基于欧几里德距离计算相似度
    val euclideanDistanceSimilarity = new EuclideanDistanceSimilarity(dataModel)
    // 基于对数似然比的相似度
    val logLikelihoodSimilarity = new LogLikelihoodSimilarity(dataModel)
    // 基于皮尔斯曼相关系数相似度
    val spearmanCorrelationSimilarity = new SpearmanCorrelationSimilarity(dataModel)
    // 基于谷本系数计算相似度
    val tanimotoCoefficientSimilarity = new TanimotoCoefficientSimilarity(dataModel)

    val k = 10
    val currentSimilarity = spearmanCorrelationSimilarity
    //定义用户的k最近邻
    val userNeighborhood = new NearestNUserNeighborhood(k, currentSimilarity, dataModel)

    //定义推荐引擎
    val recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, currentSimilarity)

    //从数据模型中获取所有用户的ID迭代器
    val usersIterator = dataModel.getUserIDs

    println("=================================")

    //通过迭代器遍历所有用户ID
    while (usersIterator.hasNext) {
      // 用户Id
      val userID = usersIterator.nextLong()
      //用户ID迭代器
      val otherUsersIterator = dataModel.getUserIDs
      //相当于两个for循环，遍历用户ID，计算任何两个用户的相似度
      while (otherUsersIterator.hasNext) {
        val otherUserID = otherUsersIterator.nextLong()
        println(s"用户 ${userID} 与用户 ${otherUserID} 的相似度为 ${currentSimilarity.userSimilarity(userID, otherUserID)}")
      }
      //userID的N-最近邻
      val userN = userNeighborhood.getUserNeighborhood(userID)
      //用户userID的推荐物品，最多推荐两个
      val recommendedItems: util.List[RecommendedItem] = recommender.recommend(userID, 10)
      println("用户 " + userID + " 的10-最近邻是 " + util.Arrays.toString(userN))
      if (recommendedItems.size > 0) {
        recommendedItems.forEach(item => {
          println("推荐的物品 " + item.getItemID + "预测评分是 " + item.getValue)
        })
      } else {
        System.out.println("无任何物品推荐")
      }
    }
  }
}
