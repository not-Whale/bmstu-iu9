package main

import (
	"flag"
	"fmt"
	"os"
	"os/signal"
	"time"

	"github.com/go-ping/ping"
)

func main() {
	timeout := flag.Duration("t", time.Second*100, "Timeout specifies a timeout before ping exits, regardless of how many packets have been received.")
	interval := flag.Duration("i", time.Second, "Interval is the wait time between each packet send.")
	count := flag.Int("c", -1, "Count tells pinger to stop after sending (and receiving) Count echo packets.")
	privileged := flag.Bool("privileged", false, "Running in privileged mode.")
	flag.Parse()

	if flag.NArg() == 0 {
		fmt.Print("Usage: ping [-c count] [-i interval] [-t timeout] [--privileged] host")
		return
	}

	host := flag.Arg(0)

	pinger, err := ping.NewPinger(host)
	if err != nil {
		fmt.Printf("Failed to create a pinger: %s\n", err.Error())
		return
	}

	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt)
	go func() {
		for range c {
			pinger.Stop()
		}
	}()

	pinger.OnSend = func(pkt *ping.Packet) {
		fmt.Printf("SEND: %d bytes from %s, ICMP sequence = %d, time = %v ttl= %v\n",
			pkt.Nbytes, pkt.IPAddr, pkt.Seq, pkt.Rtt, pkt.Ttl)
	}

	pinger.OnRecv = func(pkt *ping.Packet) {
		fmt.Printf("RECEIVED: %d bytes from %s, ICMP sequence = %d, time = %v ttl= %v\n",
			pkt.Nbytes, pkt.IPAddr, pkt.Seq, pkt.Rtt, pkt.Ttl)
	}

	pinger.OnFinish = func(stats *ping.Statistics) {
		fmt.Printf("\n----------------------------------------------------------------------------------\n")
		fmt.Printf("\n--- %s ping statistics ---\n", stats.Addr)
		fmt.Printf("%d packets sent, %d packets received, %v%% packet loss\n",
			stats.PacketsSent, stats.PacketsRecv, stats.PacketLoss)
		fmt.Printf("round-trip min/avg/max/stddev = %v/%v/%v/%v\n",
			stats.MinRtt, stats.AvgRtt, stats.MaxRtt, stats.StdDevRtt)
	}

	pinger.Count = *count
	pinger.Interval = *interval
	pinger.Timeout = *timeout
	pinger.SetPrivileged(*privileged)

	fmt.Printf("PING %s (%s):\n", pinger.Addr(), pinger.IPAddr())
	err = pinger.Run()
	if err != nil {
		fmt.Printf("Failed to ping target host: %s", err)
	}
}