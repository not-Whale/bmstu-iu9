package main

import (
	"fmt"
	"github.com/skorobogatov/input"
	"strings"
)

func main() {
	var n, m, q1 int
	input.Scanf("%d %d %d", &n, &m, &q1)
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

	var n2, m2, q2 int
	input.Scanf("%d %d %d", &n2, &m2, &q2)
	delta2 := make([][]int, n2)
	phi2 := make([][]string, n2)
	for i := 0; i < n2; i++ {
		delta2[i] = make([]int, m2)
		phi2[i] = make([]string, m2)
	}
	for i := 0; i < n2; i++ {
		for j := 0; j < m2; j++ {
			input.Scanf("%d ", &delta2[i][j])
		}
	}
	for i := 0; i < n2; i++ {
		s := input.Gets()
		words := strings.Fields(s)
		for j := 0; j < m2; j++ {
			phi2[i][j] = words[j]
		}
	}

	if m != m2 {
		fmt.Printf("NOT EQUAL")
		return
	}

	R := make([]*Q, n)
	for i := 0; i < n; i++ {
		R[i] = &Q{i : i}
	}
	delta, phi, n = minimize(delta, phi, n, m, q1)
	A := auto{delta, phi, n, m, R}

	R2 := make([]*Q, n2)
	for i := 0; i < n2; i++ {
		R2[i] = &Q{i : i}
	}
	delta2, phi2, n2 = minimize(delta2, phi2, n2, m2, q2)
	A2 := auto{delta2, phi2, n2, m2, R2}

	result := equality(A, A2)

	if result {
		fmt.Printf("EQUAL")
	} else {
		fmt.Printf("NOT EQUAL")
	}
}

func equality(A, A2 auto) bool {
	delta, phi, n, m := A.delta, A.phi, A.n, A.m
	delta2, phi2, n2, m2 := A2.delta, A2.phi, A2.n, A2.m
	if n != n2 || m != m2 {
		return false
	}
	for i := 0; i < n; i++ {
		for j := 0; j < m; j++ {
			if delta[i][j] != delta2[i][j] {
				return false
			}
			if phi[i][j] != phi2[i][j] {
				return false
			}
		}
	}
	return true
}

func minimize(delta [][]int, phi [][]string, n, m, q0 int) ([][]int, [][]string, int) {
	R := make([]*Q, n)
	for i := 0; i < n; i++ {
		R[i] = &Q{i : i}
	}
	A := auto{delta, phi, n, m, R}
	A2, q2 := AufenkampHohn(A, q0)
	return DFS(A2.delta, A2.phi, A2.n, A2.m, q2)
}

func contains(arr []*Q, comp *Q) bool {
	for _, x := range arr {
		if x == comp {
			return true
		}
	}
	return false
}

func Union(x *Q, y *Q) {
	rootx := Find(x)
	rooty := Find(y)
	rootx.parent = rooty
}

func Find(x *Q) *Q {
	if x.parent == x {
		return x
	} else {
		return Find(x.parent)
	}
}

func AufenkampHohn(A auto, q0 int) (auto, int) {
	m, pi := Split1(A)
	for {
		m2 := Split(A, pi)
		if m == m2 {
			break
		}
		m = m2
	}

	var (
		R2 []*Q
		delta2 [][]int
		phi2 [][]string
	)
	delta2 = make([][]int, A.n)
	phi2 = make([][]string, A.n)
	for i := 0; i < A.n; i++ {
		delta2[i] = make([]int, A.m)
		phi2[i] = make([]string, A.m)
		for j := 0; j < A.m; j++ {
			delta2[i][j] = -1
		}
	}
	R2 = make([]*Q, A.n)

	j := 0
	for i, _ := range A.q {
		q2 := pi[i]
		if !contains(R2, q2) {
			R2 = append(R2, q2)
			for k := 0; k < A.m; k++ {
				delta2[q2.i][k] = pi[A.delta[i][k]].i
				phi2[q2.i][k] = A.phi[i][k]
			}
			j++
		}
	}
	q := pi[q0].i
	A2 := auto{delta2, phi2, A.n, A.m, R2}
	return A2, q
}

func Split(A auto, pi []*Q) (int) {
	m := A.n
	for _, q := range A.q {
		q.parent = q
		q.depth = 0
	}
	for i := 0; i < A.n; i++ {
		for j := i; j < A.n; j++ {
			if pi[i] == pi[j] && Find(A.q[i]) != Find(A.q[j]) {
				eq := true
				for k := 0; k < A.m; k++ {
					w1 := A.delta[i][k]
					w2 := A.delta[j][k]
					if pi[w1] != pi[w2] {
						eq = false
						break
					}
				}
				if eq {
					Union(A.q[i], A.q[j])
					m--
				}
			}
		}
	}
	for i, q := range A.q {
		pi[i] = Find(q)
	}
	return m
}

func Split1(A auto) (int, []*Q) {
	m := A.n
	for _, q := range A.q {
		q.parent = q
		q.depth = 0
	}
	for i := 0; i < A.n; i++ {
		for j := i; j < A.n; j++ {
			if Find(A.q[i]) != Find(A.q[j]) {
				eq := true
				for k := 0; k < A.m; k++ {
					if A.phi[i][k] != A.phi[j][k] {
						eq = false
						break
					}
				}
				if eq {
					Union(A.q[i], A.q[j])
					m--
				}
			}
		}
	}
	pi := make([]*Q, A.n)
	for i, q := range A.q {
		pi[i] = Find(q)
	}
	return m, pi
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

type auto struct {
	delta [][]int
	phi [][]string
	n, m int
	q []*Q
}

type Q struct {
	parent *Q
	depth int
	i int
}