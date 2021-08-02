package main

import (
	"fmt"
	"github.com/skorobogatov/input"
	"strings"
)

func main() {
	var n, m, q int
	input.Scanf("%d %d %d ", &n, &m, &q)
	delta := make([][]int, n)
	phi := make([][]string, n)
	for i := 0; i < n; i++ {
		delta[i] = make([]int, m)
		phi[i] = make([]string, m)
	}
	for i := 0; i < n; i++ {
		for j := 0; j < m; j++ {
			input.Scanf("%d ", &delta[i][j])
		}
	}
	for i := 0; i < n; i++ {
		s := input.Gets()
		names := strings.Fields(s)
		for j := 0; j < m; j++ {
			phi[i][j] = names[j]
		}
	}

	fmt.Printf("digraph {\n" +
		"rankdir = LR\n" +
		"dummy [label = \"\", shape = none]\n")

	for i := 0; i < n; i++ {
		fmt.Printf("%v [shape = circle]\n", i)
	}

	fmt.Printf("dummy -> %d\n", q)
	for i := 0; i < n; i++ {
		for j := 0; j < m; j++ {
			fmt.Printf("%v -> %v [label = \"%c(%s)\"]\n", i, delta[i][j], 97 + j, phi[i][j])
		}
	}
	fmt.Printf("}")
}