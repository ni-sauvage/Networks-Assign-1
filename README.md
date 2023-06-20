# Networks-Assign-1

## An assignment to design a File Transfer Protocol for [CS33031 Computer Networks](https://teaching.scss.tcd.ie/module/csu33031-computer-networks/) as taught by [Dr. Stefan Weber](https://www.tcd.ie/research/profiles/?profile=sweber).

### Architecture
The architecture of my solution was designed as follows:
![Topology Diagram](https://github.com/ni-sauvage/Networks-Assign-1/blob/master/Images/topology_in_theory.png)

In practice, this looks like:
![Topology Diagram in Practice](https://github.com/ni-sauvage/Networks-Assign-1/blob/master/Images/topology_in_practice.png)

Files are transferred from the selected worker node via the server to the client. This data is then written into a file on the client-side. All workers are queried to find if they have the file requested by the client.

### Packet Design

Inheritance was very useful here as I was able to create a [PacketContent](https://github.com/ni-sauvage/Networks-Assign-1/blob/master/Java/PacketContent.java) class of which all the other packet types are subclasses. 

The operation of this protocol operated as follows:

![FlowDiag](https://github.com/ni-sauvage/Networks-Assign-1/blob/master/Images/Flow_Diagram_Complete.png)

Each packet contained around 1200 bytes, with each `ACK` packet containing how many bytes had been received previously. The protocol roughly follows an automated [Stop and Wait ARQ](https://en.wikipedia.org/wiki/Stop-and-wait_ARQ) protocol. 

### Wireshark
We see the packet capture of a successful operation of this protocol.
![Wireshark](https://github.com/ni-sauvage/Networks-Assign-1/blob/master/Images/wireshark.png)

## Any more details?

Absolutely, see [the report section](https://github.com/ni-sauvage/Networks-Assign-1/blob/master/Report/CSU33031___Assignment__1__File_Transfer_Protocol.pdf) of this repo. It contains the full writeup on this protocol. 

