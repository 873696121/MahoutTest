import java.io.{ByteArrayInputStream, ByteArrayOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

object JDKTest {
  def main(args: Array[String]): Unit = {
    val seq = Seq("1", "2")
    val str = serialize(seq)
    println(str.toString)
    val anoSeq = unserizlize(str)
    println(anoSeq)
  }

  //序列化
  def serialize(obj: Any): Array[Byte] = {
    var objectOut: ObjectOutputStream = null
    var byteArrayOut: ByteArrayOutputStream = null
    try {
      byteArrayOut = new ByteArrayOutputStream()
      objectOut = new ObjectOutputStream(byteArrayOut)
      objectOut.writeObject(obj)
      val byteArray = byteArrayOut.toByteArray
      return byteArray
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
    null
  }

  //反序列化
  def unserizlize(byteArray: Array[Byte]): Seq[String] = {
    var objectInput: ObjectInputStream = null
    var byteArrayInput: ByteArrayInputStream = null
    val byteInput = new ByteArrayInputStream(byteArray)
    objectInput = new ObjectInputStream(byteInput)
    val obj = objectInput.readObject()
    obj.asInstanceOf[Seq[String]]
  }
}
