package main

import (
	"flag"
	"fmt"
	"github.com/go-ping/ping"
	"time"
	"log"
)

var (
	timeout = flag.Duration("t", time.Second*100, "Timeout specifies a timeout before ping exits, regardless of how many packets have been received.")
	interval = flag.Duration("i", time.Second, "Interval is the wait time between each packet send.")
	count = flag.Int("c", -1, "Count tells pinger to stop after sending (and receiving) Count echo packets.")
	privileged = flag.Bool("privileged", false, "Running in privileged mode.")
)

func main() {
	flag.Parse()

	if flag.NArg() == 0 {
		fmt.Print("Usage: ping [-c count] [-i interval] [-t timeout] [--privileged] host")
		return
	}

	hostname := flag.Arg(0)

	for i := 0; i < 5; i++ {
		go func(i int) {
			pinger := CreatePinger(hostname, i)
			fmt.Printf("PING %s (%s) %d time:\n", pinger.Addr(), pinger.IPAddr(), i)
			err := pinger.Run()
			if err != nil {
				log.Fatal("Failed to ping target host: %s", err)
			}
		}(i)
	}

	for i := 0; i < 5; i++ {
		select {
		case <-time.After(time.Second*10):
			{
				fmt.Println("\nTIME OUT\n")
				return
			}
		}
	}
}

func CreatePinger(hostname string, i int) (pinger *ping.Pinger) {
	pinger, err := ping.NewPinger(hostname)
	if err != nil {
		fmt.Printf("Failed to create a pinger: %s\n", err.Error())
		return
	}

	pinger.OnSend = func(pkt *ping.Packet) {
		fmt.Printf("%d SEND: %d bytes from %s, ICMP sequence = %d, time = %v ttl = %v\n",
			i, pkt.Nbytes, pkt.IPAddr, pkt.Seq, pkt.Rtt, pkt.Ttl)
	}

	pinger.OnRecv = func(pkt *ping.Packet) {
		fmt.Printf("%d RECEIVED: %d bytes from %s, ICMP sequence = %d, time = %v ttl = %v\n",
			i, pkt.Nbytes, pkt.IPAddr, pkt.Seq, pkt.Rtt, pkt.Ttl)
	}

	pinger.OnFinish = func(stats *ping.Statistics) {
		fmt.Printf("\n---------------------------------" +
			"-------------------------------------------------\n" +
			"\n--- %s ping statistics n.%d ---\n" +
			"%d packets sent, %d packets received, %v%% packet loss\n" +
			"round-trip min/avg/max/stddev = %v/%v/%v/%v\n",
			stats.Addr, i, stats.PacketsSent, stats.PacketsRecv, stats.PacketLoss,
			stats.MinRtt, stats.AvgRtt, stats.MaxRtt, stats.StdDevRtt)
	}

	pinger.Count = *count
	pinger.Interval = *interval
	pinger.Timeout = *timeout
	pinger.SetPrivileged(*privileged)

	return
}