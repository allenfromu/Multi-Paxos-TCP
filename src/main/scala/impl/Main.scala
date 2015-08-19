package impl

import akka.actor.ActorSystem
import akka.actor.Props
import java.util.logging.SimpleFormatter
import java.util.logging.FileHandler
import java.util.logging.Logger
import com.typesafe.config.ConfigFactory
import data.ConfigLoader

object Main extends App{
  override def main(args:Array[String]){
     var logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
     logger.setUseParentHandlers(false);
     var fileTxt = new FileHandler("log_4.txt");
     var formatterTxt = new SimpleFormatter();
     fileTxt.setFormatter(formatterTxt);
     logger.addHandler(fileTxt);
           
    var id = -1
    
    try{
      var m = Util.loadPaxos()
      while(id < 0){
        try{
          print("Select node id:")
          id = readLine().toInt
          if(!(id > 0 && id <=m.size)){
            println("id should be a number between 1 and " +m.size)
            id = -1
         }
        }
        catch{
          case e:Exception => println("nid should be a number between 1 and " +m.size); id = -1
        }
      }   
      val system = ActorSystem("RemoteSystem"+id,ConfigFactory.load(ConfigLoader.load(m(id)._1, m(id)._2)))
      var master = system.actorOf(Props(classOf[Paxos_Actor],m,id), "Paxos"+id)      
     while(true){
        print("->")
        var command = readLine()
        if(command.equals("propose")){
          print("value:")
          var v = readLine().asInstanceOf[java.io.Serializable]
          print("node:")
          try{
            var nid = readLine().toInt
            if(nid > 0 && nid <=m.size)
              master ! propose(v,nid)
            else
              println("nid should be a number between 1 and " +m.size)
          }catch{
            case e:Exception => println("nid should be a number between 1 and " +m.size)
          }
        }  
        else if(command.equals("PL")){
          master ! Print_logs()
        }
        
      }
    }    
    catch{
      case e:Exception => println(e.getMessage)
    } 
  }

}