/*
 * Main.scala
 *
 * Licensed to the Communitivity, Inc under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at*
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.communitivity.echoxmpp

import scala.actors.Actor 
import scala.actors.Actor._
import org.xmpp.packet.Packet
import org.xmpp.packet.Message

import org.communitivity.shellack.ComponentConfig
import org.communitivity.shellack.XMPPComponent


//case class PacketReceived(pkt : Packet, out : Actor)
//case class PacketResponse(pkt : Packet)

object Main {

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]) :Unit = {
      new XMPPComponent(
        new ComponentConfig() {
            def secret() : String = { "secret.goes.here" }
            def server() : String = { "communitivity.com" }
            def subdomain() : String = { "weather" }
            def name() : String = { "US Weather" }
            def description() : String = { "Weather component that also supported SPARQL/XMPP" }
        },
       actor {
        loop {
            react {
                case (pkt:Packet, out : Actor) =>
                    Console.println("Received packet...\n"+pkt.toXML)
                    pkt match {
                        case message:Message =>
                            val reply  = new Message()
                            reply.setTo(message.getFrom())
                            reply.setFrom(message.getTo())
                            reply.setType(message.getType())
                            reply.setThread(message.getThread())
                            reply.setBody("Received '"+message.getBody()+"', tyvm")
                            out ! reply
                        case _ =>
                            Console.println("Received something other than Message")
                    }
                 case _ =>
                    Console.println("Received something other than (Packet, actor)")
            }
        }
       }
    ).start
  }
}
