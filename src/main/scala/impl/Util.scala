package impl

import akka.util.ByteString
import scala.io.Source
import java.net.InetSocketAddress
import data.ProposalID
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * singleton object contains useful functions to support the functionalities of paxos_Actor
 * 
 * @author Zepeng Zhao
 */
object Util {
  

  
  /**
   * load paxos properties(id, ip addr and port number) from file 'paxos.config' 
   * and store it to a map
   * the config file must store all paxoses properties in a format like below;
   * id1 host1 port_number1   eg.   1 127.0.0.1 2015
   * id2 host2 port_number2         2 127.0.0.1 2016
   *    ... ...
   *  Note that id must be a unique number
   */
  def loadPaxos():Map[Int,(String, Int)] ={
    var m:Map[Int,(String, Int)] = Map()
    val filename = "paxos.config"
    for (line <- Source.fromFile(filename).getLines()) {
      var arr = line.split(" ")
      m+=(arr(0).toInt->(arr(1),arr(2).toInt))
    }   
    m   
  }
  
  def writeToDisk(filename:String, m:Map[BigInt,(java.io.Serializable,ProposalID)]){
    val fos = new FileOutputStream(filename)
    val oos = new ObjectOutputStream(fos)
    oos.writeObject(m)
    oos.close()
  }
  
  def read_from_disk(filename:String):Map[BigInt,(java.io.Serializable,ProposalID)]={
    val fis = new FileInputStream(filename)
    val ois = new ObjectInputStream(fis)
    val obj = ois.readObject()
    ois.close()
    obj.asInstanceOf[Map[BigInt,(java.io.Serializable,ProposalID)]]    
  }
   
  
}