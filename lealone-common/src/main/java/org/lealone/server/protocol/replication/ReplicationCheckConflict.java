/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.server.protocol.replication;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lealone.net.NetInputStream;
import org.lealone.net.NetOutputStream;
import org.lealone.server.protocol.Packet;
import org.lealone.server.protocol.PacketDecoder;
import org.lealone.server.protocol.PacketType;

public class ReplicationCheckConflict implements Packet {

    public final String mapName;
    public final ByteBuffer key;
    public final String replicationName;

    public ReplicationCheckConflict(String mapName, ByteBuffer key, String replicationName) {
        this.mapName = mapName;
        this.key = key;
        this.replicationName = replicationName;
    }

    @Override
    public PacketType getType() {
        return PacketType.REPLICATION_CHECK_CONFLICT;
    }

    @Override
    public PacketType getAckType() {
        return PacketType.REPLICATION_CHECK_CONFLICT_ACK;
    }

    @Override
    public void encode(NetOutputStream out, int version) throws IOException {
        out.writeString(mapName).writeByteBuffer(key).writeString(replicationName);
    }

    public static final Decoder decoder = new Decoder();

    private static class Decoder implements PacketDecoder<ReplicationCheckConflict> {
        @Override
        public ReplicationCheckConflict decode(NetInputStream in, int version) throws IOException {
            String mapName = in.readString();
            ByteBuffer key = in.readByteBuffer();
            String replicationName = in.readString();
            return new ReplicationCheckConflict(mapName, key, replicationName);
        }
    }
}
