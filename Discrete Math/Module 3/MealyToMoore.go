package main

import (
	"fmt"
	"github.com/skorobogatov/input"
	"strings"
)

func main() {
	var m int
	input.Scanf("%d ", &m)
	X := make([]string, m)
	s := input.Gets()
	words := strings.Fields(s)
	for i := 0; i < m; i++ {
		X[i] = words[i]
	}

	var k int
	input.Scanf("%d ", &k)
	Y := make([]string, k)
	s = input.Gets()
	words = strings.Fields(s)
	for i := 0; i < k; i++ {
		Y[i] = words[i]
	}

	var n int
	input.Scanf("%d ", &n)

	delta := make([][]int, n)
	for i := 0; i < n; i++ {
		delta[i] = make([]int, m)
		for j := 0; j < m; j++ {
			input.Scanf("%d ", &delta[i][j])
		}
	}

	phi := make([][]string, n)
	for i := 0; i < n; i++ {
		phi[i] = make([]string, m)
		s := input.Gets()
		words := strings.Fields(s)
		for j := 0; j < m; j++ {
			phi[i][j] = words[j]
		}
	}

	count := 0
	r := make([]*R, 0)
	for i := 0; i < n; i++ {
		for j := 0; j < m; j++ {
			targetK := delta[i][j]
			targetY := phi[i][j]
			k, c := find(r, targetK, targetY)
			if !c {
				k = &R{y : targetY, c : count, i : targetK}
				r = append(r, k)
				count++
			}
		}
	}
	n2 := len(r)

	psi := make([]string, n2)
	delta2 := make([][]*R, n2)
	for i := 0; i < n2; i++ {
		psi[i] = r[i].y
		delta2[i] = make([]*R, m)
	}

	for i := 0; i < n; i++ {
		for j := 0; j < m; j++ {
			number := delta[i][j]
			name := phi[i][j]
			target, _ := find(r, number, name)
			parents := findbyi(r, i)
			for _, p := range parents {
				delta2[p.c][j] = target
			}
		}
	}

	fmt.Printf("digraph {\nrankdir = LR\n")
	for i := 0; i < n2; i++ {
		fmt.Printf("%d [label = \"(%d,%s)\"]\n", i, r[i].i, r[i].y)
		for j := 0; j < m; j++ {
			fmt.Printf("%d -> %d [label = \"%s\"]\n", r[i].c, delta2[i][j].c, X[j])
		}
	}
	fmt.Printf("}\n")
}

func findbyi(r []*R, k int) []*R {
	result := make([]*R, 0)
	for i := 0; i < len(r); i++ {
		if r[i].i == k {
			result = append(result, r[i])
		}
	}
	return result
}

func find(r []*R, k int, y string) (*R, bool) {
	for i := 0; i < len(r); i++ {
		if r[i].y == y && r[i].i == k {
			return r[i], true
		}
	}
	return nil, false
}

type Mealy struct {
	X []int
	Y []string
	Q []*Q
	delta [][]int
	phi [][]string
}

type Moore struct {
	X []int
	Y []string
	R []*R
	delta [][]int
	psi [][]int
}

type Q struct {
	i int
}

type R struct {
	y string
	i, c int
}