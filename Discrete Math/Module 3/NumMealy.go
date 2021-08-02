package main

import (
	"fmt"
	"github.com/skorobogatov/input"
	"strings"
)

func main() {
	var n, m, q0 int
	input.Scanf("%d %d %d", &n, &m, &q0)
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
		words := strings.Fields(s)
		for j := 0; j < m; j++ {
			phi[i][j] = words[j]
		}
	}
	delta2, phi2, n2 := DFS(delta, phi, n, m, q0)
	fmt.Printf("%d\n%d\n%d\n", n2, m, 0)
	for i := 0; i < n2; i++ {
		for j := 0; j < m; j++ {
			fmt.Printf("%d ", delta2[i][j])
		}
		fmt.Printf("\n")
	}
	for i := 0; i < n2; i++ {
		for j := 0; j < m; j++ {
			fmt.Printf("%s ", phi2[i][j])
		}
		fmt.Printf("\n")
	}
}

func DFS(delta [][]int, phi [][]string, n, m, q int) ([][]int, [][]string, int) {
	marks := make([]int, n)
	nums := make([]int, n)
	for i := 0; i < n; i++ {
		marks[i] = 0
		nums[i] = -1
	}
	count := 0
	VisitVertex(delta, m, q, &count, marks, nums)
	n2 := n
	for i := 0; i < n; i++ {
		if nums[i] == -1 {
			n2--
		}
	}
	delta2 := make([][]int, n2)
	phi2 := make([][]string, n2)
	for i := 0; i < n2; i++ {
		delta2[i] = make([]int, m)
		phi2[i] = make([]string, m)
	}
	for i := 0; i < n; i++ {
		if nums[i] != -1 {
			for j := 0; j < m; j++ {
				delta2[nums[i]][j] = nums[delta[i][j]]
				phi2[nums[i]][j] = phi[i][j]
			}
		}
	}
	return delta2, phi2, n2
}

func VisitVertex(delta [][]int, m, current int, count *int, marks []int, nums []int) {
	marks[current] = 1
	nums[current] = *count
	*count++
	for i := 0; i < m; i++ {
		u := delta[current][i]
		if marks[u] == 0 {
			VisitVertex(delta, m, u, count, marks, nums)
		}
	}
	marks[current] = 2
}