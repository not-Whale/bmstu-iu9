package main

import (
	"fmt"
	"github.com/skorobogatov/input"
	"sort"
)

func main() {
	var n, m, q int
	input.Scanf("%d %d ", &n, &m)
	final := make([]bool, n)
	names := make(map[string]int)
	reverse := make([]string, 0)
	delta := make([][][]int, n)
	d := make([][]int, m)
	singals := make([]string, m)
	count := 0
	for i := 0; i < m; i++ {
		var mark string
		d[i] = make([]int, 2)
		input.Scanf("%d %d ", &d[i][0], &d[i][1])
		mark = input.Gets()
		singals[i] = mark
		_, ok := names[mark]
		if !ok {
			names[mark] = count
			if len(reverse) <= count {
				reverse = append(reverse, mark)
			}
			reverse[count] = mark
			count++
		}
	}
	count--
	for i := 0; i < n; i++ {
		var b int
		input.Scanf("%d ", &b)
		if b == 1 {
			final[i] = true
		}
	}
	input.Scanf("%d", &q)

	k := len(names)
	for i := 0; i < n; i++ {
		delta[i] = make([][]int, k)
		for j := 0; j < k; j++ {
			delta[i][j] = make([]int, 0)
		}
	}

	for i := 0; i < m; i++ {
		delta[d[i][0]][names[singals[i]]] = append(delta[d[i][0]][names[singals[i]]], d[i][1])
	}

	lambda, ok := names["lambda"]
	if !ok {
		lambda = -1
	}

	Q, delta2, F, q0, nums := Det(names, delta, final, q, lambda)
	Q[0] = Q[0]

	fmt.Printf("digraph {\nrankdir = LR\n")
	fmt.Printf("dummy [label = \"\", shape = none]\n")
	for i, v := range nums {
		sort.Ints(v)
		fmt.Printf("%d [label = \"[", i)
		for j, k := range v {
			fmt.Printf("%d", k)
			if j != len(v) - 1 {
				fmt.Printf(" ")
			}
		}
		fmt.Printf("]\", shape = ")
		if F[i] {
			fmt.Printf("doublecircle]\n")
		} else {
			fmt.Printf("circle]\n")
		}
	}
	fmt.Printf("dummy -> %d\n", indexOf(nums, q0))

	for i := 0; i < len(delta2); i++ {
		buf := make([][]int, len(nums))
		for j := 0; j < len(delta2[i]); j++ {
			for k := 0; k < len(delta2[i][j]); k++ {
				if delta2[i][j][k] != -1 {
					buf[delta2[i][j][k]] = append(buf[delta2[i][j][k]], j)
				}
			}
		}
		for j := 0; j < len(buf); j++ {
			if len(buf[j]) != 0 && !(len(buf[j]) == 1 && (buf[j][0] == lambda)){
				fmt.Printf("%d -> %d [label = \"", i, j)
				c := 0
				for k := 0; k < len(buf[j]); k++ {
					if buf[j][k] != -1 && buf[j][k] != lambda {
						if c != 0 {
							fmt.Printf(", ")
						}
						c++
						fmt.Printf("%s", reverse[buf[j][k]])
					}
				}
				fmt.Printf("\"]\n")
			}
		}
	}
	fmt.Printf("}")
}

func indexOf(nums [][]int, n []int) int {
	for i, v := range nums {
		if equal(v, n) {
			return i
		}
	}
	return -1
}

func equal(a []int, b []int) bool {
	n, m := len(a), len(b)
	if n != m {
		return false
	}
	for i := 0; i < n; i++ {
		if a[i] != b[i] {
			return false
		}
	}
	return true
}

func Det(names map[string]int, delta [][][]int, final []bool, q int, lambda int) ([][]int, [][][]int, []bool, []int, [][]int) {
	q0 := Closure(delta, []int{q}, lambda)

	Q := make([][]int, 0)
	Q = append(Q, q0)

	nums := make([][]int, 0)
	F := make([]bool, 0)
	d := make([][][]int, 0)

	count := 0
	s := InitStack()

	nums = append(nums, q0)
	F = append(F, false)
	d = append(d, make([][]int, len(delta[0])))
	Push(&s, count)

	count++
	for !StackEmpty(s) {
		z := Pop(&s)
		for _, u := range nums[z] {
			if final[u] {
				F[z] = true
				break
			}
		}
		for i := 0; i < len(delta[0]); i++ {
			unity := make([]int, 0)
			if i != lambda {
				for _, u := range nums[z] {
					for _, ua := range delta[u][i] {
						unity = append(unity, ua)
					}
				}
				z2 := Closure(delta, unity, lambda)
				if !containsArray(Q, z2) {
					Q = append(Q, z2)
					nums = append(nums, z2)
					F = append(F, false)
					d = append(d, make([][]int, len(delta[0])))
					Push(&s, count)
					count++
				}
				d[z][i] = append(d[z][i], indexOf(nums, z2))
			}
		}
	}
	return Q, d, F, q0, nums
}

func Closure(delta [][][]int, z []int, lambda int) []int {
	C := make([]int, 0)
	for _, q := range z {
		C = DFS(delta, q, C, lambda)
	}
	return C
}

func DFS(delta [][][]int, q int, C []int, lambda int) []int {
	if !containsInt(C, q) {
		C = append(C, q)
		if lambda != -1 {
			for _, w := range delta[q][lambda] {
				C = DFS(delta, w, C, lambda)
			}
		}
	}
	return C
}

func containsInt(C []int, q int) bool {
	for _, v := range C {
		if v == q {
			return true
		}
	}
	return false
}

func containsArray(Q [][]int, z2 []int) bool {
	for _, v := range Q {
		if equal(v, z2) {
			return true
		}
	}
	return false
}

func addArray(Q []int, z2 []int) []int {
	for _, v := range z2 {
		if !containsInt(Q, v) {
			Q = append(Q, v)
		}
	}
	return Q
}

func InitStack() Stack {
	s := Stack{}
	s.data = make([]int, 0)
	s.top = 0
	return s
}

func StackEmpty(s Stack) bool {
	return s.top == 0
}

func Push(s *Stack, q int) {
	if len(s.data) <= s.top {
		s.data = append(s.data, q)
	} else {
		s.data[s.top] = q
	}
	s.top++
}

func Pop(s *Stack) int {
	if !StackEmpty(*s) {
		s.top--
		return s.data[s.top]
	} else {
		fmt.Printf("stack is empty")
		return 0
	}
}

type Stack struct {
	data []int
	top int
}